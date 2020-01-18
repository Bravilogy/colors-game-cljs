(ns colors-game.views
  (:require [re-frame.core :refer [subscribe dispatch]]))


(defn- color-item
  [color]
  (let [playing? @(subscribe [:playing?])]
    [:li.list-inline-item
     [:span.label {:style    {:background-color color
                              :text-transform   "uppercase"
                              :padding          "3px 10px"
                              :border-radius    "3px"
                              :font-size        "10px"
                              :color            :white
                              :cursor           :pointer}
                   :on-click #(when playing?
                                (dispatch [:select-color color]))}
      color]]))


(defn- colors-list
  []
  (let [colors @(subscribe [:active-colors])]
    [:ul.list-inline
     (for [color colors]
       ^{:key color}
       [color-item color])]))


(defn- subject-color
  []
  (let [{:keys [fake-color real-color]} @(subscribe [:subject-color])]
    [:div.text-center
     {:style {:padding "2rem"}}
     [:div {:style {:color          fake-color
                    :font-weight    700
                    :font-size      "18px"
                    :text-transform "uppercase"}}
      real-color]]))


(defn- game-info
  []
  (let [points    @(subscribe [:points])
        speed     @(subscribe [:speed])
        time-left @(subscribe [:time-left])
        playing?  @(subscribe [:playing?])]
    [:<>
     [:ul.list-unstyled
      [:li
       [:h4.text-uppercase
        "Points: " [:span.text-green points]]]
      (when playing?
        [:<>
         [:li
          [:small.text-muted "Speed: "
           (if (>= speed 1000)
             (str (/ speed 1000) "s")
             (str speed "ms"))]]
         [:li
          [:small.text-muted
           [:i.fa.fa-hourglass-half] " " time-left]]])]

     (when-not playing?
       [:<>
        [:p "Oops, the time is up"]
        [:button.btn.btn-sm.btn-danger
         {:on-click #(dispatch [:restart-game])}
         "Restart"]])]))


(defn game []
  [:section.text-center.mt-sm-2
   [:h1 {:style {:font-weight 200
                 :font-size   "2rem"}}
    "Riddle me this - what is my color?"]
   [colors-list]
   [:hr]
   [subject-color]
   [game-info]])


(defn intro
  []
  [:div.jumbotron
   {:style {:background-color "transparent"
            :margin-top       "180px"}}
   [:div.row>div.col-12.text-center
    [:h2 "Welcome to this awesome game!"]
    [:p "Your objective is simple: select the color it says you should select."]
    [:p [:strong "Remember"] " you have just a few seconds to do so."
     [:br]
     "Time is ticking and ticking, ticking..."]]
   [:div.row>div.col-2.offset-5.text-center
    [:button.btn.btn-md.btn-danger.btn-block
     {:on-click #(dispatch [:start-playing])
      :style    {:letter-spacing 1
                 :font-weight    300}}
     "Start"]]])


(defn main-panel []
  (let [active-colors @(subscribe [:active-colors])
        started?      @(subscribe [:started?])]
    [:div.container
     (if started? [game] [intro])]))

