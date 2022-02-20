(ns fixtures.application
  (:require [clojure.test :refer :all]
            [schema-generators.complete :as c]
            [dumbo.models.application :as models.application]
            [dumbo.wire.in.application :as wire.in.application]))

(def internal-application (c/complete {} models.application/Application))

(def wire-in-reddit-application (c/complete {:type "REDDIT"} wire.in.application/PreApplication))
(def wire-in-youtube-application (c/complete {:type "YOUTUBE"} wire.in.application/PreApplication))
