(ns colors-game.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [colors-game.views :as views]
            [colors-game.config :as config]
            [colors-game.events]
            [colors-game.subs]))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn init []
  (re-frame/dispatch-sync [:initialize-db])
  (dev-setup)
  (mount-root))
