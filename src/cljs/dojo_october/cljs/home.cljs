(ns dojo-october.cljs.home
  (:require [dommy.core :as d]
            [ajax.core :refer [GET]])
  (:require-macros [dommy.macros :refer [node sel1]]))

(defn render-graph [canvas data]
  (let [ctx (.getContext canvas "2d")]
    (.Pie (js/Chart. ctx) (clj->js data) (clj->js {}))))

(defn rand-color []
  (str "rgb(" (rand-int 255) "," (rand-int 255) "," (rand-int 255) ")"))

(defn random-value []
  {:value (rand-int 100)
   :color (rand-color)})

(defn sample-data []
  (repeatedly 3 random-value))

(defn render-app [bind-input]
  (let [$canvas (node [:canvas {:height 400 :width 400 :style {:margin-top "2em"}}])]
    (node
     [:div
      $canvas
      [:div {:style {:margin-top "2em"}}
       (doto (node [:input {:type "text"}])
         (bind-input $canvas))]])))

(defn bind-username-box [$input load-user]
  (d/listen! $input :keyup
             (fn [e]
               (when (= 13 (.-keyCode e))
                 (load-user (d/value $input))))))

(defn load-twitter-user [$canvas screen-name]
  (GET "/user"
      {:params {:screen-name screen-name}
       :handler (fn [resp]
                  (js/console.log (pr-str resp))
                  (render-graph $canvas resp))}))

(defn watch-hash! [!hash]
  (add-watch !hash :home-page
             (fn [_ _ _ hash]
               (when (= "#/" hash)
                 (d/replace-contents!
                  (sel1 :#content)
                  (render-app (fn [$input $canvas]
                                (bind-username-box $input #(load-twitter-user $canvas %)))))))))
