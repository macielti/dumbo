(ns dumbo.diplomat.http-server
  (:require [dumbo.diplomat.http-server.application :as diplomat.http-server.application]))

(def routes ["/application" :post diplomat.http-server.application/create! :route-name :create-application])
