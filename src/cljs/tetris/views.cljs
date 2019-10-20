(ns tetris.views
  (:require [tetris.defs :as d]
            [tetris.blocks :as blocks]
            [re-frame.core :refer [subscribe dispatch]]))


(defn draw-block [row column color]
  (if (> color 0)
    [:rect {:x (d/col->px column)
            :y (d/row->px row)
            :width d/block-width
            :height d/block-height
            :fill (blocks/i->color color)
            :key (str "block" row column)}]))


(defn draw-active-block [active-block]
  (let [bid (:id active-block)
        block (get blocks/blocks bid)
        state (nth (:states block) (:state active-block))
        x (:column active-block)
        y (:row active-block)]
    (for [row (range (blocks/height state))]
      (for [column (range (blocks/width state))]
        (draw-block (+ row y) (+ column x) (blocks/color state row column))))))


(defn board []
  (let [current-state @(subscribe [:current-state])
        states @(subscribe [:states])
        state (nth states current-state)
        active-block (:active-block state)
        board (:board state)]
    [:div#board-container
      [:svg#board {:width d/board-width :height d/board-height}
       [:rect.block-outline {:x 0 :y 0 :width d/board-width :height d/board-height}]
       [:g
         (for [column (range 1 d/board-columns)]
          (let [x (d/col->px column)]
            [:line.block-outline {:x1 x :y1 0 :x2 x :y2 d/board-height :key (str "board-column-outline-" column)}]))]
       [:g
        (for [row (range 1 d/board-rows)]
         (let [y (d/row->px row)]
           [:line.block-outline {:x1 0 :y1 y :x2 d/board-width :y2 y :key (str "board-row-outline-" row)}]))]
       (for [row (range (count board))]
         (for [column (range (count (nth board row)))]
           (draw-block row column (nth (nth board row) column))))
       (if active-block
         (draw-active-block active-block))]]))


(defn controls []
  (let [running? @(subscribe [:running?])
        prev-disabled? @(subscribe [:prev-disabled?])
        next-disabled? @(subscribe [:next-disabled?])
        game-over? @(subscribe [:game-over?])]

    [:div#controls-container
      [:button#prev
       {:on-click #(dispatch [:prev]) :disabled prev-disabled?} "Prev"]
      [:button#play
        {:on-click #(if running?
                     (dispatch [:pause])
                     (dispatch [:play]))
         :disabled game-over?}
        (if running?
          "Pause"
          "Play")]
      [:button#next
       {:on-click #(dispatch [:next]) :disabled next-disabled?} "Next"]
      [:button#reset
       {:on-click #(dispatch [:reset])} "Reset"]]))


(defn game-over []
  (let [game-over? @(subscribe [:game-over?])]
    (if game-over?
      [:h2.game-over "Game Over"])))


(defn clear []
  [:div.clear])


(defn main-panel []
  (let [running? @(subscribe [:running?])]
    [:div.tetris (if running?
                    {:tab-index 0
                     :on-key-press #(case (.-key %)
                                      "l" (dispatch [:right])
                                      "h" (dispatch [:left])
                                      "k" (dispatch [:rotate-right])
                                      "j" (dispatch [:rotate-left])
                                      " " (dispatch [:drop])
                                      nil)})
     [:h1 "Tetris"]
     [board]
     [controls]
     [game-over]
     [clear]]))
