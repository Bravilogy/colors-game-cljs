(ns colors-game.events
  (:require [colors-game.db :as db]
            [colors-game.utils :as utils]
            [re-frame.core :refer [reg-event-db reg-event-fx ->interceptor]]))


;; -- helpers --
(defn- add-more-colors
  "this function will add more colors to the pool."
  [ctx]
  (update-in ctx [:effects :db :active-colors-count] inc))


(defn- shuffle-colors
  "shuffle all colors."
  [ctx]
  (update-in ctx [:effects :db :all-colors] shuffle))


(defn- increase-speed
  [ctx]
  (let [new-speed (- (get-in ctx [:effects :db :speed]) 100)]
    (-> ctx
        (assoc-in [:effects :db :speed] new-speed)
        (assoc-in [:effects :interval] {:id        :tick
                                        :action    :adjust
                                        :frequency new-speed
                                        :event     [:tick]}))))


;; -- interceptors --
(defonce check-time-interceptor
  (->interceptor
   :id :check-time-interceptor
   :after (fn [ctx]
            (cond-> ctx
              ;; if the time has run out, dispatch end-game event
              (< (-> ctx :effects :db :time-left) 1)
              (assoc-in [:effects :dispatch] [:end-game])))))


(defonce game-progression-interceptor
  (->interceptor
   :id :game-progression-interceptor
   :after (fn [ctx]
            (let [{{db :db} :effects} ctx
                  {:keys [points]}    db]
              (cond-> ctx
                (zero? (mod points 4))
                (add-more-colors)

                (and (zero? (mod points 5)) (< points 21))
                (increase-speed)

                (zero? (mod points 6))
                (shuffle-colors)

                ;; always refresh colors
                true
                (update-in [:effects :db] utils/refresh-subject-color))))))


;; -- events --
(reg-event-db
 :initialize-db
 (fn [_ _]
   db/default-db))


(reg-event-fx
 :select-color
 [game-progression-interceptor]
 (fn [{:keys [db]} [_ color]]
   (if (= color (:real-color db))
     ;; if the answer is correct
     {:db (-> db
              (update :points inc)
              (update :time-left + 2))}
     ;; if the answer is wrong
     {:db (update db :time-left dec)})))


(reg-event-fx
 :end-game
 (fn [{:keys [db]}]
   {:db       (-> db
                  (assoc :in-progress? false))
    :interval {:id     :tick
               :action :end}}))


(reg-event-fx
 :start-playing
 (fn [{:keys [db]}]
   {:db       (-> db
                  (assoc :in-progress? true
                         :started? true)
                  (utils/refresh-subject-color))
    :interval {:id        :tick
               :action    :start
               :frequency (:speed db)
               :event     [:tick]}}))


(reg-event-fx
 :restart-game
 (fn []
   {:db       (-> db/default-db
                  (assoc :in-progress? true
                         :started? true)
                  (utils/refresh-subject-color))
    :interval {:id        :tick
               :action    :start
               :frequency (:speed db/default-db)
               :event     [:tick]}}))


(reg-event-fx
 :tick
 [check-time-interceptor]
 (fn [{:keys [db]}]
   {:db (update db :time-left dec)}))
