(ns skirmish-stats.attacker
  (:require [skirmish-stats.character :as c]))

(defn spec []
  {:shipType {:id_str java.lang.String
              :href java.lang.String
              :id java.lang.Integer
              :name java.lang.String
              :icon {:href java.lang.String}}
   :corporation {:id_str java.lang.String
                 :href java.lang.String
                 :id java.lang.Integer
                 :name java.lang.String
                 :icon {:href java.lang.String}}
   :character (c/spec)
   :damageDone_str java.lang.String
   :weaponType {:id_str java.lang.String
                :href java.lang.String
                :id java.lang.Integer
                :name java.lang.String
                :icon {:href java.lang.String}}
   :finalBlow java.lang.Boolean
   :securityStatus java.lang.Double
   :damageDone java.lang.Integer})
