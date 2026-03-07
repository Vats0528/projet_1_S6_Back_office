Feature  : Planification & Assignation de véhicule
(sprint3)
TL :  ETU003330         -Vatosoa        
FO :  ETU002647         -Nallitiana
BO :  ETU003244         -Kiady         

<!-- ///////////////////////////////////////////////////// -->
A faire -> TL
[Objectif-par-branche]
    [Sprint3/Feat]
        [ ] - todo et distribution des tâches
        [ ] - créer et committer [TABLE_sprint_3_2026.sql]
              (migration : hotel → lieu, ajout distance, vehicule,
               carburant, param_vehicule, reservation_vehicule,
               details_reservation_client, details_trajet)
        [ ] - committer les données de test [init-sql-sprint3]
              (lieux, distances entre lieux, véhicules, carburants, param_vehicule)
        [ ] - final merge de toutes les branches feature

    [Coordination]
        [ ] - vérifier que BO et FO utilisent bien les mêmes noms d'endpoints
        [ ] - valider la logique d'assignation avec l'équipe avant implémentation
        [ ] - vérifier la compatibilité token sprint2 avec les nouveaux endpoints
        [ ] - review du calcul date_heure_retour côté BO

    [Staging]
        [ ] - simulation data (données réalistes : lieux, distances, véhicules)
        [ ] - cherry pick → release / merge main (repo BO)
        [ ] - cherry pick → release / merge main (repo FO)
        [ ] - test end-to-end staging (FO → API BO → assignation → résultat)

    [Release]
        [ ] - validation finale du workflow complet
        [ ] - merge release → main

<!-- ///////////////////////////////////////////////////// -->

A faire -> FO
[objectif-par-pages]
    -[page_form_reservation]                             [ ] -[page_lieu]
        [ ] - liste des lieux
        [ ] - formulaire ajout/modif lieu

[ ] -[page_vehicule]
        [ ] - liste des véhicules (modèle, nb_place, carburant)
        [ ] - formulaire ajout/modif véhicule

[ ] -[page_param_vehicule]
        [ ] - affichage et modification de VM et TA

[ ] -[page_form_reservation]
        [ ] - liste déroulante client                     [id_client]
        [ ] - liste déroulante lieu destination           [id_lieu]
        [ ] - input : nb_passager, date_heure_arrivee
        [ ] - soumission → appel API reservation

[ ] -[page_liste_reservation]
        [ ] - liste des réservations
        [ ] - filtre par date/heure

[ ] -[page_assignation]
        [ ] - afficher réservations non encore assignées
        [ ] - lancer l'assignation → appel API
        [ ] - afficher le résultat :
                [ ] - groupe de clients regroupés
                [ ] - véhicule assigné (modèle, capacité, carburant)
                [ ] - ordre des arrêts (lieu 1 → lieu 2 → ...)
                [ ] - date_heure_depart et date_heure_retour calculés

<!-- ///////////////////////////////////////////////////// -->

A faire -> BO
[objectif-par-end-point]
    [ ] -[LIEU]              : CRUD                          .../api/lieu
[ ] -[DISTANCE]          : CRUD (entre deux lieux)        .../api/distance
[ ] -[CARBURANT]         : CRUD                          .../api/carburant
[ ] -[VEHICULE]          : CRUD                          .../api/vehicule
[ ] -[PARAM_VEHICULE]    : CRUD (VM, TA)                 .../api/param-vehicule
[ ] -[RESERVATION]       : CRUD                          .../api/reservation

[ ] -[ASSIGNATION]       : Logique métier                .../api/assignation
        [ ] - regrouper les réservations dont date_heure_arrivee est dans ± TA (30min)
              (peu importe le lieu destination)
        [ ] - filtrer véhicules disponibles : nb_place >= total nb_passager du groupe
        [ ] - choisir le véhicule optimal (moins de trajets déjà assignés)
        [ ] - ordonner les arrêts par distance croissante depuis l'aéroport
              → construire details_trajet avec succession (1, 2, 3...)
        [ ] - calculer date_heure_depart depuis l'aéroport
        [ ] - calculer date_heure_retour :
                somme des (distance/VM) de chaque étape
                + temps_attente à chaque arrêt
                + trajet retour vers aéroport (dernier lieu → aéroport)
        [ ] - créer reservation_vehicule + details_reservation_client + details_trajet

*Rappel logique de calcul date_heure_retour
    Pour chaque étape i (triée par distance croissante) :
    temps_trajet_i = distance_i / VM

date_heure_retour = date_heure_depart
  + Σ temps_trajet_i          (tous les arrêts)
  + Σ temps_attente           (à chaque arrêt)
  + temps_retour_aeroport     (dernier lieu → aéroport)


*Notes: 

    Acteurs
        client — les clients qui font des réservations (id + nom)
        token — gestion d'authentification avec niveau d'accès et expiration

    Lieux
        lieu — les points de départ/arrivée (hôtels, aéroports, etc.) avec un code et un libellé
        distance — la distance et le temps de trajet entre deux lieux (relation lieu → lieu)

    Véhicules
        carburant — type de carburant (essence, diesel, électrique…)
        vehicule — modèle, nombre de places, type de carburant
        param_vehicule — paramètres globaux : vitesse moyenne, temps d'attente

    Réservations
        reservation_client — réservation faite par un client : nb passagers, heure d'arrivée, lieu de destination
        reservation_vehicule — réservation d'un véhicule : départ/retour
        details_reservation_client — table de jointure réservation client ↔ réservation véhicule (un client peut avoir plusieurs véhicules, un véhicule peut couvrir plusieurs clients)
        details_trajet — les étapes du trajet d'une réservation véhicule, avec un ordre (succession)

Status:
    en attente de sprint 2
