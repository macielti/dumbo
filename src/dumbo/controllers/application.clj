(ns dumbo.controllers.application
  (:require [schema.core :as s]
            [dumbo.models.application :as models.application]))

(s/defn create! :- models.application/Application
  [pre-application :- models.application/PreApplication]
  (let [application nil]))
