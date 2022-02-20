(ns dumbo.diplomat.http-client
  (:require [schema.core :as s]
            [clj-http.client :as client]
            [dumbo.models.application :as models.application]))

(defmulti fetch-application!
          (s/fn [{:pre-application/keys [type]} :- models.application/Application
                 config]
            type))

(s/defmethod fetch-application! :youtube :- models.application/YoutubePreApplication
             [{:pre-application/keys [access-code]} :- models.application/YoutubePreApplication
              {:keys [youtube-client-id youtube-client-secret] :as config}]
             (let [form-params {:code          access-code
                                :client_id     youtube-client-id
                                :client_secret youtube-client-secret
                                :redirect_uri  "urn:ietf:wg:oauth:2.0:oob"
                                :grant_type    "authorization_code"}]
               (-> (client/post "https://accounts.google.com/o/oauth2/token" {:form-params form-params})
                   :body)))

