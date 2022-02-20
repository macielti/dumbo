(ns dumbo.db.datomic.config
  (:require [dumbo.wire.datomic.application :as wire.datomic.application]))

(def schemas
  (concat []
          wire.datomic.application/application-schema))
