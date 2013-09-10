(ns hanoi.core)

(defn peg [& discs] (seq discs))

(defn frame [& pegs] (map (partial apply peg) pegs))

(defn valid-peg? [p]
  (or (empty? p)
      (let [s (seq p)]
        (= s (-> s sort distinct reverse)))))

(defn every-disc-distinct? [f] (let [discs (apply concat f)] (= discs (distinct discs))))

