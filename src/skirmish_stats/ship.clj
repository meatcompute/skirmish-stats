(ns skirmish-stats.ship
  (:require [skirmish-stats.icon :as icon]))

(defn spec []
  {::id java.lang.Integer
   ::href java.net.URI
   ::name java.lang.String
   ::icon (icon/spec)
   ::pilot java.lang.String
   ::type java.lang.String
   ::points java.lang.Long
   ::dps java.lang.Double
   })
