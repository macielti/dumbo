(ns dumbo.jobs.application
  (:require [schema.core :as s]
            [dumbo.db.datomic.application :as database.application]
            [dumbo.controllers.application :as controllers.application]
            [taoensso.timbre :as timbre]))

(s/defn refresh-application-authentications!
  [{:keys [datomic config] :as components}]
  (let [expired-applications (database.application/expired-ones (:connection datomic))]
    (doseq [application expired-applications]
      ;TODO: Use kafka producer/consumer to call the controllers.application/refresh-application-authentication! controller so we can be more scalable
      (controllers.application/refresh-application-authentication! (timbre/spy application) config datomic))))
