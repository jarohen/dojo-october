(ns dojo-october.cljs.home
  (:require [dommy.core :as d])
  (:require-macros [dommy.macros :refer [node sel1]]))

(defn render-graph [canvas]
  (let [data [{:value 30
               :color "#F38630"}
              {:value 25
               :color "#008630"}
              {:value 11
               :color "#AA8630"}]
        ctx (.getContext canvas "2d")]
    (.Pie (js/Chart. ctx) (clj->js data) (clj->js {}))))



(defn watch-hash! [!hash]
  (add-watch !hash :home-page
             (fn [_ _ _ hash]
               (when (= "#/" hash)
                 (d/replace-contents! (sel1 :#content) (doto (node [:canvas {:height 400 :width 400}])
                                                         (render-graph)))))))
