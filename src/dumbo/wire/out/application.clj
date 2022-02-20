(ns dumbo.wire.out.application
  (:require [schema.core :as s]
            [dumbo.wire.datomic.application :as wire.datomic.application]
            [camel-snake-kebab.core :as camel-snake-kebab]))

(def ApplicationType (->> wire.datomic.application/application-types
                          (mapv camel-snake-kebab/->SCREAMING_SNAKE_CASE_STRING)
                          (apply s/enum)))

(s/defschema Application
  {:id              s/Str
   :userId          s/Str
   :accessToken     s/Str
   :refreshToken    s/Str
   :type            ApplicationType
   :accessExpiresAt s/Str
   :updatedAt       s/Str
   :createdAt       s/Str})
