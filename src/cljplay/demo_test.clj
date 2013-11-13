(ns cljplay.demo-test
  (:use cljplay.core
        cljplay.utils
        quil.core))

(def pts
  (vec (concat
         (generate-2d-points 500 40 120 60 140)
         (generate-2d-points 300 100 200 100 300)
         (generate-2d-points 240 300 400 300 500)
         (generate-2d-points 300 400 500 0 100))))


; Fails update with this center for some reason!
; (def centers [[208 151] [31 489] [323 27]])
; (def centers (generate-2d-points 3 0 200 0 200))


; But using the sample centers work. I wonder if it's boundry issues?
(def centers (cljplay.utils/generate-k-non-repeating-samples pts 3))

;; Call defsketch, then repeat this call to see it move!
(do
  (def centers (update-centers pts centers))
  (def center-clusters (points-to-centers pts centers)))

(defn setup []
  (smooth)
  (frame-rate 10)
  (background 0))

(def colors [[255 0 0] [0 255 0] [0 0 255] [255 255 0] [0 255 255] [255 0 255]])

(defn draw-point
  ([x y] (ellipse x y 3 3)))

(defn draw-center [x y]
  (ellipse x y 10 10))

(defn draw []
  (stroke 255)
  (stroke-weight 1)
  (fill 100)
  (background 0)

  (doseq [[idx clusters] (map vector (iterate inc 0) center-clusters)]
    (doseq [pt clusters
            :let [x (first pt)
                  y (last pt)]]
      (apply stroke (colors idx))
      (draw-point x y)))
  (doseq [[idx center] (map vector (iterate inc 0) centers)
          :let [x (first center)
                y (last center)]]
    (stroke 255)
    (stroke-weight 2)
    (apply fill (colors idx))
    (draw-center x y)))

(defsketch example
  :title "K-means demo"
  :setup setup
  :draw draw
  :size [500 500])

;;;;;;;;;;;;;;;;;;;;;

(defn setup []
  (smooth)
  (no-stroke)
  (set-state! :mouse-position (atom [0 0])))

(defn draw
  []
  (background-float 125)
  (stroke-weight 20)
  (stroke-float 10)
  (let [[x y] @(state :mouse-position)]
    (point x y)))

(defn mouse-moved []
  (let [x (mouse-x)  y (mouse-y)]
    (reset! (state :mouse-position) [x y])))

(defsketch mouse-example
  :title "Mouse example."
  :size [200 200]
  :setup setup
  :draw draw
  :mouse-moved mouse-moved)

