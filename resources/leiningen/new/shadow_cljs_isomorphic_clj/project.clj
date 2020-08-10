(defproject {{name}} "0.1.0-SNAPSHOT"

  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :dependencies ~(->> ["incl/luminus/dependencies.edn"
                       "incl/proj/dependencies.edn"]
                      (map (comp read-string slurp))
                      (apply concat))

  :min-lein-version "2.0.0"
  
  :source-paths ["src/clj" "src/cljs" "src/cljc"]
  :test-paths ["test/clj"]
  :resource-paths ["resources" "target/cljsbuild"]

  :target-path "target/%s/"
  :main ^:skip-aot {{name}}.core

  :plugins ~(->> ["incl/luminus/plugins.edn"]
                 (map (comp read-string slurp))
                 (apply concat))
  :clean-targets ^{:protect false}
  [:target-path "target/cljsbuild"]

  :shadow-cljs ~(-> (slurp "incl/luminus/shadow-cljs.edn")
                    (read-string))
  :npm-deps ~(->> ["incl/luminus/npm-deps.edn"]
                  (map (comp read-string slurp))
                  (apply concat))
  :npm-dev-deps ~(->> ["incl/luminus/npm-deps.edn"]
                      (map (comp read-string slurp))
                      (apply concat))
  
  :profiles
  {:uberjar {:omit-source true
             :prep-tasks ["compile" ["shadow" "release" "app"]]
             
             :aot :all
             :uberjar-name "{{name}}.jar"
             :source-paths ["env/prod/clj"  "env/prod/cljs" ]
             :resource-paths ["env/prod/resources"]}

   :dev           [:project/dev :profiles/dev]
   :test          [:project/dev :project/test :profiles/test]

   :project/dev  {:jvm-opts ["-Dconf=dev-config.edn"]  ;; without this, lein start will throw "empty conf" caused by cprop/load-config
                  :dependencies ~(->> ["incl/luminus/profiles-project-dev-dependencies.edn"]
                                      (map (comp read-string slurp))
                                      (apply concat))
                  :plugins      ~(->> ["incl/luminus/profiles-project-dev-plugins.edn"]
                                      (map (comp read-string slurp))
                                      (apply concat)) 
                  
                  :source-paths ["env/dev/clj"  "env/dev/cljs" "test/cljs" ]
                  :resource-paths ["env/dev/resources"]
                  :repl-options {:init-ns user
                                 :timeout 120000}
                  :injections [(require 'pjstadig.humane-test-output)
                               (pjstadig.humane-test-output/activate!)]}
   :project/test {:jvm-opts ["-Dconf=test-config.edn" ]
                  :resource-paths ["env/test/resources"]}
   :profiles/dev {}
   :profiles/test {}})
