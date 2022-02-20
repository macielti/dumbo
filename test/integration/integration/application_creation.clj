(ns integration.application-creation
  (:require [clojure.test :refer :all]
            [schema.test :as s]
            [com.stuartsierra.component :as component]
            [common-clj.component.helper.core :as component.helper]
            [matcher-combinators.test :refer [match?]]
            [integration.aux.http :as aux.http]
            [fixtures.application]
            [dumbo.components :as components]))

(s/deftest create-application-test
  (let [system     (component/start components/system-test)
        service-fn (-> (component.helper/get-component-content :service system)
                       :io.pedestal.http/service-fn)]

    (testing "not authenticated users can't create application via api endpoint"
      (is (match? {:status 422
                   :body   {:cause "Invalid JWT"}}
                  (aux.http/create-application! fixtures.application/wire-in-reddit-application service-fn)))
      (is (match? {:status 422
                   :body   {:cause "Invalid JWT"}}
                  (aux.http/create-application! fixtures.application/wire-in-youtube-application service-fn))))
    (component/stop system)))
