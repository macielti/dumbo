(ns dumbo.diplomat.http-server.application
  (:require [schema.core :as s]
            [dumbo.adapters.application :as adapters.application]
            [dumbo.controllers.application :as controllers.application]))

(s/defn create!
  [{wire-pre-application :json-params
    {:keys [datomic]}    :components}]
  (let [pre-application (adapters.application/wire->internal-pre-application wire-pre-application)])
  {:status 201
   :body   (controllers.application/create! )})
