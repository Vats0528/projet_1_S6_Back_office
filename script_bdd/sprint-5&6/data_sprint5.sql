--- 1. LIEUX
INSERT INTO lieu (code, libelle) VALUES 
('AIRP', 'Aéroport International'),
('H-HILTON', 'Hôtel Hilton'),
('H-IBIS', 'Hôtel Ibis'),
('GAR-1', 'Garage Principal');

--- 2. HÔTELS
INSERT INTO hotel (nom_hotel, id_lieu) VALUES 
('Hilton Garden', 2),
('Ibis Budget', 3);

--- 3. PARAMÈTRES & CARBURANT
INSERT INTO param_vehicule (vitess_moyenne, temps_attente) VALUES (50, 60);
INSERT INTO carburant (id_carburant, nom_carburant) VALUES (1, 'Diesel'), (2, 'Essence');

--- 4. VÉHICULES
INSERT INTO vehicule (model, nb_place, id_carburant) VALUES 
('Peugeot Partner', 4, 1),
('Renault Trafic', 9, 1),
('Mercedes Sprinter', 17, 1);

--- 5. DISTANCES (Temps en INTEGER - Minutes)
INSERT INTO distance (id_lieu, id_lieu_1, distance, temps) VALUES 
(1, 2, 15.0, 30), -- 30 minutes
(2, 3, 10.0, 20), -- 20 minutes
(3, 4, 12.0, 25), -- 25 minutes
(1, 3, 20.0, 40), -- 40 minutes
(2, 4, 15.0, 30); -- 30 minutes
-- Création du segment Hôtel Ibis (3) -> Aéroport (1)
INSERT INTO distance (id_lieu, id_lieu_1, distance, temps) 
VALUES (3, 1, 20.0, 40); -- ID de cette distance sera probablement 6




--- 6. CLIENTS ET RÉSERVATIONS
INSERT INTO client (nom_client) VALUES ('Groupe Jean'), ('Groupe Maria');
INSERT INTO client (nom_client) VALUES ('Groupe Test1'), ('Groupe test2');

INSERT INTO reservation_client (nb_passager, date_heure_arrivee, status, id_hotel, id_client) 
VALUES 
(10, '2024-06-01 08:15:00', 'CONFIRME', 1, 1),
(6, '2024-06-01 08:30:00', 'CONFIRME', 2, 2);

--- 7. MISSIONS VÉHICULES (reservation_vehicule)
INSERT INTO reservation_vehicule (date_heure_depart, date_heure_retour, titre, id_vehicule) 
VALUES ('2024-06-01 09:00:00', '2024-06-01 10:15:00', 'Transfert Matin 09h', 3);




--- 8. LIAISONS (Assignation des groupes à la mission)
INSERT INTO details_reservation_client (id_reservation_client, id_reservation_vehicule) 
VALUES (1, 1), (2, 1);
--- 9. DÉTAILS DU TRAJET (L'itinéraire réel de la mission)
-- Succession 1 : Aéroport (1) -> Hilton (2) [Distance ID 1]
-- Succession 2 : Hilton (2) -> Ibis (3) [Distance ID 2]
-- Succession 3 : Ibis (3) -> Garage (4) [Distance ID 3]

INSERT INTO details_trajet (id_reservation_vehicule, id_distance, succession) 
VALUES 
(1, 1, 1), 
(1, 2, 2), 
(1, 3, 3);

-- On remplace le segment qui allait au garage (id_distance 3) 
-- par celui qui retourne à l'aéroport (id_distance 6)
UPDATE details_trajet 
SET id_distance = 6 
WHERE id_reservation_vehicule = 1 AND succession = 3;

UPDATE param_vehicule
SET temps_attente = 30
WHERE id_param_vehicule = 1 ;