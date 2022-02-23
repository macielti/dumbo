(ns dumbo.logic.application-test
  (:require [clojure.test :refer :all]
            [dumbo.logic.application :as logic.application]
            [mockfn.macros :as mfn]))

(deftest expires-at-test
  (testing "that we can receive a expires_in data and calculate when it will be expired from now"
    (is (= nil
           (mfn/providing
             []
             (logic.application/expires-at))))))
