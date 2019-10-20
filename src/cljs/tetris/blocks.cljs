(ns tetris.blocks
  (:require [tetris.defs :as d]
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


(def cmap
 {:1 "cyan"})


(def rotate-left-transitions
  {:0 3
   :1 0
   :2 1
   :3 2})


(def rotate-right-transitions
  {:0 1
   :1 2
   :2 3
   :3 0})


(defn random-block-id []
  :block-i)



(defn get-block [bid]
  (bid blocks))


(defn i->keyword [i]
  (keyword (str i)))


(defn state-transition-keyword [from to]
  (keyword (str from "-" to)))


(defn i->color [i]
  ((i->keyword i) cmap))


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


(defn rotate-left-state [current]
  ((i->keyword current) rotate-left-transitions))


(defn rotate-left [active-block]
  (let [from (:state active-block)
        to (rotate-left-state from)
        block ((:id active-block) blocks)
        translation ((state-transition-keyword from to) (:translations block))
        column-delta (nth translation 0)
        row-delta (nth translation 1)
        row (+ (:row active-block) row-delta)
        column (+ (:column active-block) column-delta)]
    (assoc active-block :row row :column column :state to)))


(defn rotate-right-state [current]
  ((i->keyword current) rotate-right-transitions))


(defn rotate-right [active-block]
  (let [from (:state active-block)
        to (rotate-right-state from)
        block ((:id active-block) blocks)
        translation ((state-transition-keyword from to) (:translations block))
        column-delta (nth translation 0)
        row-delta (nth translation 1)
        row (+ (:row active-block) row-delta)
        column (+ (:column active-block) column-delta)]
    (assoc active-block :row row :column column :state to)))
