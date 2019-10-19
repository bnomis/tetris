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


(defn dispatch-tick-event []
 (rf/dispatch [:tick]))


(defn start-timer []
 (js/setInterval dispatch-tick-event d/gravity))


(defn stop-timer [tid]
 (js/clearInterval tid))


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
      (assoc current-state :active-block {:row row :column column :id block-id :state state}))))

(defn draw-active-block-on-board [current-state]
  (let [ab (:active-block current-state)
        bid (:id ab)
        block-state-num (:state ab)
        block (blocks/state bid block-state-num)
        column (:column ab)
        row (:row ab)
        board (:board current-state)
        expanded-block (blocks/blank-board-with-block block row column)]
    (loop [board-row (first board)
           block-row (first expanded-block)
           board-rest (rest board)
           block-rest (rest expanded-block)
           out []]
      (if (nil? board-row)
        out
        (recur (first board-rest) (first block-rest) (rest board-rest) (rest block-rest) (conj out (map + board-row block-row)))))))


(defn add-active-block-to-board [current-state]
  (-> current-state
    (assoc :board (draw-active-block-on-board current-state))
    (assoc :active-block nil)))


(defn decend-block [current-state]
  (let [ab (:active-block current-state)
        bid (:id ab)
        block-state-num (:state ab)
        state (blocks/state bid block-state-num)
        board (:board current-state)
        column (:column ab)
        next-row (+ (:row ab) 1)]
    (if (block-fits board column next-row state)
      (assoc current-state :active-block (assoc ab :row next-row))
      (add-active-block-to-board current-state))))


(defn next-state [current-state]
  (if-not (:active-block current-state)
    (spawn-new-block current-state)
    (decend-block current-state)))


(defn game-over [db]
   (stop-timer (:timer db))
   (-> db
     (assoc :timer nil)
     (assoc :game-over true)))


(rf/reg-event-db
 :tick
 (fn-traced [db _]
   (let [states (:states db)
         current-state (nth states (:current-state db))
         next-state (next-state current-state)]
     (if (nil? next-state)
       (game-over db)
       (-> db
         (assoc :states (conj states next-state))
         (update :current-state inc))))))


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


(defn block-left [current-state]
  (let [ab (:active-block current-state)
        bid (:id ab)
        block-state-num (:state ab)
        state (blocks/state bid block-state-num)
        board (:board current-state)
        column (:column ab)
        row (:row ab)
        next-column (- column 1)]
    (if (block-fits board next-column row state)
      (assoc current-state :active-block (assoc ab :column next-column))
      current-state)))


(rf/reg-event-db
  :left
  (fn-traced [db _]
    (let [states (:states db)
          current-state (nth states (:current-state db))]
      (-> db
        (assoc :states (conj states (block-left current-state)))
        (update :current-state inc)))))


(defn block-right [current-state]
  (let [ab (:active-block current-state)
        bid (:id ab)
        block-state-num (:state ab)
        state (blocks/state bid block-state-num)
        board (:board current-state)
        column (:column ab)
        row (:row ab)
        next-column (+ column 1)]
    (if (block-fits board next-column row state)
      (assoc current-state :active-block (assoc ab :column next-column))
      current-state)))


(rf/reg-event-db
  :right
  (fn-traced [db _]
    (let [states (:states db)
          current-state (nth states (:current-state db))]
      (-> db
        (assoc :states (conj states (block-right current-state)))
        (update :current-state inc)))))


(defn block-rotate-left [current-state]
  (let [ab (:active-block current-state)
        bid (:id ab)
        block-state-num (:state ab)
        state (blocks/state bid block-state-num)
        board (:board current-state)
        column (:column ab)
        row (:row ab)
        next-column (+ column 1)]
    (if (block-fits board next-column row state)
      (assoc current-state :active-block (assoc ab :column next-column))
      current-state)))


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


(defn drop-depth [current-state]
  (let [ab (:active-block current-state)
        bid (:id ab)
        block-state-num (:state ab)
        state (blocks/state bid block-state-num)
        board (:board current-state)
        column (:column ab)
        row (:row ab)]
    (loop [depth 1]
      (if (not (block-fits board column (+ row depth) state))
        (- depth 1)
        (recur (inc depth))))))


(defn drop-block [current-state depth]
  (let [ab (:active-block current-state)
        row (:row ab)]
    (assoc current-state :active-block (assoc ab :row (+ row depth)))))


(defn block-drop [current-state]
  (let [depth (drop-depth current-state)]
    (if (= 0 depth)
      current-state
      (-> current-state
        (drop-block depth)))))


(rf/reg-event-db
  :drop
  (fn-traced [db _]
    (let [states (:states db)
          current-state (nth states (:current-state db))
          active-block (:active-block current-state)]
      (if (nil? active-block)
        db
        (-> db
          (assoc :states (conj states (block-drop current-state)))
          (update :current-state inc))))))
