(ns open-korean-text-4clj.core-test
  (:require [midje.sweet :refer :all]
            [open-korean-text-4clj.core :refer :all])
  (:import [org.openkoreantext.processor KoreanPosJava]))

(fact "test normalize"
      (normalize "한국어를 처리하는 예시입니닼ㅋㅋㅋㅋㅋ") => "한국어를 처리하는 예시입니다ㅋㅋㅋ")


(facts "test tokenize"

       (fact "default operation"
             (let [tokens (tokenize "한국어를 처리하는 예시입니닼ㅋㅋ")]
               (-> tokens count) => 8
               (-> tokens (get 6) :text) => "닼"))

       (fact "with normalization"
             (let [tokens (tokenize "한국어를 처리하는 예시입니닼ㅋㅋ" :norm true)]
               (-> tokens count) => 7
               (-> tokens (get 5) :text) => "입니다"))

       (fact "with normalization & stemming"
             (let [tokens (tokenize "한국어를 처리하는 예시입니닼ㅋㅋ" :norm true :stem true)]
               (-> tokens count) => 7
               (-> tokens (get 5) :text) => "이다"))

       (fact "as-strs (return texts only)"
             (tokenize "한국어를 처리하는 예시입니닼ㅋㅋ" :as-strs true)
             => (contains "닼")

             (tokenize "한국어를 처리하는 예시입니닼ㅋㅋ" :as-strs true :norm true)
             => (contains "입니다")

             (tokenize "한국어를 처리하는 예시입니닼ㅋㅋ" :as-strs true :norm true :stem true)
             => (contains "이다")))


(fact "tokenize-top-n"
      (let [tokens (tokenize-top-n "대선 후보" 3)]
        (-> tokens count) => 2
        (-> tokens first first first :text) => "대선"))

(fact "detokenize"
      (let [s (detokenize ["연세", "대학교", "보건", "대학원","에","오신","것","을","환영","합니다", "!"])]
        s => (contains "연세대학교 보건 대학원")
        s => (contains "환영합니다")))

(facts "extract-phrases"

       (fact "default operation"
             (let [phrases (extract-phrases "한국어를 처리하는 예시입니닼ㅋㅋ")]
               (-> phrases count) => 4
               (-> phrases (get 2) :text) => "처리하는 예시"))

       (fact "as-strs (return texts only)"
             (extract-phrases "한국어를 처리하는 예시입니닼ㅋㅋ" :as-strs true)
             => (contains "처리하는 예시")))

(fact "split-sentences"
      (-> (split-sentences "가을이다! 남자는 가을을 탄다...... 그렇지? 루루야! 버버리코트 사러 가자!!!!")
          (get 1)
          :text)
      =>
      "남자는 가을을 탄다......")

(fact "add-nouns-to-dictionary"
      (-> (tokenize "불방망이") (get 0) :text)
      => "불"

      (add-nouns-to-dictionary ["불방망이"])

      (-> (tokenize "불방망이") (get 0) :text)
      => "불방망이")

(fact "add-words-to-dictionary"
      (-> (tokenize "그라믄 당신 먼저 얼렁 가이소") (get 0) :text)
      => "그"

      (add-words-to-dictionary KoreanPosJava/Conjunction ["그라믄"])

      (-> (tokenize "그라믄 당신 먼저 얼렁 가이소") (get 0) :text)
      => "그라믄")
