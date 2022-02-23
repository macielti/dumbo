(ns dumbo.diplomat.http-server.application
  (:require [schema.core :as s]
            [dumbo.adapters.application :as adapters.application]
            [dumbo.controllers.application :as controllers.application]
            [dumbo.jobs.application :as jobs.application]
            [taoensso.timbre :as timbre]))

(s/defn create!
  [{wire-pre-application       :json-params
    {:user-identity/keys [id]} :user-identity
    {:keys [datomic config]}   :components}]
  {:status 201
   :body   (-> (adapters.application/wire->internal-pre-application wire-pre-application)
               (controllers.application/create! id config (:connection datomic))
               adapters.application/internal-application->wire)})

(s/defn refresh-application-authorization!
  [{components :components}]                                ;TODO: This endpoint should be callable only for admin users
  (jobs.application/refresh-application-authentications! components)
  {:status 200
   :body   nil})
