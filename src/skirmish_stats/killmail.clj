(ns skirmish-stats.killmail)

(defn ship-type []
  {:id_str java.lang.String
   :href java.lang.String
   :id java.lang.Integer
   :name java.lang.String
   :icon {:href java.lang.String}})

(defn character []
  {:id_str java.lang.String
   :href java.lang.String
   :id java.lang.Integer
   :name java.lang.String
   :icon {:href java.lang.String}})

(defn corporation []
  {:id_str java.lang.String
   :href java.lang.String
   :id java.lang.Integer
   :name java.lang.String
   :icon {:href java.lang.String}})

(defn attacker []
  {:shipType (ship-type)
   :corporation (corporation)
   :character (character)
   :damageDone_str java.lang.String
   :weaponType {:id_str java.lang.String
                :href java.lang.String
                :id java.lang.Integer
                :name java.lang.String
                :icon {:href java.lang.String}}
   :finalBlow java.lang.Boolean
   :securityStatus java.lang.Double
   :damageDone java.lang.Integer})

(defn item []
  {:singleton java.lang.Integer
   :itemType {:id_str java.lang.String
              :href java.lang.String
              :id java.lang.Integer
              :name java.lang.String
              :icon {:href java.lang.String}}
   :flag java.lang.Integer
   :flag_str java.lang.String
   :singleton_str java.lang.String
   :quantityDropped java.lang.Integer
   :quantityDropped_str java.lang.String
   :quantityDestroyed java.lang.Integer
   :quantityDestroyed_str java.lang.String})

(defn position []
  {:y java.lang.Double
   :x java.lang.Double
   :z java.lang.Double})

(defn schema []
  {:killID java.lang.Integer
   :killID_str java.lang.String
   :attackers [(attacker)]
   :war {:href java.lang.String
         :id java.lang.Integer
         :id_str java.lang.String}
   :victim {:damageTaken java.lang.Integer
            :items [(item)]
            :damageTaken_str java.lang.String
            :character (character)
            :shipType (ship-type)
            :corporation (corporation)
            :position (position)}
   :solarSystem {:id_str java.lang.String
                 :href java.lang.String
                 :id java.lang.Integer
                 :name java.lang.String}
   :attackerCount java.lang.Integer
   :attackerCount_str java.lang.String
   :killTime java.lang.String})
