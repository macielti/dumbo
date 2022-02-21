(ns dumbo.jobs
  (:require [com.stuartsierra.component :as component]
            [medley.core :as medley]
            [overtone.at-at :as at-at]
            [dumbo.jobs.application :as jobs.application]))

(defrecord Jobs [config datomic telegram]
  component/Lifecycle
  (start [component]
    (let [{config-content :config} config
          components (medley/assoc-some {}
                                        :datomic (:datomic datomic)
                                        :config config-content
                                        :telegram (:telegram telegram))
          pool       (at-at/mk-pool)]

      (at-at/interspaced 60000 (partial jobs.application/refresh-application-authentications! components) pool)

      (merge component {:jobs {:pool pool}})))

  (stop [{:keys [jobs]}]
    (at-at/stop-and-reset-pool! (:pool jobs) :stop)))

(defn new-jobs []
  (->Jobs {} {} {}))
