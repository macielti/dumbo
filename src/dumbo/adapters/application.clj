(ns dumbo.adapters.application
  (:require [schema.core :as s]
            [dumbo.models.application :as models.application]
            [dumbo.wire.in.application :as wire.in.application]
            [camel-snake-kebab.core :as camel-snake-kebab]))

(defmulti wire->internal-pre-application
          (s/fn [:keys [type]] :- models.application/PreApplicationType
            (camel-snake-kebab/->kebab-case-keyword type)))

(s/defmethod wire->internal-pre-application :youtube :- models.application/YoutubePreApplication
             [{:keys [accessCode type]} :- wire.in.application/YoutubePreApplication]
             {:pre-application/access-code accessCode
              :pre-application/type        (camel-snake-kebab/->kebab-case-keyword type)})

(s/defmethod wire->internal-pre-application :reddit :- models.application/RedditPreApplication
             [{:keys [accessToken refreshToken type]} :- wire.in.application/RedditPreApplication]
             {:pre-application/access-code   accessToken
              :pre-application/refresh-token refreshToken
              :pre-application/type          (camel-snake-kebab/->kebab-case-keyword type)})
