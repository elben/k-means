(ns cljplay.core
  (:gen-class)
  (:use cljplay.utils))

(defn k-means
  "Returns list of lists, categorized by k-means"
  [points k]
  (if (> k (count points))
    (println "Does not support k higher than number of points")
    (let [dim (count (first points))
          starting-centers (generate-k-non-repeating-samples points k)]
      (loop [current-centers starting-centers
             iters 10]
        (if (<= iters 0)
          current-centers
          (let [center-clusters (collect-points-to-centers points current-centers)
                updated-centers (vec (map center-of-points center-clusters))]
            (recur updated-centers (dec iters))))))))

(defn generate-point
  [dim]
  "Generates an dim point"
  (vec (repeatedly dim #(rand-int 100))))

(defn generate-points
  [n dim]
  (vec (repeatedly n #(generate-point dim))))
