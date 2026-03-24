-- =============================================
-- SCRIPT : Données fictives de test
-- Sprint 3 — Assignation de véhicule
-- =============================================

-- CARBURANT
INSERT INTO carburant (id_carburant, nom_carburant) VALUES
(1, 'Essence'),
(2, 'Diesel');

-- LIEU (1 aéroport + plusieurs hôtels)
INSERT INTO lieu (code, libelle) VALUES
('AERO-TNR', 'Aéroport Ivato'),        -- id_lieu = 1
('HTL-001',  'Hotel Colbert'),          -- id_lieu = 2
('HTL-002',  'Hotel Ibis Analakely'),   -- id_lieu = 3
('HTL-003',  'Hotel Carlton'),          -- id_lieu = 4
('HTL-004',  'Hotel Sakamanga'),        -- id_lieu = 5
('HTL-005',  'Hotel Le Glacier');       -- id_lieu = 6

-- DISTANCE (depuis aéroport vers chaque hôtel + entre hôtels)
-- Format : id_lieu = départ, id_lieu_1 = arrivée
-- distance en km, temps calculé avec VM=20km/h

INSERT INTO distance (temps, distance, id_lieu, id_lieu_1) VALUES
-- Aéroport → Hôtels
('00:30:00', 10.00, 1, 2),   -- Aéroport → Colbert         10km
('00:45:00', 15.00, 1, 3),   -- Aéroport → Ibis            15km
('01:00:00', 20.00, 1, 4),   -- Aéroport → Carlton         20km
('00:36:00', 12.00, 1, 5),   -- Aéroport → Sakamanga       12km
('00:54:00', 18.00, 1, 6),   -- Aéroport → Le Glacier      18km

-- Hôtels → Aéroport (retour)
('00:30:00', 10.00, 2, 1),   -- Colbert → Aéroport
('00:45:00', 15.00, 3, 1),   -- Ibis → Aéroport
('01:00:00', 20.00, 4, 1),   -- Carlton → Aéroport
('00:36:00', 12.00, 5, 1),   -- Sakamanga → Aéroport
('00:54:00', 18.00, 6, 1),   -- Le Glacier → Aéroport

-- Entre hôtels (pour les trajets multi-arrêts)
('00:06:00',  2.00, 2, 3),   -- Colbert → Ibis
('00:06:00',  2.00, 3, 2),   -- Ibis → Colbert
('00:06:00',  2.00, 2, 5),   -- Colbert → Sakamanga
('00:06:00',  2.00, 5, 2),   -- Sakamanga → Colbert
('00:09:00',  3.00, 3, 5),   -- Ibis → Sakamanga
('00:09:00',  3.00, 5, 3),   -- Sakamanga → Ibis
('00:15:00',  5.00, 5, 4),   -- Sakamanga → Carlton
('00:15:00',  5.00, 4, 5),   -- Carlton → Sakamanga
('00:09:00',  3.00, 3, 6),   -- Ibis → Le Glacier
('00:09:00',  3.00, 6, 3),   -- Le Glacier → Ibis
('00:06:00',  2.00, 4, 6),   -- Carlton → Le Glacier
('00:06:00',  2.00, 6, 4);   -- Le Glacier → Carlton

-- PARAM_VEHICULE
INSERT INTO param_vehicule (vitess_moyenne, temps_attente) VALUES
(20, 30);  -- VM = 20km/h , TA = 30min

-- VEHICULE
INSERT INTO vehicule (model, nb_place, id_carburant) VALUES
('Toyota Hiace',     12, 2),   -- id_vehicule = 1
('Toyota Coaster',   20, 2),   -- id_vehicule = 2
('Renault Trafic',    8, 1),   -- id_vehicule = 3
('Toyota Land Cruiser', 6, 2), -- id_vehicule = 4
('Nissan Urvan',     10, 2);   -- id_vehicule = 5

-- TOKEN
INSERT INTO token (niveau, date_expiration) VALUES
('admin',     '2026-12-31 23:59:59'),
('operateur', '2026-06-30 23:59:59'),
('lecteur',   '2026-03-31 23:59:59');

-- CLIENT
INSERT INTO client (nom_client) VALUES
('Rakoto Jean'),        -- id_client = 1
('Rabe Marie'),         -- id_client = 2
('Randria Paul'),       -- id_client = 3
('Rasoa Hanta'),        -- id_client = 4
('Ramiandrisoa Luc'),   -- id_client = 5
('Rajaonarison Fara'),  -- id_client = 6
('Andriamahefa Toky'),  -- id_client = 7
('Razafy Nirina');      -- id_client = 8

-- RESERVATION CLIENT
-- Groupe 1 : arrivées proches (± 30min), à regrouper potentiellement
INSERT INTO reservation_client (nb_passager, date_heure_arrivee, id_hotel, id_client, status) VALUES
(3, '2026-02-20 08:00:00', 2, 1, 'PENDING'),   -- Rakoto → Colbert       (groupe A)
(2, '2026-02-20 08:15:00', 3, 2, 'PENDING'),   -- Rabe → Ibis            (groupe A)
(4, '2026-02-20 08:20:00', 5, 3, 'PENDING'),   -- Randria → Sakamanga    (groupe A)
(2, '2026-02-20 10:00:00', 4, 4, 'PENDING'),   -- Rasoa → Carlton        (groupe B)
(5, '2026-02-20 10:10:00', 6, 5, 'PENDING'),   -- Ramiandrisoa → Glacier (groupe B)
(3, '2026-02-20 14:00:00', 2, 6, 'PENDING'),   -- Rajaonarison → Colbert (groupe C)
(1, '2026-02-20 14:25:00', 3, 7, 'PENDING'),   -- Andriamahefa → Ibis    (groupe C)
(6, '2026-02-20 16:00:00', 4, 8, 'PENDING');   -- Razafy → Carlton       (seul)
