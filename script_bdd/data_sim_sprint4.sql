-- 1. Les Lieux (Aéroport, Hôtels et le Garage pour le retour)
INSERT INTO lieu (code, libelle) VALUES 
('AIRP', 'Aéroport International'),
('H-HILTON', 'Hôtel Hilton'),
('H-IBIS', 'Hôtel Ibis'),
('GAR-1', 'Garage Principal (Aéroport)');

-- 2. Liaison des hôtels
INSERT INTO hotel (nom_hotel, id_lieu) VALUES 
('Hilton Garden', 2),
('Ibis Budget', 3);

-- 3. Paramètres par défaut (Vitesse 50km/h, Attente 60 min)
INSERT INTO param_vehicule (vitess_moyenne, temps_attente) VALUES (50, 60);

-- 4. Carburant
INSERT INTO carburant (id_carburant, nom_carburant) VALUES (1, 'Diesel'), (2, 'Essence');



-- 5. Les 3 Voitures (4, 9 et 17 places)
INSERT INTO vehicule (model, nb_place, id_carburant) VALUES 
('Peugeot Partner', 4, 1),
('Renault Trafic', 9, 1),
('Mercedes Sprinter', 17, 1);

-- 6. Distances et Temps (Simulation de trajets)
-- Format : Aéroport (1), Hilton (2), Ibis (3), Garage (4)
INSERT INTO distance (id_lieu, id_lieu_1, distance, temps) VALUES 
(1, 2, 15.0, '00:30:00'), -- Aéroport -> Hilton (30 min)
(2, 3, 10.0, '00:20:00'), -- Hilton -> Ibis (20 min)
(3, 4, 12.0, '00:25:00'), -- Ibis -> Garage (25 min)
(1, 3, 20.0, '00:40:00'), -- Aéroport -> Ibis (40 min)
(2, 4, 15.0, '00:30:00'); -- Hilton -> Garage (30 min)




-- Imaginons que le Mercedes (id 3) est déjà pris de 08h00 à 09h30
INSERT INTO reservation_vehicule (date_heure_depart, date_heure_retour, titre, id_vehicule) 
VALUES ('2024-06-01 08:00:00', '2024-06-01 09:30:00', 'Transfert Groupe Matin', 3);


-- //////////////////////////////////////////////////////////////////////


-- 1. Créons d'abord deux clients
INSERT INTO client (nom_client) VALUES ('Groupe Jean'), ('Groupe Maria');

-- 2. Créons leurs réservations (10 pers pour Hilton, 6 pers pour Ibis)
INSERT INTO reservation_client (nb_passager, date_heure_arrivee, status, id_hotel, id_client) 
VALUES 
(10, '2024-06-01 08:15:00', 'CONFIRME', 1, 1),
(6, '2024-06-01 08:30:00', 'CONFIRME', 2, 2);

-- 3. Créons la mission du véhicule (Mercedes id=3)
INSERT INTO reservation_vehicule (date_heure_depart, date_heure_retour, titre, id_vehicule) 
VALUES ('2024-06-01 09:00:00', '2024-06-01 10:15:00', 'Transfert Matin 09h', 3);

-- 4. LE LIEN CRUCIAL : On met les clients dans la voiture
-- (On suppose que la mission créée a l'ID 2, et les réservations 1 et 2)
INSERT INTO details_reservation_client (id_reservation_client, id_reservation_vehicule) 
VALUES (1, 2), (2, 2);
