(ns {{name}}.env-loader
  (:refer-clojure :exclude [get])
  (:require [shadow-env.core :as env]
            #?(:clj [clojure.java.io :as io])
            #?(:clj [clojure.data.json :as json])
            #?(:clj [clojure.walk])))

;; write a Clojure function that returns variables to expose to :clj and :cljs.
;; the function must accept one variable, the shadow-cljs build-state
;; (which will be `nil` initially, before compile starts)
#?(:clj
   (do
     (defn read-project-file [project-file]
       (let [project-expr
             (->> project-file
                  (slurp)
                  (read-string)
                  (rest)
                  (clojure.walk/postwalk
                   (fn [val]
                     (macroexpand-1
                      (cond (symbol? val)
                            (if-let [resolved (resolve val)]
                              (cond (= #'clojure.core/unquote
                                       resolved)
                                    'identity

                                    (= #'clojure.core/require
                                       resolved)
                                    "require"

                                    :else val)
                        
                              (name val))
                      
                            :else
                            val)))))]
             
         (->> project-expr
              (drop 2)
              (partition 2)
              (map (fn [[k v]]
                     [k (if (and (seq? v)
                                 (symbol? (first v)))
                          (eval v)
                          v)]))
              (into {:name (first project-expr)
                     :version (second project-expr)}))))

     (defn make-cljs-safe [data]
       (clojure.walk/postwalk
        (fn [val]
          (cond (symbol? val)
                (str val)

                (seq? val)
                (vec val)
                                         
                :else
                val))
        data))

     (defn read-env [build-state]
       (let [
             working-dir (System/getProperty "user.dir")
             
             project-config (-> working-dir
                                (io/file "project.clj")
                                (read-project-file))

             out {:common {:user.dir working-dir}
                  :clj    {:project-config project-config}
                  :cljs   {:project-config
                           (make-cljs-safe project-config)}
                  }]
         out)
       )))

;; define & link a new var to your reader function.
;; you must pass a fully qualified symbol here, so syntax-quote (`) is useful.
;; in this example I use `get` as the name, because we can call Clojure maps
;; as functions, I like to alias my env namespace as `env`, and (env/get :some-key)
;; is very readable.
(env/link get `read-env)
