(ns tetris.defs)
(def block-width 20)
(def block-height 20)
(def outline-width 1)
(def board-columns 10)
(def board-rows 20)
(def board-width (+ (* board-columns block-width) (* (+ board-columns 1) outline-width)))
(def board-height (+ (* board-rows block-height) (* (+ board-rows 1) outline-width)))

(defn col->px [column]
  (+ (* column block-width) (* outline-width (+ column 1))))

(defn row->px [row]
  (+ (* row block-height) (* outline-width (+ row 1))))
