-- =============================================
-- SCRIPT : Population complète pour Simulation
-- Date cible : 2026-03-12
-- =============================================

-- 1. CARBURANT
INSERT INTO carburant (id_carburant, nom_carburant) VALUES
(1, 'Essence'),
(2, 'Diesel');

-- 2. LIEU (Aéroport + Hôtels)
INSERT INTO lieu (code, libelle) VALUES
('AERO-TNR', 'Aéroport Ivato'),      -- id_lieu = 1
('HTL-COL',  'Quartier Colbert'),    -- id_lieu = 2
('HTL-IBS',  'Quartier Ankorondrano'),-- id_lieu = 3
('HTL-CRT',  'Quartier Mahamasina'), -- id_lieu = 4
('HTL-SAK',  'Quartier Isoraka'),    -- id_lieu = 5
('HTL-GLC',  'Quartier Analakely');  -- id_lieu = 6

-- 3. HOTEL
INSERT INTO hotel (nom_hotel, id_lieu) VALUES
('Hotel Colbert', 2),      -- id_hotel = 1
('Hotel Ibis', 3),         -- id_hotel = 2
('Hotel Carlton', 4),      -- id_hotel = 3
('Hotel Sakamanga', 5),    -- id_hotel = 4
('Hotel Le Glacier', 6);   -- id_hotel = 5

-- 4. DISTANCE (Pour les calculs de temps de trajet)
INSERT INTO distance (temps, distance, id_lieu, id_lieu_1) VALUES
('00:30:00', 10.00, 1, 2), ('00:30:00', 10.00, 2, 1), -- Ivato <-> Colbert
('00:45:00', 15.00, 1, 3), ('00:45:00', 15.00, 3, 1), -- Ivato <-> Ibis
('01:00:00', 20.00, 1, 4), ('01:00:00', 20.00, 4, 1), -- Ivato <-> Carlton
('00:40:00', 12.00, 1, 5), ('00:40:00', 12.00, 5, 1); -- Ivato <-> Sakamanga

-- 5. VEHICULE (Le parc auto)
INSERT INTO vehicule (model, nb_place, id_carburant) VALUES
('Toyota Hiace', 12, 2),        -- id 1 (Diesel)
('Toyota Coaster', 20, 2),      -- id 2 (Diesel)
('Renault Trafic', 8, 1),       -- id 3 (Essence)
('Nissan Urvan', 10, 2),        -- id 4 (Diesel)
('Light Car', 4, 1);            -- id 5 (Essence)

-- 6. CLIENT
INSERT INTO client (nom_client) VALUES
('Groupe Air France'), ('Famille Rabe'), ('Mission ONU'), ('Touriste Solo'), ('Equipe Tech');

-- 7. RESERVATION_CLIENT (Le scénario de test)
-- Nous créons des groupes qui arrivent presque en même temps pour tester le groupage (le "Bin Packing")
INSERT INTO reservation_client (nb_passager, date_heure_arrivee, status, id_hotel, id_client) VALUES
-- Vague de 08:30 (Total 11 personnes pour des hôtels proches)
(6, '2026-03-12 08:30:00', 'EN_ATTENTE', 1, 1), -- id_res 1
(3, '2026-03-12 08:35:00', 'EN_ATTENTE', 1, 2), -- id_res 2
(2, '2026-03-12 08:40:00', 'EN_ATTENTE', 2, 4), -- id_res 3

-- Le gros groupe de 10:00 (Nécessite le Coaster)
(18, '2026-03-12 10:00:00', 'EN_ATTENTE', 3, 3), -- id_res 4

-- Vague de 14:00 (Test priorité Diesel)
(7, '2026-03-12 14:00:00', 'EN_ATTENTE', 4, 5); -- id_res 5

-- 8. PARAM_VEHICULE
INSERT INTO param_vehicule (vitess_moyenne, temps_attente) VALUES (20, 30);