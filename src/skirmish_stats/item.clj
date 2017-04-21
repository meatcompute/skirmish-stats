(ns skirmish-stats.item)

(defn spec []
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

