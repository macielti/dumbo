(ns dumbo.wire.in.application
  (:require [schema.core :as s]
            [camel-snake-kebab.core :as camel-snake-kebab]
            [schema.experimental.abstract-map :as abstract-map]
            [dumbo.wire.datomic.application :as wire.datomic.application]))

(def PreApplicationType (->> wire.datomic.application/application-types
                             (mapv camel-snake-kebab/->SCREAMING_SNAKE_CASE_STRING)
                             (apply s/enum)))

(def base-pre-application
  {:type PreApplicationType})

(s/defschema PreApplication (abstract-map/abstract-map-schema :type
                                                              base-pre-application))

(abstract-map/extend-schema YoutubePreApplication PreApplication ["YOUTUBE"]
  {:accessCode s/Str})

(abstract-map/extend-schema RedditPreApplication PreApplication ["REDDIT"]
  {:accessToken  s/Str
   :refreshToken s/Str})

(s/defschema YouTubeApplication
  {:access_token  s/Str
   :refresh_token s/Str
   :expires_in    s/Int})
