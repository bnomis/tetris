(ns tetris.views
  (:require [tetris.defs :as d]))

(defn main-panel []
  [:div.tetris
   [:h1 "Tetris"]
   [:svg#board {:width d/board-width :height d/board-height}
    [:rect.block-outline {:x 0 :y 0 :width d/board-width :height d/board-height}]
    [:g
      (for [column (range 1 d/board-columns)]
       (let [x (d/col->px column)]
         [:line.block-outline {:x1 x :y1 0 :x2 x :y2 d/board-height :key (str "board-column-outline-" column)}]))]
    [:g
     (for [row (range 1 d/board-rows)]
      (let [y (d/row->px row)]
        [:line.block-outline {:x1 0 :y1 y :x2 d/board-width :y2 y :key (str "board-row-outline-" row)}]))]]])
