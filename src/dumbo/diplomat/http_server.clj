(ns dumbo.diplomat.http-server
  (:require [dumbo.interceptors.user-identity :as interceptors.user-identity]
            [dumbo.diplomat.http-server.application :as diplomat.http-server.application]))

(def routes [["/applications" :post [interceptors.user-identity/user-identity-interceptor
                                     diplomat.http-server.application/create!] :route-name :create-application]
             ["/applications/refresh" :post [interceptors.user-identity/user-identity-interceptor
                                             diplomat.http-server.application/refresh-application-authorization!] :route-name :refresh-application-authentication]])
