(ns dumbo.db.datomic.application-test
  (:require [clojure.test :refer :all]
            [schema.test :as s]
            [datomic.api :as d]
            [matcher-combinators.test :refer [match?]]
            [common-clj.component.datomic :as component.datomic]
            [fixtures.application]
            [dumbo.db.datomic.application :as db.datomic.application]
            [dumbo.db.datomic.config :as db.datomic.config]))

(s/deftest insert!-test
  (let [mocked-datomic (component.datomic/mocked-datomic db.datomic.config/schemas)]
    (testing "that we can insert a application entity in to datomic"
      (db.datomic.application/insert! fixtures.application/internal-application mocked-datomic))
    (d/release mocked-datomic)))

(s/deftest expired-ones-test
  (let [mocked-datomic (component.datomic/mocked-datomic db.datomic.config/schemas)]
    (db.datomic.application/insert! fixtures.application/internal-expired-application mocked-datomic)
    (db.datomic.application/insert! fixtures.application/internal-application mocked-datomic)
    (testing "that we can query expired application entities"
      (is (match? [{:application/id (:application/id fixtures.application/internal-expired-application)}]
                  (db.datomic.application/expired-ones mocked-datomic))))
    (d/release mocked-datomic)))

(deftest by-application-id-test
  (let [mocked-datomic (component.datomic/mocked-datomic db.datomic.config/schemas)]
    (db.datomic.application/insert! fixtures.application/internal-expired-application mocked-datomic)
    (testing "that we can query application by it's id"
      (is (match? fixtures.application/internal-expired-application
                  (db.datomic.application/by-application-id (:application/id fixtures.application/internal-expired-application)
                                                            mocked-datomic))))
    (d/release mocked-datomic)))
