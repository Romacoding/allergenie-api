(ns allergenie.air
  (:require [clojure.data.json :as json]
            [clj-http.client :as client]
            [config.core :refer [env]]))

(defn air [req]
  (let [zip (:zip (:params req))
        airkey (or (System/getenv "AIRKEY") (:airkey env))
        resp (client/get (str "http://www.airnowapi.org/aq/observation/zipCode/current/?format=application/json&zipCode=" zip "&API_KEY=" airkey))
        body (json/read-str (resp :body)
                            :key-fn keyword)
        info (reduce (fn [acc it]
                         (let [{:keys [AQI ParameterName ReportingArea StateCode]} it
                               level (get-in it [:Category :Name])
                               color (cond
                                            (> AQI 200) "is-danger"
                                            (> AQI 100) "is-warning"
                                            :else "is-success")]
                           (conj acc {:aqi AQI :name ParameterName :level level :color color :location ReportingArea :state StateCode}))) [] body)]

    {:status  200
     :headers {"Content-Type" "application/json"}
     :body    (json/write-str
               (assoc {} :air info))}))
