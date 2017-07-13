(ns open-korean-text-4clj.core
  (:import [org.openkoreantext.processor OpenKoreanTextProcessor OpenKoreanTextProcessorJava]
           [org.openkoreantext.processor.tokenizer KoreanTokenizer$KoreanToken]
           [scala.collection JavaConverters]))

(defn normalize [text]
  (OpenKoreanTextProcessor/normalize text))

(defn- compose-token-info-map [^KoreanTokenizer$KoreanToken token & {:keys [^Boolean stem]
                                                                     :or {stem false}}]
  (assoc {}
         :text (if stem
                 (let [stemmed (.stem token)]
                   (if (= (type stemmed) scala.None$)
                     (.text token)
                     (-> stemmed str (clojure.string/replace #"Some|[()]" ""))))
                 (.text token))
         :pos (-> token .pos str keyword)
         :offset (.offset token)
         :length (.length token)
         :unknown (.unknown token)))


(defn tokenize [text & {:keys [^Boolean norm ^Boolean stem ^Boolean as-strs]
                        :or {norm false stem false as-strs false}}]
  (let [tokens (->> (if norm (normalize text) text)
                    OpenKoreanTextProcessor/tokenize
                    JavaConverters/seqAsJavaList
                    (filter #(not (empty? (-> (.text %) clojure.string/trim))))
                    (map #(compose-token-info-map % :stem stem))
                    vec)]
    (if as-strs
      (vec (map :text tokens))
      tokens)))


(defn extract-phrases [text & {:keys [^Boolean filter-spam
                                     ^Boolean enable-hashtags
                                     ^Boolean as-strs]
                              :or {filter-spam true enable-hashtags true}}]
  (let [phrases (-> (OpenKoreanTextProcessor/tokenize text)
                    (OpenKoreanTextProcessorJava/extractPhrases filter-spam enable-hashtags)
                    (->> (map #(assoc {}
                                      :text (.text %)
                                      :offset (.offset %)
                                      :length (.length %))))
                    vec)]
    (if as-strs
      (vec (map :text phrases))
      phrases)))


(defn add-nouns-to-dictionay [nouns]
  (OpenKoreanTextProcessorJava/addNounsToDictionary nouns))


(defn split-sentences [text]
  (->> (OpenKoreanTextProcessorJava/splitSentences text)
       (map #(assoc {}
                    :text (.text %)
                    :start (.start %)
                    :end (.end %)))
       vec))

(defn detokenize [tokens]
  (OpenKoreanTextProcessorJava/detokenize tokens))


(defn tokenize-top-n [text n]
  (->> (OpenKoreanTextProcessor/tokenizeTopN text n)
       JavaConverters/seqAsJavaList
       (mapv #(JavaConverters/seqAsJavaList %))
       (mapv #(into [] (map (fn [token] (JavaConverters/asJavaCollection token)) %)))
       (mapv #(into [] (map (fn [token] (into [] token)) %)))
       (mapv #(mapv (fn [token] (mapv compose-token-info-map token)) %))
       (filter #(not= :Space (-> % first first :pos)))))
