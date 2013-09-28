(ns cljplay.utils)

(defn add-to-coll
  ([] [])
  ([coll point] ; Assume coll and point have same size.
   "Returns a new coll that has v appended to it"
   (vec (map #(conj (first %) (last %)) (map vector coll point)))))

(defn reduce-by-dimension [points]
  "Transform points to dimension buckets"
  (let [dim (count (first points))
        init-coll (vec (repeat dim []))]
    (reduce add-to-coll init-coll points)))

(defn find-min-max-per-dimension
  ([] [])
  ([points]
   (let [dims (reduce-by-dimension points)
         mins (map #(apply min %) dims)
         maxs (map #(apply max %) dims)]
     [mins maxs])))

(defn random-point
  [mins-maxs]
  "Given a list of plane boundaries in N-dimension, build a random point."
  (let [rand-over-min-max (fn [dim-min dim-max] (+ dim-min (rand (- dim-max dim-min))))]
    (apply map rand-over-min-max mins-maxs)))

(defn sample-k-random
  [points k]
  (repeatedly k #(rand-nth points)))

(defn generate-k-non-repeating-samples
  [points k]
  "Generate k non-repeating samples from points"
  (loop [num-samples 0
         samples []]
    (let [random-point (rand-nth points)]
      (cond
        (= num-samples k) samples ; Got all samples.

        ; random-point already exists. Try again.
        (some #(= random-point %) samples) (recur num-samples samples)

        ; random-point not in samples yet. Include it.
        :else (recur (inc num-samples) (conj samples random-point))))))
    

(defn expt [base pow] (reduce * (repeat pow base)))

(defn euclid-distance
  ([ps] (euclid-distance (first ps) (last ps)))
  ([p1 p2]
   "Find Euclidian distance between two vectors p1 and p2"
   (Math/sqrt (apply + (map #(expt % 2) (map - p1 p2))))))

(defn equal-enough [f1 f2] (< (Math/abs (- f1 f2)) 0.001))

(defn find-closest
  [point centers]
  "Returns index of center that point is closest to, by Euclidian distance."
  (let [pointer-center-pairs (for [point (list point) center centers] [point center])
        euclid-distances (map euclid-distance pointer-center-pairs)
        min-distance (apply min euclid-distances)]
    (.indexOf euclid-distances min-distance)))

(defn center-of-points [points]
  "Return the mean center of the given points"
  (let [dim (count (first points))]
  (vec (map / (apply map + points) (repeat dim (double (count points)))))))

(defn points-to-centers
  [points centers]
  "Returns a vector of centers of points, given a sequence of points and centers."
  (let [init-center-batches (vec (repeat (count centers) []))]
    (reduce (fn [center-batches p]
              (let [closest-center (find-closest p centers)
                    center-batch (nth center-batches closest-center)
                    updated-center-batch (conj center-batch p)]
                (assoc center-batches closest-center updated-center-batch)))
            init-center-batches
            points)))

