(ns clojure-demo.core
  (:require [clojure.string :as str]))
;;all clojure files start with a namespace declaration, Take the file path from src root, replace / with . and _ with - remove the clj from the file name


;;-----------------Introduction -----------

;;clojure is hosted language. that means it there is code that runs on top of an existing platform (jvm, clr, javascript engine) reads, evaluates and prints your clojure code.

;;---------------- The REPL ---------------

;;We work inside the program when we write code, its its like moulding a something into shape. Its a constant
;;interactive process with rapid feedback.If your not sure what something does, evaluate it in your repl!


(println "hello world")                                     ;the beginning


; ------------------  fundamental types ------------------



26                                                          ;java long

26.59                                                       ;java double

25M                                                         ; java big decimal

"hello trainline"                                           ;java string

nil                                                         ;similar to null

true false                                                  ;java boolean

["I am hetrogenious" true "!"]                              ;a vector / array

(list "linked " "list")                                     ;(linked) lists

:name                                                       ;a keyword

{:name "luke"
 :age  26}                                                  ;a map, keywords are usually used as keys


#{:unique :values :only}                                    ;a set


(set [:unique :values :only :only])                         ;duplicates removed

;symbols

;;symbols are identifiers to your data, you can think of them similar to the mathematical notion of symbols.
;; e.g x=2  x is a symbol that identifies the value 2,


;;defines a 'global' the symbol foo evaluates to the
;;string hello, you can use foo anywhere in this namespace
(def foo "hello")

(def two-words "world")                                     ;note the convention of - between lower case words

foo                                                         ;when you send a symbol to the repl it evals to the data

;---------------- values ------------------

;;clojure emphasises values over references, equality is based on the content rather than which specfic object
;;you have

(def person1 {:name "Luke"
              :age  26})

(def person2 {:name "Luke"
              :age  26})



(identical? person1 person2)                                ;very rarely usefull but just to demonstrate


(= person1 person2)


;;values are immutable, they do not change, you cannot change the value of the number 42 to -5. We are already
;;familar with this as all c based languages treat numbers as values, clojure extends this concept to all data types
;;but more on this later


; ---------------- functions ------------------

;;to invoke a function start with opening parens
;;function name comes first and then comes the arguements
;;this may feel unatrual but there are good reasons for this (more on this another time)

;;e.g the count function returns the number of items in a collection

(count ["clojure" "is" "cool"])                             ;invoking a function in the core

(str/upper-case "make it shouty")                           ;invoking a function in an aliased namespace

;; to create an anonymous function

(fn [input] (* input 2))                                    ;anonymous function


(def long-way (fn [input] (* input 2)))                     ;binding fn to symbol long-way (dont do this)

(long-way 10)

(defn doubler                                               ;much better, have documentation and easier to read
  "this function doubles the input"
  [input]
  (* input 2))


;---------------- control flow -------

;;notice the return value, if is an expression as is everything is clojure
(if (= 10 (doubler 5))
  "5 * 2 is 10"
  "or is it?")


;if only takes two expressions one for each case, if you want to insert more sideaffects use 'do'

(if (= 10 (doubler 5))
  (do
    (println "5 * 2 is 10")
    "doubling 5 makes 10")
  "or does it?")


(defn fizzbuzz [input]
  (cond
    (and (= (mod input 3) 0)
         (= (mod input 5) 0)) "fizzbuzz"
    (= (mod input 3) 0) "fizz"
    (= (mod input 5) 0) "buzz"
    :else nil))


(defn multiple? [input divisor]
  (= (mod input divisor) 0))


(defn fizzbuzz2 [input]
  (cond
    (and (multiple? input 5)
         (multiple? input 3)) "fizzbuzz"
    (multiple? input 3) "fizz"
    (multiple? input 5) "buzz"
    :else nil))


;;nil or false are 'falsey' they failed if conditions
(if (fizzbuzz2 19)
  "found"
  "not here")

;;everything else is truthy, e.g strings collections etc
(when (fizzbuzz2 15)
  "this was true!")

