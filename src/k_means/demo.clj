(ns k-means.demo
  (:use k-means.core
        k-means.utils
        quil.core))

;; K-means Demo
;;
;; Left click and drag to spray points.
;; Right click to add a center (you may add up to six).
;; [Space] to take a step.
;;
;; Enjoy!

;;;;;;;;;;;;;
;; Config
;;;;;;;;;;;;;

(def brush-size 20)

(def window-size [640 480]) ;; width, height

;; red, green, blue, yellow, purple, teal
(def colors [[211 48 48] [103 165 48] [20 136 255] [218 215 128] [158 0 226] [66 208 230]])

;;;;;;;;;;;;;
;; Data
;;;;;;;;;;;;;

(defn center-window-point []
  (vec (map #(/ % 2) window-size)))

(defn reset []
  ;; The data points.
  (def points (ref []))
  ;; The centers. Default to one center in middle of window.
  (def centers (ref [(center-window-point)])))

(reset)

;; Vector (size is number of centers) of vector containing points.
;;
;; Example, if there are two centers:
;; [[points in center 1] [points in center 2]]
;;
;; We save this in a global scope so that the drawing function doesn't have to
;; calculate the points to center mapping every frame rate.
;;
;; Assumes ordering matters in relation to centers. That is, (center-clusters 0)
;; is tied to (center 0), and so on.
(def center-clusters (points-to-centers @points @centers))

;;;;;;;;;;;;;
;; Drawing
;;;;;;;;;;;;;

(defn draw-point
  "Draws a data point"
  ([x y] (ellipse x y 5 5)))

(defn draw-center
  "Draws a center"
  ([x y] (ellipse x y 20 20)))

(defn draw-brush []
  "Draws the spray brush"
  (let [[x y] @(state :mouse-position)]
    (stroke-weight 2)
    (stroke 255)
    (fill 0)
    (ellipse x y brush-size brush-size)))

(defn draw-centers []
  (stroke-weight 2)
  (stroke 255)
  (fill 0 255 0)
  (doseq [[idx center] (map vector (iterate inc 0) @centers)]
    (apply fill (colors idx))
    (draw-center (first center) (last center))))

(defn draw-center-clusters []
  ;; For each center cluster, draw each point according to color of center.
  (doseq [[idx clusters] (map vector (iterate inc 0) center-clusters)]
    (doseq [pt clusters
            :let [x (first pt)
                  y (last pt)]]
      (stroke-weight 0)
      (stroke 255)
      (apply fill (colors idx))
      (draw-point x y))))

;;;;;;;;;;;;;
;; Data changes
;;;;;;;;;;;;;

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
    (dosync (alter points #(vec (concat % new-points))))))

(defn add-center []
  "Add a center"
  (let [x (mouse-x)
        y (mouse-y)]
    (if (< (count @centers) (count colors))
      (dosync (alter centers #(conj % [x y]))))))

(defn points-changed []
  "Update center clusters"
  (def center-clusters (points-to-centers @points @centers)))

;;;;;;;;;;;;;
;; Input
;;;;;;;;;;;;;

(defn mouse-moved []
  (let [x (mouse-x)  y (mouse-y)]
    (reset! (state :mouse-position) [x y])))

(defn mouse-dragged []
  (mouse-moved)
  (case (mouse-button)
    :right ()
    :left (do (add-points)
              (points-changed))))

(defn mouse-clicked []
  (case (mouse-button)
    :right (do (add-center)
               (points-changed))
    :left ()))

(defn key-typed []
  (case (int (raw-key))
    32 ; [Space] to step
    (when-not (empty? @points)
      (dosync (ref-set centers (update-centers (ensure @points) @centers)))
      (points-changed))
    114 ; 'r' for reset
      (do (reset)
          (points-changed))
    nil))

;;;;;;;;;;;;;
;; Main
;;;;;;;;;;;;;

(defn setup []
  (smooth)
  (no-stroke)
  (frame-rate 30)
  (set-state! :mouse-position (atom [0 0])))

(defn draw
  []
  (background-float 0)
  (draw-brush)
  (draw-center-clusters)
  (draw-centers))

(defsketch k-means-demo
  :title "K-means Demo"
  :size (vec window-size)
  :setup setup
  :draw draw
  :mouse-moved mouse-moved
  :mouse-dragged mouse-dragged
  :mouse-clicked mouse-clicked
  :key-typed key-typed)
