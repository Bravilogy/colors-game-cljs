(ns colors-game.db
  (:require [re-frame.core :refer [reg-fx dispatch]]))


(def default-db
  {:all-colors ["red"
                "green"
                "blue"
                "black"
                "gray"
                "orange"
                "pink"
                "aqua"
                "gold"
                "yellow"
                "olive"
                "navy"
                "silver"
                "lime"
                "brown"
                "purple"]
   :active-colors-count 3
   :in-progress? false
   :started? false
   :speed 1000
   :time-left 3
   :real-color nil
   :fake-color nil
   :points 0})


(reg-fx
 :interval
 (let [intervals (atom {})
       start-interval (fn [id event frequency]
                        (swap! intervals assoc id
                               (js/setInterval #(dispatch event) frequency)))]
   (fn [{:keys [id action event frequency]
         :or {frequency 1000}}]
     (condp = action
       :start (when event
                (start-interval id event frequency))
       :adjust (do
                 (js/clearInterval (get @intervals id))
                 (start-interval id event frequency))
       :end (do
              (js/clearInterval (get @intervals id))
              (swap! intervals dissoc id))))))
