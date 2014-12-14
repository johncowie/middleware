(ns middleware.core
  (:require [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.nested-params :refer [wrap-nested-params]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.file-info :refer [wrap-file-info]]))

(defn wrap-error-handling [handler server-error-handler]
  (fn [request]
    (try
      (handler request)
      (catch Exception e
        (-> request
            (assoc :exception e)
            (server-error-handler))))))


(defn site [handler]
  (-> handler
      wrap-session
      wrap-keyword-params
      wrap-nested-params
      wrap-params
      wrap-multipart-params
      (wrap-resource "/public")
      wrap-content-type))
