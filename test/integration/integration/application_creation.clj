(ns integration.application-creation
  (:require [clojure.test :refer :all]
            [schema.test :as s]
            [clj-http.fake :as fake]
            [clj-uuid]
            [com.stuartsierra.component :as component]
            [common-clj.component.helper.core :as component.helper]
            [matcher-combinators.test :refer [match?]]
            [integration.aux.http :as aux.http]
            [fixtures.application]
            [fixtures.user-identity]
            [dumbo.components :as components]
            [cheshire.core :as json]
            [dumbo.db.datomic.application :as db.datomic.application]))

(s/deftest create-application-test
  (let [system     (component/start components/system-test)
        service-fn (-> (component.helper/get-component-content :service system)
                       :io.pedestal.http/service-fn)]

    (testing "not authenticated users can't create application via api endpoint"
      (is (match? {:status 422
                   :body   {:cause "Invalid JWT"}}
                  (aux.http/create-application! fixtures.application/wire-in-reddit-pre-application service-fn)))
      (is (match? {:status 422
                   :body   {:cause "Invalid JWT"}}
                  (aux.http/create-application! fixtures.application/wire-in-youtube-pre-application service-fn))))

    (testing "that authenticated users can create applications"
      (is (match? {:status 201
                   :body   {:application {:id           clj-uuid/uuid-string?
                                          :userId       clj-uuid/uuid-string?
                                          :accessToken  "random-token"
                                          :refreshToken "random-refresh-token"
                                          :type         "YOUTUBE"}}}
                  (fake/with-fake-routes
                    {"https://accounts.google.com/o/oauth2/token"
                     {:post (fn [_] {:status 200 :headers {} :body (json/encode fixtures.application/wire-in-youtube-application)})}}
                    (aux.http/create-application! fixtures.application/wire-in-youtube-pre-application
                                                  fixtures.user-identity/jwt-wire
                                                  service-fn)))))
    (component/stop system)))
