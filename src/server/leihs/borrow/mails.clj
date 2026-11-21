(ns leihs.borrow.mails
  (:require
   [clojure.string :as str]
   [next.jdbc :as jdbc :refer [execute!] :rename {execute! jdbc-execute!}]
   [next.jdbc.sql :refer [query] :rename {query jdbc-query}]
   [honey.sql :refer [format] :rename {format sql-format}]
   [honey.sql.helpers :as sql]

   [leihs.core.db :as db]
   [leihs.core.languages :as lang]
   [leihs.core.mails :refer [get-tmpl log-mail-failure]]
   [leihs.core.settings :refer [settings]]
   [wet.core :as wet]
   [leihs.borrow.resources.delegations :refer [delegation?]]
   [leihs.borrow.resources.inventory-pools :as pools]
   [leihs.borrow.resources.inventory-pools.email-variables :as email-vars]
   [leihs.borrow.resources.orders.shared :as orders]
   [leihs.borrow.resources.users.shared :as users]
   [taoensso.timbre :refer [debug info warn error spy]]))

;; The time slot of a reservation ("10:00–12:00"), NULL for whole days, so
;; mail templates can use {% if l.time_slot %}.
(def ^:private time-slot-sql
  [:raw "CASE WHEN r.start_time IS NULL THEN NULL ELSE to_char(r.start_time, 'HH24:MI') || '–' || to_char(r.end_time, 'HH24:MI') END"])

;; Where the "received order" notifications of a pool go: its notification
;; email when it has one, else its own email, which is also the sender.
(defn- notification-email [tx pool-id]
  (-> (sql/select :notification_email)
      (sql/from :inventory_pools)
      (sql/where [:= :id pool-id])
      sql-format
      (->> (jdbc-query tx))
      first
      :notification_email))

(defn send-received [tx order settings]
  (let [inventory-pool (->> order
                            :inventory_pool_id
                            (pools/get-by-id tx))]
    (when (:deliver_received_order_emails inventory-pool)
      (try (let [lang-locale (-> (lang/default tx) :locale keyword)
                 tmpl (get-tmpl tx "received" (:id inventory-pool) (name lang-locale))]
             (cond
               (not inventory-pool) (warn "Pool for sending email not found or it is inactive.")
               (not tmpl) (warn (format "No 'received' mail template found for pool '%s'." (:id inventory-pool)))
               :else (let [email-signature (:email_signature settings)
                           user (users/get-by-id tx (:user_id order))
                           pool (->> inventory-pool
                                     (email-vars/merge-workdays tx lang-locale)
                                     (email-vars/merge-holidays tx lang-locale))
                           reservations (-> (sql/select :r.quantity, :r.start_date, :r.end_date
                                                        [[:to_char :r.start_time "HH24:MI"] :start_time]
                                                        [[:to_char :r.end_time "HH24:MI"] :end_time]
                                                        [time-slot-sql :time_slot]
                                                        [:m.name :model_name])
                                            (sql/from [:reservations :r])
                                            (sql/join [:models :m] [:= :r.model_id :m.id])
                                            (sql/where [:= :r.order_id (:id order)])
                                            sql-format
                                            (->> (jdbc-query tx)))
                           purpose (:purpose order)
                           order-url (str (:external_base_url settings) "/manage/" (:id pool) "/orders/" (:id order) "/edit")
                           email-body (wet/render (wet/parse (:body tmpl))
                                                  {:params {:user user
                                                            :inventory_pool pool
                                                            :email_signature email-signature
                                                            :reservations reservations
                                                            :comment nil
                                                            :purpose purpose
                                                            :order_url order-url
                                                            ;; approved on submission
                                                            ;; (orders/auto-approve?)
                                                            :approved (= "approved" (:state order))}
                                                   :filters {}})
                           address (or (:email pool) (:smtp_default_from_address settings))
                           to-address (or (notification-email tx (:id pool)) address)]
                       (-> (sql/insert-into :emails)
                           (sql/values [{:inventory_pool_id (:id pool)
                                         :from_address address
                                         :to_address to-address
                                         :subject (:subject tmpl)
                                         :body email-body
                                         :template "received"
                                         :source_pool_id (:id pool)}])
                           sql-format
                           (->> (jdbc-execute! tx))))))
           (catch Exception e
             (log-mail-failure (:id inventory-pool) e))))))

