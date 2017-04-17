(ns skirmish-stats.structure
  (:require [clojure.walk :as w]))

(defmacro both [a b pred] `(and (~pred ~a) (~pred ~b)))

(defn union-structure [a b]
  (cond (= a b)            a
        (both a b vector?) [(reduce union-structure (concat a b))]
        (both a b map?)    (merge-with union-structure a b)

        ; Dunno, said the ghoul
        true (list 'U a b)))

(defn structure [x]
  (cond (map? x)
        (into {} (map (fn [[k v]] [k (structure v)]) x))

        ; Assume vectors are homogenous and collapse them
        (vector? x) [(reduce union-structure (map structure x))]

        true
        (symbol (.getName (class x)))))

#_(structure {:people [{:name "kyle"
                        :pets [{:name "pup"
                                :type "samoyed"
                                :woof :bark}]}
                       {:name "kit"
                        :pets [{:name "woofs"
                                :type "husker"
                                :woof :arf
                                :extra? :very}]}]})
; returns
#_{:people [{:name java.lang.String
             :pets [{:extra? clojure.lang.Keyword
                     :name java.lang.String
                     :type java.lang.String
                     :woof clojure.lang.Keyword}]}]}
