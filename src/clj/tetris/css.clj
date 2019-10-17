(ns tetris.css
  (:require [garden.def :refer [defstyles]]))

(defstyles screen
  [:body
   {
     :font "14px 'Helvetica Neue', Helvetica, Arial, sans-serif"
     :line-height "1.4em"
     :background "#f5f5f5"
     :color "#4d4d4d"
     :-webkit-font-smoothing "antialiased"
     :-moz-font-smoothing "antialiased"
     :font-smoothing "antialiased"
     :font-weight "300"}]
  [:svg#board {:background-color "black"}]
  [:.block-outline {:stroke "#444" :stroke-width "1px"}]
  [:div.tetris {:outline "none"}])
