;; To run tests:
;;
;; lein test
;;
;; Or, in REPL:
;;
;; (ns k-means.core-test)
;; (run-tests)
;; (test-my-test)

(ns k-means.core-test
  (:use clojure.test)
  (:use k-means.core))

(deftest test-update-centers
  (is (= [] (update-centers [] [])))

  ;; Move center [1 1 1] to center of two points
  (is (= [[2.5 2.5 2.5]] (update-centers [[0 0 0] [5 5 5]] [[1 1 1]])))

  ;; Move two centers to the two points
  (is (= [[0.0 0.0 0.0] [5.0 5.0 5.0]] (update-centers [[0 0 0] [5 5 5]] [[1 1 1] [5 5 5]])))

  ;; Don't update center if does not cover any points (center [100 100 100]
  ;; covers 0 points).
  (is (= [[2.5 2.5 2.5] [100 100 100]] (update-centers [[0 0 0] [5 5 5]] [[5 5 5] [100 100 100]]))))

