(ns dumbo.models.application
  (:require [schema.core :as s]
            [dumbo.wire.datomic.application :as wire.datomic.application]
            [schema.experimental.abstract-map :as abstract-map]))

(def PreApplicationType (->> wire.datomic.application/application-types
                             (apply s/enum)))

(def base-pre-application
  {:pre-application/type PreApplicationType})

(s/defschema PreApplication (abstract-map/abstract-map-schema :type
                                                              base-pre-application))

(abstract-map/extend-schema YoutubePreApplication PreApplication [:youtube]
  {:pre-application/access-code s/Str})

(abstract-map/extend-schema RedditPreApplication PreApplication [:reddit]
  {:pre-application/access-token  s/Str
   :pre-application/refresh-token s/Str})
