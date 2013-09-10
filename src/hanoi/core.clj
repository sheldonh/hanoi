(ns hanoi.core)

(defn peg [& discs] (seq discs))

(defn frame [& pegs] (map (partial apply peg) pegs))

(defn game [& frames] (map (partial apply frame) frames))

(defn valid-peg? [p]
  (or (empty? p)
      (let [s (seq p)]
        (= s (-> s sort distinct reverse)))))

(defn every-disc-distinct? [f] (let [discs (apply concat f)] (= discs (distinct discs))))

(defn valid-frame? [f]
  (and (every? valid-peg? f)
       (every-disc-distinct? f)))

(defn every-frame-valid? [g] (every? valid-frame? g))
(defn every-frame-distinct? [g] (= g (distinct g)))
(defn every-frame-same-disc-count? [g]
  (or (empty? g)
      (let [frame-counts (map (fn [f] (reduce #(+ %1 (count %2)) (count (first f)) (rest f))) g)]
        (= 1 (count (distinct frame-counts))))))

(defn valid-game? [g]
  (and (every-frame-distinct? g)
       (every-frame-valid? g)
       (every-frame-same-disc-count? g)))
