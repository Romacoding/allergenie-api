(ns allergenie.api-server
  (:gen-class)
  (:require [org.httpkit.server :as server]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]
            ;[ring.util.response :refer [response]]
            [ring.handler.dump :refer [handle-dump]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [allergenie.pollen :as p]
            [allergenie.air :as a]
            [allergenie.weather :as w]))

(defn main-page [_] 
      {:status  200
       :headers {"Content-Type" "text/html"}
       :body    (str "API server is running")})

(defn request-info [req]
  (handle-dump req))

(defroutes app
  (GET "/" [] main-page)
  (GET "/request" [] request-info)
  (GET "/pollen" [] p/pollen)
  (GET "/air" [] a/air)
  (GET "/weather" [] w/weather)
  (not-found "<h1>Sorry, page not found!</h1>"))

(def api-server (atom nil))

(defn stop-server
  "Gracefully shutdown the server, waiting 100ms"
  []
  (when-not (nil? @api-server)
    (println "INFO: Gracefully shutting down server...")
    (@api-server :timeout 100)
    (reset! api-server nil)))

(defn -main
  "Start a httpkit server with a specific port
  #' enables hot-reload of the handler function and anything that code calls"
  [& {:keys [ip port]
      :or   {ip   "0.0.0.0"
             port 8000}}]
  (println "INFO: Starting httpkit server - listening on: " (str "http://" ip ":" port))
  (reset! api-server (server/run-server (wrap-defaults #'app api-defaults) {:port port})))

(def server-config
  {:ip-address "0.0.0.0"
   :port       8080})

(defn optional-keys [& {:keys [ip-address port]
                        :or   {port (:port server-config) ip-address (:ip-address server-config)}}]
  (str "Port: " port ", address " ip-address))
