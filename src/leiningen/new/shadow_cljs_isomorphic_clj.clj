(ns leiningen.new.shadow-cljs-isomorphic-clj
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files]]
            [leiningen.core.main :as main]
            [clojure.string]))

(def render (renderer "shadow-cljs-isomorphic-clj"))

(defn shadow-cljs-isomorphic-clj
  "FIXME: write documentation"
  [name]
  (let [data {:name name
              :sanitized (name-to-path name)}]
    (main/info "Generating fresh 'lein new' shadow-cljs-isomorphic-clj project.")
    (apply ->files data
           (->> (concat
                 [["gitignore" ".gitignore"]
                  "README.org"
                  "project.clj"
                  "default.nix"
                  "dev-config.edn"
                  ;; "package.json"
                  "incl/luminus/dependencies.edn"
                  "incl/luminus/npm-deps.edn"
                  "incl/luminus/npm-dev-deps.edn"
                  "incl/luminus/plugins.edn"
                  "incl/luminus/profiles-project-dev-dependencies.edn"
                  "incl/luminus/profiles-project-dev-plugins.edn"
                  "incl/luminus/shadow-cljs.edn"
                  "incl/proj/dependencies.edn"
                  "env/dev/clj/user.clj"
                  "env/dev/clj/{{sanitized}}/dev_middleware.clj"
                  "env/dev/clj/{{sanitized}}/env.clj"
                  "env/dev/cljs/{{sanitized}}/app.cljs"
                  "src/cljc/{{sanitized}}/env_loader.cljc"
                  "src/clj/{{sanitized}}/config.clj"
                  "src/clj/{{sanitized}}/core.clj"
                  "src/clj/{{sanitized}}/handler.clj"
                  "src/clj/{{sanitized}}/layout.clj"
                  "src/clj/{{sanitized}}/middleware.clj"
                  "src/clj/{{sanitized}}/middleware/formats.clj"
                  "src/clj/{{sanitized}}/nrepl.clj"
                  "src/clj/{{sanitized}}/routes/home.clj"
                  "src/cljs/{{sanitized}}/ajax.cljs"
                  "src/cljs/{{sanitized}}/core.cljs"
                  ])
                (map (fn [maybe-pair]
                       (let [source (if (string? maybe-pair)
                                      (-> maybe-pair
                                          (clojure.string/replace #"\{" "")
                                          (clojure.string/replace #"\}" ""))
                                      (first maybe-pair))
                             target (if (string? maybe-pair)
                                      maybe-pair
                                      (second maybe-pair))]
                         [target (render source data)])))))))
