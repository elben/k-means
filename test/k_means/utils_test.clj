(ns k-means.utils-test
  (:use clojure.test)
  (:use k-means.utils))

(deftest test-add-to-coll
  (is (= [] (add-to-coll)))
  (is (= [] (add-to-coll [] [1 2 3 4]))) ; a 4-D point
  (is (= [[1] [2] [3] [4]] (add-to-coll [[] [] [] []] [1 2 3 4])))
  (is (= [[1 100] [2 200] [3 300] [4 400]]
         (add-to-coll [[1] [2] [3] [4]] [100 200 300 400])))
  (is (= [[1 1] [2] [3] [4]] (add-to-coll [[1] [] [] []] [1 2 3 4]))))

(deftest test-reduce-by-dimension
  (is (= [] (reduce-by-dimension [])))
  (is (= [[1 3 -3 13] [2 5 4.5 1] [4 222 42 0]]
         (reduce-by-dimension [[1 2 4] [3 5 222] [-3 4.5 42] [13 1 0]])))
  (is (= [[1 2] [1 2] [1 2] [1 2]] (reduce-by-dimension [[1 1 1 1] [2 2 2 2]]))))


(deftest test-find-min-max-per-dimension
  (is (= [] (find-min-max-per-dimension)))
  (is (= [[] []] (find-min-max-per-dimension [])))
  (is (= [[-1 -2 -3] [100 200 300]] (find-min-max-per-dimension [[-1 -2 300] [100 200 -3]]))))

(deftest test-euclid-distance
  (is (= 0.0 (euclid-distance [0 0 0] [0 0 0])))
  (is (= 5.0 (euclid-distance [0 0] [3 4])))
  (is (= 5.0 (euclid-distance [[0 0] [3 4]])))
  (is (equal-enough 1.732 (euclid-distance [0 0 0] [1 1 1]))))

(deftest test-find-closest
  (is (= 0 (find-closest [55 55] [[50 50] [100 100]])))
  (is (= 1 (find-closest [99 99] [[50 50] [100 100]]))))

(deftest test-points-to-centers
  (is (= [] (points-to-centers [] [])))

  ;; Points 10,10 and 20,20 go to center 0, and point 100,100 goes to center 1.
  (is (= [[[10 10] [20 20]] [[100 100]]]
         (points-to-centers [[10 10] [20 20] [100 100]] [[0 0] [100 100]]))))

