(ns k-means.core
  (:gen-class)
  (:use k-means.utils))

(defn update-centers
  "The fundamental k-means step.
  Return updated centers, given points and centers."
  [points centers]
  (let [center-clusters (points-to-centers points centers)
        updated-centers (vec (map center-of-points center-clusters))

        ;; Use old center if new center if empty
        filtered-centers (vec (map (fn [new-center old-center] (if (empty? new-center) old-center new-center)) updated-centers centers))]
    filtered-centers))

;; TODO somehow make this lazy. I want some kind of interface where I can stop
;; the k-means run, print out the current state, then wait for a "continue" call
;; to do the next step. Maybe it should just be an update method (above) that I
;; call in my demo multiple times. Then the k-means below is simply a helper to
;; run k-means with 10 iterations.

(defn k-means
  "Returns list of lists, categorized by k-means"
  [points k]
  (if (> k (count points))
    (println "Does not support k higher than number of points")
    (let [starting-centers (generate-k-non-repeating-samples points k)]
      (loop [current-centers starting-centers
             iters 10]
        (if (<= iters 0)
          current-centers
          (recur (update-centers points current-centers) (dec iters)))))))

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

