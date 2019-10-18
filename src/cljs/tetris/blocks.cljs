(ns tetris.blocks)

(def blocks
  {:block-i {:color "cyan"
             :states [[[1 1 1 1]]
                      [[1] [1] [1] [1]]
                      [[1 1 1 1]]
                      [[1] [1] [1] [1]]]
             :translations {:0-1 [2 -1]
                            :1-2 [-2 2]
                            :2-3 [1 -2]
                            :3-0 [-1 1]
                            :3-2 [-1 2]
                            :2-1 [2 -2]
                            :1-0 [-2 1]}
              :spawn-column 3}
   :block-j {:color "blue"}
   :block-l {:color "orange"}
   :block-o {:color "yellow"}
   :block-s {:color "green"}
   :block-t {:color "purple"}
   :block-z {:color "red"}})


(defn random-block-id []
  :block-i)


(def cmap {:1 "cyan"})


(defn i->c [i]
  ((keyword (str i)) cmap))


(defn width [block]
  (count (nth block 0)))


(defn height [block]
  (count block))


(defn color [block row column]
  (nth (nth block row) column))


(defn row [block row]
  (nth block row))


(defn flatten-block [block]
  (loop [counter (height block)
         row-count 0
         out []]
   (if (zero? counter)
     out
     (recur (dec counter) (inc row-count) (into out (row block row-count))))))
