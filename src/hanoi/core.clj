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

(defn complete-game? [g] (every? empty? (butlast (last g))))

(defn winning-game? [g] (and (valid-game? g) (complete-game? g)))

(defn- all-naive-moves [f]
  (let [num-pegs (count f)]
    (for [src (range 0 num-pegs)
          dst (range 0 num-pegs)
          :when (not= src dst)]
      [src dst])))

(defn- legal-move? [f [src dst]]
  (let [src-disc (last (nth f src))
        dst-disc (last (nth f dst))]
    (and (not (nil? src-disc))
         (or (nil? dst-disc)
             (< src-disc dst-disc)))))

(defn all-moves [f] (filter (partial legal-move? f) (all-naive-moves f)))

