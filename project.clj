(defproject dumbo "0.1.0-SNAPSHOT"

  :description "FIXME: write description"

  :url "http://example.com/FIXME"

  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}

  :plugins [[lein-cloverage "1.2.2"]]

  :exclusions [log4j]

  :dependencies [[org.clojure/clojure "1.10.3"]
                 [nubank/mockfn "0.7.0"]
                 [ch.qos.logback/logback-classic "1.2.10"]
                 [clj-http "3.12.3"]
                 [danlentz/clj-uuid "0.1.9"]
                 [net.clojars.macielti/common-clj "9.14.10"]
                 [buddy/buddy-sign "3.4.1"]
                 [camel-snake-kebab "0.4.2"]
                 [com.stuartsierra/component "1.0.0"]
                 [prismatic/schema "1.2.0"]
                 [com.datomic/datomic-free "0.9.5697"]
                 [overtone/at-at "1.2.0"]]

  :resource-paths ["resources"]

  :test-paths ["test/unit" "test/integration" "test/helpers"]

  :repl-options {:init-ns dumbo.components}

  :main dumbo.components/start-system!)
