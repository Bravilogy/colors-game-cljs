(ns colors-game.subs
  (:require
   [re-frame.core :refer [reg-sub]]))

(reg-sub
 :started?
 (fn [db]
   (:started? db)))

(reg-sub
 :in-progress?
 (fn [db]
   (:in-progress? db)))

(reg-sub
 :active-colors-count
 (fn [db]
   (:active-colors-count db)))

(reg-sub
 :all-colors
 (fn [db]
   (:all-colors db)))

(reg-sub
 :playing?
 :<- [:started?]
 :<- [:in-progress?]
 (fn [[started? in-progress?]]
   (and started? in-progress?)))

(reg-sub
 :time-left
 (fn [db]
   (:time-left db)))

(reg-sub
 :subject-color
 (fn [db]
   (select-keys db [:fake-color :real-color])))

(reg-sub
 :speed
 (fn [db]
   (:speed db)))

(reg-sub
 :points
 (fn [db]
   (:points db)))

(reg-sub
 :active-colors
 :<- [:active-colors-count]
 :<- [:all-colors]
 (fn [[active-colors-count all-colors]]
   (take active-colors-count all-colors)))
