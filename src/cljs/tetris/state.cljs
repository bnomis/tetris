(ns tetris.state
  [:require [tetris.db :as db]
            [tetris.board :as board]
            [tetris.blocks :as blocks]])


(defn nil-active-block? [state]
  (nil? (:active-block state)))


(defn state->active-block [state]
  (:active-block state))


(defn spawn-new-block [current-state]
  (let [block-id (blocks/random-block-id)
        block (blocks/get-block block-id)
        block-state (first (:states block))
        board (:board current-state)
        column (:spawn-column block)
        row 0
        state 0]
    (if (board/block-fits board column row block-state)
      (assoc current-state :active-block {:row row :column column :id block-id :state state}))))


(defn draw-active-block-on-board [current-state]
  (let [ab (:active-block current-state)
        bid (:id ab)
        block-state-num (:state ab)
        block (blocks/state bid block-state-num)
        column (:column ab)
        row (:row ab)
        board (:board current-state)
        expanded-block (board/blank-board-with-block block row column)]
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
    (if (board/block-fits board column next-row state)
      (assoc current-state :active-block (assoc ab :row next-row))
      (add-active-block-to-board current-state))))


(defn next-state [current-state]
  (if-not (:active-block current-state)
    (spawn-new-block current-state)
    (decend-block current-state)))


(defn filter-complete-rows [current-state]
  (assoc current-state :board (board/filter-complete-rows (:board current-state))))


(defn tick-state [db]
  (-> (db/current-state db)
    (filter-complete-rows)
    (next-state)))


(defn block-left [current-state]
  (let [ab (:active-block current-state)
        bid (:id ab)
        block-state-num (:state ab)
        state (blocks/state bid block-state-num)
        board (:board current-state)
        column (:column ab)
        row (:row ab)
        next-column (- column 1)]
    (if (board/block-fits board next-column row state)
      (assoc current-state :active-block (assoc ab :column next-column))
      current-state)))


(defn block-right [current-state]
  (let [ab (:active-block current-state)
        bid (:id ab)
        block-state-num (:state ab)
        state (blocks/state bid block-state-num)
        board (:board current-state)
        column (:column ab)
        row (:row ab)
        next-column (+ column 1)]
    (if (board/block-fits board next-column row state)
      (assoc current-state :active-block (assoc ab :column next-column))
      current-state)))


(defn rotate-left [current-state]
  (let [ab (:active-block current-state)
        rotated (blocks/rotate-left ab)
        bid (:id rotated)
        block-state-num (:state rotated)
        state (blocks/state bid block-state-num)
        board (:board current-state)
        column (:column rotated)
        row (:row rotated)]
    (if (board/block-fits board column row state)
      (assoc current-state :active-block rotated)
      current-state)))


(defn rotate-right [current-state]
  (let [ab (:active-block current-state)
        rotated (blocks/rotate-right ab)
        bid (:id rotated)
        block-state-num (:state rotated)
        state (blocks/state bid block-state-num)
        board (:board current-state)
        column (:column rotated)
        row (:row rotated)]
    (if (board/block-fits board column row state)
      (assoc current-state :active-block rotated)
      current-state)))


(defn drop-depth [current-state]
  (let [ab (:active-block current-state)
        bid (:id ab)
        block-state-num (:state ab)
        state (blocks/state bid block-state-num)
        board (:board current-state)
        column (:column ab)
        row (:row ab)]
    (loop [depth 1]
      (if (not (board/block-fits board column (+ row depth) state))
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
