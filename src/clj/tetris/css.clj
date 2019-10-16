(ns tetris.css
  (:require [garden.def :refer [defstyles]]))

(defstyles screen
  [:body {:color "red" :background-color "blue"}]
  [:svg#board {:background-color "black"}]
  [:.block-outline {:stroke "#444" :stroke-width "1px"}])
