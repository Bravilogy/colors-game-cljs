(ns colors-game.utils)


(defn get-random-active-color
  "this function will pick a random color
   from active colors in database."
  [db]
  (->> (:all-colors db)
       (take (:active-colors-count db))
       (rand-nth)))


(defn shuffle-main-colors
  "this function will shuffle the
   main color in the database."
  [db]
  (update db :all-colors shuffle))


(defn refresh-subject-color
  "this function will set new colors
   both - fake and real one."
  [db]
  (assoc db
         :real-color (get-random-active-color db)
         :fake-color (get-random-active-color db)))
