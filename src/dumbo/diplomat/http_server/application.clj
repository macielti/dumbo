(ns dumbo.diplomat.http-server.application
  (:require [schema.core :as s]
            [dumbo.adapters.application :as adapters.application]
            [dumbo.controllers.application :as controllers.application]))

(s/defn create!
  [{wire-pre-application       :json-params
    {:user-identity/keys [id]} :user-identity
    {:keys [datomic config]}   :components}]
  {:status 201
   :body   (-> (adapters.application/wire->internal-pre-application wire-pre-application)
               (controllers.application/create! id config (:connection datomic))
               adapters.application/internal-application->wire)})
