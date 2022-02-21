(ns dumbo.db.datomic.application
  (:require [schema.core :as s]
            [datomic.api :as d]
            [dumbo.models.application :as models.application]
            [dumbo.wire.datomic.application :as wire.datomic.application]))

(s/defn insert! :- models.application/Application
  [application :- wire.datomic.application/Application
   datomic-connection]
  (d/transact datomic-connection [application])
  application)

;TODO: Add unit tests for this query
(s/defn expired-ones :- [models.application/Application]
  [datomic-connection]
  (->> (d/q '[:find (pull ?application [*])
              :where [?application :application/access-expires-at ?access-expires-at]
              [?application (dumbo.logic.application/expired? {:application/access-expires-at ?access-expires-at})]])
       (->> (map first))
       (->> (mapv #(dissoc % :db/id)))))
