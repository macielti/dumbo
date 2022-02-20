(ns integration.aux.http
  (:require [clojure.test :refer :all]
            [io.pedestal.test :as test]
            [cheshire.core :as json]))

(defn create-application!
  [wire-application
   service-fn]
  (let [{:keys [body status]} (test/response-for service-fn
                                                 :post "/applications"
                                                 :headers {"Content-Type" "application/json"}
                                                 :body (json/encode wire-application))]
    {:status status
     :body   (json/decode body true)}))
