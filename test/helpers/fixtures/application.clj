(ns fixtures.application
  (:require [clojure.test :refer :all]
            [schema-generators.complete :as c]
            [dumbo.models.application :as models.application]
            [dumbo.wire.in.application :as wire.in.application]))

(def internal-application (c/complete {:application/access-expires-at #inst "3000-12-31T23:59:59.999-00:00"
                                       :application/access-token      "not-expired-application"} models.application/Application))

(def internal-expired-application (c/complete {:application/access-expires-at #inst "1969-12-31T23:59:59.999-00:00"
                                               :application/access-token      "expired-application"}
                                              models.application/Application))

(def wire-in-reddit-pre-application (c/complete {:type         "REDDIT"
                                                 :accessToken  "random-one"
                                                 :refreshToken "random-two"} wire.in.application/PreApplication))

(def wire-in-youtube-pre-application (c/complete {:type "YOUTUBE"} wire.in.application/PreApplication))

(def wire-in-youtube-application (c/complete {:access_token  "random-token"
                                              :refresh_token "to-be-refreshed"
                                              :expires_in    3600}
                                             wire.in.application/YouTubeApplication))
