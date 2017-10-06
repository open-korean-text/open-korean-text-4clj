# open-korean-text-4clj 
[![Clojars Project](https://img.shields.io/clojars/v/open-korean-text-4clj.svg)](https://clojars.org/open-korean-text-4clj)
[![Build Status](https://travis-ci.org/open-korean-text/open-korean-text-4clj.svg?branch=master)](https://travis-ci.org/open-korean-text/open-korean-text-4clj)

A [Open Korean Text Processor](https://github.com/open-korean-text/open-korean-text) wrapper for Clojure. 


## Dependencies

* [org.clojure/clojure 1.8.0](https://github.com/clojure/clojure/releases/tag/clojure-1.8.0)
* [org.openkoreantext/open-korean-text 2.1.0](https://github.com/open-korean-text/open-korean-text/releases/tag/open-korean-text-2.1.0)


## Get Started

### Dependencies

[Leiningen](https://leiningen.org) dependency in `project.clj` (from [Clojars](https://clojars.org/open-korean-text-4clj)): 

```clojure
[open-korean-text-4clj "0.2.2"]
```

[Maven](http://maven.apache.org/) dependency information in pom.xml:
```xml
<dependency>
  <groupId>open-korean-text-4clj</groupId>
  <artifactId>open-korean-text-4clj</artifactId>
  <version>0.2.2</version>
</dependency>
```

### Usages

#### normalize

```clojure
(normalize "한국어를 처리하는 예시입니닼ㅋㅋㅋㅋㅋ")
;=> "한국어를 처리하는 예시입니다ㅋㅋㅋ"
```

#### tokenize

* default operation
```clojure
(tokenize "한국어를 처리하는 예시입니닼ㅋㅋ")
;=> [{:text "한국어", :pos :Noun, :offset 0, :length 3, :unknown false}
;    {:text "를", :pos :Josa, :offset 3, :length 1, :unknown false}
;    {:text "처리", :pos :Noun, :offset 5, :length 2, :unknown false}
;    {:text "하는", :pos :Verb, :offset 7, :length 2, :unknown false}
;    {:text "예시", :pos :Noun, :offset 10, :length 2, :unknown false}
;    {:text "입니", :pos :Adjective, :offset 12, :length 2, :unknown false}
;    {:text "닼", :pos :Noun, :offset 14, :length 1, :unknown true}
;    {:text "ㅋㅋ", :pos :KoreanParticle, :offset 15, :length 2, :unknown false}]
;   nil
```

* with normalization
```clojure
(tokenize "한국어를 처리하는 예시입니닼ㅋㅋ" :norm true)
;=> [{:text "한국어", :pos :Noun, :offset 0, :length 3, :unknown false}
;    {:text "를", :pos :Josa, :offset 3, :length 1, :unknown false}
;    {:text "처리", :pos :Noun, :offset 5, :length 2, :unknown false}
;    {:text "하는", :pos :Verb, :offset 7, :length 2, :unknown false}
;    {:text "예시", :pos :Noun, :offset 10, :length 2, :unknown false}
;    {:text "입니다", :pos :Adjective, :offset 12, :length 3, :unknown false}
;    {:text "ㅋㅋ", :pos :KoreanParticle, :offset 15, :length 2, :unknown false}]
;   nil
```

* with normalization & stemming
```clojure
(tokenize "한국어를 처리하는 예시입니닼ㅋㅋ" :norm true :stem true)
;=> [{:text "한국어", :pos :Noun, :offset 0, :length 3, :unknown false}
;    {:text "를", :pos :Josa, :offset 3, :length 1, :unknown false}
;    {:text "처리", :pos :Noun, :offset 5, :length 2, :unknown false}
;    {:text "하다", :pos :Verb, :offset 7, :length 2, :unknown false}
;    {:text "예시", :pos :Noun, :offset 10, :length 2, :unknown false}
;    {:text "이다", :pos :Adjective, :offset 12, :length 3, :unknown false}
;    {:text "ㅋㅋ", :pos :KoreanParticle, :offset 15, :length 2, :unknown false}]
;   nil
```

* as-strs (return texts only)
```clojure
(tokenize "한국어를 처리하는 예시입니닼ㅋㅋ" :as-strs true)
;=> ["한국어" "를" "처리" "하는" "예시" "입니" "닼" "ㅋㅋ"]

(tokenize "한국어를 처리하는 예시입니닼ㅋㅋ" :as-strs true :norm true)
;=> ["한국어" "를" "처리" "하는" "예시" "입니다" "ㅋㅋ"]

(tokenize "한국어를 처리하는 예시입니닼ㅋㅋ" :as-strs true :norm true :stem true)
;=> ["한국어" "를" "처리" "하다" "예시" "이다" "ㅋㅋ"]
```

#### tokenize-top-n
```clojure
(tokenize-top-n "대선 후보" 3)
;=> ([[{:text "대선", :pos :Noun, :offset 0, :length 2, :unknown false}]
;     [{:text "대", :pos :Modifier, :offset 0, :length 1, :unknown false}
;      {:text "선", :pos :Noun, :offset 1, :length 1, :unknown false}]
;     [{:text "대", :pos :Verb, :offset 0, :length 1, :unknown false}
;      {:text "선", :pos :Noun, :offset 1, :length 1, :unknown false}]]
;    [[{:text "후보", :pos :Noun, :offset 3, :length 2, :unknown false}]
;     [{:text "후보", :pos :Noun, :offset 3, :length 2, :unknown true}]
;     [{:text "후", :pos :Noun, :offset 3, :length 1, :unknown false}
;      {:text "보", :pos :Verb, :offset 4, :length 1, :unknown false}]])
;   nil
```

#### detokenize
```clojure
(detokenize ["연세", "대학교", "보건", "대학원","에","오신","것","을","환영","합니다", "!"])
;=> "연세대학교 보건 대학원에 오신것을 환영합니다!"
```

#### extract-phrases
```clojure
(extract-phrases "한국어를 처리하는 예시입니다 ㅋㅋ")
;=> [{:text "한국어", :offset 0, :length 3}
;    {:text "처리", :offset 5, :length 2}
;    {:text "처리하는 예시", :offset 5, :length 7}
;    {:text "예시", :offset 10, :length 2}]
;   nil

(extract-phrases "한국어를 처리하는 예시입니다 ㅋㅋ" :as-strs true)
;=> ["한국어" "처리" "처리하는 예시" "예시"]
```

#### split-sentences
```clojure
(split-sentences "가을이다! 남자는 가을을 탄다...... 그렇지? 루루야! 버버리코트 사러 가자!!!!")
;=> [{:text "가을이다!", :start 0, :end 5}
;    {:text "남자는 가을을 탄다......", :start 6, :end 22}
;    {:text "그렇지?", :start 23, :end 27}
;    {:text "루루야!", :start 28, :end 32}
;    {:text "버버리코트 사러 가자!!!!", :start 33, :end 48}]
;   nil
```

#### add-nouns-to-dictionary
```clojure
(tokenize "불방망이")
;=> [{:text "불", :pos :Noun, :offset 0, :length 1, :unknown false} 
;    {:text "방망이", :pos :Noun, :offset 1, :length 3, :unknown false}]

(add-nouns-to-dictionary ["불방망이"])
;=> nil

(tokenize "불방망이")
;=> [{:text "불방망이", :pos :Noun, :offset 0, :length 4, :unknown false}]
```

#### add-words-to-dictionary 
* added in 0.2.3
```clojure
(tokenize "그라믄")
;=> [{:text "그", :pos :Noun, :offset 0, :length 1, :unknown false} {:text "라", :pos :Josa, :offset 1, :length 1, :unknown false} {:text "믄", :pos :Modifier, :offset 2, :length 1, :unknown false}]

(add-words-to-dictionary KoreanPosJava/Conjunction ["그라믄"])
;=> nil

(tokenize "그라믄")
;=> [{:text "그라믄", :pos :Conjunction, :offset 0, :length 3, :unknown false}]
```

## License

Copyright © 2017 [Seonho Kim](http://seonho.kim)

Distributed under the Eclipse Public License either version 1.0 or any later version.
