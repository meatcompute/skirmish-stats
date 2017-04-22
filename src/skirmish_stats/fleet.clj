(ns skirmish-stats.fleet)

(defn composition-spec []
  {:composition/pilot java.lang.String
   :composition/ship java.lang.String
   :composition/points java.lang.Long
   :composition/dps java.lang.Double})

(defn spec []
  {::id java.lang.Long
   ::name java.lang.String
   ::points java.lang.Long
   ::point-limit java.lang.Long
   ::slot [composition]})

(defn team []
  ::id java.lang.Long
  )
