(ns dumbo.controllers.application
  (:require [schema.core :as s]
            [dumbo.models.application :as models.application]
            [dumbo.diplomat.http-client :as diplomat.http-client]
            [dumbo.db.datomic.application :as database.application]))

(s/defn create! :- models.application/Application
  [pre-application :- models.application/PreApplication
   user-id :- s/Uuid
   config
   datomic]
  (-> (diplomat.http-client/fetch-application! pre-application user-id config)
      (database.application/insert! datomic)))


(defmulti refresh-application-authentication!
          (s/fn [{:application/keys [type]} :- models.application/Application
                 config
                 datomic]
            type))

(s/defmethod refresh-application-authentication! :youtube
             [application :- models.application/Application
              config
              datomic]
             (some-> (diplomat.http-client/refresh-application-authentication! application config)
                     (database.application/insert! datomic)))
