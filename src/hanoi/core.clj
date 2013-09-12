(ns hanoi.core)

(defn peg [& discs] (vec discs))

(defn frame [& pegs] (vec pegs))

(defn game [& frames] (vec frames))

(defn move [src dst] (vector src dst))

(defn valid-peg? [p]
  (or (empty? p)
      (= p (-> p sort distinct reverse))))

(defn every-disc-distinct? [f] (let [discs (apply concat f)] (= discs (distinct discs))))

(defn valid-frame? [f]
  (and (every? valid-peg? f)
       (every-disc-distinct? f)))

(defn every-frame-valid? [g] (every? valid-frame? g))
(defn every-frame-distinct? [g] (= (count g) (count (distinct g))))
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

(defn apply-move [f m]
  (let [src (first m)
        dst (second m)
        disc (last (nth f src))]
    (vec
      (for [i (range 0 (count f))
            :let [p (nth f i)]]
          (cond (= i src)
                (vec (drop-last p))
                (= i dst)
                (conj p disc)
                :default
                p)))))

(defn advance-game [g]
  (let [f (last g)]
    (for [m (all-moves f)]
      (conj g (apply-move f m)))))

(defn winning-games [games]
  (do (println "considering" (count games) "games")
    (let [step (fn [g] (if (complete-game? g)
                           (list g)
                           (advance-game g)))]
    (if (every? winning-game? games)
        (do (println "Found" (count games) "winning games")
            games)
        (recur (filter valid-game? (apply concat (map step games))))))))

(defn play [g]
  (let [winners (winning-games [g])
        winner (first (sort-by count winners))
        frames (count winner)
        moves (dec frames)]
    (do (println "Returning shortest winning game of" moves "moves /" frames "frames")
        winner)))

