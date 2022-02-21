(ns dumbo.logic.application
  (:require [schema.core :as s]
            [dumbo.models.application :as models.application]))

(s/defn expired? :- s/Bool
  [{:application/keys [access-expires-at]} :- models.application/Application
   now :- s/Inst]
  (.after now access-expires-at))
