(ns tetris.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as rf]
   [tetris.views :as views]
   [tetris.config :as config]
   [tetris.events]
   [tetris.subs]))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))


(defn ^:dev/after-load mount-root []
  (rf/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))


(defn init []
  (rf/dispatch-sync [:initialize-db])
  (dev-setup)
  (mount-root))
