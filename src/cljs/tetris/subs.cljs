(ns tetris.subs
  (:require
   [re-frame.core :as rf]))


(rf/reg-sub
  :running?
  (fn [db _]
    (:timer db)))


(rf/reg-sub
  :game-over?
  (fn [db _]
    (:game-over db)))


(rf/reg-sub
  :states
  (fn [db _]
    (:states db)))


(rf/reg-sub
  :current-state
  (fn [db _]
    (:current-state db)))


(rf/reg-sub
  :last-state
  (fn [query-v _]
    (rf/subscribe [:states]))

  (fn [states query-v _]
    (- (count states) 1)))


(rf/reg-sub
  :has-states
  (fn [query-v _]
    (rf/subscribe [:states]))

  (fn [states query-v _]
    (> (count states) 1)))


(rf/reg-sub
  :prev-disabled?
  (fn [query-v _]
    [(rf/subscribe [:running?])
     (rf/subscribe [:current-state])
     (rf/subscribe [:game-over?])])

  (fn [[running? current-state game-over?] _]
    (or game-over? running? (< current-state 1))))


(rf/reg-sub
  :next-disabled?
  (fn [query-v _]
    [(rf/subscribe [:running?])
     (rf/subscribe [:current-state])
     (rf/subscribe [:last-state])
     (rf/subscribe [:game-over?])])

  (fn [[running? current-state last-state game-over?] _]
    (or game-over? running? (= current-state last-state))))
