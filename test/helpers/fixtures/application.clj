(ns fixtures.application
  (:require [clojure.test :refer :all]
            [schema-generators.complete :as c]
            [dumbo.models.application :as models.application]))

(def internal-application (c/complete {} models.application/Application))
