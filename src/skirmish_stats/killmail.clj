(ns skirmish-stats.killmail
  (:require [skirmish-stats.character :as c]
            [skirmish-stats.item :as i]
            [skirmish-stats.attacker :as a]
            [skirmish-stats.ship :as s]
            ))
(defn spec
  "FIXME: Use this to validate inbound data"
  []
  {:killID java.lang.Integer
   :attackerCount java.lang.Integer
   :killTime java.lang.String
   :solarSystem {:href java.lang.String
                 :id java.lang.Integer
                 :name java.lang.String}
   :attackers [(a/spec)]
   :victim {:damageTaken java.lang.Integer
            :items [(i/spec)]
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
