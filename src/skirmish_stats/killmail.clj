(ns skirmish-stats.killmail)

(defn schema []
  {:killID java.lang.Integer
   :killID_str java.lang.String
   :attackers [{:shipType {:id_str java.lang.String
                           :href java.lang.String
                           :id java.lang.Integer
                           :name java.lang.String
                           :icon {:href java.lang.String}}
                :corporation {:id_str java.lang.String
                              :href java.lang.String
                              :id java.lang.Integer
                              :name java.lang.String
                              :icon {:href java.lang.String}}
                :character {:id_str java.lang.String
                            :href java.lang.String
                            :id java.lang.Integer
                            :name java.lang.String
                            :icon {:href java.lang.String}}
                :damageDone_str java.lang.String
                :weaponType {:id_str java.lang.String
                             :href java.lang.String
                             :id java.lang.Integer
                             :name java.lang.String
                             :icon {:href java.lang.String}}
                :finalBlow java.lang.Boolean
                :securityStatus java.lang.Double
                :damageDone java.lang.Integer}]
   :war {:href java.lang.String
         :id java.lang.Integer
         :id_str java.lang.String}
   :victim {:damageTaken java.lang.Integer
            :items [{:singleton java.lang.Integer
                     :itemType {:id_str java.lang.String
                                :href java.lang.String
                                :id java.lang.Integer
                                :name java.lang.String
                                :icon {:href java.lang.String}}
                     :quantityDropped_str java.lang.String
                     :flag java.lang.Integer
                     :singleton_str java.lang.String
                     :quantityDropped java.lang.Integer
                     :flag_str java.lang.String
                     :quantityDestroyed_str java.lang.String
                     :quantityDestroyed java.lang.Integer}]
            :damageTaken_str java.lang.String
            :character {:id_str java.lang.String
                        :href java.lang.String
                        :id java.lang.Integer
                        :name java.lang.String
                        :icon {:href java.lang.String}}
            :shipType {:id_str java.lang.String
                       :href java.lang.String
                       :id java.lang.Integer
                       :name java.lang.String
                       :icon {:href java.lang.String}}
            :corporation {:id_str java.lang.String
                          :href java.lang.String
                          :id java.lang.Integer
                          :name java.lang.String
                          :icon {:href java.lang.String}}
            :position {:y java.lang.Double
                       :x java.lang.Double
                       :z java.lang.Double}}
   :solarSystem {:id_str java.lang.String
                 :href java.lang.String
                 :id java.lang.Integer
                 :name java.lang.String}
   :attackerCount java.lang.Integer
   :attackerCount_str java.lang.String
   :killTime java.lang.String})
