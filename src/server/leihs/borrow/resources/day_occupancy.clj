(ns leihs.borrow.resources.day-occupancy
  "How many items of a model are still available in each step of 30 minutes
  of a day, in a pool offering reservations by the hour. Shown to customers
  next to the times of pick up and return, so they see when e.g. a room is
  free instead of guessing. Only quantities, nothing about who reserved."
  (:require [java-time :as jt]
            [leihs.borrow.graphql.target-user :as target-user]
            [leihs.borrow.resources.hourly-reservations :as hourly]
            [leihs.core.availability.changes :as ch]
            [leihs.core.availability.core :as av]
            [leihs.core.availability.queries :as q]))

(defn- steps
  "[start-time end-time] of each step from the opening of the pool (on a
  step) to its closing."
  [[opening closing]]
  (let [minutes (+ (* 60 (.getHour opening)) (.getMinute opening))
        first-step (* hourly/step-minutes
                      (int (Math/ceil (/ minutes hourly/step-minutes))))]
    (->> (iterate #(jt/plus % (jt/minutes hourly/step-minutes))
                  (jt/local-time (quot first-step 60) (mod first-step 60)))
         (partition 2 1)
         (take-while (fn [[_ end]]
                       (and (not (jt/after? end closing))
                            ;; no wrap around midnight
                            (jt/after? end opening))))
         (map vec))))

(defn steps-availability
  "For each step of `date` within the hours of the pool: {:start-time
  :end-time :quantity}, the quantity the user may still reserve for exactly
  that step (as the availability for a reservation by the hour would tell).
  nil for a pool only offering whole days."
  [tx model-id user-id pool-id date exclude-res-ids]
  (when-let [pool-hours (hourly/pool-hours tx pool-id)]
    (let [model (q/get-model-by-id tx model-id)
          entitlements (q/get-entitlements-for-model-and-pool tx model-id pool-id)
          model-group-ids (q/get-inventory-pool-and-model-group-ids tx model-id pool-id)
          initial-changes (ch/init tx entitlements pool-id)
          reservations (q/running-reservations tx model-id pool-id exclude-res-ids)
          group-ids (concat [:general] (q/get-user-group-ids tx user-id))
          now (ch/now-in-time-zone tx)
          day (ch/local-date date)]
      (for [[start-time end-time] (steps pool-hours)]
        (let [period (ch/period day day [start-time end-time])
              changes (->> reservations
                           (filter #(ch/conflicts-with-period? period % now))
                           (reduce #(ch/extend-with %1 %2 model model-group-ids)
                                   initial-changes))
              inner-changes (ch/between changes day day)]
          {:start-time start-time
           :end-time end-time
           :quantity (if (empty? inner-changes)
                       0
                       (max 0 (av/summed-for-groups inner-changes group-ids)))})))))

;;; GraphQL

(defn day-occupancy
  [{{tx :tx} :request user-id ::target-user/id}
   {:keys [inventory-pool-id date exclude-reservation-ids]}
   {model-id :id}]
  (if (jt/before? (ch/local-date date) (ch/local-date))
    []
    (or (steps-availability tx model-id user-id inventory-pool-id date
                            exclude-reservation-ids)
        [])))
