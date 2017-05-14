(ns skirmish-stats.fleet)

(defn slot-spec []
  {:slot/pilot java.lang.String
   :slot/ship java.lang.String
   :slot/points java.lang.Long
   :slot/dps java.lang.Double})

(defn spec []
  {::id java.lang.Long
   ::name java.lang.String
   ::points java.lang.Long
   ::point-limit java.lang.Long
   ::slot-limit java.lang.Long
   ::slot [slot-spec]})
