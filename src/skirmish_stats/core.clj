(ns skirmish-stats.core
  (:require [skirmish-stats.structure :as s]
            [clojure.core.async :refer [<!!]]
            [clojure.walk :as walk]
            [datomic.client :as d]
            [compojure.core :refer [routes GET POST]]
            [compojure.route :as route]
            [hiccup.core :as hiccup]
            [org.httpkit.server :as server]
            [org.httpkit.client :as client]
            [cheshire.core :as cheshire]
            [mount.core :refer [defstate]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [ring.middleware.defaults :as defaults]))

(defstate conn
  :start (d/connect
          {:db-name "hello"
           :account-id d/PRO_ACCOUNT
           :secret "pihasfy83uhs"
           :region "none"
           :endpoint "localhost:8998"
           :service "peer-server"
           :access-key "ohiuygtfrdyfu32hjk32"}))

(defn db-schema []
  (slurp "resources/db/schema.edn"))

(defn db-load []
  (d/transact conn {:tx-data db-schema}))

(def killmails-all
  '[:find ?e
    :where
    [?e :killmail/id]])

(defn get-all-killmails []
  (<!! (d/q conn
            {:query killmails-all
             :args [(d/db conn)]})))

(defn generate-url
  "Returns a CREST URL to pull valid test data.
  Restructures a bunch of constants for now, can easily be paramaterized."
  []
  (let [protocol "https://"
        domain "crest-tq.eveonline.com/"
        path "killmails/"
        ;; FIXME: Two ids here, what do they mean?
        kill-id "61403482/"
        id "a53510250504dcc6d43c9b32298b11b9b98c2d51/"]
    (str protocol domain path kill-id id)))

(defn killmail-submit-form
  "Provides the user with an input box to submit a killmail url."
  []
  [:form
   {:method "POST" :action "/killmails"}
   [:label
    {:name "url"}
    "CREST Link"]
   [:input
    {:name "url"
     :id "url"
     :type "url"
     :placeholder "http://example.com/"}]])

(defn killmails-component []
  (let [kms (get-killmails)]
    [:p (str kms)]
    ))

(defn index [ring-req]
  (hiccup/html
   [:head
    [:title "Skirmish Stats"]
    [:meta {:charset "UTF-8"}]
    [:meta {:name "viewport"
            :content "width=device-width, initial-scale=1"}]
    [:link {:href "css/style.css"
            :rel "stylesheet"
            :type "text/css"}]]
   [:body
    [:h1 "Skirmish Stats"]
    (killmail-submit-form)]))

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

(defn test-structure [url]
  (-> url scrape json->edn s/structure))

(defn validate-time [x]
  (let [formatter(java.text.SimpleDateFormat. "yyyy.MM.dd hh:mm:ss")
        date (.parse formatter x)]
    date))

(defn parse
  [killmail]
  (let [id             (:killID killmail)
        attacker-count (:attackerCount killmail)
        time           (validate-time (:killTime killmail))]
    {:killmail/id id
     :killmail/attacker-count attacker-count
     :killmail/time time}))

(defn write
  [killmail]
  (d/transact conn {:tx-data [killmail]}))

(defn process
  [url]
  (-> url
      scrape
      json->edn
      parse
      write))

(defn new-killmail
  "FIXME"
  [req]
  (let [url (get-in req [:params :url])
        result (<!! (process url))]
    (cond
      (not-empty result) "Success!")))

(defn ring-routes
  "Takes a client request and routes it to a handler."
  []
  (routes
   (GET "/" [] index)
   (POST "/killmails" [] new-killmail)
   (route/not-found "<h1>Route not found, 404 :C</h1>")))

(defn defaults-config []
  {:params {:urlencoded true
            :multipart true
            :nested true
            :keywordize true}
   :cookies true
   :session {:flash true
             :cookie-attrs {:http-only true}}
   :security {:anti-forgery false
              :xss-protection {:enable? true
                               :mode :block}
              :frame-options :sameorigin
              :content-type-options :nosniff}
   :static {:resources "public"}
   :responses {:not-modified-responses true
               :absolute-redirects true
               :content-types true
               :default-charset "utf-8"}})

(defn ring-handler
  "FIXME: Wrap with ring-defaults in live"
  []
  (defaults/wrap-defaults (ring-routes)
                          (defaults-config)))

(defn start-http []
  (let [port 10069
        stop-fn (server/run-server (ring-handler) {:port port})]
    stop-fn))
