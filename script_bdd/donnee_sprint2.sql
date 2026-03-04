INSERT INTO client (nom_client) VALUES
('Hutao'),
('Martin'),
('Rakoto'),
('Smith'),
('Bernard');


INSERT INTO lieu (code, libelle) VALUES
('PAR', 'Paris'),
('LYO', 'Lyon'),
('MAR', 'Marseille'),
('TNR', 'Antananarivo'),
('NYC', 'New York');


INSERT INTO hotel (nom_hotel, id_lieu) VALUES
('Hotel Central', 1),
('Grand Lyon', 2),
('Soleil Marseille', 3),
('Palace Tana', 4),
('NY Comfort', 5);

INSERT INTO reservation_client 
(nb_passager, date_heure_arrivee, id_hotel, id_client) VALUES
(2, '2026-03-10 14:00:00', 1, 1),
(1, '2026-03-12 18:30:00', 2, 2),
(3, '2026-04-01 09:00:00', 4, 3),
(4, '2026-04-15 16:45:00', 5, 4),
(2, '2026-05-20 12:00:00', 3, 5);

INSERT INTO token 
(niveau, valeur_token, date_creation, date_expiration, est_actif) VALUES
('ADMIN', 'abc123xyz', NOW(), NOW() + INTERVAL '30 days', true),
('USER', 'user456token', NOW(), NOW() + INTERVAL '7 days', true),
('USER', 'expired789', NOW() - INTERVAL '10 days', NOW() - INTERVAL '1 day', false);