;;not flips between truthy and falsy

(not true)
(not (not true))

(when (not (fizzbuzz2 19))
  "19 is not fizzed or buzzed")

(when-not (fizzbuzz2 19)
  "19 is not fizzed or buzzed")


;----------- sequences -----------

;;seq's are a fundamental abstraction in clojure, it is not a concrete data type.
;;in order for a data structure to be a seq it must support the following protocol
;;(protocoles are like interfaces)

(first ["1" "2"])

(cons "hello" ["world"])

(rest ["hello" "world"])

;on top of these basic functions there is exists a standard library of functions for manipulating seqs

(def my-vector [11 22 33])

(map doubler my-vector)                                     ;the classic, note if a function takes a single arg
; you can just pass directly

(filter (fn [x] (> x 30)) my-vector)

;using shorthand syntax
(filter #(> % 30) my-vector)

(sort > my-vector)

(sort > (filter (fn [x] (> x 30)) (map doubler my-vector)))

;you can chain multiple seq transformations with the threaded through macro, each expressions result is inserted
;as the *last* arguement to the next expression. Notice all the seq transformation take a seq as the last parameter
;as they are designed to be used with this macro functions return a seq and this you to compose your operations

(->> my-vector
     (map doubler)
     (filter (fn [x] (> x 30)))
     (sort >))

;...segue to lisp evaluation and macros

(macroexpand-1 (quote (->> my-vector
                          (map doubler)
                          (filter (fn [x] (> x 30)))
                          (sort >))))


;many data types in clojure are seq'able


(first "hello")                                             ;strings are sequences of charactors

(first #{:sets :are :seqs})                                 ;sets are sequences of their contents, order is undefined

(first {:name "luke"                                        ;maps are sequences of their kv pairs
        :age  16})


;sequences are lazy and have the potential to be infinite

(repeat "clojure")                                          ;this is an infinite sequence of "clojure"

(range)                                                     ;every possible integer (that can fit into memory)


;(count (repeat "clojure"))                                ;dont try this at home


(->> (repeat "clojure")
     (take 100)
     (map (fn [x]
            (Thread/sleep 3000)                             ;fetch from the db
            (str x " is cool")))
     (first))


;------------------ collections ------------------------

;the collection abstraction implies a fully realised collection and the

(def my-list (list 11 22 33))

(conj my-list 44)                                           ;my list is still the same

(conj my-vector 44)

(count my-vector)

(empty? [])

;----------------- maps ----------------


(def data-engineers [{:name           "Bruno"
                      :favouirte-food :pasta
                      :wear-glasses?  true
                      :pets           2}
                     {:name           "Sathya"
                      :favouirte-food :pizza
                      :wear-glasses?  false
                      :pets           1}
                     {:name           "Olivier"
                      :favouirte-food :chocolate
                      :wear-glasses?  false
                      :pets           0}
                     {:name           "Luke"
                      :favouirte-food :olives
                      :wear-glasses?  true
                      :pets           0}])

(def bruno (first data-engineers))

(get bruno :name)

;;keywords act as functions, can be applied with a map to lookup the value
(:name bruno)

(assoc bruno :favouirte-food :tofu)

(def other-bruno (assoc bruno :favouirte-food :tofu))

(clojure.data/diff bruno other-bruno )

(dissoc bruno :name)

(def common-attributes {:favouirte-language :clojure
                        :location           [123123 1241512521]})

(merge bruno common-attributes)

(map (fn [engineer]
       (merge engineer common-attributes)) data-engineers)


(update bruno :pets inc)
(update bruno :pets doubler)




;---------------- sets ---------------

(def functional-languages #{:clojure :clojure-script :haskell :f#})

(def oo-languages #{:python :c++ :java})

(def dynamic-languages #{:clojure :clojure-script :python :javascript})


(clojure.set/union functional-languages oo-languages dynamic-languages)

(clojure.set/intersection functional-languages dynamic-languages)

(clojure.set/difference dynamic-languages functional-languages)



; ----------- locals and scope --------------

;symbols defined in let block are only available in the scope of the let block's scope
(let [english-greeting "hello"
      french-greeting "bonjour"]
  (str english-greeting french-greeting))

(comment
  english-greeting)

(defn multiple? [input divisor]
  (= (mod input divisor) 0))

(defn multiple2? [input divisor]
  (let [modulus (mod input divisor)]
    (= modulus 0)))

;; -------------apply ---------------

;;some functions are variadic, they take n number of arguements e.g str concatenates strings

(str "clojure" "is" "fun")

;;sometimes you only have the arguements you want to apply at runtime

(def args ["clojure" "is" "fun"])

;;apply takes a collection and applies it as arguements to a supplied fn, e.g

(apply str args)


;; -------------partial application ------------------



;;this is 'full' application, you apply a function with all the args and it evals
;;to a result
(re-find #"[Ww]ally" "wheres Wally gone?")                     ;#"" creates a regex

;;however if you can create a specialised version of a generic function where you
;;pre fill some of the arguements, starting from the left
(def wally-finder (partial re-find #"wally[0-9]"))

;;this returns a function, its like all other functions, you can call it or pass it around
(fn? wally-finder)

(wally-finder "wheres wally gone?")

(def books ["wheres wally1 gone?"
            "trainspotting"
            "wheres wally in las vegas"
            "fear and lothing in las vegas"
            "php for dummies"])


(filter (fn [text] (re-find #"wally" text)) books)

(filter wally-finder books)


;----------------- function composition ---------------------------------------

;business case:

;1) extract all instances of wally in a string
;2) make it upper case
;3) reverse it
;4) profit

(defn my-composition [arg]
  (str/reverse (str/upper-case (wally-finder arg))))

(my-composition " where is wally")


;;functions are highly composable, particularly if  they take 1 arguement, you can compose multiple functions, and the
;;end result is still a function that takes one arguement and returns one thing

;Most clojure librarys expose their behaviour in terms of standard functions, since they are a universal
;abstraction in the language you can compose functions from completely different places easily

(def my-comp (comp str/reverse str/upper-case wally-finder))

(fn? my-comp)

(my-comp "where is WALLY")


;; partial application and funciton compositions are tools to enable
;; you to easily manipulate existing fns and create new fn's, typically to give to someone else.
;; They are completely optional and the same behaviour can be achived with anonymous functions or
;; other techniques but its a usefull tool to know and often the idiomatic way to write clojure

;---------- recursion and reduce --------------

;;no tail call elimination, dont do this
(defn fizz-buzzer-rec [numbers accumulator]
  (if (seq numbers)
    (let [result (fizzbuzz2 (first numbers))
          accumulator (if result (conj accumulator result) accumulator)]
      (fizz-buzzer-rec (rest numbers)
                       accumulator))
    accumulator))

(fizz-buzzer-rec (range 100) [])

;;performs tail call optimisation
(defn fizz-buzzer-loop [n]
  (loop [numbers (range n)                                  ;lowest level, rarely needed
         accumulator []]
    (if (seq numbers)
      (let [result (fizzbuzz2 (first numbers))
            accumulator (if result (conj accumulator result) accumulator)]
        (recur (rest numbers)
               accumulator))
      accumulator)))

(fizz-buzzer-loop 100)

(defn fizz-buzzer-reduce [n]
  (reduce (fn [acc x]                                       ;slightly higher level, often usefull,
            (if-let [res (fizzbuzz2 x)]
              (conj acc res)
              acc)) [] (range n)))

(fizz-buzzer-reduce 100)

(defn fizz-buzzer-seq [n]
  (->> (range n)
       (map fizzbuzz2)
       (remove nil?)))

(fizz-buzzer-seq 10)



; ---------- pop quiz -------------



;----------- further reading -------

; clojure for brave and true http://www.braveclojure.com/foreword/
; persistent data structures http://hypirion.com/musings/understanding-persistent-vector-pt-1
; destructuring http://blog.brunobonacci.com/2014/11/16/clojure-complete-guide-to-destructuring/
