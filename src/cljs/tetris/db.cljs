(ns tetris.db
  (:require [tetris.defs :as d]))


(defn row-of-zeros [columns row]
  (if (< (count row) columns)
    (row-of-zeros columns (conj row 0))
    row))


(defn blank-board [rows columns board]
  (if (< (count board) rows)
    (blank-board rows columns (conj board (row-of-zeros columns [])))
    board))


(def default-db
  {:timer nil
   :current-state 0
   :states [{:active-block nil
             :board (blank-board d/board-rows d/board-columns [])}]})
