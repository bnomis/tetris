(ns tetris.board)

(defn row-complete [row]
  (every? #(> % 0) row))


(defn row [board row]
  (nth board row))
