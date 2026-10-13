(ns leihs.borrow.translations)

(def dict
  {:borrow

  ; --- GENERAL TERMS ---
  ; prefer using those over something like `example-page-section-title-contracts`
  ; but NOT for interpolation / string concating (that assumes to much about the languages)
  ; TODO: implement fallback keys so we can have both: `(t [:example-page-section-title-contracts :terms.contracts])
   {:terms {:contract {:de-CH "Vertrag" :en-GB "Contract" :es "Contrato" :ca "Contracte" :fr-CH "Contrat"}
            :contracts {:de-CH "Verträge" :en-GB "Contracts" :es "Contratos" :ca "Contractes" :fr-CH "Contrats"}

            :delegation {:de-CH "Delegation" :en-GB "Delegation" :es "Delegación" :ca "Delegació" :fr-CH "Délégation"}
            :delegations {:de-CH "Delegationen" :en-GB "Delegations" :es "Delegaciones" :ca "Delegacions" :fr-CH "Délégations"}
            :favorite {:de-CH "Favorit" :en-GB "Favorite" :es "Favorito" :ca "Preferit" :fr-CH "Favori"}
            :close {:de-CH "Schliessen" :en-GB "Close" :es "Cerrar" :ca "Tancar" :fr-CH "Fermer"}

            :weekdays {:monday {:de-CH "Montag" :en-GB "Monday" :es "Lunes" :ca "Dilluns" :fr-CH "Lundi"}
                       :tuesday {:de-CH "Dienstag" :en-GB "Tuesday" :es "Martes" :ca "Dimarts" :fr-CH "Mardi"}
                       :wednesday {:de-CH "Mittwoch" :en-GB "Wednesday" :es "Miércoles" :ca "Dimecres" :fr-CH "Mercredi"}
                       :thursday {:de-CH "Donnerstag" :en-GB "Thursday" :es "Jueves" :ca "Dijous" :fr-CH "Jeudi"}
                       :friday {:de-CH "Freitag" :en-GB "Friday" :es "Viernes" :ca "Divendres" :fr-CH "Vendredi"}
                       :saturday {:de-CH "Samstag" :en-GB "Saturday" :es "Sábado" :ca "Dissabte" :fr-CH "Samedi"}
                       :sunday {:de-CH "Sonntag" :en-GB "Sunday" :es "Domingo" :ca "Diumenge" :fr-CH "Dimanche"}}}

  ; --- GENERAL PHRASES ---
  ; use them for consistency between different parts of the app
  ; but NOT for interpolation / string concating (that assumes to much about the languages)
    :phrases
    {:items-count
     {:de-CH "{itemCount, plural,
                =0 {Keine Gegenstände}
                =1 {# Gegenstand}
                other {# Gegenstände}
                }"
      :en-GB "{itemCount, plural,
                =0 {No items}
                one {# item}
                other {# items}
                }"
      :es "{itemCount, plural, =0 {Ningún artículo} one {# artículo} other {# artículos} }" :ca "{itemCount, plural, =0 {Cap article} one {# article} other {# articles} }"
      :fr-CH "{itemCount, plural,
                =0 {Aucun objet}
                =1 {# objet}
                other {# objets}
                }"}

     :contract-display-name
     {:de-CH "Vertrag {ID} mit {poolName} vom {date, date, narrow}"
      :en-GB "Contract {ID} with {poolName} from {date, date, narrow}"
      :es "Contrato {ID} con {poolName} del {date, date, narrow}" :ca "Contracte {ID} amb {poolName} del {date, date, narrow}"
      :fr-CH "Contrat {ID} avec {poolName} du {date, date, narrow}"}

     :user-or-delegation-personal-postfix
     {:de-CH " (persönlich)" :en-GB " (personal)" :es " (personal)" :ca " (personal)" :fr-CH " (personnel)"}}

    :all {:en-GB "All" :es "Todos" :ca "Tots"
          :de-CH "Alle"
          :fr-CH "Tous"}

  ; --- PER COMPONENT TRANSLATIONS (reused between some features) ---

    :order-panel
    {:label {:quantity {:en-GB "Quantity" :es "Cantidad" :ca "Quantitat" :de-CH "Anzahl" :fr-CH "Quantité"}
             :minus {:en-GB "Minus" :es "Menos" :ca "Menys" :de-CH "Minus" :fr-CH "Moins"}
             :plus {:en-GB "Plus" :es "Más" :ca "Més" :de-CH "Plus" :fr-CH "Plus"}
             :pool {:en-GB "Inventory pool" :es "Grupo de inventario" :ca "Grup d'inventari" :de-CH "Inventarpark" :fr-CH "Pool d'inventaire"}
             :pool-max-amount {:en-GB "{pool} (max. {amount, number})" :es "{pool} (máx. {amount, number})" :ca "{pool} (màx. {amount, number})" :de-CH "{pool} (max. {amount, number})" :fr-CH "{pool} (max. {amount, number})"}
             :pool-max-amount-info {:en-GB  "Maximum available amount: {amount, number}" :es "Cantidad máxima disponible: {amount, number}" :ca "Quantitat màxima disponible: {amount, number}" :de-CH "Maximal verfügbarer Bestand: {amount, number}" :fr-CH "Quantité maximale disponible : {amount, number}"}
             :user-delegation {:en-GB "Order for" :es "Pedido para" :ca "Comanda per a" :de-CH "Bestellung für" :fr-CH "Commande pour"}
             :timespan {:en-GB "Time span" :es "Período" :ca "Període" :de-CH "Zeitraum" :fr-CH "Période"}
             :from {:en-GB "From" :es "Desde" :ca "Des de" :de-CH "Von" :fr-CH "De"}
             :until {:en-GB "Until" :es "Hasta" :ca "Fins a" :de-CH "Bis" :fr-CH "À"}
             :undefined {:en-GB "Not set" :es "Sin definir" :ca "Sense definir" :de-CH "Unbestimmt" :fr-CH "Indéfini"}
             :show-day-quants {:en-GB "Show availability in calendar" :es "Mostrar disponibilidad en el calendario" :ca "Mostrar la disponibilitat al calendari" :de-CH "Verfügbarkeit im Kalender anzeigen" :fr-CH "Afficher la disponibilité dans le calendrier"}
             :reservation-type {:en-GB "Type of reservation" :es "Tipo de reserva" :ca "Tipus de reserva" :de-CH "Art der Reservation" :fr-CH "Type de réservation"}
             :by-hours {:en-GB "By the hour" :es "Por horas" :ca "Per hores" :de-CH "Stundenweise" :fr-CH "À l'heure"}
             :whole-day {:en-GB "Whole days" :es "Días completos" :ca "Dies complets" :de-CH "Ganze Tage" :fr-CH "Journées entières"}
             :pickup-time {:en-GB "Pickup at" :es "Hora de retiro" :ca "Hora de recollida" :de-CH "Abholung um" :fr-CH "Retrait à"}
             :return-time {:en-GB "Return at" :es "Hora de devolución" :ca "Hora de retorn" :de-CH "Rückgabe um" :fr-CH "Retour à"}
             :by-hours-info {:en-GB "You pick the item up on the first day and return it on the last day at the chosen times." :es "Retiras el artículo el primer día y lo devuelves el último día a las horas elegidas." :ca "Reculls l'article el primer dia i el retornes l'últim dia a les hores triades." :de-CH "Du holst den Gegenstand am ersten Tag ab und gibst ihn am letzten Tag zur gewählten Zeit zurück." :fr-CH "Tu retires l'article le premier jour et le retournes le dernier jour aux heures choisies."}
             :whole-day-hours-info {:en-GB "Whole days, from {startTime} on the first day until {endTime} on the last day." :es "Días completos, desde las {startTime} del primer día hasta las {endTime} del último." :ca "Dies complets, des de les {startTime} del primer dia fins a les {endTime} de l'últim." :de-CH "Ganze Tage, von {startTime} am ersten Tag bis {endTime} am letzten Tag." :fr-CH "Journées entières, de {startTime} le premier jour jusqu'à {endTime} le dernier jour."}
             :max-hours-info {:en-GB "{hours, plural, one {You can reserve for at most # hour in a row.} other {You can reserve for at most # hours in a row.}}" :es "{hours, plural, one {Puedes reservar como máximo # hora seguida.} other {Puedes reservar como máximo # horas seguidas.}}" :ca "{hours, plural, one {Pots reservar com a màxim # hora seguida.} other {Pots reservar com a màxim # hores seguides.}}" :de-CH "{hours, plural, one {Du kannst höchstens # Stunde am Stück reservieren.} other {Du kannst höchstens # Stunden am Stück reservieren.}}" :fr-CH "{hours, plural, one {Tu peux réserver au maximum # heure d'affilée.} other {Tu peux réserver au maximum # heures d'affilée.}}"}
             :no-max-hours-info {:en-GB "There is no limit of hours." :es "No hay un límite de horas." :ca "No hi ha límit d'hores." :de-CH "Es gibt keine Stundenbegrenzung." :fr-CH "Il n'y a pas de limite d'heures."}}
     :validate {:missing-quantity
                {:de-CH "Verfügbarkeit kann nicht geprüft werden, da die Anzahl fehlt"
                 :en-GB "Availability can not be checked because the quantity is missing"
                 :es "No se puede comprobar la disponibilidad porque falta la cantidad" :ca "No es pot comprovar la disponibilitat perquè falta la quantitat"
                 :fr-CH "La disponibilité ne peut pas être vérifiée car la quantité est manquante"}

                :invalid-start-date
                {:de-CH "Ungültiges Beginndatum"
                 :en-GB "Invalid start date"
                 :es "Fecha de inicio no válida" :ca "Data d'inici no vàlida"
                 :fr-CH "Date de début invalide"}
                :invalid-end-date
                {:de-CH "Ungültiges Enddatum"
                 :en-GB "Invalid end date"
                 :es "Fecha de fin no válida" :ca "Data de fi no vàlida"
                 :fr-CH "Date de fin invalide"}
                :return-before-pickup
                {:de-CH "Die Rückgabe muss nach der Abholung sein"
                 :en-GB "The return has to be after the pickup"
                 :es "La devolución tiene que ser posterior al retiro" :ca "El retorn ha de ser posterior a la recollida"
                 :fr-CH "Le retour doit être postérieur au retrait"}
                :pickup-time-over
                {:de-CH "Die Abholzeit ist bereits vorbei"
                 :en-GB "The pickup time is already over"
                 :es "La hora de retiro ya pasó" :ca "L'hora de recollida ja ha passat"
                 :fr-CH "L'heure de retrait est déjà passée"}
                :max-reservation-hours
                {:de-CH "{hours, plural, one {Du kannst höchstens # Stunde am Stück reservieren} other {Du kannst höchstens # Stunden am Stück reservieren}}"
                 :en-GB "{hours, plural, one {You can reserve for at most # hour in a row} other {You can reserve for at most # hours in a row}}"
                 :es "{hours, plural, one {Puedes reservar como máximo # hora seguida} other {Puedes reservar como máximo # horas seguidas}}" :ca "{hours, plural, one {Pots reservar com a màxim # hora seguida} other {Pots reservar com a màxim # hores seguides}}"
                 :fr-CH "{hours, plural, one {Tu peux réserver au maximum # heure d'affilée} other {Tu peux réserver au maximum # heures d'affilée}}"}
                :not-member-of-a-group
                {:de-CH "Nur Mitglieder einer Gruppe dieses Inventarparks können hier reservieren. Bitte wende dich an den Inventarpark."
                 :en-GB "Only members of a group of this inventory pool can reserve here. Please contact the inventory pool."
                 :es "Para reservar aquí tienes que pertenecer a uno de los grupos de este inventario. Contacta con el almacén." :ca "Per reservar aquí has de pertànyer a un dels grups d'aquest inventari. Contacta amb el magatzem."
                 :fr-CH "Seuls les membres d'un groupe de ce pool d'inventaire peuvent réserver ici. Merci de contacter le pool d'inventaire."}
                :quantity-not-available-in-period
                {:de-CH "Im gewählten Zeitraum ist nicht genügend verfügbar"
                 :en-GB "Not enough items are available in the chosen period"
                 :es "No hay suficientes unidades disponibles en el horario elegido" :ca "No hi ha prou unitats disponibles en l'horari triat"
                 :fr-CH "Pas assez d'articles disponibles pendant la période choisie"}
                :pickup-day-over
                {:de-CH "Abholung an diesem Tag nicht mehr möglich"
                 :en-GB "Pickup on this day is no longer possible"
                 :es "El retiro en este día ya no es posible" :ca "Ja no es pot recollir aquest dia"
                 :fr-CH "Le retrait n'est plus possible ce jour-là"}
                :start-after-end
                {:de-CH "Enddatum muss nach Beginndatum sein"
                 :en-GB "End date must be after start date"
                 :es "La fecha de fin debe ser posterior a la fecha de inicio" :ca "La data de fi ha de ser posterior a la data d'inici"
                 :fr-CH "La date de fin doit être postérieure à la date de début"}
                :end-date-too-late
                {:de-CH "Datum darf nicht nach {maxDate, date, small} sein"
                 :en-GB "Date must not be after {maxDate, date, small}"
                 :es "La fecha no puede ser posterior al {maxDate, date, small}" :ca "La data no pot ser posterior al {maxDate, date, small}"
                 :fr-CH "La date ne doit pas dépasser {maxDate, date, small}"}

                :start-date-in-past
                {:de-CH "Abholdatum liegt in der Vergangenheit"
                 :en-GB "Pickup date is in the past"
                 :es "La fecha de retiro está en el pasado" :ca "La data de recollida és en el passat"
                 :fr-CH "La date de retrait se situe dans le passé"}
                :start-date-not-before
                {:de-CH "{days, plural, =1 {Abholung frühestens morgen} other {Abholung frühestens heute in # Arbeitstagen}}"
                 :en-GB "{days, plural, =1 {Earliest pickup date is tomorrow} other {Earliest pickup date in # working days from now}}"
                 :es "{days, plural, =1 {El retiro es posible a partir de mañana} other {El retiro es posible dentro de # días hábiles}}" :ca "{days, plural, =1 {Es pot recollir a partir de demà} other {Es pot recollir d'aquí a # dies feiners}}"
                 :fr-CH "{days, plural,
                          =1 {Retrait possible au plus tôt demain}
                          other {Retrait possible dans # jours ouvrables}}"}
                :quantity-to-large-at-day
                {:de-CH
                 "Gegenstand ist am {startDate, date, small} nicht in der gewünschten Menge verfügbar"
                 :en-GB "Item is not available in the requested quantity on {startDate, date, small}."
                 :es "El artículo no está disponible en la cantidad solicitada el {startDate, date, small}." :ca "L'article no està disponible en la quantitat sol·licitada el {startDate, date, small}."
                 :fr-CH "L'article n'est pas disponible dans la quantité demandée le {startDate, date, small}."}
                :quantity-to-large-in-range
                {:de-CH "Gegenstand ist in diesem Zeitraum nicht in der gewünschten Menge verfügbar"
                 :en-GB "Item is not available in the desired quantity during this period"
                 :es "El artículo no está disponible en la cantidad deseada durante este período" :ca "L'article no està disponible en la quantitat desitjada durant aquest període"
                 :fr-CH "L'article n'est pas disponible dans la quantité souhaitée durant cette période"}

                :pool-closed-at-start-date
                {:de-CH "Abholung am {startDate, date, small} nicht möglich"
                 :en-GB "Pickup not possible on {startDate, date, small}"
                 :es "No es posible retirar el {startDate, date, small}" :ca "No es pot recollir el {startDate, date, small}"
                 :fr-CH "Retrait non possible le {startDate, date, small}"}
                :pool-closed-at-end-date
                {:de-CH "Rückgabe am {endDate, date, small} nicht möglich"
                 :en-GB "Return not possible on {endDate, date, small}"
                 :es "No es posible devolver el {endDate, date, small}" :ca "No es pot retornar el {endDate, date, small}"
                 :fr-CH "Retour non possible le {endDate, date, small}"}
                :pool-closed-at-start-and-end-date
                {:de-CH "Abholung/Rückgabe am {startDate, date, small} nicht möglich"
                 :en-GB "Pickup/return not possible on {startDate, date, small}"
                 :es "No es posible retirar/devolver el {startDate, date, small}" :ca "No es pot recollir/retornar el {startDate, date, small}"
                 :fr-CH "Retrait/retour non possible le {startDate, date, small}"}
                :pool-closed-max-visits
                {:de-CH " (maximale Besucherzahl erreicht)"
                 :en-GB " (maximum visitor capacity reached)"
                 :es " (se alcanzó el número máximo de visitas)" :ca " (s'ha arribat al nombre màxim de visites)"
                 :fr-CH " (capacité maximale de visiteurs atteinte)"}
                :closed-on-day-of-week
                {:de-CH "{dayName} geschlossen"
                 :en-GB "closed on {dayName}"
                 :es "cerrado los {dayName}" :ca "tancat els {dayName}"
                 :fr-CH "fermé le {dayName}"}

                :maximum-reservation-duration
                {:de-CH "Maximale Reservationsdauer ist beschränkt auf {days} Tage"
                 :en-GB "Maximum reservation duration is restricted to {days} days"
                 :es "La duración máxima de una reserva está limitada a {days} días" :ca "La durada màxima d'una reserva està limitada a {days} dies"
                 :fr-CH "La durée maximale de réservation est limitée à {days} jours"}

                :no-pool-access
                {:de-CH "Keine Berechtigung für diesen Inventarpark"
                 :en-GB "No access to this inventory pool"
                 :es "Sin acceso a este grupo de inventario" :ca "Sense accés a aquest grup d'inventari"
                 :fr-CH "Aucun accès à ce pool d'inventaire"}
                :pool-suspension
                {:de-CH "Benutzer für diesen Inventarpark gesperrt"
                 :en-GB "User suspended for this inventory pool"
                 :es "Usuario suspendido en este grupo de inventario" :ca "Usuari suspès en aquest grup d'inventari"
                 :fr-CH "Utilisateur suspendu pour ce pool d'inventaire"}
                :item-not-available-in-pool
                {:de-CH "Gegenstand in diesem Inventarpark nicht verfügbar"
                 :en-GB "Item not available in this inventory pool"
                 :es "Artículo no disponible en este grupo de inventario" :ca "Article no disponible en aquest grup d'inventari"
                 :fr-CH "Article non disponible dans ce pool d'inventaire"}
                :unknown-pool
                {:de-CH "Unbekannter Inventarpark"
                 :en-GB "Unknown inventory pool"
                 :es "Grupo de inventario desconocido" :ca "Grup d'inventari desconegut"
                 :fr-CH "Pool d'inventaire inconnu"}}}

  ; --- PER FEATURE TRANSLATIONS ---

    :catalog {:title {:en-GB "Catalog" :es "Catálogo" :ca "Catàleg"
                      :de-CH "Katalog"
                      :fr-CH "Catalogue"}
              :categories {:en-GB "Categories" :es "Categorías" :ca "Categories"
                           :de-CH "Kategorien"
                           :fr-CH "Catégories"}
              :no-reservable-items {:en-GB "No reservable items found" :es "No se encontraron artículos reservables" :ca "No s'han trobat articles reservables"
                                    :de-CH "Keine reservierbaren Gegenstände gefunden"
                                    :fr-CH "Aucun article réservable trouvé"}
              :check-available-pools {:en-GB "Check available inventory pools" :es "Ver los grupos de inventario disponibles" :ca "Veure els grups d'inventari disponibles"
                                      :de-CH "Verfügbare Inventarparks prüfen"
                                      :fr-CH "Vérifier les pools d'inventaire disponibles"}
              :templates {:en-GB "Templates" :es "Plantillas" :ca "Plantilles"
                          :de-CH "Vorlagen"
                          :fr-CH "Modèles"}}
    :categories {:sub-categories {:en-GB "Sub-categories" :es "Subcategorías" :ca "Subcategories"
                                  :de-CH "Unterkategorien"
                                  :fr-CH "Sous-catégories"}
                 :items {:en-GB "Items" :es "Artículos" :ca "Articles"
                         :de-CH "Gegenstände"
                         :fr-CH "Articles"}
                 :category-root {:en-GB "All Categories" :es "Todas las categorías" :ca "Totes les categories"
                                 :de-CH "Alle Kategorien"
                                 :fr-CH "Toutes les catégories"}}
    :current-user {:title {:en-GB "User Account" :es "Cuenta de usuario" :ca "Compte d'usuari"
                           :de-CH "Benutzerkonto"
                           :fr-CH "Compte utilisateur"}
                   :user-data {:en-GB "User data" :es "Datos de usuario" :ca "Dades d'usuari"
                               :de-CH "Nutzerdaten"
                               :fr-CH "Données utilisateur"}
                   :no-contracts {:en-GB "None" :es "Ninguno" :ca "Cap"
                                  :de-CH "Noch keine"
                                  :fr-CH "Aucun"}
                   :fields {:email {:en-GB "E-mail" :es "Correo electrónico" :ca "Correu electrònic"
                                    :de-CH "E-Mail"
                                    :fr-CH "E-mail"}
                            :secondary-email {:en-GB "Secondary e-mail" :es "Correo electrónico secundario" :ca "Correu electrònic secundari"
                                              :de-CH "Zweite E-Mail"
                                              :fr-CH "Deuxième e-mail"}
                            :phone {:en-GB "Phone number" :es "Teléfono" :ca "Telèfon"
                                    :de-CH "Telefon"
                                    :fr-CH "Numéro de téléphone"}
                            :org {:en-GB "Organisation" :es "Organización" :ca "Organització"
                                  :de-CH "Organisation"
                                  :fr-CH "Organisation"}
                            :org-id {:en-GB "ID" :es "ID" :ca "ID"
                                     :de-CH "ID"
                                     :fr-CH "ID"}
                            :badge-id {:en-GB "Badge ID" :es "Número de credencial" :ca "Número de credencial"
                                       :de-CH "Badge-ID"
                                       :fr-CH "Identifiant du badge"}}}

    :debug-page {:title {:en-GB "Debugging" :es "Depuración" :ca "Depuració"
                         :de-CH "Debugging"
                         :fr-CH "Débogage"}}

    :errors {:error {:en-GB "Error" :es "Error" :ca "Error"
                     :de-CH "Fehler"
                     :fr-CH "Erreur"}
             :processing-error {:en-GB "Error when processing this action" :es "Error al procesar esta acción" :ca "Error en processar aquesta acció"
                                :de-CH "Fehler beim Ausführen dieser Aktion"
                                :fr-CH "Erreur lors du traitement de cette action"}
             :render-error {:en-GB "Error displaying this content" :es "Error al mostrar este contenido" :ca "Error en mostrar aquest contingut"
                            :de-CH "Fehler bei der Anzeige"
                            :fr-CH "Erreur lors de l'affichage de ce contenu"}
             :loading-error {:en-GB "Error loading this content" :es "Error al cargar este contenido" :ca "Error en carregar aquest contingut"
                             :de-CH "Fehler beim Laden"
                             :fr-CH "Erreur lors du chargement de ce contenu"}
             ; 401
             :unauthorized {:en-GB "User not logged in" :es "Usuario sin sesión iniciada" :ca "Usuari sense sessió iniciada"
                            :de-CH "Benutzer ist nicht angemeldet"
                            :fr-CH "Utilisateur non connecté"}
             ; 403
             :forbidden {:en-GB "This Resource is not available for the current profile" :es "Este recurso no está disponible para el perfil actual" :ca "Aquest recurs no està disponible per al perfil actual"
                         :de-CH "Diese Ressource ist für das aktuelle Profil nicht verfügbar"
                         :fr-CH "Cette ressource n'est pas disponible pour le profil actuel"}
             :reload {:en-GB "Reload current page" :es "Recargar la página" :ca "Tornar a carregar la pàgina"
                      :de-CH "Seite neu laden"
                      :fr-CH "Recharger la page"}
             :go-to-start {:en-GB "Go to start page" :es "Ir a la página de inicio" :ca "Anar a la pàgina d'inici"
                           :de-CH "Zur Startseite"
                           :fr-CH "Aller à la page d'accueil"}
             :go-to-login {:en-GB "Go to login" :es "Ir al inicio de sesión" :ca "Anar a l'inici de sessió"
                           :de-CH "Zum Login"
                           :fr-CH "Aller à la connexion"}}

    :favorite-models {:title {:en-GB "Favorites" :es "Favoritos" :ca "Preferits"
                              :de-CH "Favoriten"
                              :fr-CH "Favoris"}
                      :items {:en-GB "Items" :es "Artículos" :ca "Articles"
                              :de-CH "Gegenstände"
                              :fr-CH "Éléments"}
                      :no-favorites {:en-GB "No favorites added yet" :es "Todavía no se agregaron favoritos" :ca "Encara no s'han afegit preferits"
                                     :de-CH "Noch keine Favoriten hinzugefügt"
                                     :fr-CH "Aucun favori ajouté pour le moment"}
                      :go-to-catalog {:en-GB "Go to catalog" :es "Ir al catálogo" :ca "Anar al catàleg"
                                      :de-CH "Hier geht's zum Katalog"
                                      :fr-CH "Aller au catalogue"}}
    :filter {:search-input-placeholder {:de-CH "Suchbegriff"
                                        :en-GB "Search term"
                                        :es "Término de búsqueda" :ca "Terme de cerca"
                                        :fr-CH "Terme de recherche"}
             :search-button-label {:de-CH "Suchen"
                                   :en-GB "Search"
                                   :es "Buscar" :ca "Cercar"
                                   :fr-CH "Rechercher"}
             :clear-input {:de-CH "Eingabe löschen" :en-GB "Clear input" :es "Borrar el texto" :ca "Esborrar el text" :fr-CH "Effacer la saisie"}
             :clear-filter {:de-CH "Filter entfernen" :en-GB "Clear filter" :es "Quitar el filtro" :ca "Treure el filtre" :fr-CH "Supprimer le filtre"}

             :filter {:en-GB "Filter" :es "Filtro" :ca "Filtre"
                      :de-CH "Filter"
                      :fr-CH "Filtrer"}

             :pool-select-label {:en-GB "Inventory pools" :es "Grupos de inventario" :ca "Grups d'inventari" :de-CH "Inventarparks" :fr-CH "Pools d'inventaire"}
             :all-pools-option-label {:en-GB "All inventory pools" :es "Todos los grupos de inventario" :ca "Tots els grups d'inventari" :de-CH "Alle Inventarparks" :fr-CH "Tous les pools d'inventaire"}
             :invalid-pool-option-label {:en-GB "Invalid selection" :es "Selección no válida" :ca "Selecció no vàlida" :de-CH "Ungültige Auswahl" :fr-CH "Sélection invalide"}
             :invalid-pool-message {:en-GB "The pre-selected inventory pool is not available for the current profile" :es "El grupo de inventario preseleccionado no está disponible para el perfil actual" :ca "El grup d'inventari preseleccionat no està disponible per al perfil actual"
                                    :de-CH "Der vorher gewählte Inventarpark ist für das aktuelle Profil nicht verfügbar"
                                    :fr-CH "Le pool d'inventaire pré-sélectionné n'est pas disponible pour le profil actuel"}
             :pool-suspended-message {:de-CH "Benutzer für diesen Inventarpark gesperrt"
                                      :en-GB "User suspended for this inventory pool"
                                      :es "Usuario suspendido en este grupo de inventario" :ca "Usuari suspès en aquest grup d'inventari"
                                      :fr-CH "Utilisateur suspendu pour ce pool d'inventaire"}

             :availability-button-label {:en-GB "Availability" :es "Disponibilidad" :ca "Disponibilitat"
                                         :de-CH "Verfügbarkeit"
                                         :fr-CH "Disponibilité"}
             :availability-unrestricted {:en-GB "Availability from/until" :es "Disponibilidad desde/hasta" :ca "Disponibilitat des de/fins a"
                                         :de-CH "Verfügbarkeit von/bis"
                                         :fr-CH "Disponibilité de/à"}
             :availability-label {:de-CH "{quantity} Stück verfügbar {startDate, date, narrow} – {endDate, date, narrow}",
                                  :en-GB "{quantity} {quantity, plural, =1 {item} other {items}} available {startDate, date, narrow} – {endDate, date, narrow}"
                                  :es "{quantity} {quantity, plural, =1 {artículo disponible} other {artículos disponibles}} {startDate, date, narrow} – {endDate, date, narrow}" :ca "{quantity} {quantity, plural, =1 {article disponible} other {articles disponibles}} {startDate, date, narrow} – {endDate, date, narrow}"
                                  :fr-CH "{quantity} {quantity, plural, =1 {pièce disponible} other {pièces disponibles}} du {startDate, date, narrow} au {endDate, date, narrow}"}

             :availability-modal {:title {:en-GB "Filter Availability" :es "Filtrar disponibilidad" :ca "Filtrar disponibilitat"
                                          :de-CH "Filter Verfügbarkeit"
                                          :fr-CH "Filtrer la disponibilité"}
                                  :timespan {:title {:en-GB "Timespan" :es "Período" :ca "Període"
                                                     :de-CH "Zeitraum"
                                                     :fr-CH "Période"}
                                             :undefined {:en-GB "Not set" :es "Sin definir" :ca "Sense definir"
                                                         :de-CH "Unbestimmt"
                                                         :fr-CH "Indéfini"}
                                             :errors {:start-date-and-end-date-set {:en-GB "Start and end date must be set." :es "Se deben indicar la fecha de inicio y la de fin." :ca "Cal indicar la data d'inici i la de fi."
                                                                                    :de-CH "Start- und Enddatum müssen gesetzt sein."
                                                                                    :fr-CH "Les dates de début et de fin doivent être définies."}
                                                      :start-date-equal-or-before-end-date {:en-GB "Start date must be equal to or before end date." :es "La fecha de inicio debe ser igual o anterior a la fecha de fin." :ca "La data d'inici ha de ser igual o anterior a la data de fi."
                                                                                            :de-CH "Startdatum muss entweder gleich oder vor dem Enddatum sein."
                                                                                            :fr-CH "La date de début doit être antérieure ou égale à la date de fin."}}}
                                  :from {:en-GB "From" :es "Desde" :ca "Des de"
                                         :de-CH "Von"
                                         :fr-CH "De"}
                                  :until {:en-GB "Until" :es "Hasta" :ca "Fins a"
                                          :de-CH "Bis"
                                          :fr-CH "À"}
                                  :quantity {:en-GB "Quantity" :es "Cantidad" :ca "Quantitat"
                                             :de-CH "Anzahl"
                                             :fr-CH "Quantité"}
                                  :cancel {:en-GB "Cancel" :es "Cancelar" :ca "Cancel·lar" :de-CH "Abbrechen" :fr-CH "Annuler"}
                                  :apply {:en-GB "Apply" :es "Aplicar" :ca "Aplicar" :de-CH "Anwenden" :fr-CH "Appliquer"}}}

    :home-page {} ; see catalog
    :logout {:en-GB "Logout" :es "Cerrar sesión" :ca "Tancar sessió"
             :de-CH "Abmelden"
             :fr-CH "Déconnexion"}
    :menu {:borrow {:section-title {:en-GB "Borrow" :es "Préstamos" :ca "Préstecs"
                                    :de-CH "Ausleihen"
                                    :fr-CH "Emprunter"}
                    :catalog {:en-GB "Catalog" :es "Catálogo" :ca "Catàleg"
                              :de-CH "Katalog"
                              :fr-CH "Catalogue"}
                    :shopping-cart {:en-GB "Cart" :es "Carrito" :ca "Cistella"
                                    :de-CH "Warenkorb"
                                    :fr-CH "Panier"}
                    :pools {:en-GB "Inventory Pools" :es "Grupos de inventario" :ca "Grups d'inventari"
                            :de-CH "Inventarparks"
                            :fr-CH "Pools d'inventaire"}
                    :favorite-models {:en-GB "Favorites" :es "Favoritos" :ca "Preferits"
                                      :de-CH "Favoriten"
                                      :fr-CH "Favoris"}}
           :cart-item {:menu-title {:en-GB "Cart" :es "Carrito" :ca "Cistella"
                                    :de-CH "Warenkob"
                                    :fr-CH "Panier"}}
           :user {:section-title {:en-GB "User" :es "Usuario" :ca "Usuari"
                                  :de-CH "Benutzer"
                                  :fr-CH "Utilisateur"}
                  :menu-title {:en-GB "User Menu" :es "Menú de usuario" :ca "Menú d'usuari"
                               :de-CH "Benutzermenu"
                               :fr-CH "Menu utilisateur"}
                  :rentals {:en-GB "Orders" :es "Pedidos" :ca "Comandes"
                            :de-CH "Bestellungen"
                            :fr-CH "Commandes"}
                  :current-user {:en-GB "User Account" :es "Cuenta de usuario" :ca "Compte d'usuari"
                                 :de-CH "Benutzerkonto"
                                 :fr-CH "Compte utilisateur"}
                  :logout {:en-GB "Logout" :es "Cerrar sesión" :ca "Tancar sessió"
                           :de-CH "Abmelden"
                           :fr-CH "Déconnexion"}}
           :app-switch {:button-label {:en-GB "Borrow" :es "Préstamos" :ca "Préstecs"
                                       :de-CH "Ausleihen"
                                       :fr-CH "Emprunter"}
                        :menu-title {:en-GB "Section Menu" :es "Menú de secciones" :ca "Menú de seccions"
                                     :de-CH "Bereichsmenu"
                                     :fr-CH "Menu de section"}
                        :section-title {:en-GB "Switch Section" :es "Cambiar de sección" :ca "Canviar de secció"
                                        :de-CH "Bereich wechseln"
                                        :fr-CH "Changer de section"}
                        :admin {:en-GB "Admin" :es "Administración" :ca "Administració"
                                :de-CH "Admin"
                                :fr-CH "Admin"}
                        :procure {:en-GB "Procurement" :es "Compras" :ca "Compres"
                                  :de-CH "Bedarfsermittlung"
                                  :fr-CH "Approvisionnement"}
                        :manage {:en-GB "Lending / Inventory" :es "Préstamo / Inventario" :ca "Préstec / Inventari"
                                 :de-CH "Verleih / Inventar"
                                 :fr-CH "Prêt / Inventaire"}}
           :documentation {:en-GB "Help" :es "Ayuda" :ca "Ajuda"
                           :de-CH "Hilfe"
                           :fr-CH "Aide"}
           :language {:section-title {:en-GB "Language" :es "Idioma" :ca "Idioma"
                                      :de-CH "Sprache"
                                      :fr-CH "Langue"}}}
    :model-show {:loading {:en-GB "Loading item" :es "Cargando artículo" :ca "Carregant l'article"
                           :de-CH "Gegenstand wird geladen"
                           :fr-CH "Chargement de l'élément"}
                 :description {:en-GB "Description" :es "Descripción" :ca "Descripció"
                               :de-CH "Beschreibung"
                               :fr-CH "Description"}
                 :properties {:en-GB "Properties" :es "Propiedades" :ca "Propietats"
                              :de-CH "Eigenschaften"
                              :fr-CH "Propriétés"}
                 :documents {:en-GB "Documents" :es "Documentos" :ca "Documents"
                             :de-CH "Dokumente"
                             :fr-CH "Documents"}
                 :compatibles {:en-GB "Compatible Items" :es "Artículos compatibles" :ca "Articles compatibles"
                               :de-CH "Ergänzende Gegenstände"
                               :fr-CH "Éléments compatibles"}
                 :add-item-to-cart {:en-GB "Add item" :es "Agregar artículo" :ca "Afegir article"
                                    :de-CH "Gegenstand hinzufügen"
                                    :fr-CH "Ajouter l'élément"}
                 :add-to-favorites {:en-GB "Add to favorites" :es "Agregar a favoritos" :ca "Afegir a preferits"
                                    :de-CH "Zu Favoriten hinzufügen"
                                    :fr-CH "Ajouter aux favoris"}
                 :remove-from-favorites {:en-GB "Remove from favorites" :es "Quitar de favoritos" :ca "Treure de preferits"
                                         :de-CH "Von Favoriten entfernen"
                                         :fr-CH "Retirer des favoris"}
                 :previous-image {:en-GB "Previous image" :es "Imagen anterior" :ca "Imatge anterior"
                                  :de-CH "Vorheriges Bild"
                                  :fr-CH "Image précédente"}
                 :next-image {:en-GB "Next image" :es "Imagen siguiente" :ca "Imatge següent"
                              :de-CH "Nächstes Bild"
                              :fr-CH "Image suivante"}
                 :not-available-for-current-profile {:en-GB "Item not available for current profile" :es "Artículo no disponible para el perfil actual" :ca "Article no disponible per al perfil actual"
                                                     :de-CH "Gegenstand für das aktuelle Profil nicht verfügbar"
                                                     :fr-CH "Élément non disponible pour le profil actuel"}
                 :order-dialog {:title {:en-GB "Add item" :es "Agregar artículo" :ca "Afegir article" :de-CH "Gegenstand hinzufügen" :fr-CH "Ajouter l'élément"}
                                :cancel {:en-GB "Cancel" :es "Cancelar" :ca "Cancel·lar" :de-CH "Abbrechen" :fr-CH "Annuler"}
                                :add {:en-GB "Add" :es "Agregar" :ca "Afegir" :de-CH "Hinzufügen" :fr-CH "Ajouter"}}
                 :order-success-notification {:title {:en-GB "Item added" :es "Artículo agregado" :ca "Article afegit" :de-CH "Gegenstand hinzugefügt" :fr-CH "Élément ajouté"}
                                              :item-was-added {:en-GB "The item was added to the cart" :es "El artículo se agregó al carrito" :ca "L'article s'ha afegit a la cistella" :de-CH "Der Gegenstand wurde zum Warenkorb hinzugefügt" :fr-CH "L'élément a été ajouté au panier"}}}
    :models {:title {:en-GB "Search results" :es "Resultados de la búsqueda" :ca "Resultats de la cerca" :de-CH "Suchresultate" :fr-CH "Résultats de recherche"}
             :no-items-found {:en-GB "No items found" :es "No se encontraron artículos" :ca "No s'han trobat articles"
                              :de-CH "Keine Gegenstände gefunden"
                              :fr-CH "Aucun élément trouvé"}}
    :pagination {:load-more {:en-GB "Load more" :es "Cargar más" :ca "Carregar-ne més"
                             :de-CH "Mehr laden"
                             :fr-CH "Charger plus"}}
    :pools {:title {:en-GB "Inventory Pools" :es "Grupos de inventario" :ca "Grups d'inventari"
                    :de-CH "Inventarparks"
                    :fr-CH "Pools d'inventaire"}
            :available-pools {:en-GB "Available inventory pools" :es "Grupos de inventario disponibles" :ca "Grups d'inventari disponibles"
                              :de-CH "Verfügbare Inventarparks"
                              :fr-CH "Pools d'inventaire disponibles"}
            :no-available-pools {:en-GB "No inventory pool available" :es "Ningún grupo de inventario disponible" :ca "Cap grup d'inventari disponible"
                                 :de-CH "Kein Inventarpark verfügbar"
                                 :fr-CH "Aucun pool d'inventaire disponible"}
            :access-suspended {:en-GB "Access suspended" :es "Acceso suspendido" :ca "Accés suspès"
                               :de-CH "Zugang gesperrt"
                               :fr-CH "Accès suspendu"}
            :no-reservable-models {:en-GB "No reservable items" :es "Sin artículos reservables" :ca "Sense articles reservables"
                                   :de-CH "Keine reservierbaren Gegenstände"
                                   :fr-CH "Aucun élément réservable"}
            :maximum-reservation-duration {:en-GB "Maximum reservation duration {days} days" :es "Duración máxima de una reserva: {days} días" :ca "Durada màxima d'una reserva: {days} dies"
                                           :de-CH "Maximale Reservationsdauer von {days} Tagen"
                                           :fr-CH "Durée maximale de réservation : {days} jours"}}
    :pool-show {:email {:en-GB "E-mail" :es "Correo electrónico" :ca "Correu electrònic"
                        :de-CH "E-Mail"
                        :fr-CH "E-mail"}
                :description {:en-GB "Description" :es "Descripción" :ca "Descripció"
                              :de-CH "Beschreibung"
                              :fr-CH "Description"}
                :contact {:title {:en-GB "Contact" :es "Contacto" :ca "Contacte"
                                  :de-CH "Kontakt"
                                  :fr-CH "Contact"}}
                :opening-times {:title {:en-GB "Opening times" :es "Horario de atención" :ca "Horari d'atenció"
                                        :de-CH "Öffnungszeiten"
                                        :fr-CH "Heures d'ouverture"}}
                :reservation-constraint {:title {:en-GB "Reservation constraint" :es "Restricción de reserva" :ca "Restricció de reserva"
                                                 :de-CH "Reservationseinschränkung"
                                                 :fr-CH "Restriction de réservation"}}
                :holidays {:title {:en-GB "Holidays" :es "Feriados" :ca "Dies festius"
                                   :de-CH "Feiertage"
                                   :fr-CH "Jours fériés"}}
                :show-remaining-holidays {:more {:en-GB "Show remaining" :es "Mostrar el resto" :ca "Mostrar la resta"
                                                 :de-CH "Restliche Feiertage anzeigen"
                                                 :fr-CH "Afficher les jours restants"}
                                          :hide {:en-GB "Hide" :es "Ocultar" :ca "Amagar"
                                                 :de-CH "Verbergen"
                                                 :fr-CH "Masquer"}}
                :closed {:en-GB "Closed" :es "Cerrado" :ca "Tancat"
                         :de-CH "Geschlossen"
                         :fr-CH "Fermé"}}

    :profile-menu {:title {:en-GB "Switch Profile" :es "Cambiar de perfil" :ca "Canviar de perfil"
                           :de-CH "Profil wechseln"
                           :fr-CH "Changer de profil"}}

    :rentals {:title {:en-GB "Orders" :es "Pedidos" :ca "Comandes"
                      :de-CH "Bestellungen"
                      :fr-CH "Commandes"}
              :section-title-current-lendings {:en-GB "Current lendings" :es "Préstamos actuales" :ca "Préstecs actuals"
                                               :de-CH "Aktuelle Ausleihen"
                                               :fr-CH "Prêts en cours"}
              :section-title-open-rentals {:en-GB "Active orders" :es "Pedidos activos" :ca "Comandes actives"
                                           :de-CH "Aktive Bestellungen"
                                           :fr-CH "Commandes actives"}
              :section-title-closed-rentals {:en-GB "Closed orders" :es "Pedidos cerrados" :ca "Comandes tancades"
                                             :de-CH "Abgeschlossene Bestellungen"
                                             :fr-CH "Commandes clôturées"}
              :no-matches-found {:en-GB "No results found for the current search filter" :es "No hay resultados para el filtro de búsqueda actual" :ca "No hi ha resultats per al filtre de cerca actual"
                                 :de-CH "Keine Treffer zum aktuellen Suchfilter gefunden"
                                 :fr-CH "Aucun résultat pour le filtre de recherche actuel"}
              :no-current-lendings-yet {:en-GB "No current lendings" :es "No hay préstamos actuales" :ca "No hi ha préstecs actuals"
                                        :de-CH "Keine aktuellen Ausleihen vorhanden"
                                        :fr-CH "Aucun prêt en cours"}
              :no-active-orders-yet {:en-GB "No active orders" :es "No hay pedidos activos" :ca "No hi ha comandes actives"
                                     :de-CH "Keine aktiven Bestellungen vorhanden"
                                     :fr-CH "Aucune commande active"}
              :no-closed-orders-yet {:en-GB "No closed orders yet" :es "Todavía no hay pedidos cerrados" :ca "Encara no hi ha comandes tancades"
                                     :de-CH "Noch keine abgeschlossene Bestellungen vorhanden"
                                     :fr-CH "Aucune commande clôturée pour l'instant"}
              :info-see-also-delegations {:en-GB "To view orders from a delegation, select the corresponding profile in the user menu" :es "Para ver los pedidos de una delegación, seleccione el perfil correspondiente en el menú de usuario" :ca "Per veure les comandes d'una delegació, selecciona el perfil corresponent al menú d'usuari"
                                          :de-CH "Zum Anzeigen von Bestellungen einer Delegation das entsprechende Profil im Benutzermenü auswählen"
                                          :fr-CH "Pour voir les commandes d'une délégation, sélectionnez le profil correspondant dans le menu utilisateur"}
              :filter  {:pools {:title {:en-GB "Inventory pools" :es "Grupos de inventario" :ca "Grups d'inventari" :de-CH "Inventarparks" :fr-CH "Pools d'inventaire"}
                                :all {:en-GB "All inventory pools" :es "Todos los grupos de inventario" :ca "Tots els grups d'inventari" :de-CH "Alle Inventarparks" :fr-CH "Tous les pools d'inventaire"}
                                :invalid-option {:en-GB "Invalid selection" :es "Selección no válida" :ca "Selecció no vàlida" :de-CH "Ungültige Auswahl" :fr-CH "Sélection invalide"}
                                :invalid-option-info {:en-GB "The pre-selected inventory pool is not available for the current profile" :es "El grupo de inventario preseleccionado no está disponible para el perfil actual" :ca "El grup d'inventari preseleccionat no està disponible per al perfil actual"
                                                      :de-CH "Der vorher gewählte Inventarpark ist für das aktuelle Profil nicht verfügbar"
                                                      :fr-CH "Le pool d'inventaire pré-sélectionné n'est pas disponible pour le profil actuel"}}

                        :timespan-modal {:title {:en-GB "Timespan" :es "Período" :ca "Període" :de-CH "Zeitraum" :fr-CH "Période"}
                                         :from {:en-GB "From" :es "Desde" :ca "Des de" :de-CH "Von" :fr-CH "De"}
                                         :until {:en-GB "Until" :es "Hasta" :ca "Fins a" :de-CH "Bis" :fr-CH "À"}
                                         :undefined {:en-GB "Not set" :es "Sin definir" :ca "Sense definir" :de-CH "unbestimmt" :fr-CH "indéfini"}
                                         :errors {:start-date-equal-or-before-end-date
                                                  {:en-GB "Start date must be equal to or before end date." :es "La fecha de inicio debe ser igual o anterior a la fecha de fin." :ca "La data d'inici ha de ser igual o anterior a la data de fi."
                                                   :de-CH "Startdatum muss entweder gleich oder vor dem Enddatum sein."
                                                   :fr-CH "La date de début doit être antérieure ou égale à la date de fin."}}
                                         :cancel {:en-GB "Cancel" :es "Cancelar" :ca "Cancel·lar" :de-CH "Abbrechen" :fr-CH "Annuler"}
                                         :apply {:en-GB "Apply" :es "Aplicar" :ca "Aplicar" :de-CH "Anwenden" :fr-CH "Appliquer"}}

                        ;; Note: the JS text resolver does not support nesting
                        :js-component {:search-button-label {:en-GB "Search" :es "Buscar" :ca "Cercar" :de-CH "Suchen" :fr-CH "Rechercher"}
                                       :clear-input {:en-GB "Clear input" :es "Borrar el texto" :ca "Esborrar el text" :de-CH "Eingabe löschen" :fr-CH "Effacer la saisie"}
                                       :clear-filter {:en-GB "Clear filter" :es "Quitar el filtro" :ca "Treure el filtre" :de-CH "Filter entfernen" :fr-CH "Supprimer le filtre"}
                                       :search-input-placeholder {:en-GB "Search term" :es "Término de búsqueda" :ca "Terme de cerca" :de-CH "Suchbegriff" :fr-CH "Terme de recherche"}
                                       :filter {:en-GB "Filter" :es "Filtro" :ca "Filtre" :de-CH "Filter" :fr-CH "Filtrer"}
                                       :status-select-label {:en-GB "Status" :es "Estado" :ca "Estat" :de-CH "Status" :fr-CH "Statut"}
                                       :pool-select-label {:en-GB "Inventory pools" :es "Grupos de inventario" :ca "Grups d'inventari" :de-CH "Inventarparks" :fr-CH "Pools d'inventaire"}
                                       :timespan-button-label {:en-GB "Timespan from/until" :es "Período desde/hasta" :ca "Període des de/fins a" :de-CH "Zeitraum von/bis" :fr-CH "Période de/à"}
                                       :timespan-label {:en-GB "{startDate, date, narrow} – {endDate, date, narrow}" :es "{startDate, date, narrow} – {endDate, date, narrow}" :ca "{startDate, date, narrow} – {endDate, date, narrow}" :de-CH "{startDate, date, narrow} – {endDate, date, narrow}"  :fr-CH "{startDate, date, narrow} – {endDate, date, narrow}"}
                                       :timespan-label-from {:en-GB "{startDate, date, narrow} – " :es "{startDate, date, narrow} –" :ca "{startDate, date, narrow} –" :de-CH "{startDate, date, narrow} – "  :fr-CH "{startDate, date, narrow} – "}
                                       :timespan-label-until {:en-GB " – {endDate, date, narrow}" :es "– {endDate, date, narrow}" :ca "– {endDate, date, narrow}" :de-CH " – {endDate, date, narrow}"  :fr-CH " – {endDate, date, narrow}"}
                                       :timespan-unrestricted {:en-GB "Timespan from/until" :es "Período desde/hasta" :ca "Període des de/fins a" :de-CH "Zeitraum von/bis" :fr-CH "Période de/à"}}}

              :fulfillment-state-label {; states flowchart
                                        :IN_APPROVAL {:de-CH "Genehmigung" :en-GB "Approval" :es "Aprobación" :ca "Aprovació" :fr-CH "Approbation"}
                                        :TO_PICKUP {:de-CH "Abholung" :en-GB "Pickup" :es "Retiro" :ca "Recollida" :fr-CH "Retrait"}
                                        :TO_RETURN {:de-CH "Rückgabe" :en-GB "Return" :es "Devolución" :ca "Retorn" :fr-CH "Retour"}
                                        :RETURNED {:de-CH "Alle Gegenstände zurückgebracht" :en-GB "All items returned" :es "Todos los artículos devueltos" :ca "Tots els articles retornats" :fr-CH "Tous les éléments ont été retournés"}
                                        :REJECTED {:de-CH "Bestellung wurde abgelehnt" :en-GB "Order was rejected" :es "El pedido fue rechazado" :ca "La comanda s'ha rebutjat" :fr-CH "La commande a été refusée"}
                                        :EXPIRED-UNAPPROVED {:de-CH "Abgelaufen (nicht genehmigt)" :en-GB "Expired (not approved)" :es "Vencido (no aprobado)" :ca "Caducada (no aprovada)" :fr-CH "Expiré (non approuvé)"}
                                        :CANCELED {:de-CH "Bestellung wurde storniert" :en-GB "Order was canceled" :es "El pedido fue cancelado" :ca "La comanda s'ha cancel·lat" :fr-CH "La commande a été annulée"}
                                        :EXPIRED {:de-CH "Abgelaufen (nicht abgeholt)" :en-GB "Expired (not picked up)" :es "Vencido (no retirado)" :ca "Caducada (no recollida)" :fr-CH "Expiré (non retiré)"}
                                        :OVERDUE {:de-CH "Rückgabe überfällig" :en-GB "Overdue" :es "Atrasado" :ca "Endarrerida" :fr-CH "Retour en retard"}}

              :x-items {:en-GB "{itemCount, plural, =1 {{itemCount} item} other {{itemCount} items}}" :es "{itemCount, plural, =1 {{itemCount} artículo} other {{itemCount} artículos}}" :ca "{itemCount, plural, =1 {{itemCount} article} other {{itemCount} articles}}"
                        :de-CH "{itemCount, plural, =1 {{itemCount} Gegenstand} other {{itemCount} Gegenstände}}"
                        :fr-CH "{itemCount, plural,
                                  =1 {{itemCount} élément}
                                  other {{itemCount} éléments}}"}

              :summary-line
              {:open {:de-CH "{totalDays, plural,
                                =1 {{itemCount, plural,
                                  =1 {Am {fromDate, date, short}, {itemCount} Gegenstand}
                                  other {Am {fromDate, date, short}, {itemCount} Gegenstände}}}
                                other {{itemCount, plural,
                                  =1 {Zwischen {fromDate, date, short} und {untilDate, date, short}, {itemCount} Gegenstand}
                                  other {Zwischen {fromDate, date, short} und {untilDate, date, short}, {itemCount} Gegenstände}}}}"
                      :en-GB "{totalDays, plural,
                                =1 {{itemCount, plural,
                                  =1 {On {fromDate, date, short}, {itemCount} item}
                                  other {On {fromDate, date, short}, {itemCount} items}}}
                                other {{itemCount, plural,
                                  =1 {Between {fromDate, date, short} and {untilDate, date, short}, {itemCount} item}
                                  other {Between {fromDate, date, short} and {untilDate, date, short}, {itemCount} items}}}}"
                      :es "{totalDays, plural, =1 {{itemCount, plural, =1 {El {fromDate, date, short}, {itemCount} artículo} other {El {fromDate, date, short}, {itemCount} artículos}}} other {{itemCount, plural, =1 {Entre el {fromDate, date, short} y el {untilDate, date, short}, {itemCount} artículo} other {Entre el {fromDate, date, short} y el {untilDate, date, short}, {itemCount} artículos}}}}" :ca "{totalDays, plural, =1 {{itemCount, plural, =1 {El {fromDate, date, short}, {itemCount} article} other {El {fromDate, date, short}, {itemCount} articles}}} other {{itemCount, plural, =1 {Entre el {fromDate, date, short} i el {untilDate, date, short}, {itemCount} article} other {Entre el {fromDate, date, short} i el {untilDate, date, short}, {itemCount} articles}}}}"
                      :fr-CH "{totalDays, plural,
                                =1 {{itemCount, plural,
                                  =1 {Le {fromDate, date, short}, {itemCount} élément}
                                  other {Le {fromDate, date, short}, {itemCount} éléments}}}
                                other {{itemCount, plural,
                                  =1 {Entre le {fromDate, date, short} et le {untilDate, date, short}, {itemCount} élément}
                                  other {Entre le {fromDate, date, short} et le {untilDate, date, short}, {itemCount} éléments}}}}"}
               :closed {:de-CH "{totalDays, plural,
                                =1 {{itemCount, plural,
                                  =1 {Am {fromDate, date, short}, {itemCount} Gegenstand}
                                  other {Am {fromDate, date, short}, {itemCount} Gegenstände}}}
                                other {{itemCount, plural,
                                  =1 {Zwischen {fromDate, date, short} und {untilDate, date, short}, {itemCount} Gegenstand}
                                  other {Zwischen {fromDate, date, short} und {untilDate, date, short}, {itemCount} Gegenstände}}}}"
                        :en-GB "{totalDays, plural,
                                =1 {{itemCount, plural,
                                  =1 {On {fromDate, date, short}, {itemCount} item}
                                  other {On {fromDate, date, short}, {itemCount} items}}}
                                other {{itemCount, plural,
                                  =1 {Between {fromDate, date, short} and {untilDate, date, short}, {itemCount} item}
                                  other {Between {fromDate, date, short} and {untilDate, date, short}, {itemCount} items}}}}"
                        :es "{totalDays, plural, =1 {{itemCount, plural, =1 {El {fromDate, date, short}, {itemCount} artículo} other {El {fromDate, date, short}, {itemCount} artículos}}} other {{itemCount, plural, =1 {Entre el {fromDate, date, short} y el {untilDate, date, short}, {itemCount} artículo} other {Entre el {fromDate, date, short} y el {untilDate, date, short}, {itemCount} artículos}}}}" :ca "{totalDays, plural, =1 {{itemCount, plural, =1 {El {fromDate, date, short}, {itemCount} article} other {El {fromDate, date, short}, {itemCount} articles}}} other {{itemCount, plural, =1 {Entre el {fromDate, date, short} i el {untilDate, date, short}, {itemCount} article} other {Entre el {fromDate, date, short} i el {untilDate, date, short}, {itemCount} articles}}}}"
                        :fr-CH "{totalDays, plural,
                                =1 {{itemCount, plural,
                                  =1 {Le {fromDate, date, short}, {itemCount} élément}
                                  other {Le {fromDate, date, short}, {itemCount} éléments}}}
                                other {{itemCount, plural,
                                  =1 {Entre le {fromDate, date, short} et le {untilDate, date, short}, {itemCount} élément}
                                  other {Entre le {fromDate, date, short} et le {untilDate, date, short}, {itemCount} éléments}}}}"}}
               ;

              :fulfillment-state
              {:summary-line
               {:IN_APPROVAL
                {:de-CH "{totalCount, plural,
                          =1 {{doneCount} von {totalCount} Gegenstand genehmigt}
                          other {{doneCount} von {totalCount} Gegenständen genehmigt}
                        }"
                 :en-GB "{totalCount, plural,
                          =1 {{doneCount} of {totalCount} item approved}
                          other {{doneCount} of {totalCount} items approved}
                        }"
                 :es "{totalCount, plural, =1 {{doneCount} de {totalCount} artículo aprobado} other {{doneCount} de {totalCount} artículos aprobados} }" :ca "{totalCount, plural, =1 {{doneCount} de {totalCount} article aprovat} other {{doneCount} de {totalCount} articles aprovats} }"
                 :fr-CH "{totalCount, plural,
                          =1 {{doneCount} sur {totalCount} élément approuvé}
                          other {{doneCount} sur {totalCount} éléments approuvés}}"}
                :TO_PICKUP
                {:de-CH "{totalCount, plural,
                          =1 {{doneCount} von {totalCount} Gegenstand abgeholt}
                          other {{doneCount} von {totalCount} Gegenständen abgeholt}
                        }"
                 :en-GB "{totalCount, plural,
                          =1 {{doneCount} of {totalCount} item picked up}
                          other {{doneCount} of {totalCount} items picked up}
                        }"
                 :es "{totalCount, plural, =1 {{doneCount} de {totalCount} artículo retirado} other {{doneCount} de {totalCount} artículos retirados} }" :ca "{totalCount, plural, =1 {{doneCount} de {totalCount} article recollit} other {{doneCount} de {totalCount} articles recollits} }"
                 :fr-CH "{totalCount, plural,
                          =1 {{doneCount} sur {totalCount} élément retiré}
                          other {{doneCount} sur {totalCount} éléments retirés}}"}
                :TO_RETURN
                {:de-CH "{totalCount, plural,
                          =1 {{doneCount} von {totalCount} Gegenstand zurückgebracht}
                          other {{doneCount} von {totalCount} Gegenständen zurückgebracht}
                        }"
                 :en-GB "{totalCount, plural,
                          =1 {{doneCount} of {totalCount} item returned}
                          other {{doneCount} of {totalCount} items returned}
                        }"
                 :es "{totalCount, plural, =1 {{doneCount} de {totalCount} artículo devuelto} other {{doneCount} de {totalCount} artículos devueltos} }" :ca "{totalCount, plural, =1 {{doneCount} de {totalCount} article retornat} other {{doneCount} de {totalCount} articles retornats} }"
                 :fr-CH "{totalCount, plural,
                          =1 {{doneCount} sur {totalCount} élément retourné}
                          other {{doneCount} sur {totalCount} éléments retournés}}"}}

               :partial-status {:REJECTED {:de-CH " ({count} abgelehnt)" :en-GB " ({count} rejected)" :es "({count} rechazados)" :ca "({count} rebutjats)" :fr-CH " ({count} refusé)"}
                                :EXPIRED {:de-CH " ({count} abgelaufen)" :en-GB " ({count} expired)" :es "({count} vencidos)" :ca "({count} caducats)" :fr-CH " ({count} expiré)"}
                                :OVERDUE {:de-CH " ({count} überfällig)" :en-GB " ({count} overdue)" :es "({count} atrasados)" :ca "({count} endarrerits)" :fr-CH " ({count} en retard)"}}}}

    :rental-show {:page-title {:de-CH "Bestellung" :en-GB "Order" :es "Pedido" :ca "Comanda" :fr-CH "Commande"}
                  :message-403 {:de-CH "Diese Bestellung ist für das aktuelle Profil nicht sichtbar"
                                :en-GB "This order is not visible for the current profile"
                                :es "Este pedido no es visible para el perfil actual" :ca "Aquesta comanda no és visible per al perfil actual"
                                :fr-CH "Cette commande n'est pas visible pour le profil actuel"}
                  :state {:de-CH "Status" :en-GB "State" :es "Estado" :ca "Estat" :fr-CH "Statut"}
                  :cancel-action-label {:de-CH "Bestellung stornieren" :en-GB "Cancel order" :es "Cancelar pedido" :ca "Cancel·lar comanda" :fr-CH "Annuler la commande"}
                  :purpose {:de-CH "Zweck" :en-GB "Purpose" :es "Motivo" :ca "Motiu" :fr-CH "But"}
                  :contact-details {:de-CH "Kontaktdaten" :en-GB "Contact details" :es "Datos de contacto" :ca "Dades de contacte" :fr-CH "Coordonnées"}
                  :pools-section-title {:de-CH "Inventarparks" :en-GB "Inventory pools" :es "Grupos de inventario" :ca "Grups d'inventari" :fr-CH "Pools d'inventaire"}
                  :items-section-title {:de-CH "Gegenstände" :en-GB "Items" :es "Artículos" :ca "Articles" :fr-CH "Éléments"}
                  :documents-section-title {:de-CH "Dokumente" :en-GB "Documents" :es "Documentos" :ca "Documents" :fr-CH "Documents"}
                  :user-or-delegation-section-title {:de-CH "Bestellung für" :en-GB "Order for" :es "Pedido para" :ca "Comanda per a" :fr-CH "Commande pour"}

                  :reservation-line
                  {:title {:de-CH "{itemCount}× {itemName}" :en-GB "{itemCount}× {itemName}" :es "{itemCount}× {itemName}" :ca "{itemCount}× {itemName}" :fr-CH "{itemCount}× {itemName}"}
                   :duration-days {:de-CH "{totalDays, plural,
                                             =1 {# Tag}
                                             other {# Tage}
                                           }"
                                   :en-GB "{totalDays, plural,
                                             =1 {# day}
                                             other {# days}
                                           }"
                                   :es "{totalDays, plural, =1 {# día} other {# días} }" :ca "{totalDays, plural, =1 {# dia} other {# dies} }"
                                   :fr-CH "{totalDays, plural,
                                             =1 {# jour}
                                             other {# jours}}"}
                   :overdue {:de-CH "überfällig" :en-GB "overdue" :es "atrasado" :ca "endarrerit" :fr-CH "en retard"}
                   :option {:de-CH "Option" :en-GB "Option" :es "Opción" :ca "Opció" :fr-CH "Option"}}

                  :reservation-status-label {:DRAFT {:de-CH "DRAFT" :en-GB "DRAFT" :es "BORRADOR" :ca "ESBORRANY" :fr-CH "DRAFT"} ; (will never appear in this view)
                                             :UNSUBMITTED {:de-CH "UNSUBMITTED" :en-GB "UNSUBMITTED" :es "SIN ENVIAR" :ca "SENSE ENVIAR" :fr-CH "UNSUBMITTED"} ; (will never appear in this view)
                                             :SUBMITTED {:de-CH "In Genehmigung" :en-GB "In approval" :es "En aprobación" :ca "En aprovació" :fr-CH "En cours d'approbation"}
                                             :APPROVED {:de-CH "Abholung" :en-GB "Pick up" :es "Retirar" :ca "Recollir" :fr-CH "Retrait"}
                                             :REJECTED {:de-CH "Abgelehnt" :en-GB "Rejected" :es "Rechazado" :ca "Rebutjat" :fr-CH "Refusé"}
                                             :SIGNED {:de-CH "Rückgabe" :en-GB "Return" :es "Devolver" :ca "Retornar" :fr-CH "Retour"}
                                             :CLOSED {:de-CH "Zurückgebracht" :en-GB "Returned" :es "Devuelto" :ca "Retornat" :fr-CH "Retourné"}
                                             :CANCELED {:de-CH "Storniert" :en-GB "Canceled" :es "Cancelado" :ca "Cancel·lat" :fr-CH "Annulé"}
                                             ; Temporal statusses:
                                             :EXPIRED-UNAPPROVED {:de-CH "Abgelaufen (nicht genehmigt)" :en-GB "Expired (not approved)" :es "Vencido (no aprobado)" :ca "Caducat (no aprovat)" :fr-CH "Expiré (non approuvé)"}
                                             :EXPIRED {:de-CH "Abgelaufen (nicht abgeholt)" :en-GB "Expired (not picked up)" :es "Vencido (no retirado)" :ca "Caducat (no recollit)" :fr-CH "Expiré (non retiré)"}}
                  :in-x-days {:de-CH "{days, plural,
                                      =0 {heute}
                                      =1 {morgen}
                                      other {in # Tagen}
                                      }"
                              :en-GB "{days, plural,
                                      =0 {today}
                                      =1 {tomorrow}
                                      other {in # days}
                                      }"
                              :es "{days, plural, =0 {hoy} =1 {mañana} other {en # días} }" :ca "{days, plural, =0 {avui} =1 {demà} other {d'aquí a # dies} }"
                              :fr-CH "{days, plural,
                                      =0 {aujourd'hui}
                                      =1 {demain}
                                      other {dans # jours}}"}
                  :cancellation-dialog {:title {:de-CH "Bestellung stornieren" :en-GB "Cancel order" :es "Cancelar pedido" :ca "Cancel·lar comanda" :fr-CH "Annuler la commande"}
                                        :confirm {:de-CH "Stornieren" :en-GB "Cancel order" :es "Cancelar pedido" :ca "Cancel·lar comanda" :fr-CH "Annuler la commande"}
                                        :cancel {:de-CH "Abbrechen" :en-GB "Abort" :es "Volver" :ca "Tornar" :fr-CH "Abandonner"}}
                  :repeat-order {:repeat-action-label {:de-CH "Bestellung wiederholen" :en-GB "Repeat order" :es "Repetir pedido" :ca "Repetir comanda" :fr-CH "Répéter la commande"}
                                 :dialog {:title {:de-CH "Gegenstände hinzufügen" :en-GB "Add items" :es "Agregar artículos" :ca "Afegir articles" :fr-CH "Ajouter des articles"}
                                          :info {:de-CH "{count, plural,
                                                         =1 {Ein Gegenstand wird zum Warenkorb hinzugefügt.}
                                                         other {# Gegenstände werden zum Warenkorb hinzugefügt.}}"
                                                 :en-GB "{count, plural,
                                                         =1 {One item will be added to the cart.}
                                                         other {# items will be added to the cart.}}"
                                                 :es "{count, plural, =1 {Se agregará un artículo al carrito.} other {Se agregarán # artículos al carrito.}}" :ca "{count, plural, =1 {S'afegirà un article a la cistella.} other {S'afegiran # articles a la cistella.}}"
                                                 :fr-CH "{count, plural,
                                                         =1 {Un article sera ajouté au panier.}
                                                         other {# articles seront ajoutés au panier.}}"}
                                          :error-only-options {:de-CH "Optionen können nur durch die Verleihstelle hinzugefügt werden."
                                                               :en-GB "Options can only be added by the lending desk."
                                                               :es "Las opciones solo pueden ser agregadas por el mostrador de préstamos." :ca "Les opcions només les pot afegir el taulell de préstecs."
                                                               :fr-CH "Les options ne peuvent être ajoutées que par le guichet de prêt."}
                                          :warning-some-options {:de-CH "{count, plural,
                                                         =1 {Hinweis: Eine Option kann nur durch die Verleihstelle hinzugefügt werden.}
                                                         other {Hinweis: # Optionen können nur durch die Verleihstelle hinzugefügt werden.}}"
                                                                 :en-GB "{count, plural,
                                                         =1 {Please note: One option can only be added by the lending desk.}
                                                         other {Please note: # options can only be added by the lending desk.}}"
                                                                 :es "{count, plural, =1 {Atención: una opción solo puede ser agregada por el mostrador de préstamos.} other {Atención: # opciones solo pueden ser agregadas por el mostrador de préstamos.}}" :ca "{count, plural, =1 {Atenció: una opció només la pot afegir el taulell de préstecs.} other {Atenció: # opcions només les pot afegir el taulell de préstecs.}}"
                                                                 :fr-CH "{count, plural,
                                                         =1 {Remarque : Une option ne peut être ajoutée que par le guichet de prêt.}
                                                         other {Remarque : # options ne peuvent être ajoutées que par le guichet de prêt.}}"}
                                          :info-multi-pool {:de-CH "Öffnungszeiten werden nicht angezeigt, wenn Gegenstände aus mehreren Inventarparks enthalten sind. Die Verfügbarkeiten können nach dem Hinzufügen im Warenkorb geprüft werden."
                                                            :en-GB "Opening hours are not displayed when items from multiple inventory pools are included. Availability can be checked in the shopping cart after adding the items."
                                                            :es "Los horarios de atención no se muestran cuando se incluyen artículos de varios grupos de inventario. La disponibilidad se puede comprobar en el carrito después de agregar los artículos." :ca "L'horari d'atenció no es mostra quan s'inclouen articles de diversos grups d'inventari. La disponibilitat es pot comprovar a la cistella després d'afegir els articles."
                                                            :fr-CH "Les heures d'ouverture ne sont pas affichées lorsque des articles de plusieurs pools d'inventaire sont inclus. La disponibilité peut être vérifiée dans le panier après l'ajout des articles."}
                                          :timespan {:de-CH "Zeitraum" :en-GB "Time span" :es "Período" :ca "Període" :fr-CH "Période"}
                                          :undefined {:de-CH "Unbestimmt" :en-GB "Not set" :es "Sin definir" :ca "Sense definir" :fr-CH "Indéfini"}
                                          :validation {:start-after-end {:de-CH "Enddatum muss nach Beginndatum sein"
                                                                          :en-GB "End date must be after start date"
                                                                          :es "La fecha de fin debe ser posterior a la fecha de inicio" :ca "La data de fi ha de ser posterior a la data d'inici"
                                                                          :fr-CH "La date de fin doit être postérieure à la date de début"}}
                                          :from {:de-CH "Von" :en-GB "From" :es "Desde" :ca "Des de" :fr-CH "De"}
                                          :until {:de-CH "Bis" :en-GB "Until" :es "Hasta" :ca "Fins a" :fr-CH "À"}
                                          :order-for {:de-CH "Bestellung für" :en-GB "Order for" :es "Pedido para" :ca "Comanda per a" :fr-CH "Commande pour"}
                                          :cancel {:de-CH "Abbrechen" :en-GB "Cancel" :es "Cancelar" :ca "Cancel·lar" :fr-CH "Annuler"}
                                          :submit {:de-CH "Hinzufügen" :en-GB "Add" :es "Agregar" :ca "Afegir" :fr-CH "Ajouter"}}
                                 :success-notification {:title {:de-CH "Gegenstände hinzugefügt" :en-GB "Items added" :es "Artículos agregados" :ca "Articles afegits" :fr-CH "Articles ajoutés"}
                                                        :message {:de-CH "{count, plural,
                                                                          =1 {# Gegenstand wurde zum Warenkorb hinzugefügt und kann nun dort überprüft werden.}
                                                                          other {# Gegenstände wurden zum Warenkorb hinzugefügt und können nun dort überprüft werden.}}"
                                                                  :en-GB "{count, plural,
                                                                          =1 {# item was added to the cart and can be reviewed/edited there.}
                                                                          other {# items were added to the cart and can be reviewed/edited there.}}"
                                                                  :es "{count, plural, =1 {Se agregó # artículo al carrito, donde puede revisarlo o modificarlo.} other {Se agregaron # artículos al carrito, donde puede revisarlos o modificarlos.}}" :ca "{count, plural, =1 {S'ha afegit # article a la cistella, on el pots revisar o modificar.} other {S'han afegit # articles a la cistella, on els pots revisar o modificar.}}"
                                                                  :fr-CH "{count, plural,
                                                                          =1 {# article a été ajouté au panier et peut y être révisé/modifié.}
                                                                          other {# articles ont été ajoutés au panier et peuvent y être révisés/modifiés.}}"}
                                                        :confirm {:de-CH "Zum Warenkorb" :en-GB "Go to cart" :es "Ir al carrito" :ca "Anar a la cistella" :fr-CH "Aller au panier"}}}}
                  ;

    :shopping-cart {:title {:en-GB "Cart" :es "Carrito" :ca "Cistella"
                            :de-CH "Warenkorb"
                            :fr-CH "Panier"}
                    :edit {:en-GB "Edit" :es "Editar" :ca "Editar"
                           :de-CH "Editieren" :fr-CH "Modifier"}
                    :draft {:title {:en-GB "Draft" :es "Borrador" :ca "Esborrany"
                                    :de-CH "Draft" :fr-CH "Brouillon"}
                            :add-to-cart {:en-GB "Add to cart" :es "Agregar al carrito" :ca "Afegir a la cistella"
                                          :de-CH "Add to cart" :fr-CH "Ajouter au panier"}
                            :delete {:en-GB "Delete draft" :es "Eliminar borrador" :ca "Eliminar esborrany"
                                     :de-CH "Delete draft" :fr-CH "Supprimer le brouillon"}
                            :empty {:en-GB "Your draft is empty" :es "Su borrador está vacío" :ca "El teu esborrany està buit"
                                    :de-CH "Your draft is empty" :fr-CH "Votre brouillon est vide"}}
                    :countdown {:section-title {:en-GB "Status" :es "Estado" :ca "Estat"
                                                :de-CH "Status" :fr-CH "Statut"}
                                :time-limit {:en-GB "Time limit" :es "Tiempo límite" :ca "Temps límit"
                                             :de-CH "Zeitlimit" :fr-CH "Limite de temps"}
                                :time-left {:en-GB "{minutesLeft, plural,
                                                    =1 {# minute left}
                                                    other {# minutes left}
                                                    }" :es "{minutesLeft, plural, =1 {Queda # minuto} other {Quedan # minutos} }" :ca "{minutesLeft, plural, =1 {Queda # minut} other {Queden # minuts} }"
                                            :de-CH "{minutesLeft, plural,
                                                    =1 {Noch eine Minute übrig}
                                                    other {Noch # Minuten übrig}
                                                    }"
                                            :fr-CH "{minutesLeft, plural,
                                                    =1 {# minute restante}
                                                    other {# minutes restantes}}"}
                                :time-left-last-minute {:en-GB "Less than one minute left" :es "Queda menos de un minuto" :ca "Queda menys d'un minut"
                                                        :de-CH "Weniger als eine Minute übrig" :fr-CH "Moins d'une minute restante"}
                                :no-valid-items {:en-GB "No valid items" :es "Sin artículos válidos" :ca "Sense articles vàlids"
                                                 :de-CH "Keine gültigen Gegenstände" :fr-CH "Aucun élément valide"}
                                :expired {:en-GB "Expired" :es "Vencido" :ca "Caducat"
                                          :de-CH "Abgelaufen" :fr-CH "Expiré"}
                                :reset {:en-GB "Reset time limit" :es "Reiniciar el tiempo límite" :ca "Reiniciar el temps límit"
                                        :de-CH "Zeitlimit zurückstellen" :fr-CH "Réinitialiser la limite de temps"}}
                    :delegation {:section-title {:de-CH "Bestellung für" :en-GB "Order for" :es "Pedido para" :ca "Comanda per a" :fr-CH "Commande pour"}}
                    :line {:section-title {:en-GB "Items" :es "Artículos" :ca "Articles"
                                           :de-CH "Gegenstände" :fr-CH "Éléments"}
                           :total {:en-GB "Total" :es "Total" :ca "Total"
                                   :de-CH "Total" :fr-CH "Total"}
                           :total-models {:en-GB "Model(s)" :es "Modelo(s)" :ca "Model(s)"
                                          :de-CH "Modell(e)" :fr-CH "Modèle(s)"}
                           :total-items {:en-GB "Item(s)" :es "Artículo(s)" :ca "Article(s)"
                                         :de-CH "Gegenstand/Gegenstände" :fr-CH "Élément(s)"}
                           :from {:en-GB "from" :es "desde" :ca "des de"
                                  :de-CH "aus" :fr-CH "de"}
                           :first-pickup {:en-GB "First pickup" :es "Primer retiro" :ca "Primera recollida"
                                          :de-CH "Erste Abholung" :fr-CH "Premier retrait"}
                           :last-return {:en-GB "last return" :es "última devolución" :ca "darrer retorn"
                                         :de-CH "letzte Rückgabe" :fr-CH "dernier retour"}
                           :invalid-items-warning {:en-GB "{invalidItemsCount, plural,
                                                           =1 {# invalid item}
                                                           other {# invalid items}
                                                           }" :es "{invalidItemsCount, plural, =1 {# artículo no válido} other {# artículos no válidos} }" :ca "{invalidItemsCount, plural, =1 {# article no vàlid} other {# articles no vàlids} }"
                                                   :de-CH "{invalidItemsCount, plural,
                                                           =1 {# Gegenstand ungültig}
                                                           other {# Gegenstände ungültig}
                                                           }"
                                                   :fr-CH "{invalidItemsCount, plural,
                                                           =1 {# élément non valide}
                                                           other {# éléments non valides}}"}
                           :duration-days {:de-CH "{totalDays, plural,
                                                                        =1 {# Tag}
                                                                        other {# Tage}
                                                                      }"
                                           :en-GB "{totalDays, plural,
                                                                        =1 {# day}
                                                                        other {# days}
                                                                      }"
                                           :es "{totalDays, plural, =1 {# día} other {# días} }" :ca "{totalDays, plural, =1 {# dia} other {# dies} }"
                                           :fr-CH "{totalDays, plural,
                                                                        =1 {# jour}
                                                                        other {# jours}}"}}
                    :edit-dialog {:dialog-title {:en-GB "Edit reservation" :es "Editar reserva" :ca "Editar reserva" :de-CH "Reservation bearbeiten" :fr-CH "Modifier la réservation"}
                                  :delete-reservation {:en-GB "Remove reservation" :es "Quitar reserva" :ca "Treure reserva" :de-CH "Reservation entfernen" :fr-CH "Supprimer la réservation"}
                                  :cancel {:en-GB "Cancel" :es "Cancelar" :ca "Cancel·lar" :de-CH "Abbrechen" :fr-CH "Annuler"}
                                  :confirm {:en-GB "Confirm" :es "Confirmar" :ca "Confirmar" :de-CH "Bestätigen" :fr-CH "Confirmer"}}
                    :confirm-order {:en-GB "Send order" :es "Enviar pedido" :ca "Enviar comanda"
                                    :de-CH "Bestellung abschicken"
                                    :fr-CH "Envoyer la demande"}
                    :delete-order {:en-GB "Delete cart" :es "Vaciar carrito" :ca "Buidar la cistella"
                                   :de-CH "Warenkorb löschen"
                                   :fr-CH "Supprimer le panier"}
                    :order-overview {:en-GB "Cart" :es "Carrito" :ca "Cistella"
                                     :de-CH "Warenkorb"
                                     :fr-CH "Panier"}
                    :empty-order {:en-GB "No items added" :es "No se agregaron artículos" :ca "No s'han afegit articles"
                                  :de-CH "Noch keine Gegenstände hinzugefügt"
                                  :fr-CH "Aucun article ajouté"}
                    :borrow-items {:en-GB "Go to catalog" :es "Ir al catálogo" :ca "Anar al catàleg"
                                   :de-CH "Hier geht's zum Katalog"
                                   :fr-CH "Aller au catalogue"}
                    :confirm-dialog {:dialog-title {:en-GB "Send order" :es "Enviar pedido" :ca "Enviar comanda" :de-CH "Bestellung abschicken" :fr-CH "Envoyer la commande"}
                                     :title {:en-GB "Title" :es "Título" :ca "Títol" :de-CH "Titel" :fr-CH "Titre"}
                                     :title-hint {:en-GB "As a reference for you" :es "Como referencia para usted" :ca "Com a referència per a tu" :de-CH "Als Referenz für dich" :fr-CH "À titre de référence pour vous"}
                                     :purpose {:en-GB "Purpose" :es "Motivo" :ca "Motiu" :de-CH "Zweck" :fr-CH "Objet ou référence"}
                                     :purpose-hint {:en-GB "For the inventory pool" :es "Para el grupo de inventario" :ca "Per al grup d'inventari" :de-CH "Für den Inventarpark" :fr-CH "À l'attention du responsable de l'inventaire"}
                                     :contact-details {:en-GB "Contact details" :es "Datos de contacto" :ca "Dades de contacte" :de-CH "Kontaktdaten" :fr-CH "Coordonnées"}
                                     :contact-details-hint {:en-GB "The indication of a telephone number is recommended" :es "Se recomienda indicar un número de teléfono" :ca "Es recomana indicar un número de telèfon"
                                                            :de-CH "Die Angabe einer Telefonnummer ist empfohlen"
                                                            :fr-CH "L'indication d'un numéro de téléphone est recommandée"}
                                     :lending-terms {:en-GB "Lending terms" :es "Condiciones de préstamo" :ca "Condicions de préstec" :de-CH "Ausleihbedingungen" :fr-CH "Conditions de prêt"}
                                     :i-accept {:en-GB "I accept the lending terms" :es "Acepto las condiciones de préstamo" :ca "Accepto les condicions de préstec"
                                                :de-CH "Ich akzeptiere die Ausleihbedingungen"
                                                :fr-CH "J'accepte les conditions de prêt"}
                                     :cancel {:en-GB "Cancel" :es "Cancelar" :ca "Cancel·lar" :de-CH "Abbrechen" :fr-CH "Annuler"}
                                     :confirm {:en-GB "Send" :es "Enviar" :ca "Enviar" :de-CH "Abschicken" :fr-CH "Envoyer"}}
                    :order-success-notification {:title {:en-GB "Order submitted" :es "Pedido enviado" :ca "Comanda enviada" :de-CH "Bestellung übermittelt" :fr-CH "Commande envoyée"}
                                                 :order-submitted {:en-GB "Order was submitted but still needs to be approved!" :es "El pedido fue enviado, pero todavía debe ser aprobado." :ca "La comanda s'ha enviat, però encara s'ha d'aprovar."
                                                                   :de-CH "Die Bestellung wurde übermittelt, muss aber noch genehmigt werden!"
                                                                   :fr-CH "La commande a été envoyée mais doit encore être approuvée."}}
                    :delete-dialog {:dialog-title {:en-GB "Delete cart" :es "Vaciar carrito" :ca "Buidar la cistella"
                                                   :de-CH "Warenkorb löschen"
                                                   :fr-CH "Supprimer le panier"}
                                    :really-delete-order {:en-GB "Really remove all reservations?" :es "¿Realmente desea quitar todas las reservas?" :ca "Segur que vols treure totes les reserves?"
                                                          :de-CH "Wirklich alle Reservationen entfernen?"
                                                          :fr-CH "Supprimer vraiment toutes les réservations ?"}
                                    :cancel {:en-GB "Cancel" :es "Cancelar" :ca "Cancel·lar"
                                             :de-CH "Abbrechen"
                                             :fr-CH "Annuler"}
                                    :confirm {:en-GB "Delete" :es "Vaciar" :ca "Buidar"
                                              :de-CH "Löschen"
                                              :fr-CH "Supprimer"}}}
    :templates {:index {:title {:de-CH "Vorlagen" :en-GB "Templates" :es "Plantillas" :ca "Plantilles" :fr-CH "Modèles"}
                        :no-templates-for-current-profile {:de-CH "Für das aktuelle Profil sind keine Vorlagen verfügbar"
                                                           :en-GB "No templates available for the current profile"
                                                           :es "No hay plantillas disponibles para el perfil actual" :ca "No hi ha plantilles disponibles per al perfil actual" :fr-CH "Aucun modèle disponible pour le profil actuel"}}
                :show {:title {:de-CH "Vorlage" :en-GB "Template" :es "Plantilla" :ca "Plantilla" :fr-CH "Modèle"}
                       :items {:de-CH "Gegenstände" :en-GB "Items" :es "Artículos" :ca "Articles" :fr-CH "Éléments"}
                       :item-title {:de-CH "{itemCount}× {itemName}" :en-GB "{itemCount}× {itemName}" :es "{itemCount}× {itemName}" :ca "{itemCount}× {itemName}" :fr-CH "{itemCount}× {itemName}"}
                       :template-not-available {:de-CH "Diese Vorlage ist für das aktuelle Profil nicht verfügbar"
                                                :en-GB "This template is not available for the current profile"
                                                :es "Esta plantilla no está disponible para el perfil actual" :ca "Aquesta plantilla no està disponible per al perfil actual" :fr-CH "Ce modèle n'est pas disponible pour le profil actuel"}
                       :user-suspended {:de-CH "Zugang zu {poolName} gesperrt"
                                        :en-GB "Access to {poolName} is suspended"
                                        :es "El acceso a {poolName} está suspendido" :ca "L'accés a {poolName} està suspès" :fr-CH "L'accès à {poolName} est suspendu"}
                       :some-items-not-available {:de-CH "Die Gegenstände in grauer Schrift sind für das aktuelle Profil nicht reservierbar"
                                                  :en-GB "The items shown in grey font are not reservable for the current profile"
                                                  :es "Los artículos en gris no se pueden reservar con el perfil actual" :ca "Els articles en gris no es poden reservar amb el perfil actual" :fr-CH "Les éléments affichés en gris ne sont pas réservables pour le profil actuel"}
                       :no-items-available {:de-CH "Diese Vorlage enthält keine Gegenstände, welche für das aktuelle Profil reservierbar sind"
                                            :en-GB "This template does not contain any items that can be reserved for the current profile"
                                            :es "Esta plantilla no contiene artículos que se puedan reservar con el perfil actual" :ca "Aquesta plantilla no conté articles que es puguin reservar amb el perfil actual" :fr-CH "Ce modèle ne contient aucun élément réservable pour le profil actuel"}
                       :apply-button-label {:de-CH "Gegenstände bestellen"
                                            :en-GB "Order items"
                                            :es "Pedir los artículos" :ca "Demanar els articles" :fr-CH "Commander les éléments"}}
                :apply {:dialog {:title {:de-CH "Gegenstände hinzufügen" :en-GB "Add items" :es "Agregar artículos" :ca "Afegir articles" :fr-CH "Ajouter des éléments"}
                                 :info {:de-CH "{count, plural,
                                                         =1 {Ein Gegenstand wird zum Warenkorb hinzugefügt.}
                                                         other {# Gegenstände werden zum Warenkorb hinzugefügt.}}"
                                        :en-GB "{count, plural,
                                                         =1 {One item will be added to the cart.}
                                                         other {# items will be added to the cart.}}"
                                        :es "{count, plural, =1 {Se agregará un artículo al carrito.} other {Se agregarán # artículos al carrito.}}" :ca "{count, plural, =1 {S'afegirà un article a la cistella.} other {S'afegiran # articles a la cistella.}}"
                                        :fr-CH "{count, plural,
                                                         =1 {Un élément sera ajouté au panier.}
                                                         other {# éléments seront ajoutés au panier.}}"}
                                 :error-no-items {:de-CH "Keine Gegenstände gefunden"
                                                  :en-GB "No items found"
                                                  :es "No se encontraron artículos" :ca "No s'han trobat articles"
                                                  :fr-CH "Aucun élément trouvé"}
                                 :timespan {:de-CH "Zeitraum" :en-GB "Time span" :es "Período" :ca "Període" :fr-CH "Période"}
                                 :undefined {:de-CH "Unbestimmt" :en-GB "Not set" :es "Sin definir" :ca "Sense definir" :fr-CH "Indéfini"}
                                 :validation {:start-after-end {:de-CH "Enddatum muss nach Beginndatum sein"
                                                                 :en-GB "End date must be after start date"
                                                                 :es "La fecha de fin debe ser posterior a la fecha de inicio" :ca "La data de fi ha de ser posterior a la data d'inici"
                                                                 :fr-CH "La date de fin doit être postérieure à la date de début"}}
                                 :from {:de-CH "Von" :en-GB "From" :es "Desde" :ca "Des de" :fr-CH "De"}
                                 :until {:de-CH "Bis" :en-GB "Until" :es "Hasta" :ca "Fins a" :fr-CH "À"}
                                 :order-for {:de-CH "Bestellung für" :en-GB "Order for" :es "Pedido para" :ca "Comanda per a" :fr-CH "Commande pour"}
                                 :pool {:de-CH "Inventarpark" :en-GB "Inventory pool" :es "Grupo de inventario" :ca "Grup d'inventari" :fr-CH "Pool d'inventaire"}
                                 :cancel {:de-CH "Abbrechen" :en-GB "Cancel" :es "Cancelar" :ca "Cancel·lar" :fr-CH "Annuler"}
                                 :submit {:de-CH "Hinzufügen" :en-GB "Add" :es "Agregar" :ca "Afegir" :fr-CH "Ajouter"}}
                        :success-notification {:title {:de-CH "Gegenstände hinzugefügt" :en-GB "Items added" :es "Artículos agregados" :ca "Articles afegits" :fr-CH "Éléments ajoutés"}
                                               :message {:de-CH "{count, plural,
                                                                          =1 {Ein Gegenstand wurde zum Warenkorb hinzugefügt und kann nun dort überprüft werden.}
                                                                          other {# Gegenstände wurden zum Warenkorb hinzugefügt und können nun dort überprüft werden.}}"
                                                         :en-GB "{count, plural,
                                                                          =1 {One item was added to the cart and can be reviewed/edited there.}
                                                                          other {# items were added to the cart and can be reviewed/edited there.}}"
                                                         :es "{count, plural, =1 {Se agregó un artículo al carrito, donde puede revisarlo o modificarlo.} other {Se agregaron # artículos al carrito, donde puede revisarlos o modificarlos.}}" :ca "{count, plural, =1 {S'ha afegit un article a la cistella, on el pots revisar o modificar.} other {S'han afegit # articles a la cistella, on els pots revisar o modificar.}}"
                                                         :fr-CH "{count, plural,
                                                                          =1 {Un élément a été ajouté au panier et peut y être révisé/modifié.}
                                                                          other {# éléments ont été ajoutés au panier et peuvent y être révisés/modifiés.}}"}
                                               :confirm {:de-CH "Zum Warenkorb" :en-GB "Go to cart" :es "Ir al carrito" :ca "Anar a la cistella" :fr-CH "Aller au panier"}}}}}})
