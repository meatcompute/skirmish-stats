(ns skirmish-stats.core
  (:require [skirmish-stats.structure :as s]
            [clojure.walk :as walk]
            [compojure.core :refer [routes GET POST]]
            [compojure.route :as route]
            [hiccup.core :as hiccup]
            [org.httpkit.server :as server]
            [org.httpkit.client :as client]
            [cheshire.core :as cheshire]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [ring.middleware.defaults :as defaults]))

#_(def db-spec {:classname "org.postgresql.Driver"
              :subprotocol "postgresql"
              :subname "//localhost:5432/skirmish-stats"
              :user "mkcp"})

#_(defqueries "db.sql" {:connection db-spec})

(defn generate-url
  "Returns a CREST URL to pull valid test data.
  Restructures a bunch of constants for now, can easily be paramaterized."
  []
  (let [protocol "https://"
        domain "crest-tq.eveonline.com/"
        path "killmails/"
        ;; FIXME: Two ids here, what do they mean?
        ids "61403482/a53510250504dcc6d43c9b32298b11b9b98c2d51/"]
    (str protocol domain path ids)))

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

(defn store [killmail]
  (pr killmail))

(defn new-killmail [req]
  (let [url (get-in req [:params :url])]
    (-> url scrape json->edn store)))

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
