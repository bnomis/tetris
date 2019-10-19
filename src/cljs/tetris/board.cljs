(ns tetris.board)


(defn row-of-zeros [columns row]
  (if (< (count row) columns)
    (row-of-zeros columns (conj row 0))
    row))


(defn blank-board [rows columns board]
  (if (< (count board) rows)
    (blank-board rows columns (conj board (row-of-zeros columns [])))
    board))


(defn row-complete [row]
  (every? #(> % 0) row))


(defn row [board row]
  (nth board row))
