(ns tetris.css
  (:require [garden.def :refer [defstyles]]))

(defstyles screen
  [:body
   {:font "14px 'Helvetica Neue', Helvetica, Arial, sans-serif"
    :line-height "1.4em"
    :background "#f5f5f5"
    :color "#4d4d4d"
    :-webkit-font-smoothing "antialiased"
    :-moz-font-smoothing "antialiased"
    :font-smoothing "antialiased"
    :font-weight "300"}]
  [:div#app {:margin "10px" :width "800px"}]
  [:div#left {:float "left" :width "300px"}]
  [:div#right {:float "left"}]
  [:div#controls-container]
  [:div#controls-container [:button {:margin-left "10px" :width "50px"}]]
  [:div#block-controls {:margin "10px"}]
  [:div#board-container]
  [:svg#board {:background-color "black"}]
  [:.block-outline {:stroke "#444" :stroke-width "1px"}]
  [:div.tetris {:outline "none"}]
  [:div.clear {:clear "both"}])
