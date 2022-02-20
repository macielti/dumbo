(ns dumbo.wire.datomic.application
  (:require [schema.core :as s]))

(def application-schema
  [{:db/ident       :application/id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity
    :db/doc         "Application Id"}
   {:db/ident       :application/user-id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/doc         "User Id that owns that application"}
   {:db/ident       :application/access-token
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "Access Token for the application authentication"}
   {:db/ident       :application/refresh-token
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "Refresh Token for the application authentication"}
   {:db/ident       :application/type
    :db/valueType   :db.type/keyword
    :db/cardinality :db.cardinality/one
    :db/doc         "Application type: (:youtube, :reddit)"}
   {:db/ident       :application/access-expires-at
    :db/valueType   :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc         "When the tokens will be expired"}
   {:db/ident       :application/updated-at
    :db/valueType   :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc         "Last time when this application entity was updated"}
   {:db/ident       :application/created-at
    :db/valueType   :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc         "When this application was created"}])

(def application-types [:youtube :reddit])

(def ApplicationType (apply s/enum application-types))

(s/defschema Application
  {:application/id                s/Uuid
   :application/user-id           s/Uuid
   :application/access-token      s/Str
   :application/refresh-token     s/Str
   :application/type              ApplicationType
   :application/access-expires-at s/Inst
   :application/updated-at        s/Inst
   :application/created-at        s/Inst})
