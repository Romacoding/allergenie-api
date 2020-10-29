(ns allergenie.weather
  (:require [clojure.data.json :as json]
            [clj-http.client :as client]
            [config.core :refer [env]]
            [clojure.string :as str]))

(defn wind-dir [deg]
  (let [val (Math/floor (+ (/ deg 22.5) 0.5))
        arr ["&#8593 N"
             "&#8599 NNE"
             "&#8599 NE"
             "&#8599 ENE"
             "&#8594 E"
             "&#8600 ESE"
             "&#8600 SE"
             "&#8600 SSE"
             "&#8595 S"
             "&#8601 SSW"
             "&#8601 SW"
             "&#8601 WSW"
             "&#8592 W"
             "&#8598 WNW"
             "&#8598 NW"
             "&#8598 NNW"]]
    (nth arr (mod val 16))))

(defn weather [req]
  (let [zip (:zip (:params req))
        weatherkey (or (System/getenv "WEATHERKEY") (:weatherkey env))
        resp (client/get (str "http://api.openweathermap.org/data/2.5/weather?zip=" zip ",us&appid=" weatherkey))
        body (json/read-str (resp :body)
                            :key-fn keyword)
        info     (assoc {} :description (str/capitalize (:description (first
                                                                       (:weather body))))
                        :temperature (int (Math/floor (- (* 1.8 (:temp (:main body))) 459.67)))
                        :humidity (:humidity (:main body))
                        :pressure (format "%.2f" (* 0.02953 (:pressure (:main body))))
                        :icon (:icon (first (:weather body)))
                        :wind-speed (int (Math/floor (* 2.236937 (:speed (:wind body)))))
                        :wind-deg (:deg (:wind body))
                        :wind-dir (wind-dir (:deg (:wind body)))
                        :location (str (:name body) ", " (:country (:sys body))))]

    {:status  200
     :headers {"Content-Type" "application/json"}
     :body    (json/write-str info)}))
