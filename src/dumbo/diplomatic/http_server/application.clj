(ns dumbo.diplomatic.http-server.application
  (:require [schema.core :as s]))

(s/defn create!
  [{application       :json-params
    {:keys [datomic]} :components}]
  {:status 201
   :body   nil})
