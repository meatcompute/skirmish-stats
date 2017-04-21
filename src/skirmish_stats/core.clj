(ns skirmish-stats.core
  (:require [skirmish-stats.killmail :as km]
            [skirmish-stats.db :as db]
            [clojure.core.async :refer [<!!]]
            [clojure.walk :as walk]
            [compojure.core :refer [routes GET POST]]
            [compojure.route :as route]
            [hiccup.core :as hiccup]
            [org.httpkit.server :as server]
            [cheshire.core :as cheshire]
            [mount.core :refer [defstate]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [ring.middleware.defaults :as defaults]))

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

(defn killmails-component
  "FIXME Maybe pull instead of query?"
  []
  (let [kms (<!! (km/get-all db/conn))]
    [:ul
     (for [km kms]
       [:li (pr-str km)])]))

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
    (killmail-submit-form)
    (killmails-component)]))

(defn new-killmail [req]
  (let [url         (get-in req [:params :url])
        killmail    (km/parse url)
        transaction (<!! (km/write db/conn killmail))]
    (ring.util.response/redirect (get-in req [:headers "referer"]))))

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

(defstate http
  :start (let [port 10069
               stop-fn (server/run-server (ring-handler) {:port port})]
           stop-fn)
  :stop (http))

(mount.core/start)
