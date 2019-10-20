(ns tetris.events
  (:require
    [re-frame.core :as rf]
    [tetris.db :as db]
    [tetris.state :as state]
    [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]))


(rf/reg-event-db
  :initialize-db
  (fn-traced [_ _]
    (db/initial-db)))


(rf/reg-event-db
  :tick
  (fn-traced [db _]
    (let [next-state (state/tick-state db)]
      (if (nil? next-state)
        (db/game-over db)
        (db/append-state db next-state)))))


(rf/reg-event-db
  :play
  (fn-traced [db _]
    (db/play db)))


(rf/reg-event-db
  :pause
  (fn-traced [db _]
    (db/pause db)))


(rf/reg-event-db
  :reset
  (fn-traced [db _]
    (db/reset db)))


(rf/reg-event-db
  :prev
  (fn-traced [db _]
    (db/prev-state db)))


(rf/reg-event-db
  :next
  (fn-traced [db _]
    (db/next-state db)))


(rf/reg-event-db
  :left
  (fn-traced [db _]
    (db/keypress db state/block-left)))


(rf/reg-event-db
  :right
  (fn-traced [db _]
    (db/keypress db state/block-right)))


(rf/reg-event-db
  :rotate-left
  (fn-traced [db _]
    (db/keypress db state/rotate-left)))


(rf/reg-event-db
  :rotate-right
  (fn-traced [db _]
    (db/keypress db state/rotate-right)))


(rf/reg-event-db
  :drop
  (fn-traced [db _]
    (db/keypress db state/block-drop)))
