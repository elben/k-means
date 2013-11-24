(ns k-means.demo
  (:use k-means.core
        k-means.utils
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

(def centers [[0 0]])

(def center-clusters (points-to-centers points centers))

(def colors [[255 0 0] [0 255 0] [0 0 255] [255 255 0] [0 255 255] [255 0 255]])

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

(defn draw-center
  ([x y] (ellipse x y 20 20)))

(defn draw-points []
  (stroke-weight 2)
  (stroke 0)
  (fill 255 255 255)
  (doseq [p points]
    (draw-point (first p) (last p))))

(defn draw-centers []
  (stroke-weight 2)
  (fill 0 255 0)
  (doseq [[idx center] (map vector (iterate inc 0) centers)]
    (apply fill (colors idx))
    (draw-center (first center) (last center))))

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

(defn add-center []
  "Add a center"
  (let [x (mouse-x)
        y (mouse-y)]
    (if (< (count centers) (count colors))
      (def centers (conj centers [x y])))))

(defn points-changed []
  (def center-clusters (points-to-centers points centers)))

(defn mouse-dragged []
  (mouse-moved)
  (case (mouse-button)
    :right :noop
    :left (do (add-points) (points-changed))))

(defn mouse-clicked []
  (case (mouse-button)
    :right (do (add-center) (points-changed))
    :left :noop))

(defn draw-center-clusters []
  (doseq [[idx clusters] (map vector (iterate inc 0) center-clusters)]
    (doseq [pt clusters
            :let [x (first pt)
                  y (last pt)]]
      (stroke-weight 1)
      (stroke 0)
      (apply fill (colors idx))
      (draw-point x y))))

(defn key-typed []
  (println (str "Pressed: " (raw-key) " key code: " (int (raw-key))))
  (if (= 32 (int (raw-key)))
    (do
      (println "space!")
      (def centers (update-centers points centers))
      (points-changed))))

(defn draw
  []
  (background-float 0)
  (draw-brush)
  ; (draw-points)
  (draw-center-clusters)
  (draw-centers))

(defsketch k-means-demo
  :title "K-means Demo"
  :size [640 480]
  :setup setup
  :draw draw
  :mouse-moved mouse-moved
  :mouse-dragged mouse-dragged
  :mouse-clicked mouse-clicked
  :key-typed key-typed)

;; Call defsketch, then repeat this call to see it move!
;; TODO This isn't working right now.
; (do
;   (def centers (update-centers points centers))
;   (def center-clusters (points-to-centers points centers)))


;; TODO
;; - Each new added center should take its own color
;;  - when you add a new center, run a step, so that all the other points are
;;    colored (this may be slow!).
;; - [Enter] should take a step.

