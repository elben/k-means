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
          (let [center-clusters (points-to-centers points current-centers)
                updated-centers (vec (map center-of-points center-clusters))]
            (recur updated-centers (dec iters))))))))

(defn generate-point
  "Generates a dim-dimension point"
  ([dim]
   (generate-point dim 100))
  ([dim max-size]
   (vec (repeatedly dim #(rand-int max-size)))))

(defn generate-2d-point
  [min-x max-x min-y max-y]
  (let [diff-x (- max-x min-x)
        diff-y (- max-y min-y)
        x (+ min-x (rand-int diff-x))
        y (+ min-y (rand-int diff-y))]
    [x y]))

(defn generate-points
  ([n dim]
   (vec (repeatedly n #(generate-point dim))))
  ([n dim max-size]
   (vec (repeatedly n #(generate-point dim max-size)))))


(defn generate-2d-points [n min-x max-x min-y max-y]
  (vec (repeatedly n #(generate-2d-point min-x max-y min-x max-x))))

(def pts (vec (concat
                (generate-2d-points 400 0 100 0 100)
                (generate-2d-points 500 120 150 120 300)
                (generate-2d-points 450 300 400 300 400))))

