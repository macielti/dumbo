(ns dumbo.adapters.application
  (:require [schema.core :as s]
            [dumbo.models.application :as models.application]
            [dumbo.wire.in.application :as wire.in.application]
            [camel-snake-kebab.core :as camel-snake-kebab]
            [clj-time.core :as time])
  (:import (java.util UUID Date)))

(defmulti wire->internal-pre-application
          (s/fn [{:keys [type] :as pre-application}] :- models.application/PreApplicationType
            (s/validate wire.in.application/PreApplication pre-application)
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

(s/defn wire->internal-youtube-application :- models.application/Application
  [{:keys [access_token refresh_token expires_in]} :- wire.in.application/YouTubeApplication
   user-id :- s/Uuid]
  {:application/id                (UUID/randomUUID)
   :application/user-id           user-id
   :application/access-token      access_token
   :application/refresh-token     refresh_token
   :application/type              :youtube
   :application/access-expires-at (->> (time/seconds expires_in)
                                       (time/plus (time/now))
                                       .toDate)
   :application/updated-at        (Date.)
   :application/created-at        (Date.)})
