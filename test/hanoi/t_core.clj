(ns hanoi.t-core
  (:use midje.sweet)
  (:use [hanoi.core]))

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

