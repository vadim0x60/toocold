(ns toocold.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]))

(defn farenheit-to-celcius [t] (* 5/9 (- t 32)))

(defn temperatures [doc label]
  (let [regex (str #"(?<=\|)\s*(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\s+" 
                   label
                   #"[^|]+([CF])\s*=\s*([\d\.]+)\s*(?=[\|{])")]
    (not-empty
      (for [[line month scale temp] (re-seq (re-pattern regex) doc)]
        (let [temp-num (read-string temp)]
          (if (= scale "F") (farenheit-to-celcius temp-num) temp-num))))))

(defn avg [numbers] (/ (apply + numbers) (count numbers)))

(def wiki "https://en.wikipedia.org/wiki/")

(defn summarize [name]
  (let [city-page (slurp (str wiki name "?action=raw"))
        weatherbox-addr (re-find #"(?<=\{\{)[^\{\}]+[Ww]eatherbox(?=\}\})" city-page)
        weather-page (if (nil? weatherbox-addr) city-page (slurp (str wiki "Template:" weatherbox-addr "?action=raw")))]
    (and weather-page
      {:city name 
       :upper-bound (apply max (temperatures weather-page "high"))
       :average-high (avg (temperatures weather-page "high"))
       :average-low (avg (temperatures weather-page "low"))
       :lower-bound (apply min (temperatures weather-page "low"))})))

(s/defschema ClimateSummary
  {:city s/Str 
   :upper-bound s/Num
   :average-high s/Num
   :average-low s/Num
   :lower-bound s/Num})

(def app
  (api
    {:swagger
     {:ui "/"
      :spec "/swagger.json"
      :data {:info {:title "Too cold; didn't move"
                    :description "An incredibly simple API to look up how good a city's climate is"}
             :tags [{:name "api", :description "Climate API"}]}}}

    (context "/" []
      :tags ["api"]

      (GET "/climate" []
        :return ClimateSummary
        :query-params [city :- s/Str]
        :summary "Returns a brief summary of a city's temperature range in Celcius"
        (ok (summarize city))))))
