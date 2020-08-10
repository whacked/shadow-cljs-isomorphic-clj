(ns {{name}}.routes.home
  (:require
   [clojure.java.io :as io]
   [{{name}}.middleware :as middleware]
   [hiccup.core :as h]
   [hiccup.page :as hp]
   [garden.core :as garden]
   [ring.util.response]
   [ring.util.http-response :as response]
   [clj-yaml.core :as yaml]
   [{{name}}.env-loader :as eld]
   
   [clojure.pprint]))

(defn home-routes []
  (let [build-key :app
        build-config (-> (eld/get :project-config)
                         (get-in [:shadow-cljs :builds build-key]))
        frontend-module-name (-> build-config
                                 (:modules)
                                 (keys)
                                 (first)
                                 (name))

        frontend-main-file-name (str frontend-module-name ".js")]
    [""
     {:middleware [middleware/wrap-csrf
                   middleware/wrap-formats]}
   
     ["/:extension/*.file"
      {:get
       (fn [request]
         (let [{filename :.file
                extension :extension}
               (:path-params request)]
           (or
            (case extension
              "css"
              (if (= filename "style.css")
                {:status 200
                 :headers {"Content-Type" "text/plain"}
                 :body (garden/css
                        [[:* {:margin 0
                              :padding 0}]])})
           
              "js"
              (if (= filename frontend-main-file-name)
                {:status 200
                 :headers {"Content-Type" "application/javascript"}
                 :body (-> (str "resources/public/"
                                frontend-main-file-name)
                           (io/file)
                           (slurp))})
           
              nil)
          
            {:status 200
             :headers {"Content-Type" "text/plain"}
             :body (with-out-str
                     (clojure.pprint/pprint request))})))}]
   
     ["/config"
      {:get
       (fn [_]
         {:status 200
          :headers {"Content-Type" "text/html"}
          :body (h/html
                 [:pre
                  (with-out-str
                    (clojure.pprint/pprint
                     (eld/get :project-config)))])})}]
   
     ["/"
      {:get
       (fn [_]
         {:status 200
          :headers {"Content-Type" "text/html"}
          :body (hp/html5
                 [:head
                  [:style
                   (garden/css [[:* {:margin 0
                                     :padding 0}]])]]
                 [:body
                  [:div
                   {:id frontend-module-name}
                   (str
                    frontend-module-name
                    " loading in #"
                    frontend-module-name
                    "...")]
                  [:script
                   {:type "text/javascript"
                    :src
                    (str "/js/"
                         frontend-main-file-name)}]])})}]]))
