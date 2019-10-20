(ns tetris.board
  (:require [tetris.defs :as d]
            [tetris.blocks :as blocks]))


(defn row-of-zeros [columns row]
  (if (= (count row) columns)
    row
    (row-of-zeros columns (conj row 0))))


(defn blank-board [rows columns board]
  (if (= (count board) rows)
    board
    (blank-board rows columns (conj board (row-of-zeros columns [])))))


(defn row-complete [row]
  (every? #(> % 0) row))


(defn get-row [board row]
  (nth board row))


(defn within-bounds [x y block-state]
  (and
    (>= x 0)
    (< (- (+ x (blocks/width block-state)) 1) d/board-columns)
    (>= y 0)
    (< (- (+ y (blocks/height block-state)) 1) d/board-rows)))


(defn sample-row [row x width]
  (take width (drop x row)))


(defn sample-board [board x y width height]
  (loop [count height
         row y
         out []]
    (if (zero? count)
      out
      (recur (dec count) (inc row) (into out (sample-row (get-row board row) x width))))))


(defn collide [a b]
  (if (> (bit-and a b) 0)
    true
    false))


(defn collisions? [board x y block-state]
  (loop [board-sample (sample-board board x y (blocks/width block-state) (blocks/height block-state))
         flat-block (blocks/flatten-block block-state)
         a (first board-sample)
         b (first flat-block)
         board-rest (rest board-sample)
         block-rest (rest flat-block)]
    (if (nil? a)
      false
      (if (collide a b)
        true
        (recur board-rest block-rest (first board-rest) (first block-rest) (rest board-rest) (rest block-rest))))))


(defn no-collisions [board x y block-state]
  (not (collisions? board x y block-state)))


(defn block-fits [board x y block-state]
  (and (within-bounds x y block-state) (no-collisions board x y block-state)))


(defn blank-next-row-with-block [start-row current-row block]
  (let [last-row-index (- (count block) 1)
        row-index (- current-row start-row)]
    (if (or (< row-index 0) (> row-index last-row-index))
      (row-of-zeros d/board-columns [])
      (nth block row-index))))


(defn blank-board-with-block [block row column]
  (let [expanded-block (blocks/expand-block block column)]
    (loop [current-row 0
           out []]
      (if (= current-row d/board-rows)
        out
        (recur (inc current-row) (conj out (blank-next-row-with-block row current-row expanded-block)))))))


(defn append-if-not-complete [board row]
  (if (row-complete row)
    board
    (conj board row)))


(defn remove-complete-rows [board]
  (let [reversed (reverse board)]
    (loop [row (first reversed)
           rest-of-board (rest reversed)
           out []]
      (if (nil? row)
        out
        (recur (first rest-of-board) (rest rest-of-board) (append-if-not-complete out row))))))


(defn replace-removed-rows [board]
  (if (= (count board) d/board-rows)
    board
    (replace-removed-rows (conj board (row-of-zeros d/board-columns [])))))


(defn filter-complete-rows [board]
  (-> board
    (remove-complete-rows)
    (replace-removed-rows)
    (reverse)))
