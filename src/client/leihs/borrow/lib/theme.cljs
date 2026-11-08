(ns leihs.borrow.lib.theme
  "Light/dark color theme. The choice is stored in this browser only
  (localStorage); without one the page follows the operating system.
  The inline script in leihs.borrow.html applies it before the app loads."
  (:require [reagent.core :as reagent]))

(def ^:private storage-key "leihs-borrow-theme")

(defn- read-stored []
  (try
    (let [v (.getItem js/localStorage storage-key)]
      (if (#{"light" "dark"} v) v "auto"))
    (catch :default _ "auto")))

;; "auto", "light" or "dark"
(defonce preference (reagent/atom (read-stored)))

(defn- apply! []
  (let [p @preference
        dark? (or (= p "dark")
                  (and (= p "auto")
                       (some-> js/window (.matchMedia "(prefers-color-scheme: dark)") .-matches)))]
    (.setAttribute js/document.documentElement "data-bs-theme" (if dark? "dark" "light"))))

(defn set-preference! [p]
  (try
    (if (= p "auto")
      (.removeItem js/localStorage storage-key)
      (.setItem js/localStorage storage-key p))
    (catch :default _ nil))
  (reset! preference p)
  (apply!))
