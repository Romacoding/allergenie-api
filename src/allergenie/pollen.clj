(ns allergenie.pollen
  (:require [clojure.data.json :as json]
            [clj-http.client :as client]))

(defn pollen [req]
  (let [zip (:zip (:params req))
        resp (client/get (str "https://www.pollen.com/api/forecast/current/pollen/" zip)
                         {:headers
                          {:User-Agent (str "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                           :Referer (str "https://www.pollen.com/api/forecast/current/pollen/" zip)}})
        body (json/read-str (resp :body)
                            :key-fn keyword)]
    {:status  200
     :headers {"Content-Type" "application/json"}
     :body    (json/write-str
               (assoc {} :location (get-in body [:Location :DisplayLocation]) :triggers (get-in body [:Location :periods 1 :Triggers]) :index (get-in body [:Location :periods 1 :Index])))}))
