(ns tetris.blocks
  (:require [tetris.defs :as d]
            [tetris.board :as board]
            [cljs.pprint]))

(def blocks
  {:block-i {:color "cyan"
             :states [[[1 1 1 1]]
                      [[1] [1] [1] [1]]
                      [[1 1 1 1]]
                      [[1] [1] [1] [1]]]
             :translations {:0-1 [2 -1]
                            :1-2 [-2 2]
                            :2-3 [1 -2]
                            :3-0 [-1 1]
                            :3-2 [-1 2]
                            :2-1 [2 -2]
                            :1-0 [-2 1]}
              :spawn-column 3}
   :block-j {:color "blue"}
   :block-l {:color "orange"}
   :block-o {:color "yellow"}
   :block-s {:color "green"}
   :block-t {:color "purple"}
   :block-z {:color "red"}})


(defn random-block-id []
  :block-i)


(def cmap {:1 "cyan"})


(defn i->c [i]
  ((keyword (str i)) cmap))


(defn width [block]
  (count (first block)))


(defn height [block]
  (count block))


(defn color [block row column]
  (nth (nth block row) column))


(defn row [block row]
  (nth block row))


(defn state [block-id state-number]
  (let [block (get blocks block-id)]
    (nth (:states block) state-number)))


(defn flatten-block [block]
  (loop [counter (height block)
         row-count 0
         out []]
   (if (zero? counter)
     out
     (recur (dec counter) (inc row-count) (into out (row block row-count))))))


(defn expanded-cell-value [row column current-column]
  (let [row-last (- (count row) 1)
        row-index (- current-column column)]
    (if (or (< row-index 0) (> row-index row-last))
      0
      (nth row row-index))))


(defn expand-row [row column]
  (loop [current-column 0
         out []]
    (if (= current-column d/board-columns)
      out
      (recur (inc current-column) (conj out (expanded-cell-value row column current-column))))))


(defn expand-block [block column]
  (loop [row (first block)
         rest-block (rest block)
         out []]
    (if (nil? row)
      out
      (recur (first rest-block) (rest rest-block) (conj out (expand-row row column))))))


(defn next-row [start-row current-row block]
  (let [last-row-index (- (count block) 1)
        row-index (- current-row start-row)]
    (if (or (< row-index 0) (> row-index last-row-index))
      (board/row-of-zeros d/board-columns [])
      (nth block row-index))))


(defn blank-board-with-block [block row column]
  (let [expanded-block (expand-block block column)]
    (loop [current-row 0
           out []]
      (if (= current-row d/board-rows)
        out
        (recur (inc current-row) (conj out (next-row row current-row expanded-block)))))))
