(ns dumbo.db.datomic.application
  (:require [schema.core :as s]
            [datomic.api :as d]
            [dumbo.models.application :as models.application]
            [dumbo.wire.datomic.application :as wire.datomic.application])
  (:import (java.util Date)))

(s/defn insert! :- models.application/Application
  [application :- wire.datomic.application/Application
   datomic-connection]
  (d/transact datomic-connection [application])
  application)

(s/defn expired-ones :- [models.application/Application]
  [datomic-connection]
  (some->> (d/q '[:find (pull ?application [*])
                  :in $ ?now
                  :where [?application :application/access-expires-at ?access-expires-at]
                  [(> ?now ?access-expires-at)]]
                (d/db datomic-connection) (Date.))
           (map first)
           (mapv #(dissoc % :db/id))))

;TODO: add unit test for that query function
(s/defn by-application-id :- (s/maybe models.application/Application)
  [application-id :- s/Uuid
   datomic-connection]
  (some-> (d/q '[:find (pull ?application [*])
                 :in $ ?application-id
                 :where [?application :application/id ?application-id]]
               (d/db datomic-connection) application-id)
          ffirst
          (dissoc :db/id)))
