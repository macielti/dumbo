(ns dumbo.jobs.application
  (:require [schema.core :as s]
            [dumbo.db.datomic.application :as database.application]))

(s/defn refresh-application-authentications!
  [{:keys [datomic] :as components}]
  (let [expired-applications (database.application/expired-ones (:connection datomic))]
    (doseq [application expired-applications])))
