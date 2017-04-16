(ns skirmish-stats.core
  (:require [compojure.core :refer [routes GET POST]]
            [compojure.route :as route]
            [hiccup.core :as hiccup]
            [org.httpkit.server :as http-kit]
            [ring.middleware.defaults :as defaults]))

#_(def db-spec {:classname "org.postgresql.Driver"
              :subprotocol "postgresql"
              :subname "//localhost:5432/skirmish-stats"
              :user "mkcp"})

#_(defqueries "db.sql" {:connection db-spec})

(defn killmail-submit-form []
  [:form
   {:method "POST" :action "/killmails"}
   [:label
    {:name "url"}
    "CREST Link"]
   [:input
    {:name "url"
     :type "url"
     :placeholder "example.com/"}]])

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

(defn new-killmail
  [ring-req]
  (let [{:keys [body]} ring-req]
    (pr ring-req)))

(defn ring-routes
  "Takes a client request and routes it to a handler."
  []
  (routes
   (GET "/" ring-req (index ring-req))
   (POST "/killmails" ring-req (new-killmail ring-req))
   (route/not-found "<h1>Route not found, 404 :C</h1>")))

(defn ring-handler
  "FIXME: Wrap with ring-defaults in live"
  []
  (ring-routes))

(defn start-http []
  (let [port 10069
        stop-fn (http-kit/run-server (ring-handler) {:port port})]
    stop-fn))
