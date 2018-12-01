(ns open-korean-text-4clj.core-test
  (:require [clojure.test :refer :all]
            [open-korean-text-4clj.core :refer :all])
  (:import [org.openkoreantext.processor KoreanPosJava]))

(deftest normalize-test
  (is (= (normalize "한국어를 처리하는 예시입니닼ㅋㅋㅋㅋㅋ")
         "한국어를 처리하는 예시입니다ㅋㅋㅋ")))


(deftest tokenize-test
  (testing "default operation"
    (let [tokens (tokenize "한국어를 처리하는 예시입니닼ㅋㅋ")]
      (is (= (-> tokens count)
             8))
      (is (= (-> tokens (get 6) :text)
             "닼"))))

  (testing "with normalization"
        (let [tokens (tokenize "한국어를 처리하는 예시입니닼ㅋㅋ" :norm true)]
          (is (= (-> tokens count)
                 7))
          (is (= (-> tokens (get 5) :text)
                 "입니다"))))

  (testing "with normalization & stemming"
        (let [tokens (tokenize "한국어를 처리하는 예시입니닼ㅋㅋ" :norm true :stem true)]
          (is (= (-> tokens count)
                 7))
          (is (= (-> tokens (get 5) :text)
                 "이다"))))

  (testing "as-strs (return texts only)"
    (is (.contains (tokenize "한국어를 처리하는 예시입니닼ㅋㅋ" :as-strs true)
                   "닼"))

    (is (.contains (tokenize "한국어를 처리하는 예시입니닼ㅋㅋ" :as-strs true :norm true)
                   "입니다"))

    (is (.contains (tokenize "한국어를 처리하는 예시입니닼ㅋㅋ" :as-strs true :norm true :stem true)
                   "이다"))))


(deftest tokenize-top-n-test
  (let [tokens (tokenize-top-n "대선 후보" 3)]
    (is (= (-> tokens count)
           2))
    (is (= (-> tokens first first first :text)
           "대선"))))


(deftest detokenize-test
  (let [s (detokenize ["연세", "대학교", "보건", "대학원","에","오신","것","을","환영","합니다", "!"])]
    (is (.contains s "연세대학교 보건 대학원"))
    (is (.contains s "환영합니다!"))))


(deftest extract-phrases-test
  (testing "default operation"
    (let [phrases (extract-phrases "한국어를 처리하는 예시입니닼ㅋㅋ")]
      (is (= (-> phrases count)
             4))
      (is (= (-> phrases (get 2) :text)
             "처리하는 예시"))))

  (testing "as-strs (return texts only)"
    (is (.contains (extract-phrases "한국어를 처리하는 예시입니닼ㅋㅋ" :as-strs true)
                   "처리하는 예시"))))

(deftest split-sentences-test
  (is (= (-> (split-sentences "가을이다! 남자는 가을을 탄다...... 그렇지? 루루야! 버버리코트 사러 가자!!!!")
             (get 1)
             :text)
         "남자는 가을을 탄다......")))


(deftest add-nouns-to-dictionary-test
  (is (= (-> (tokenize "불방망이") (get 0) :text)
         "불"))

  (add-nouns-to-dictionary ["불방망이"])

  (is (= (-> (tokenize "불방망이") (get 0) :text)
         "불방망이")))


(deftest add-words-to-dictionary-test
  (is (= (-> (tokenize "그라믄 당신 먼저 얼렁 가이소") (get 0) :text)
         "그"))

  (add-words-to-dictionary KoreanPosJava/Conjunction ["그라믄"])

  (is (= (-> (tokenize "그라믄 당신 먼저 얼렁 가이소") (get 0) :text)
         "그라믄")))
