(defproject skirmish-stats "0.1.0"
  :description "Analytics tools for competitive EVE Online players."
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async "0.3.442"]
                 [com.datomic/clj-client "0.8.606"]
                 [hiccup "1.0.5"]
                 [yesql "0.5.3"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [http-kit "2.2.0"]
                 [ring "1.4.0"]
                 [ring/ring-defaults "0.2.0"]
                 [com.taoensso/timbre "4.3.1"]
                 [compojure "1.5.0"]
                 [cheshire "5.7.0"]
                 [mount "0.1.11"]])
