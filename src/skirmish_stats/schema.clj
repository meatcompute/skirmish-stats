(ns skirmish-stats.schema
  "Defines schemas for every EVE object present in a killmail. Collections are plural and are
  always homogenous vectors of EVE objects.")

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

#_(defn attacker []
    {:shipType (ship-type)
     :corporation (corporation)
     :character (character)
     :damageDone_str java.lang.String
     :weaponType (weapon-type)
     :finalBlow java.lang.Boolean
     :securityStatus java.lang.Double
     :damageDone java.lang.Integer})

(defn item-type []
  {:id_str java.lang.String
   :href java.lang.String
   :id java.lang.Integer
   :name java.lang.String
   :icon {:href java.lang.String}})

(defn item []
  {:singleton java.lang.Integer
   :itemType (item-type)
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

(defn ship-type []
  {:id_str java.lang.String
   :href java.lang.String
   :id java.lang.Integer
   :name java.lang.String
   :icon {:href java.lang.String}})

(defn victim []
  {:damageTaken java.lang.Integer
   :items [(item)]
   :damageTaken_str java.lang.String
   :character (character)
   :shipType (ship-type)
   :corporation (corporation)
   :position (position)})

(defn solar-system []
  {:id_str java.lang.String
   :href java.lang.String
   :id java.lang.Integer
   :name java.lang.String})

(defn war []
  {:href java.lang.String
   :id java.lang.Integer
   :id_str java.lang.String})

(defn killmail []
  {:killID java.lang.Integer
   :killID_str java.lang.String
   :attackers [(attacker)]
   :war (war)
   :victim (victim)
   :solarSystem (solar-system)
   :attackerCount java.lang.Integer
   :attackerCount_str java.lang.String
   :killTime java.lang.String})

(defn comp []
  {:id java.lang.Integer
   :name java.lang.String
   :ship-types [(ship-type)]})

(defn match []
  {:id java.lang.Integer
   :name java.lang.String
   :datetime java.lang.String
   :characters [(character)]})

(defn team []
  {:id java.lang.Integer
   :name java.lang.String
   :characters [(character)]
   :matches [(match)]})
