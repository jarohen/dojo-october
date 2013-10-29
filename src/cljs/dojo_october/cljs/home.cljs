(ns dojo-october.cljs.home
  (:require [dommy.core :as d])
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

(defn watch-hash! [!hash]
  (add-watch !hash :home-page
             (fn [_ _ _ hash]
               (when (= "#/" hash)
                 (d/replace-contents! (sel1 :#content) (doto (node [:canvas {:height 400 :width 400 :style {:margin-top "2em"}}])
                                                         (render-graph (sample-data))))))))

