(ns tetris.db
  (:require [tetris.defs :as d]
            [tetris.board :as board]
            [tetris.time :as time]))


(def initial-db-state
  {:timer nil
   :game-over nil
   :current-state 0
   :states [{:active-block nil
             :board (board/blank-board d/board-rows d/board-columns [])}]})


(defn initial-db []
  initial-db-state)


(defn states [db]
  (:states db))


(defn last-state [db]
  (- (count (:states db)) 1))


(defn goto-last-state [db]
  (assoc db :current-state (last-state db)))


(defn current-state [db]
  (nth (:states db) (:current-state db)))


(defn prev-state [db]
  (update db :current-state dec))


(defn next-state [db]
  (update db :current-state inc))


(defn active-block [db]
  (:active-block (current-state db)))


(defn append-state [db state]
  (-> db
    (assoc :states (conj (:states db) state))
    (update :current-state inc)))


(defn start-time [db]
  (assoc db :timer (time/start)))


(defn stop-time [db]
  (assoc db :timer (time/stop (:timer db))))


(defn game-over [db]
  (-> db
    (assoc :timer (time/stop (:timer db)))
    (assoc :game-over true)))


(defn keypress [db state-func]
  (if (nil? (active-block db))
    db
    (append-state db (state-func (current-state db)))))


(defn reset [db]
  (stop-time db)
  (initial-db))


(defn play [db]
  (-> db
    (goto-last-state)
    (start-time)))


(defn pause [db]
  (stop-time db))