(defn- send-to-customer [tx order settings template]
  (let [user (users/get-by-id tx (:user_id order))]
    (try
      (let [target-user-id (if (delegation? user)
                             (orders/delegated-user-id tx (:id order))
                             (:id user))
            target-user (users/get-by-id tx target-user-id)
            lang-locale (-> (lang/get-the-one-to-use tx target-user-id)
                            :locale
                            keyword)
            inventory-pool (->> order
                                :inventory_pool_id
                                (pools/get-by-id tx)
                                (email-vars/merge-workdays tx lang-locale)
                                (email-vars/merge-holidays tx lang-locale))
            tmpl (get-tmpl tx template (:id inventory-pool) (name lang-locale))]
        (cond
          (not inventory-pool) (warn "Pool for sending email not found or it is inactive.")
          (not tmpl) (warn (format "No '%s' mail template found for pool '%s'." template (:id inventory-pool)))
          :else (let [email-signature (:email_signature settings)
                      reservations (-> (sql/select :r.quantity, :r.start_date, :r.end_date
                                                   [[:to_char :r.start_time "HH24:MI"] :start_time]
                                                   [[:to_char :r.end_time "HH24:MI"] :end_time]
                                                   [time-slot-sql :time_slot]
                                                   [:m.name :model_name])
                                       (sql/from [:reservations :r])
                                       (sql/join [:models :m] [:= :r.model_id :m.id])
                                       (sql/where [:= :r.order_id (:id order)])
                                       sql-format
                                       (->> (jdbc-query tx)))
                      purpose (:purpose order)
                      order-url (str (:external_base_url settings) "/manage/" (:id inventory-pool) "/orders/" (:id order) "/edit")
                      email-body (wet/render (wet/parse (:body tmpl))
                                             {:params {:user target-user
                                                       :inventory_pool inventory-pool
                                                       :email_signature email-signature
                                                       :reservations reservations
                                                       :comment nil
                                                       :purpose purpose
                                                       :order_url order-url}
                                              :filters {}})
                      to-address (or (:email target-user) (:secondary_email target-user))
                      from-address (or (:email inventory-pool) (:smtp_default_from_address settings))]
                  (-> (sql/insert-into :emails)
                      (sql/values [{:user_id target-user-id
                                    :from_address from-address
                                    :to_address to-address
                                    :subject (:subject tmpl)
                                    :body email-body
                                    :template template
                                    :source_pool_id (:id inventory-pool)}])
                      sql-format
                      (->> (jdbc-execute! tx))))))
      (catch Exception e
        (log-mail-failure (:id user) e)))))

(defn send-submitted [tx order settings]
  (send-to-customer tx order settings "submitted"))

;; For orders approved on submission (see orders/auto-approve?).
(defn send-approved [tx order settings]
  (send-to-customer tx order settings "approved"))

