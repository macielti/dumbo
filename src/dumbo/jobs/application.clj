(ns dumbo.jobs.application
  (:require [schema.core :as s]
            [dumbo.db.datomic.application :as database.application]
            [dumbo.controllers.application :as controllers.application]
            [taoensso.timbre :as timbre]))

(s/defn refresh-application-authentications!
  [{:keys [datomic config]}]
  (let [expired-applications (database.application/expired-ones (:connection datomic))]
    (doseq [application expired-applications]
      (controllers.application/refresh-application-authentication! (timbre/spy application) config (:connection datomic)))))
