(ns leihs.borrow.resources.missing-contents
  "Which items of a package may be missing in a period: the borrowable items
  in a package (e.g. a synth mounted in a buc) are lent on their own to
  teachers, so whoever books the package sees what will not be there. Only
  models and quantities, nothing about who reserved."
  (:require [honey.sql :refer [format] :rename {format sql-format}]
            [honey.sql.helpers :as sql]
            [java-time :as jt]
            [leihs.borrow.resources.models.core :refer [base-sqlmap]]
            [leihs.core.availability.changes :as ch]
            [leihs.core.availability.queries :as q]
            [next.jdbc.sql :refer [query] :rename {query jdbc-query}]))

(def ^:private competing-statuses #{"submitted" "approved" "signed"})

(defn- lendable-children
  "The borrowable items in the packages of the package model in the pool."
  [tx package-model-id pool-id]
  (-> (sql/select :child.id :child.model_id)
      (sql/from [:items :child])
      (sql/join [:items :package] [:= :package.id :child.parent_id])
      (sql/where [:= :package.model_id package-model-id])
      (sql/where [:= :package.inventory_pool_id pool-id])
      (sql/where [:= :package.retired nil])
      (sql/where [:= :child.retired nil])
      (sql/where [:= :child.is_borrowable true])
      sql-format
      (->> (jdbc-query tx))))

(defn- unpackaged-count
  "Borrowable items of the model in the pool outside any package: a
  reservation not assigned yet may get one of them instead."
  [tx model-id pool-id]
  (-> (sql/select [[:count :*] :n])
      (sql/from :items)
      (sql/where [:= :model_id model-id])
      (sql/where [:= :inventory_pool_id pool-id])
      (sql/where [:= :retired nil])
      (sql/where [:= :is_borrowable true])
      (sql/where [:= :parent_id nil])
      sql-format
      (->> (jdbc-query tx))
      first
      :n))

(defn- missing-quantity
  "How many of `child-ids` (items of the model) are taken in `period`: the
  ones assigned to competing reservations, plus the reservations not assigned
  yet that the items outside the packages can not take."
  [tx model-id pool-id child-ids period exclude-res-ids now]
  (let [competing (->> (q/running-reservations tx model-id pool-id exclude-res-ids)
                       (filter #(competing-statuses (some-> % :status name)))
                       (filter #(ch/conflicts-with-period? period % now)))
        child-ids (set child-ids)
        assigned (->> competing
                      (filter #(child-ids (:item_id %)))
                      (map :quantity)
                      (apply + 0))
        unassigned (->> competing
                        (filter #(nil? (:item_id %)))
                        (map :quantity)
                        (apply + 0))
        overflow (max 0 (- unassigned (unpackaged-count tx model-id pool-id)))]
    (min (count child-ids) (+ assigned overflow))))

(defn missing
  "[{:model :quantity}] of the items in the packages of the model that may
  be missing between start-date and end-date (by the hour from start-time to
  end-time)."
  [tx package-model-id pool-id start-date end-date window exclude-res-ids]
  (let [children (lendable-children tx package-model-id pool-id)]
    (when (seq children)
      (let [period (ch/period start-date end-date (some->> window (mapv ch/->local-time)))
            now (ch/now-in-time-zone tx)
            quantities (->> children
                            (group-by :model_id)
                            (keep (fn [[model-id items]]
                                    (let [n (missing-quantity tx model-id pool-id
                                                              (map :id items)
                                                              period
                                                              exclude-res-ids now)]
                                      (when (pos? n) [model-id n]))))
                            (into {}))]
        (when (seq quantities)
          (->> (-> base-sqlmap
                   (sql/where [:in :models.id (keys quantities)])
                   sql-format)
               (jdbc-query tx)
               (map (fn [model] {:model model
                                 :quantity (get quantities (:id model))}))))))))

;;; GraphQL

(defn missing-package-contents
  [{{tx :tx} :request}
   {:keys [inventory-pool-id start-date end-date start-time end-time
           exclude-reservation-ids]}
   {model-id :id}]
  (if (jt/before? (ch/local-date end-date) (ch/local-date))
    []
    (or (missing tx model-id inventory-pool-id start-date end-date
                 (when (and start-time end-time) [start-time end-time])
                 exclude-reservation-ids)
        [])))
