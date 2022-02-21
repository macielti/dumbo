(ns dumbo.adapters.application-test
  (:require [clojure.test :refer :all]
            [clj-uuid]
            [matcher-combinators.test :refer [match?]]
            [fixtures.application]
            [dumbo.adapters.application :as adapters.application]))

(deftest internal-application->wire-test
  (testing "that I can externalize a application entity"
    (is (match? {:application {:accessToken  string?
                               :id           string?
                               :refreshToken string?
                               :type         string?
                               :userId       clj-uuid/uuid-string?}}
                (adapters.application/internal-application->wire fixtures.application/internal-application)))))

(deftest wire-update->internal-youtube-application-test
  (testing "that I can externalize a application entity"
    (is (match? {:application/refresh-token "to-be-refreshed"}
                (adapters.application/wire-update->internal-youtube-application fixtures.application/wire-in-youtube-application
                                                                                fixtures.application/internal-application)))))
