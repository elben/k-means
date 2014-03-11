# K-means

Demo of k-means in Clojure to make it easy to visualize 2-d k-means clustering
algorithm.

GUI built on top of Quil.

![K-means demo](https://raw.github.com/elben/clojure-play/master/k-means-demo.gif "K-means Demo")

## Running the Demo

In the k-means folder, run:

    lein repl
    
    # Run tests
    lein test

A window should pop up.

- Left-click to spray points.
- Right-click to add centers (up to six may be added).
- [Space] to take one step in k-means clustering algorithm.
- `r` will reset the points.

That's about it!

# Miscellaneous

To run tests: `lein test`.

Or, to run tests in the REPL:

```clojure
(ns k-means.core-test)
(run-tests)

;; Runs a specific test named test-my-test.
(test-my-test)
```
