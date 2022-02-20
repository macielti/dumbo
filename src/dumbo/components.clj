(ns dumbo.components
  (:require [com.stuartsierra.component :as component]
            [common-clj.component.config :as component.config]
            [common-clj.component.datomic :as component.datomic]
            [common-clj.component.service :as component.service]
            [common-clj.component.routes :as component.routes]
            [common-clj.component.kafka.producer :as component.producer]
            [common-clj.component.kafka.consumer :as component.consumer]))

(def system
  (component/system-map
    :config (component.config/new-config "resources/config.json" :prod)
    :routes (component.routes/new-routes diplomatic.http-server/routes)
    :datomic (component/using (component.datomic/new-datomic database.config/schemas) [:config])
    :service (component/using (component.service/new-service) [:config :routes :datomic])))

(defn start-system! []
  (component/start system))

(def system-test
  (component/system-map
    :config (component.config/new-config "resources/config.example.json" :test)
    :routes (component.routes/new-routes diplomatic.http-server/routes)
    :datomic (component/using (component.datomic/new-datomic database.config/schemas) [:config])
    :consumer (component/using (component.consumer/new-mock-consumer diplomatic.consumer/topic-consumers) [:config :datomic])
    :producer (component/using (component.producer/new-mock-producer) [:consumer :config])
    :service (component/using (component.service/new-service) [:config :routes :datomic :producer])))
