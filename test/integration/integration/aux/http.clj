(ns integration.aux.http
  (:require [clojure.test :refer :all]
            [io.pedestal.test :as test]
            [cheshire.core :as json]
            [medley.core :as medley]))

(defn create-application!
  ([wire-application
    service-fn]
   (create-application! wire-application nil service-fn))
  ([wire-application
    jwt-wire
    service-fn]
   (let [{:keys [body status]} (test/response-for service-fn
                                                  :post "/applications"
                                                  :headers (medley/assoc-some {"Content-Type" "application/json"}
                                                                              "Authorization" (some->> jwt-wire
                                                                                                       (format "Bearer %s")))
                                                  :body (json/encode wire-application))]
     {:status status
      :body   (json/decode body true)})))
