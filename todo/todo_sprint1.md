Feature  : reservation simple by client
(sprint1)
TL : ETU002647          - Nalitiana
FO : ETU003244          - Kiady
BO : ETU003330          - Vatosoa

<!-- ///////////////////////////////////////////////////// -->
A faire -> TL
[Objectif-par-branche]
        [Sprint1/Feat]
            -[ ] todo et distribution des tache
            -[x] faire et commit [TABLE_sprint_1_2026_02_06.sql] (type:[feat])
            -[]  commit []
            -[]
            -[]
            -[] Final merge pour les feature
        [staging]
            [ ]- simulation data
            [ ]- cheery pick -> release-06-02-2026(from main) /merge main

<!-- ///////////////////////////////////////////////////// -->

A faire -> FO
[objectif-par-pages]
    -[page_form_reservation]                             [DONEEE_a_recuperer]            [donnee_a_envoier]
        [x]-liste deroublate client                         [clients]                       [id_client]
        [x]-liste deroulante hotels                         [hotels]                       [id_hotels]
        [x]-input champ: nbpassager , dateheure_arrivee
        [x]-finaliation test env dev frontoffice  (tester-avec-l'api) -------Branche STAGING
    -[page-liste-reservation]
        [x]-liste reservation                             [reservations]                   [TSISY]
        [x]-filtre /date  et par /heure  (donc date heure)

<!-- ///////////////////////////////////////////////////// -->

A faire -> BO
[objectif-par-end-point]
    [ ] -[HOTEL]      : optionnel entre (CRUD  , SCRIPT avec la fonction getAll fotsiny) <!-- preferable crude -->   [base-end-point]:  .../api/hotel 
    [ ] -[CLIENT]     : CRUD                                                                                                             .../api/client
    [ ] -[RESERVATION]: CRUD     [il_y_a_assignation_client_hotel_ici]                                                                  .../api/reservation





liste todo [ENCOURS]

statu todo [ENCOURS]
