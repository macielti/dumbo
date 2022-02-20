(ns dumbo.components
  (:require [com.stuartsierra.component :as component]
            [common-clj.component.config :as component.config]
            [common-clj.component.datomic :as component.datomic]
            [common-clj.component.service :as component.service]
            [common-clj.component.routes :as component.routes]
            [dumbo.diplomat.http-server :as diplomat.http-server]
            [dumbo.db.datomic.config :as datomic.config]))

(def system
  (component/system-map
    :config (component.config/new-config "resources/config.json" :prod)
    :routes (component.routes/new-routes diplomat.http-server/routes)
    :datomic (component/using (component.datomic/new-datomic datomic.config/schemas) [:config])
    :service (component/using (component.service/new-service) [:config :routes :datomic])))

(defn start-system! []
  (component/start system))

(def system-test
  (component/system-map
    :config (component.config/new-config "resources/config.example.json" :test)
    :routes (component.routes/new-routes diplomat.http-server/routes)
    :datomic (component/using (component.datomic/new-datomic datomic.config/schemas) [:config])
    :service (component/using (component.service/new-service) [:config :routes :datomic])))
