(ns tetris.state)


(defn nil-active-block? [state]
  (nil? (:active-block state)))


(defn state->active-block [state]
  (:active-block state))
