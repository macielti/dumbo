(ns dumbo.adapters.application
  (:require [schema.core :as s]
            [common-clj.time.core :as common-time]
            [dumbo.logic.application :as logic.application]
            [dumbo.models.application :as models.application]
            [dumbo.wire.in.application :as wire.in.application]
            [camel-snake-kebab.core :as camel-snake-kebab]
            [dumbo.wire.out.application :as wire.out.application]
            [taoensso.timbre :as timbre])
  (:import (java.util UUID)))

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
             {:pre-application/access-token  accessToken
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
   :application/access-expires-at (logic.application/expires-at expires_in)
   :application/updated-at        (common-time/now-datetime)
   :application/created-at        (common-time/now-datetime)})

(s/defn wire-update->internal-youtube-application :- models.application/Application
  [{:keys [access_token refresh_token expires_in]} :- wire.in.application/YouTubeApplication
   application :- models.application/Application]
  (assoc application
    :application/access-token access_token
    :application/refresh-token refresh_token
    :application/access-expires-at (timbre/spy (logic.application/expires-at expires_in))
    :application/updated-at (common-time/now-datetime)))

(s/defn internal-application->wire :- wire.out.application/ApplicationDocument
  [{:application/keys [id user-id access-token refresh-token type]} :- models.application/Application]
  {:application {:id           (str id)
                 :userId       (str user-id)
                 :accessToken  access-token
                 :refreshToken refresh-token
                 :type         (camel-snake-kebab/->SCREAMING_SNAKE_CASE_STRING type)}})
