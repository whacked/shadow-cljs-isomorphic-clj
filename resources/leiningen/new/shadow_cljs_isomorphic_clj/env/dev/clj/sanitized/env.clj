(ns {{name}}.env
  (:require
    [clojure.tools.logging :as log]
    [{{name}}.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[{{name}} started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[{{name}} has shut down successfully]=-"))
   :middleware wrap-dev})
