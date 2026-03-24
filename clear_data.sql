-- =============================================
-- SCRIPT : Effacer les données de toutes les tables
-- Sprint 3
-- =============================================

-- Désactiver les contraintes FK temporairement
SET session_replication_role = replica;

-- Tables de jointure en premier
TRUNCATE TABLE details_trajet RESTART IDENTITY CASCADE;
TRUNCATE TABLE details_reservation_client RESTART IDENTITY CASCADE;

-- Tables de réservation
TRUNCATE TABLE reservation_vehicule RESTART IDENTITY CASCADE;
TRUNCATE TABLE reservation_client RESTART IDENTITY CASCADE;

-- Tables principales
TRUNCATE TABLE vehicule RESTART IDENTITY CASCADE;
TRUNCATE TABLE distance RESTART IDENTITY CASCADE;
TRUNCATE TABLE param_vehicule RESTART IDENTITY CASCADE;
TRUNCATE TABLE token RESTART IDENTITY CASCADE;
TRUNCATE TABLE lieu RESTART IDENTITY CASCADE;
TRUNCATE TABLE hotel RESTART IDENTITY CASCADE;
TRUNCATE TABLE client RESTART IDENTITY CASCADE;
TRUNCATE TABLE carburant RESTART IDENTITY CASCADE;

-- Réactiver les contraintes FK
SET session_replication_role = DEFAULT;
