(ns leihs.borrow.time
  (:require [leihs.core.settings :refer [settings]]
            [leihs.core.time-zone :as time-zone]
            [taoensso.timbre :refer [debug info warn error spy]]
            [leihs.core.db :as db]
            java-time)
  (:import [java.time.format DateTimeFormatter]))

(defn time-zone
  "The time zone of the settings (a Rails name like Bern or an IANA id), UTC
  if unknown."
  [tx]
  (or (-> (settings tx [:time_zone]) :time_zone time-zone/zone-id)
      (java.time.ZoneId/of "UTC")))

(defn now
  "The current date and time in UTC, for timestamps (created_at, updated_at):
  like Rails, leihs stores them in UTC."
  [_tx]
  (java-time/local-date-time (java-time/instant) (java.time.ZoneId/of "UTC")))

(defn local-now
  "The current date and time in the time zone of the settings, for comparing
  with times of day (time slots)."
  [tx]
  (java-time/local-date-time (java-time/instant) (time-zone tx)))

(defn expired-time? [tx timestamp]
  (java-time/after? (now tx)
                    (java-time/local-date-time timestamp (time-zone tx))))

(defn past-date? [date]
  (java-time/after?
   (java-time/local-date)
   (java-time/local-date DateTimeFormatter/ISO_LOCAL_DATE (.toString date))))
