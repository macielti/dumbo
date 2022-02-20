(ns dumbo.adapters.application-test
  (:require [clojure.test :refer :all]
            [matcher-combinators.test :refer [match?]]
            [fixtures.application]
            [dumbo.adapters.application :as adapters.application]))

(deftest internal-application->wire-test
  (testing "that I can externalize a application entity"
    (is (match? {:accessToken  string?
                 :id           string?
                 :refreshToken string?
                 :type         string?
                 :userId       string?}
                (adapters.application/internal-application->wire fixtures.application/internal-application)))))
