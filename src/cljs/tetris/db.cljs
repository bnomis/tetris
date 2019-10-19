(ns tetris.db
  (:require [tetris.defs :as d]
            [tetris.board :as board]))


(def default-db
  {:timer nil
   :game-over nil
   :current-state 0
   :states [{:active-block nil
             :board (board/blank-board d/board-rows d/board-columns [])}]})


(defn last-state [db]
  (- (count (:states db)) 1))
