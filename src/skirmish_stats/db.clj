(ns skirmish-stats.db
  (:require [mount.core :refer [defstate]]
            [datomic.client :as d]
            [clojure.core.async :refer [<!!]]))

(defstate conn
  :start (<!! (d/connect
               {:db-name "skirmish-stats"
                :account-id d/PRO_ACCOUNT
                :secret "mysecret"
                :region "none"
                :endpoint "localhost:8998"
                :service "peer-server"
                :access-key "myaccesskey"})))

(defstate schema
  :start (slurp "resources/db/schema.edn"))

(defn schema-load []
  (d/transact conn {:tx-data schema}))

(defn get [query]
  (let [chan (d/q conn {:query query :args [(d/db conn)]})
        result (if-not (d/error? chan)
                 (<!! chan)) ;; Might not be a channel.
        ]
    (cond
      (vector? result) (flatten result)
      true result)))

(defn pull-many
  [eids]
  (map #(<!! (d/pull (d/db conn) {:selector '[*] :eid %}))
       eids))

(defstate test-data
  :start [{:match/opponent "Not spooky"
           :match/pilot "Mayrin"}
          {:match/opponent "Not spooky"
           :match/pilot "Vordak"}
          {:match/opponent "Not spooky"
           :match/pilot "Tau"}
          {:match/opponent "Not spooky"
           :match/pilot "W4r"}])
