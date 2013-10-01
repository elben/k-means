(ns cljplay.demo
  (:use cljplay.core quil.core))

; (def frame (java.awt.Frame.))

; (def pts (vec (concat
;                 (generate-2d-points 400 0 100 0 100)
;                 (generate-2d-points 500 120 150 120 300)
;                 (generate-2d-points 450 300 400 300 400))))

(def pts (generate-2d-points 500 100 200 100 500))


; Fails update with this center for some reason!
; (def centers [[208 151] [31 489] [323 27]])
; (def centers (generate-2d-points 3 0 200 0 200))


; But using the sample centers work. I wonder if it's boundry issues?
(def centers (cljplay.utils/generate-k-non-repeating-samples pts 3))

;; Call defsketch, then repeat this call to see it move!
(def centers (update-centers pts centers))

(defn setup []
  (smooth)
  (frame-rate 10)
  (background 0))

(defn draw-point [x y]
  (ellipse x y 3 3))

(defn draw-center [x y]
  (ellipse x y 10 10))

(defn draw []
  (stroke 255)
  (stroke-weight 1)
  (fill 100)
  (background 0)

  (dorun
    (for [pt pts
          :let [x (first pt)
                y (last pt)]]
      (draw-point x y)))
  (dorun
    (for [center centers
          :let [x (first center)
                y (last center)]]
      (do
        (stroke 255 0 0)
        (draw-center x y)))))

(defsketch example                  ;;Define a new sketch named example
  :title "Oh so many grey circles"  ;;Set the title of the sketch
  :setup setup                      ;;Specify the setup fn
  :draw draw                        ;;Specify the draw fn
  :size [500 500])                  ;;You struggle to beat the golden ratio

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
