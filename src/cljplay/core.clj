;; K-means. Playing around to get N-dimensional k-means.
;; Started September 20, 2013.

(ns cljplay.core
  (:gen-class)
  (:use clojure.test))

;; Helper methods

(defn add-to-coll
  ([] [])
  ([coll vv] ; Assume coll and vv have same size.
   "Returns a new coll that has v appended to it"
   (map-indexed (fn [idx dim-coll] (conj dim-coll (vv idx))) coll)))

(deftest test-add-to-coll
  (is (= [] (add-to-coll)))
  (is (= [] (add-to-coll [] [1 2 3 4]))) ; a 4-D point
  (is (= [[1] [2] [3] [4]] (add-to-coll [[] [] [] []] [1 2 3 4])))
  (is (= [[1 100] [2 200] [3 300] [4 400]]
         (add-to-coll [[1] [2] [3] [4]] [100 200 300 400])))
  (is (= [[1 1] [2] [3] [4]] (add-to-coll [[1] [] [] []] [1 2 3 4]))))

(defn transform-points [points]
  "Transform points to dimension buckets"
  (let [n-dim (count (first points))
        init-coll (repeat n-dim [])]
    (reduce add-to-coll init-coll points)))

(deftest test-transform-points
  (is (= [] (transform-points [])))
  (is (= [[1 3 -3 13] [2 5 4.5 1] [4 222 42 0]]
         (transform-points [[1 2 4] [3 5 222] [-3 4.5 42] [13 1 0]])))
  (is (= [[1 2] [1 2] [1 2] [1 2]] (transform-points [[1 1 1 1] [2 2 2 2]]))))

(defn find-min-max-per-dimension
  ([] [])
  ([points]
   (let [dims (transform-points points)
         mins (map #(apply min %) dims)
         maxs (map #(apply max %) dims)]
     [mins maxs])))

(deftest test-find-min-max-per-dimension
  (is (= [] (find-min-max-per-dimension)))
  (is (= [[] []] (find-min-max-per-dimension [])))
  (is (= [[-1 -2 -3] [100 200 300]] (find-min-max-per-dimension [[-1 -2 300] [100 200 -3]]))))

(defn random-point
  [mins-maxs]
  "Given a list of plane boundaries in N-dimension, build a random point."
  (let [rand-over-min-max (fn [dim-min dim-max] (+ dim-min (rand (- dim-max dim-min))))]
    (apply map rand-over-min-max mins-maxs)))

(defn sample-k-random
  [points k]
  (repeatedly k #(rand-nth points)))

(defn expt [base pow] (reduce * (repeat pow base)))

(defn euclid-distance
  ([ps] (euclid-distance (first ps) (last ps)))
  ([p1 p2]
   "Find Euclidian distance between two vectors p1 and p2"
   (Math/sqrt (apply + (map #(expt % 2) (map - p1 p2))))))

(defn equal-enough [f1 f2] (< (Math/abs (- f1 f2)) 0.001))

(deftest test-euclid-distance
  (is (= 0.0 (euclid-distance [0 0 0] [0 0 0])))
  (is (= 5.0 (euclid-distance [0 0] [3 4])))
  (is (= 5.0 (euclid-distance [[0 0] [3 4]])))
  (is (equal-enough 1.732 (euclid-distance [0 0 0] [1 1 1]))))

(defn find-closest
  [p centers]
  "Returns index of center that p is closest to."
  (let [pointer-center-pairs (apply vector (map (fn [p center] [p center]) (repeat (count centers) p) centers))
        euclid-distances (map euclid-distance pointer-center-pairs)
        min-distance (apply min euclid-distances)]
    (.indexOf euclid-distances min-distance)))

(def centers [[50 50] [100 100]])
(def p [55 55])

(deftest test-find-closest
  (is (= 0 (find-closest [55 55] [[50 50] [100 100]])))
  (is (= 1 (find-closest [99 99] [[50 50] [100 100]]))))

(defn find-centers
  [points centers]
  "Find closest center for each point")

(defn k-means
  "Returns list of lists, categorized by k-means"
  [points k]
  (let [dim (count (first points))
        starting-centers (sample-k-random points k)]
    (loop [current-centers starting-centers
           iters 10
           center-buckets (repeat dim [])]
      (if (> iters 0)
        ((group-by (fn [p] (find-closest p current-centers)) points)
         ;; for each point
            ;; find nearest cluster and separate out to different clusters.
          ;; then find new k cluster centers
         (recur updated-centers (incr iters)))))))

(def points [[10 10] [20 20] [100 100]])
(def current-centers [[0 0] [120 120]])
(group-by (fn [p] (find-closest p current-centers)) points)


(defn generate-point
  [n-dim]
  "Generates an n-dim point"
  (repeatedly n-dim #(rand-int 100)))

(defn generate-points
  [n-dim n]
  (repeatedly n #(generate-point n-dim)))


; (defn k-means
;   "Returns list of lists, categorized by k-means"
;   [points n]
;   (let [mins-maxs (find-min-max-per-dimension points)
;         random-points (repeat n (random-point mins-maxs))]
;     random-points))

;; Write method for checking monte hall problem.


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!" args))


