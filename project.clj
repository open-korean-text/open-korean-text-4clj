(defproject open-korean-text-4clj "0.2.2"
  :description "Open Korean Text Processor wrapper for Clojure"
  :url "http://github.com/open-korean-text/open-korean-text-4clj"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.openkoreantext/open-korean-text "2.1.0"]
                 [midje "1.8.3"]]

  :profiles {:dev {:dependencies [[midje "1.8.3"]]
                   :plugins [[lein-midje "3.2.1"]]}}
  
  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :creds :gpg}]])
