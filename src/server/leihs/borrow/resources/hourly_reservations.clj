(ns leihs.borrow.resources.hourly-reservations
  "Reservations by the hour: in a pool offering them, customers choose the
  time of pick up and of return within the hours of the pool, in steps of 30
  minutes, also over several days. Only members of a group of the pool may
  reserve there, at most as long as the highest limit of their groups."
  (:require [honey.sql :refer [format] :rename {format sql-format}]
            [honey.sql.helpers :as sql]
            [java-time :as jt]
            [leihs.borrow.graphql.target-user :as target-user]
            [leihs.borrow.time :as time]
            [leihs.core.availability.changes :refer [->local-time]]
            [next.jdbc.sql :refer [query] :rename {query jdbc-query}]))

(def step-minutes 30)

(defn pool-hours
  "[start-time end-time] (local times) of a pool offering reservations by the
  hour, nil for a pool only offering whole days."
  [tx pool-id]
  (let [pool (-> (sql/select :hourly_reservations
                             :hourly_reservations_start_time
                             :hourly_reservations_end_time)
                 (sql/from :inventory_pools)
                 (sql/where [:= :id pool-id])
                 sql-format
                 (->> (jdbc-query tx))
                 first)]
    (when (:hourly_reservations pool)
      [(->local-time (:hourly_reservations_start_time pool))
       (->local-time (:hourly_reservations_end_time pool))])))

(defn group-limits
  "The max_reservation_hours (nil: no limit) of each group of the pool the
  user is a member of, directly or through a group of users."
  [tx user-id pool-id]
  (-> (sql/select :entitlement_groups.max_reservation_hours)
      (sql/from :entitlement_groups)
      (sql/join :entitlement_groups_users
                [:= :entitlement_groups_users.entitlement_group_id
                 :entitlement_groups.id])
      (sql/where [:= :entitlement_groups.inventory_pool_id pool-id]
                 [:= :entitlement_groups_users.user_id user-id])
      sql-format
      (->> (jdbc-query tx)
           (map :max_reservation_hours))))

(defn max-hours
  "The highest of the limits, nil if one of the groups has none (or there
  are no groups)."
  [limits]
  (when (and (seq limits) (every? some? limits))
    (apply max limits)))

(defn- date-time [date local-time]
  (jt/local-date-time (jt/local-date (str date)) local-time))

(defn period
  "[from to) as local date times: from start-time of start-date to end-time
  of end-date, for whole days from the opening of the pool on start-date to
  its closing on end-date."
  [[opening closing] start-date end-date start-time end-time]
  [(date-time start-date (or start-time opening))
   (date-time end-date (or end-time closing))])

(defn hours
  "The duration of the period in hours (real time, nights included)."
  [[from to]]
  (/ (.toMinutes (java.time.Duration/between from to)) 60.0))

(defn- on-step? [t]
  (and (zero? (mod (.getMinute t) step-minutes))
       (zero? (.getSecond t))
       (zero? (.getNano t))))

(defn- within? [[opening closing] t]
  (not (or (jt/before? t opening) (jt/after? t closing))))

(defn problem
  "Why the user may not reserve from start-date (start-time) to end-date
  (end-time) in the pool, nil if they may. Without times it is a whole-day
  reservation."
  [tx user-id pool-id start-date end-date start-time end-time]
  (let [start-time (some-> start-time ->local-time)
        end-time (some-> end-time ->local-time)
        pool-hours* (pool-hours tx pool-id)]
    (cond
      (and (nil? pool-hours*) (or start-time end-time))
      "This inventory pool does not offer reservations by the hour."

      (nil? pool-hours*) nil

      (not= (nil? start-time) (nil? end-time))
      "Both startTime and endTime are required for a reservation by the hour."

      :else
      (let [limits (group-limits tx user-id pool-id)
            max-hours* (max-hours limits)
            [from to :as period*] (period pool-hours* start-date end-date
                                          start-time end-time)
            now (time/local-now tx)]
        (cond
          (empty? limits)
          "Only members of a group of the inventory pool may reserve in it."

          (and start-time (not (and (on-step? start-time) (on-step? end-time))))
          (str "The times have to be in steps of " step-minutes " minutes.")

          (and start-time
               ;; picked up before closing, returned from opening on
               (not (and (within? pool-hours* start-time)
                         (within? pool-hours* end-time)
                         (jt/before? start-time (second pool-hours*)))))
          "The times have to be within the hours of the inventory pool."

          (not (jt/before? from to))
          "The reservation has to end after it starts."

          ;; the current step can still be booked, a whole day until closing
          (if start-time
            (not (jt/after? (jt/plus from (jt/minutes step-minutes)) now))
            (not (jt/after? (date-time start-date (second pool-hours*)) now)))
          "The time of pick up is already over."

          (and max-hours* (> (hours period*) max-hours*))
          (str "Reservations are limited to " max-hours* " hours.")

          :else nil)))))

;;; GraphQL

(defn max-reservation-hours
  "The longest reservation the target user may make in the pool, nil without
  a limit."
  [{{tx :tx} :request user-id ::target-user/id} _ {pool-id :id}]
  (max-hours (group-limits tx user-id pool-id)))

(defn is-member-of-a-group?
  [{{tx :tx} :request user-id ::target-user/id} _ {pool-id :id}]
  (boolean (seq (group-limits tx user-id pool-id))))
