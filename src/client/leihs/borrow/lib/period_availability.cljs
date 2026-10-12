(ns leihs.borrow.lib.period-availability
  "The available quantity of a model for exactly a period by the hour (asked
  for by the OrderPanel through onPeriodChange), which the availability by
  day can not tell (e.g. from 16:00 to 10:00 the next day)."
  (:require
   [leihs.borrow.lib.helpers :as h]
   [leihs.borrow.lib.re-frame :refer [reg-event-db reg-event-fx reg-sub]]
   [re-graph.core :as re-graph]
   [shadow.resource :as rc]))

(defn calendar-window?
  "Whether the availability by day can be asked for the times (`{:startTime
  :endTime}`) of a period by the hour on each day: not for one over night
  (e.g. from 16:00 to 10:00 the next day), for which the whole days are shown
  instead."
  [time-slot]
  (and time-slot
       (neg? (compare (:startTime time-slot) (:endTime time-slot)))))

(reg-event-fx
 ::fetch
 (fn [{:keys [db]} [_ {:keys [model-id user-id exclude-reservation-ids]} jsargs]]
   (let [{:keys [poolId startDate endDate timeSlot]} (js->clj jsargs :keywordize-keys true)
         request {:poolId poolId
                  :startDate startDate
                  :endDate endDate
                  :startTime (:startTime timeSlot)
                  :endTime (:endTime timeSlot)}]
     (when (:startTime request)
       {:db (assoc db ::request request ::result nil)
        :dispatch [::re-graph/query
                   (rc/inline "leihs/borrow/lib/getPeriodAvailability.gql")
                   {:modelId model-id
                    :userId user-id
                    :poolIds [poolId]
                    :startDate (h/date-format-day startDate)
                    :endDate (h/date-format-day endDate)
                    :startTime (:startTime request)
                    :endTime (:endTime request)
                    :excludeReservationIds exclude-reservation-ids}
                   [::on-fetched request]]}))))

(reg-event-db
 ::on-fetched
 (fn [db [_ request {:keys [data errors]}]]
   ;; only for the period still chosen
   (if (and (= request (::request db)) (not errors))
     (assoc db ::result (assoc request
                               :quantity
                               (-> data :model :available-quantity-in-date-range)))
     db)))

(reg-event-db
 ::clear
 (fn [db _] (dissoc db ::request ::result)))

(reg-sub ::result (fn [db _] (::result db)))
