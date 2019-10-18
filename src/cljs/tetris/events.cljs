(ns tetris.events
  (:require
   [re-frame.core :as rf]
   [tetris.db :as db]
   [tetris.blocks :as blocks]
   [tetris.defs :as d]
   [tetris.board :as board]
   [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]))


(rf/reg-event-db
 :initialize-db
 (fn-traced [_ _]
   db/default-db))


(defn within-bounds [x y block-state]
  (and
    (>= x 0)
    (< (- (+ x (blocks/width block-state)) 1) d/board-columns)
    (>= y 0)
    (< (- (+ y (blocks/height block-state)) 1) d/board-rows)))


(defn sample-row [row x width]
  (take width (drop x row)))


(defn sample-board [board x y width height]
  (loop [count height
         row y
         out []]
    (if (zero? count)
      out
      (recur (dec count) (inc row) (into out (sample-row (board/row board row) x width))))))


(defn collide [a b]
  (if (> (bit-and a b) 0)
    true
    false))


(defn collisions? [board x y block-state]
  (loop [board-sample (sample-board board x y (blocks/width block-state) (blocks/height block-state))
         flat-block (blocks/flatten-block block-state)
         a (first board-sample)
         b (first flat-block)
         board-rest (rest board-sample)
         block-rest (rest flat-block)]
    (if (nil? a)
      false
      (if (collide a b)
        true
        (recur board-rest block-rest (first board-rest) (first block-rest) (rest board-rest) (rest block-rest))))))


(defn no-collisions [board x y block-state]
  (not (collisions? board x y block-state)))


(defn block-fits [board x y block-state]
  (and (within-bounds x y block-state) (no-collisions board x y block-state)))


(defn spawn-new-block [current-state]
  (let [block-id (blocks/random-block-id)
        block (get blocks/blocks block-id)
        block-state (first (:states block))
        board (:board current-state)
        column (:spawn-column block)
        row 0
        state 0]
    (if (block-fits board column row block-state)
      (assoc current-state :active-block {:row row :column column :id block-id :state state})
      (rf/dispatch [:game-over]))))


(defn next-state [current-state]
  (if-not (:active-block current-state)
    (spawn-new-block current-state)
    current-state))


(rf/reg-event-db
 :tick
 (fn-traced [db _]
   (let [states (:states db)
         current-state (nth states (:current-state db))]
     (-> db
       (assoc :states (conj states (next-state current-state)))
       (update :current-state inc)))))


(defn dispatch-tick-event []
  (rf/dispatch [:tick]))


(defn start-timer []
  (js/setInterval dispatch-tick-event 1000))


(defn stop-timer [tid]
  (js/clearInterval tid))


(rf/reg-event-db
 :play
 (fn-traced [db _]
   (let [last-state (db/last-state db)]
     (-> db
       (assoc :current-state last-state)
       (assoc :timer (start-timer))))))


(rf/reg-event-db
 :pause
 (fn-traced [db _]
   (stop-timer (:timer db))
   (assoc db :timer nil)))


(rf/reg-event-db
  :game-over
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


(rf/reg-event-db
  :left
  (fn-traced [db _]
    (println "left")
    db))


(rf/reg-event-db
  :right
  (fn-traced [db _]
    (println "right")
    db))


(rf/reg-event-db
  :rotate-left
  (fn-traced [db _]
    (println "rotate-left")
    db))


(rf/reg-event-db
  :rotate-right
  (fn-traced [db _]
    (println "rotate-right")
    db))


(rf/reg-event-db
  :drop
  (fn-traced [db _]
    (println "drop")
    db))
