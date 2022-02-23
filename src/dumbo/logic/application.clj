(ns dumbo.logic.application
  (:require [schema.core :as s]
            [clj-time.core :as time])
  (:import (java.util Date)))

(s/defn expires-at :- Date
  "expires-in -> value in seconds"
  [expires-in :- s/Int]
  (->> (time/seconds expires-in)
       (time/plus (time/now))
       .toDate))
