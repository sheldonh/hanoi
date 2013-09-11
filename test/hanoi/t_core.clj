(ns hanoi.t-core
  (:use midje.sweet)
  (:use [hanoi.core]))

(comment
(facts "about `valid-peg?`"
       (fact "is true for a sequence of zero or more discs in reverse size order"
             (valid-peg? (peg))       => true
             (valid-peg? (peg 1))     => true
             (valid-peg? (peg 2 1))   => true
             (valid-peg? (peg 3 2 1)) => true
             (valid-peg? (peg 3 1))   => true)
       (fact "is false for a sequence of two or more misordered discs"
             (valid-peg? (peg 1 2))   => false
             (valid-peg? (peg 1 2 3)) => false
             (valid-peg? (peg 3 1 2)) => false
             (valid-peg? (peg 2 3 1)) => false)
       (fact "is false for a sequence in which two or more discs have the same size"
             (valid-peg? (peg 3 3))     => false
             (valid-peg? (peg 3 2 2 1)) => false))

(facts "about `every-disc-distinct?`"
       (fact "is true for a frame of zero or more discs of distinct size"
             (every-disc-distinct? (frame))               => true
             (every-disc-distinct? (frame [1] [] []))     => true
             (every-disc-distinct? (frame [3] [2] [1]))   => true
             (every-disc-distinct? (frame [1] [] [2 3]))  => true
             (every-disc-distinct? (frame [3 2 1] [] [])) => true)
       (fact "is false for a frame in which two or more discs have the same size"
             (every-disc-distinct? (frame [3 2 1 2] [] []))         => false
             (every-disc-distinct? (frame [3 3] [2] [1]))           => false
             (every-disc-distinct? (frame [3 2 1] [1 2 3] [2 3 1])) => false
             (every-disc-distinct? (frame [3 2 1] [1] []))          => false))
)

(facts "about `valid-frame?`"
       (fact "is true when every peg is valid and every disc is distinct"
             (valid-frame? (frame)) => true
             (valid-frame? (frame [1] [] [])) => true
             (valid-frame? (frame [3] [2 1] [])) => true
             (valid-frame? (frame [3] [2] [1])) => true
             (valid-frame? (frame [1] [2] [3])) => true
             (valid-frame? (frame [] [3 2] [1])) => true
             (valid-frame? (frame [] [2] [3 1])) => true
             (valid-frame? (frame [] [] [3 2 1])) => true)
       (fact "is false if one or more pegs is invalid"
             (valid-frame? (frame [1 2 3] [] [])) => false)
       (fact "is false if two or more discs are duplicated"
             (valid-frame? (frame [3] [2] [2])) => false))

(facts "about `valid-game?`"
       (fact "is true when every frame valid, distinct and has the same disc count"
             (valid-game? (game)) => true
             (valid-game? (game (frame [3 2 1] [] []))) => true
             (valid-game? (game (frame [3 2 1] [] []) (frame [3 2] [1] []) (frame [3] [1] [2]))) => true)
       (fact "is false if one or more frames is invalid"
             (valid-game? (game (frame [3 2 1] [] []) (frame [3 2 1] [] [3 2 1]))) => false)
       (fact "is false if two or more frames are duplicated"
             (valid-game? (game (frame [3 2 1] [] []) (frame [3 2] [1] []) (frame [3 2 1] [] []))) => false)
       (fact "is false if any frame's disc count differs from any other's"
             (valid-game? (game (frame [3 2 1] [] []) (frame [3 2] [] []) (frame [3] [2] []))) => false
             (valid-game? (game (frame [3 2 1] [] []) (frame [4 3 2] [1] []) (frame [4 3] [1] [2]))) => false))

(facts "about `complete-game?`"
       (fact "is true if all but the last peg is empty in the last frame of the game"
             (complete-game? (game)) => true
             (complete-game? (game (frame [] [] [3 2 1]))) => true
             (complete-game? (game (frame [] [] [1]))) => true)
       (fact "is false if any but the last peg is empty in the last frame of the game"
             (complete-game? (game (frame [] [1] [3 2]))) => false)
       (fact "is true even if the game is not valid"
             (complete-game? (game (frame [] [] [1 2 3]))) => true))

(facts "about `winning-game?`"
       (fact "is true if the game is valid and complete"
             (winning-game? (game)) => true)
       (fact "is false if the game is not valid"
             (winning-game? (game)) => false
             (provided
               (valid-game? (game)) => false)
               (complete-game? (game)) => true :times (range))
       (fact "is false if the game is not complete"
             (winning-game? (game)) => false
             (provided
               (complete-game? (game)) => false
               (valid-game? (game)) => true :times (range))))

(facts "about `all-moves`"
       (fact "returns all legal moves possible from the current frame"
             (all-moves (frame)) => empty?
             (all-moves (frame [3] [2] [1])) => [[1 0] [2 0] [2 1]]
             (all-moves (frame [3 2 1] [] [])) => [[0 1] [0 2]]))

(facts "about `apply-move`"
       (fact "returns the frame advanced by a move"
             (apply-move (frame [3 2 1] [] []) (move 0 1)) => (frame [3 2] [1] [])
             (apply-move (frame [3 2 1] [] []) (move 0 2)) => (frame [3 2] [] [1])
             (apply-move (frame [3 2] [] [1]) (move 0 1)) => (frame [3] [2] [1])
             (apply-move (frame [3] [2] [1]) (move 2 1)) => (frame [3] [2 1] [])))

