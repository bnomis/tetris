(ns tetris.time
  (:require
   [re-frame.core :as rf]
   [tetris.defs :as d]))


(defn dispatch-tick-event []
  (rf/dispatch [:tick]))


(defn start []
  (js/setInterval dispatch-tick-event d/tick))


(defn stop [tid]
  (js/clearInterval tid)
  nil)
