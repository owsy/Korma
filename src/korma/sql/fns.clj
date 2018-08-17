(ns korma.sql.fns
  (:require [korma.db :as db]
            [korma.sql.engine :as eng]
            [korma.sql.utils :as utils])
  (:use [korma.sql.engine :only [infix group-with sql-func trinary wrapper]]))

;;*****************************************************
;; Predicates
;;*****************************************************


(defn pred-and [& args]
  (apply eng/pred-and args))

(defn pred-or [& args] (group-with " OR " args))
(defn pred-not [v] (wrapper "NOT" v))

(defn pred-in [k v]     (infix k "IN" v))
(defn pred-not-in [k v] (infix k "NOT IN" v))
(defn pred-> [k v]      (infix k ">" v))
(defn pred-< [k v]      (infix k "<" v))
(defn pred->= [k v]     (infix k ">=" v))
(defn pred-<= [k v]     (infix k "<=" v))
(defn pred-like [k v]   (infix k "LIKE" v))
(defn pred-not-like [k v]   (infix k "NOT LIKE" v))
(defn pred-ilike [k v]  (infix k "ILIKE" v))
(defn pred-not-ilike [k v]  (infix k "NOT ILIKE" v))

(defn pred-exists [v]   (wrapper "EXISTS" v))

(defn pred-between [k [v1 v2]]
  (trinary k "BETWEEN" v1 "AND" v2))

(def pred-= eng/pred-=)
(defn pred-not= [k v] (cond
                        (and k v) (infix k "<>" v)
                        k         (infix k "IS NOT" v)
                        v         (infix v "IS NOT" k)))

;;*****************************************************
;; Aggregates
;;*****************************************************

(defn agg-count [_query_ v]
  (if (= "*" (name v))
    (sql-func "COUNT" (utils/generated "*"))
    (sql-func "COUNT" v)))

(defn agg-sum [_query_ v]   (sql-func "SUM" v))
(defn agg-avg [_query_ v]   (sql-func "AVG" v))
(defn agg-stdev [_query_ v] (sql-func "STDEV" v))
(defn agg-min [_query_ v]   (sql-func "MIN" v))
(defn agg-max [_query_ v]   (sql-func "MAX" v))
(defn agg-first [_query_ v] (sql-func "FIRST" v))
(defn agg-last [_query_ v]  (sql-func "LAST" v))
