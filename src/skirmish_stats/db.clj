(ns skirmish-stats.db
  (:require [mount.core :refer [defstate]]
            [datomic.client :as d]
            [clojure.core.async :refer [<!!]]))

(defstate conn
  :start (<!! (d/connect
               {:db-name "hello"
                :account-id d/PRO_ACCOUNT
                :secret "pihasfy83uhs"
                :region "none"
                :endpoint "localhost:8998"
                :service "peer-server"
                :access-key "ohiuygtfrdyfu32hjk32"})))

(defstate schema
  :start (slurp "resources/db/schema.edn"))

(defn schema-load []
  (d/transact conn {:tx-data schema}))
