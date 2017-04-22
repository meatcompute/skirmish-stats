(ns skirmish-stats.killmail
  (:require [skirmish-stats.character :as c]
            [skirmish-stats.attacker :as a]
            [skirmish-stats.ship :as s]
            [clojure.core.async :refer [<!!]]
            [datomic.client :as d]
            [org.httpkit.client :as client]
            [cheshire.core :as cheshire]
            [clojure.walk :as walk]))

(defn item-spec []
  {:item/singleton java.lang.Integer
   :item/type {:item-type/href java.lang.String
               :item-type/id java.lang.Integer
               :item-type/name java.lang.String
               :item-type/icon {:icon/href java.lang.String}}
   :item/flag java.lang.Integer
   :item/quantity-dropped java.lang.Integer
   :item/quantity-destroyed java.lang.Integer})

(defn solar-system-spec []
  {:solar-system/href java.lang.String
   :solar-system/id java.lang.Integer
   :solar-system/name java.lang.String})

(defn spec
  "TODO Produce a clojure spec from this and use to parse and validate inbound data."
  []
  {::id java.lang.Integer
   ::attacker-count java.lang.Integer
   ::kill-time java.lang.String
   ::damageTaken java.lang.Integer
   ::solar-system (solar-system-spec)
   ::attackers [(a/spec)]
   ::items [(item-spec)]
   ::character (c/spec)
   ::shipType (s/spec)
   ::corporation {:corporation/href java.lang.String
                  :corporation/id java.lang.Integer
                  :corporation/name java.lang.String
                  :corporation/icon {:href java.lang.String}}
   ::position {:position/y java.lang.Double
               :position/x java.lang.Double
               :position/z java.lang.Double}})

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

(def all-uris
  '[:find ?uri
    :where [_ :killmail/uri ?uri]])

(def all-eids
  '[:find ?e
    :where [?e :killmail/id]])

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

(defn validate
  "Add spec validation"
  [url killmail]
  (let [id             (:killID killmail)
        attacker-count (:attackerCount killmail)
        time           (validate-time (:killTime killmail))
        damage-taken   (get-in killmail [:victim :damageTaken])
        uri            (java.net.URI. url)]
    {:killmail/id id
     :killmail/attacker-count attacker-count
     :killmail/time time
     :killmail/damage-taken damage-taken
     :killmail/uri uri}))

(defn write
  "FIXME Add error handling."
  [conn killmail]
  (let [result (<!! (d/transact conn {:tx-data [killmail]}))]
    (println result)
    result))

(defn parse [url]
  (let [json (scrape url)
        edn (json->edn json)]
    (validate url edn)))