;; Notification to the pool when a customer cancels an order it had already
;; approved. Written here rather than as a mail template, since it is an
;; internal notice for the staff and needs no per-pool wording.
(def ^:private canceled-texts
  {"ca" {:subject "Comanda cancel·lada: %s"
         :intro "%s ha cancel·lat una comanda que ja estava aprovada. Els articles queden lliures i no cal preparar-los."
         :purpose "Finalitat" :items "Articles" :link "Veure la comanda"}
   "es" {:subject "Pedido cancelado: %s"
         :intro "%s canceló un pedido que ya estaba aprobado. Los artículos quedan libres y no hace falta prepararlos."
         :purpose "Finalidad" :items "Artículos" :link "Ver el pedido"}
   "de" {:subject "Bestellung storniert: %s"
         :intro "%s hat eine bereits genehmigte Bestellung storniert. Die Gegenstände sind wieder frei und müssen nicht vorbereitet werden."
         :purpose "Zweck" :items "Gegenstände" :link "Bestellung ansehen"}
   "fr" {:subject "Commande annulée : %s"
         :intro "%s a annulé une commande déjà approuvée. Les objets sont à nouveau libres et n'ont pas besoin d'être préparés."
         :purpose "Motif" :items "Objets" :link "Voir la commande"}
   "en" {:subject "Order canceled: %s"
         :intro "%s canceled an order that had already been approved. The items are free again and need not be prepared."
         :purpose "Purpose" :items "Items" :link "View the order"}})

(defn send-canceled [tx order settings]
  (let [inventory-pool (->> order :inventory_pool_id (pools/get-by-id tx))]
    (when (:deliver_received_order_emails inventory-pool)
      (try
        (let [locale (-> (lang/default tx) :locale str)
              texts (get canceled-texts (subs locale 0 (min 2 (count locale)))
                         (get canceled-texts "en"))
              user (users/get-by-id tx (:user_id order))
              delegated (when (delegation? user)
                          (some->> (orders/delegated-user-id tx (:id order))
                                   (users/get-by-id tx)))
              who (if delegated
                    (str (:name delegated) " (" (:name user) ")")
                    (:name user))
              reservations (-> (sql/select :r.quantity :r.start_date :r.end_date
                                           [time-slot-sql :time_slot]
                                           [:m.name :model_name])
                               (sql/from [:reservations :r])
                               (sql/join [:models :m] [:= :r.model_id :m.id])
                               (sql/where [:= :r.order_id (:id order)])
                               (sql/order-by :r.start_date :r.start_time :m.name)
                               sql-format
                               (->> (jdbc-query tx)))
              date (fn [d] (.format (java.time.format.DateTimeFormatter/ofPattern "dd/MM/yyyy")
                                    (java.time.LocalDate/parse (str d))))
              line (fn [{:keys [quantity start_date end_date time_slot model_name]}]
                     (str "- " quantity " × " model_name ": " (date start_date)
                          (cond time_slot (str " " time_slot)
                                (not= (str start_date) (str end_date)) (str " – " (date end_date)))))
              purpose (:purpose order)
              order-url (str (:external_base_url settings) "/manage/"
                             (:id inventory-pool) "/orders/" (:id order) "/edit")
              body (str (format (:intro texts) who) "\n\n"
                        (when-not (str/blank? purpose)
                          (str (:purpose texts) ": " purpose "\n\n"))
                        (:items texts) ":\n"
                        (str/join "\n" (map line reservations)) "\n\n"
                        (:link texts) ": " order-url
                        (when-let [signature (:email_signature settings)]
                          (str "\n\n" signature)))
              address (or (:email inventory-pool) (:smtp_default_from_address settings))
              to-address (or (notification-email tx (:id inventory-pool)) address)]
          (-> (sql/insert-into :emails)
              (sql/values [{:inventory_pool_id (:id inventory-pool)
                            :from_address address
                            :to_address to-address
                            :subject (format (:subject texts) who)
                            :body body
                            :template "canceled"
                            :source_pool_id (:id inventory-pool)}])
              sql-format
              (->> (jdbc-execute! tx))))
        (catch Exception e
          (log-mail-failure (:id inventory-pool) e))))))

(comment (let [tx (db/get-ds)
               order (-> (sql/select :*)
                         (sql/from :orders)
                         (sql/where [:= :id #uuid "63b4e83e-e2c3-4924-ae89-3596824b1c95"])
                         sql-format
                         (->> (jdbc-query tx))
                         first)]
           ; order
           (send-received tx (settings tx) order)))
