(ns dumbo.diplomatic.http-server.access-info
  (:require [schema.core :as s]))

(s/defn upsert!
  [{access-info       :json-params
    {:keys [datomic]} :components}]
  {:status 201
   :body   (-> (adapters.post/wire-post->datomic-post post)
               (controllers.post/create! (:connection datomic) producer)
               adapters.post/datomic-post->wire-post)})
