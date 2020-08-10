(ns {{name}}.core
  (:require
    [reagent.core :as r]
    [reagent.dom :as rdom]
    [goog.dom :as gdom]
    [{{name}}.env-loader :as eld]))


(def $main-component-key
  (-> (eld/get :project-config)
      (get-in [:shadow-cljs :builds])
      (keys)
      (first)))


(defn init! []
  (cljs.pprint/pprint (eld/get :project-config))
  (rdom/render
   [:div
    [:h1 (str "Hello from {{name}}'s" $main-component-key)]
    [:span "app initialized"]]
   (gdom/getElement
    (name $main-component-key))))

(init!)
