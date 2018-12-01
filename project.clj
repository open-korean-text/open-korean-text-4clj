(defproject open-korean-text-4clj "0.2.5"
  :description "Open Korean Text Processor wrapper for Clojure"
  :url "http://github.com/open-korean-text/open-korean-text-4clj"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.openkoreantext/open-korean-text "2.3.0"]]

  :repl-options {:init-ns open-korean-text-4clj.core}

  :lein-release {:deploy-via :clojars})
