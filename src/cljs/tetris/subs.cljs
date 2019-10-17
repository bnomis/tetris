(ns tetris.subs
  (:require
   [re-frame.core :as rf]))


(rf/reg-sub
  :running?
  (fn [db _]
    (:timer db)))


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
     (rf/subscribe [:current-state])])

  (fn [[running? current-state] _]
    (or running? (< current-state 1))))


(rf/reg-sub
  :next-disabled?
  (fn [query-v _]
    [(rf/subscribe [:running?])
     (rf/subscribe [:current-state])
     (rf/subscribe [:last-state])])

  (fn [[running? current-state last-state] _]
    (or running? (= current-state last-state))))
