(ns dojo-october.handler
  (:require [ring.util.response :refer [response]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [resources]]
            [compojure.handler :refer [api]]
            [hiccup.page :refer [html5 include-css include-js]]
            [frodo :refer [repl-connect-js]]
            
            [twitter.oauth :refer [make-oauth-creds]]
            [twitter.api.restful :refer [users-show]]
            [clojure.set :refer [rename-keys]]))

(defn page-frame []
  (html5
   [:head
    [:title "dojo-october - CLJS Single Page Web Application"]
    (include-js "//cdnjs.cloudflare.com/ajax/libs/jquery/2.0.3/jquery.min.js")
    (include-js "//netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js")
    (include-js "//cdnjs.cloudflare.com/ajax/libs/Chart.js/0.2.0/Chart.min.js")
    (include-css "//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css")

    (include-js "/js/dojo-october.js")]
   [:body
    [:div.container
     [:div#content]
     [:script (repl-connect-js)]]]))


(defn get-followers [screen-name]
  (-> (users-show :oauth-creds oauth-creds :params {:screen-name screen-name})
      :body
      (select-keys [:friends_count :followers_count])
      (rename-keys {:friends_count :following
                    :followers_count :followers})))

(defn ->pie [{:keys [followers following]}]
  [{:value following :color "#123823"}
   {:value followers :color "#3823ff"}])

(defroutes app-routes
  (GET "/" [] (response (page-frame)))
  (GET "/user" [screen-name]
    (response (pr-str (-> (get-followers screen-name)
                          (->pie)))))
  (resources "/js" {:root "js"}))

(def app 
  (-> app-routes
      api))
