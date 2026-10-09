(ns leihs.borrow.resources.time-slots
  (:require [honey.sql :refer [format] :rename {format sql-format}]
            [honey.sql.helpers :as sql]
            [next.jdbc.sql :refer [query] :rename {query jdbc-query}]))

(def columns [:time_slots.id
              :time_slots.name
              :time_slots.start_time
              :time_slots.end_time])

(defn base-sqlmap [pool-id]
  (-> (apply sql/select columns)
      (sql/from :time_slots)
      (sql/where [:= :inventory_pool_id pool-id])
      (sql/order-by :position :start_time :name)))

(defn get-by-pool-id [tx pool-id]
  (-> pool-id
      base-sqlmap
      sql-format
      (->> (jdbc-query tx))))

(defn get-multiple [{{tx :tx} :request} _ {pool-id :id}]
  (get-by-pool-id tx pool-id))

(defn offered?
  "Whether the pool offers a time slot with exactly this window."
  [tx pool-id start-time end-time]
  (-> (sql/select true)
      (sql/from :time_slots)
      (sql/where [:= :inventory_pool_id pool-id]
                 [:= :start_time [:cast start-time :time]]
                 [:= :end_time [:cast end-time :time]])
      sql-format
      (->> (jdbc-query tx))
      seq
      boolean))
