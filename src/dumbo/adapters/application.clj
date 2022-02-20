(ns dumbo.adapters.application
  (:require [schema.core :as s]
            [dumbo.models.application :as models.application]
            [dumbo.wire.in.application :as wire.in.application]
            [camel-snake-kebab.core :as camel-snake-kebab])
  (:import (java.util UUID)))

(s/defn wire->internal-application :- models.application/Application
  [{:keys [accessToken refreshToken type]} :- wire.in.application/Application]
  {:application/id            (UUID/randomUUID)
   :application/access-token  accessToken
   :application/refresh-token refreshToken
   :application/type          (camel-snake-kebab/->kebab-case-keyword type)})
