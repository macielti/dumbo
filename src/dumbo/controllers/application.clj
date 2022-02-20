(ns dumbo.controllers.application
  (:require [schema.core :as s]
            [dumbo.models.application :as models.application]
            [dumbo.diplomat.http-client :as diplomat.http-client]
            [dumbo.db.datomic.application :as database.application]
            [taoensso.timbre :as timbre]))

(s/defn create! :- models.application/Application
  [pre-application :- models.application/PreApplication
   user-id :- s/Uuid
   config
   datomic]
  (timbre/spy (-> (diplomat.http-client/fetch-application! pre-application user-id config)
                  (database.application/insert! datomic))))
