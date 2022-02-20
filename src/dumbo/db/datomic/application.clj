(ns dumbo.db.datomic.application
  (:require [schema.core :as s]
            [datalevin.core :as d]
            [dumbo.models.application :as models.application]
            [dumbo.wire.datomic.application :as wire.datomic.application]))

(s/defn insert! :- models.application/Application
  [application :- wire.datomic.application/Application
   datomic-connection]
  (d/transact datomic-connection [application])
  application)
