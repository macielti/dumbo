(ns integration.aux.refresh-application-authentication
  (:require [clojure.test :refer :all]
            [schema.test :as s]
            [clj-uuid]
            [com.stuartsierra.component :as component]
            [dumbo.components :as components]
            [common-clj.component.helper.core :as component.helper]
            [matcher-combinators.test :refer [match?]]
            [clj-http.fake :as fake]
            [cheshire.core :as json]
            [integration.aux.http :as aux.http]
            [fixtures.application]
            [fixtures.user-identity]
            [taoensso.timbre :as timbre]))

(s/deftest create-application-test
  (let [system             (component/start components/system-test)
        service-fn         (-> (component.helper/get-component-content :service system)
                               :io.pedestal.http/service-fn)
        datomic-connection (timbre/spy (-> (component.helper/get-component-content :datomic system)
                                           :connection))]

    (aux.http/create-application! fixtures.application/wire-in-youtube-pre-application
                                  fixtures.user-identity/jwt-wire
                                  service-fn)

    (testing "that authenticated users can create applications"
      (is (match? {:status 200
                   :body   nil}
                  (fake/with-fake-routes
                    {"https://accounts.google.com/o/oauth2/token"
                     {:post (fn [_] {:status 200 :headers {} :body (json/encode fixtures.application/wire-in-youtube-application)})}}
                    (aux.http/refresh-application-authentication! fixtures.user-identity/jwt-wire
                                                                  service-fn)))))

    (testing "after calling the refresh endpoint, the application must be refreshed"
      (is (match? nil
                  (db.datomic.application/))))
    (component/stop system)))
