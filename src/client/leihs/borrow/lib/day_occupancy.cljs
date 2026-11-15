(ns leihs.borrow.lib.day-occupancy
  "The quantity of a model still available in each step of 30 minutes of a
  day, shown by the OrderPanel in pools with reservations by the hour (asked
  for through onOccupancyDayChange), so customers see when e.g. a room is
  free."
  (:require
   [leihs.borrow.lib.helpers :as h]
   [leihs.borrow.lib.re-frame :refer [reg-event-db reg-event-fx reg-sub]]
   [re-graph.core :as re-graph]
   [shadow.resource :as rc]))

(reg-event-fx
 ::fetch
 (fn [{:keys [db]} [_ {:keys [model-id user-id exclude-reservation-ids]} jsargs]]
   (let [{:keys [poolId date]} (js->clj jsargs :keywordize-keys true)
         request {:poolId poolId :date (h/date-format-day date)}]
     {:db (assoc db ::request request ::result nil)
      :dispatch [::re-graph/query
                 (rc/inline "leihs/borrow/lib/getDayOccupancy.gql")
                 {:modelId model-id
                  :userId user-id
                  :poolId poolId
                  :date (:date request)
                  :excludeReservationIds exclude-reservation-ids}
                 [::on-fetched request]]})))

(reg-event-db
 ::on-fetched
 (fn [db [_ request {:keys [data errors]}]]
   ;; only for the day still shown
   (if (and (= request (::request db)) (not errors))
     (assoc db ::result (assoc request :steps (-> data :model :day-occupancy)))
     db)))

(reg-event-db
 ::clear
 (fn [db _] (dissoc db ::request ::result)))

(reg-sub ::result (fn [db _] (::result db)))
