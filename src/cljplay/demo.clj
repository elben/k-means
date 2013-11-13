(ns cljplay.demo
  (:use cljplay.core
        cljplay.utils
        quil.core))

;; This demo will work like this:
;;
;; When you start up the program, it loads the demo main.
;;
;; In the demo program, you can be in either the 'edit' mode or the 'run' mode
;;
;; Edit mode
;; - Add points via holding down mouse button
;; - Clear all points
;; - Add centers
;; - Clear all centers
;;
;; Run mode
;; - Run a step, using the centers
;; 

(def brush-size 20)

(def points [])

(defn setup []
  (smooth)
  (no-stroke)
  (frame-rate 30)
  (set-state! :mouse-position (atom [0 0])))

(defn draw-brush []
  (let [[x y] @(state :mouse-position)]
    (stroke-weight brush-size)
    (stroke-float 10)
    (point x y)))

(defn draw-point
  ([x y] (ellipse x y 5 5)))

(defn draw-points []
  (stroke-weight 2)
  (stroke 0)
  (fill 255 255 255)
  (doseq [p points]
    (draw-point (first p) (last p))))

(defn mouse-moved []
  (let [x (mouse-x)  y (mouse-y) btn (mouse-button)]
    (reset! (state :mouse-position) [x y])))

(defn jittered-point [x y]
  "Create a point jittered by brush-sie"
  (let [point (random-point [[(+ x brush-size) (+ y brush-size)] [(- x brush-size) (- y brush-size)]])]
    ;; If negative, make 0.
    (vec (map #(if (< % 0) 0 %) point))))

(defn add-points []
  "Add three random jittered points to points"
  (let [x (mouse-x)
        y (mouse-y)
        new-points (repeatedly 3 #(jittered-point x y))]
    (def points (vec (concat points new-points)))))

(defn mouse-pressed []
  (mouse-moved)
  (add-points))

(defn draw
  []
  (background-float 125)
  (draw-brush)
  (draw-points))

(defsketch k-means-demo
  :title "K-means Demo"
  :size [640 480]
  :setup setup
  :draw draw
  :mouse-moved mouse-moved
  :mouse-dragged mouse-pressed)

