-- verification result de sim = tsy mis normalement
SELECT id_vehicule, model, nb_place 
FROM vehicule 
WHERE nb_place >= 16 -- (10 + 6 passagers)
AND id_vehicule NOT IN (
    SELECT id_vehicule 
    FROM reservation_vehicule 
    WHERE (date_heure_depart, date_heure_retour) OVERLAPS ('2024-06-01 09:00:00'::timestamp, '2024-06-01 10:15:00'::timestamp)
);



-- Test à 10h00 du matin
SELECT id_vehicule, model, nb_place 
FROM vehicule 
WHERE nb_place >= 16 
AND id_vehicule NOT IN (
    SELECT id_vehicule 
    FROM reservation_vehicule 
    WHERE (date_heure_depart, date_heure_retour) OVERLAPS ('2024-06-01 10:00:00'::timestamp, '2024-06-01 11:15:00'::timestamp)
);


SELECT 
    rv.date_heure_depart,
    v.model AS voiture,
    v.nb_place AS capacite_voiture,
    c.nom_client,
    rc.nb_passager AS nb_personnes_groupe,
    h.nom_hotel AS destination
FROM reservation_vehicule rv
JOIN vehicule v ON rv.id_vehicule = v.id_vehicule
JOIN details_reservation_client drc ON rv.id_reservation_vehicule = drc.id_reservation_vehicule
JOIN reservation_client rc ON drc.id_reservation_client = rc.id_reservation_client
JOIN client c ON rc.id_client = c.id_client
JOIN hotel h ON rc.id_hotel = h.id_hotel
WHERE rv.date_heure_depart = '2024-06-01 09:00:00' -- L'heure choisie
ORDER BY v.model;







SELECT 
    '2024-06-01 09:00:00'::timestamp AS depart_fixe,
    COUNT(rc.id_reservation_client) AS nb_groupes_clients,
    SUM(rc.nb_passager) AS total_passagers,
    v.model AS vehicule_suggere,
    v.nb_place AS places_disponibles,
    (v.nb_place - SUM(rc.nb_passager)) AS places_libres_restantes
FROM reservation_client rc
CROSS JOIN vehicule v 
WHERE rc.status = 'EN_ATTENTE'
AND rc.date_heure_arrivee >= '2024-06-01 08:00:00' 
AND rc.date_heure_arrivee < '2024-06-01 09:00:00'
-- On vérifie si le véhicule est réellement libre dans reservation_vehicule
AND v.id_vehicule NOT IN (
    SELECT id_vehicule FROM reservation_vehicule 
    WHERE (date_heure_depart, date_heure_retour) OVERLAPS ('2024-06-01 09:00:00', '2024-06-01 10:15:00')
)
GROUP BY v.id_vehicule, v.model, v.nb_place
HAVING v.nb_place >= SUM(rc.nb_passager)
ORDER BY v.nb_place ASC;