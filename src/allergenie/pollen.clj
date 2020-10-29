(ns allergenie.pollen
  (:require [clojure.data.json :as json]
            [clj-http.client :as client]))

(defn pollen-level [index]
  (cond
    (> index 9.6) "High"
    (> index 7.2) "Medium High"
    (> index 4.8) "Medium"
    (> index 2.4) "Low Medium"
    :else "Low"))

(defn pollen-color [index]
  (cond
    (> index 9.6) "is-danger"
    (> index 4.8) "is-warning"
    :else "is-success"))

(defn pollen [req]
  (let [zip (:zip (:params req))
        resp (client/get (str "https://www.pollen.com/api/forecast/current/pollen/" zip)
                         {:headers
                          {:User-Agent (str "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                           :Referer (str "https://www.pollen.com/api/forecast/current/pollen/" zip)}})
        body (json/read-str (resp :body)
                            :key-fn keyword)
        index (get-in body [:Location :periods 1 :Index])
        level (pollen-level index)
        color (pollen-color index)]

    {:status  200
     :headers {"Content-Type" "application/json"}
     :body    (json/write-str
               (assoc {} :location (get-in body [:Location :DisplayLocation]) :triggers (get-in body [:Location :periods 1 :Triggers]) :index index :level level :color color))}))
