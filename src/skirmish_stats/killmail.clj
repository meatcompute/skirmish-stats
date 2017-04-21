(ns skirmish-stats.killmail
  (:require [skirmish-stats.character :as c]
            [skirmish-stats.attacker :as a]
            [skirmish-stats.ship :as s]
            [datomic.client :as d]
            [org.httpkit.client :as client]
            [cheshire.core :as cheshire]
            [clojure.walk :as walk]))

(defn generate-url
  "Returns a CREST URL to pull valid test data.
  Restructures a bunch of constants for now, can easily be paramaterized."
  []
  (let [protocol "https://"
        domain "crest-tq.eveonline.com/"
        path "killmails/"
        kill-id "61403482/"
        ;; FIXME: What is this CREST id?
        id "a53510250504dcc6d43c9b32298b11b9b98c2d51/"]
    (str protocol domain path kill-id id)))

(def all-query
  '[:find ?e
    :where
    [?e :killmail/id]])

(defn get-all
  [conn]
  (d/q conn
       {:query all-query
        :args [(d/db conn)]}))

(defn scrape
  "GETs the contents of the url, FIXME should handle errors better"
  [url]
  (let [res (client/get url)
        {:keys [status] :as res} @res]
    (if (= 200 status)
      (:body res)
      {:error status})))

(defn json->edn
  "Convert JSON body to clj data.
  TODO rename keys to be snake case instead of the mess of data CREST is throwing."
  [body]
  (-> body
      cheshire/parse-string
      walk/keywordize-keys))

(defn validate-time [x]
  (let [formatter (java.text.SimpleDateFormat. "yyyy.MM.dd hh:mm:ss")
        date (.parse formatter x)]
    date))

(defn add-namespace
  [killmail]
  (let [id             (:killID killmail)
        attacker-count (:attackerCount killmail)
        time           (validate-time (:killTime killmail))]
    {:killmail/id id
     :killmail/attacker-count attacker-count
     :killmail/time time}))

(defn write
  [conn killmail]
  (d/transact conn {:tx-data [killmail]}))

(defn parse
  [url]
  (-> url scrape json->edn add-namespace))

(defn item-spec []
  {:singleton java.lang.Integer
   :itemType {:href java.lang.String
              :id java.lang.Integer
              :name java.lang.String
              :icon {:href java.lang.String}}
   :flag java.lang.Integer
   :quantityDropped java.lang.Integer
   :quantityDestroyed java.lang.Integer})

(defn spec
  "TODO Produce a clojure spec from this and use to parse and validate inbound data."
  []
  {:killID java.lang.Integer
   :attackerCount java.lang.Integer
   :killTime java.lang.String
   :solarSystem {:href java.lang.String
                 :id java.lang.Integer
                 :name java.lang.String}
   :attackers [(a/spec)]
   :victim {:damageTaken java.lang.Integer
            :items [(item-spec)]
            :damageTaken_str java.lang.String
            :character (c/spec)
            :shipType (s/spec)
            :corporation {:href java.lang.String
                          :id java.lang.Integer
                          :name java.lang.String
                          :icon {:href java.lang.String}}
            :position {:y java.lang.Double
                       :x java.lang.Double
                       :z java.lang.Double}}})
