(ns leihs.borrow.time
  (:require [leihs.core.settings :refer [settings]]
            [taoensso.timbre :refer [debug info warn error spy]]
            [leihs.core.db :as db]
            java-time)
  (:import [java.time.format DateTimeFormatter]))

(defn time-zone [tx]
  (-> (settings tx [:time_zone])
      :time_zone
      java.util.TimeZone/getTimeZone))

(defn now [tx]
  (java-time/local-date-time (java-time/instant) (time-zone tx)))

(defn expired-time? [tx timestamp]
  (java-time/after? (now tx)
                    (java-time/local-date-time timestamp (time-zone tx))))

(defn past-date? [date]
  (java-time/after?
   (java-time/local-date)
   (java-time/local-date DateTimeFormatter/ISO_LOCAL_DATE (.toString date))))

(defn time-slot-ended?
  "Whether the time slot ending at `end-time` (\"HH:mm[:ss]\") of `date` is
  already over (in the time zone of the settings)."
  [tx date end-time]
  (let [now* (now tx)
        date* (java.time.LocalDate/parse (str date))
        end-time* (java.time.LocalTime/parse (str end-time))]
    (not (java-time/after? (java-time/local-date-time date* end-time*) now*))))
