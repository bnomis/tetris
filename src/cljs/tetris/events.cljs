(ns tetris.events
  (:require
   [re-frame.core :as rf]
   [tetris.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]))


(rf/reg-event-db
 :initialize-db
 (fn-traced [_ _]
   db/default-db))


(rf/reg-event-db
 :tick
 (fn-traced [db _]
   (let [states (conj (:states db) (first (:states db/default-db)))]
     (-> db
       (update :current-state inc)
       (assoc :states states)))))


(defn dispatch-tick-event []
  (rf/dispatch [:tick]))


(defn start-timer []
  (js/setInterval dispatch-tick-event 1000))


(defn stop-timer [tid]
  (js/clearInterval tid))


(rf/reg-event-db
 :play
 (fn-traced [db _]
   (assoc db :timer (start-timer))))


(rf/reg-event-db
 :pause
 (fn-traced [db _]
   (stop-timer (:timer db))
   (assoc db :timer nil)))


(rf/reg-event-db
  :reset
  (fn-traced [db _]
    (stop-timer (:timer db))
    db/default-db))


(rf/reg-event-db
 :prev
 (fn-traced [db _]
   (update db :current-state dec)))


(rf/reg-event-db
  :next
  (fn-traced [db _]
    (update db :current-state inc)))
