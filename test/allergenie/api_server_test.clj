(ns allergenie.api-server-test
  (:require [clojure.test :refer [deftest is testing]]
            [allergenie.api-server :as SUT]))

(deftest handler-test
  (testing "Response to events"
    (is (= 200 (:status (SUT/handler {}))))
    (is (not= nil? (:body (SUT/handler {}))))))