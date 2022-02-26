(ns integration.refresh-application-authentication
  (:require [clojure.test :refer :all]
            [schema.test :as s]
            [clj-uuid]
            [mockfn.macros :as mfn]
            [com.stuartsierra.component :as component]
            [common-clj.time.core :as common-time]
            [dumbo.components :as components]
            [common-clj.component.helper.core :as component.helper]
            [matcher-combinators.test :refer [match?]]
            [clj-http.fake :as fake]
            [cheshire.core :as json]
            [integration.aux.http :as aux.http]
            [fixtures.application]
            [fixtures.user-identity]
            [dumbo.db.datomic.application :as db.datomic.application]
            [dumbo.logic.application :as logic.application])
  (:import (java.util UUID)))

(s/deftest create-application-test
  (let [system             (component/start components/system-test)
        service-fn         (-> (component.helper/get-component-content :service system)
                               :io.pedestal.http/service-fn)
        datomic-connection (-> (component.helper/get-component-content :datomic system)
                               :connection)
        {wire-created-application :body} (fake/with-fake-routes
                                           {"https://accounts.google.com/o/oauth2/token"
                                            {:post (fn [_] {:status 200 :headers {} :body (json/encode fixtures.application/wire-in-youtube-expired-application)})}}
                                           (aux.http/create-application! fixtures.application/wire-in-youtube-pre-application
                                                                         fixtures.user-identity/jwt-wire-admin
                                                                         service-fn))]

    (testing "you can't call refresh application endpoint if you are not an admin"
      (is (match? {:status 403
                   :body   {:cause "Insufficient privileges/roles/permission"}}
                  (aux.http/refresh-application-authentication! fixtures.user-identity/jwt-wire
                                                                service-fn))))

    (testing "admin users can refresh applications"
      (is (match? {:status 200
                   :body   nil}
                  (fake/with-fake-routes
                    {"https://accounts.google.com/o/oauth2/token"
                     {:post (fn [_] {:status 200 :headers {} :body (json/encode fixtures.application/wire-in-youtube-application)})}}
                    (mfn/providing
                      [(logic.application/expires-at 3600) #inst "1999-12-31T23:59:59.999-00:00"
                       (common-time/now-datetime) #inst "2030-12-31T23:59:59.999-00:00"]
                      (aux.http/refresh-application-authentication! fixtures.user-identity/jwt-wire-admin
                                                                    service-fn))))))

    (testing "after calling the refresh endpoint, the application must be refreshed"
      (is (match? {:application/access-token  "random-token"
                   :application/refresh-token "to-be-refreshed"}
                  (db.datomic.application/by-application-id (-> wire-created-application :application :id UUID/fromString) datomic-connection))))
    (component/stop system)))
