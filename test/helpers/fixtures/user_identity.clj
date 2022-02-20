(ns fixtures.user-identity
  (:require [clojure.test :refer :all]
            [buddy.sign.jwt :as jwt])
  (:import (java.util UUID)))

(def jwt-wire
  (jwt/sign {:user {:id (str (UUID/randomUUID))}}
            "9837ed52-87b5-48c4-bd09-1cd06d18c1c5"))
