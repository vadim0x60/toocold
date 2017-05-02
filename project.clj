(defproject toocold "0.1.0-SNAPSHOT"
  :description "An incredibly simple API to look up how good a city's climate is"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [metosin/compojure-api "1.1.10"]]
  :min-lein-version "2.0.0"
  :ring {:handler toocold.handler/app}
  :main toocold.handler
  :profiles {:dev {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]]
                   :plugins [[lein-ring "0.10.0"]]}})
