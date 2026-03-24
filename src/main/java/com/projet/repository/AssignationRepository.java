package com.projet.repository;

import com.projet.config.DatabaseConnection;
import com.projet.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssignationRepository {

    /**
     * Enregistre une mission et lie les passagers dans une transaction.
     */
    public void confirmerAssignation(ReservationVehicule mission) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            // 1. Insérer la mission (Utilisation de Timestamp.valueOf pour convertir nos String Java)
            String queryMission = """
                INSERT INTO reservation_vehicule (date_heure_depart, date_heure_retour, id_vehicule)
                VALUES (?::timestamp, ?::timestamp, ?) RETURNING id_reservation_vehicule
                """;
            
            int idMission;
            try (PreparedStatement stmt = conn.prepareStatement(queryMission)) {
                stmt.setString(1, mission.getDateHeureDepart());
                stmt.setString(2, mission.getDateHeureRetour());
                stmt.setInt(3, mission.getIdVehicule());
                
                ResultSet rs = stmt.executeQuery();
                rs.next();
                idMission = rs.getInt(1);
            }

            // 2. Lier chaque réservation client et mettre à jour le status
            String queryLien = "INSERT INTO details_reservation_client (id_reservation_client, id_reservation_vehicule) VALUES (?, ?)";
            String queryUpdateStatus = "UPDATE reservation_client SET status = 'CONFIRME' WHERE id_reservation_client = ?";

            try (PreparedStatement stmtLien = conn.prepareStatement(queryLien);
                 PreparedStatement stmtUpdate = conn.prepareStatement(queryUpdateStatus)) {
                
                // Note : On utilise getPassagers() conformément à ton modèle ReservationVehicule
                for (Reservation res : mission.getPassagers()) {
                    stmtLien.setInt(1, res.getIdReservation());
                    stmtLien.setInt(2, idMission);
                    stmtLien.addBatch();

                    stmtUpdate.setInt(1, res.getIdReservation());
                    stmtUpdate.addBatch();
                }
                stmtLien.executeBatch();
                stmtUpdate.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    /**
     * Récupère les missions d'une date.
     */
    public List<ReservationVehicule> findAssignationsByDate(String date) throws SQLException {
        List<ReservationVehicule> missions = new ArrayList<>();
        String query = """
            SELECT rv.*, v.model as model_vehicule, v.nb_place as nb_place_vehicule, c.nom_carburant
            FROM reservation_vehicule rv
            JOIN vehicule v ON rv.id_vehicule = v.id_vehicule
            JOIN carburant c ON v.id_carburant = c.id_carburant
            WHERE DATE(rv.date_heure_depart) = ?::date
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, date);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ReservationVehicule mission = new ReservationVehicule();
                    mission.setIdReservationVehicule(rs.getInt("id_reservation_vehicule"));
                    
                    // Utilise les setters "bridge" de ton modèle (Timestamp -> String)
                    mission.setDateHeureDepart(rs.getTimestamp("date_heure_depart"));
                    mission.setDateHeureRetour(rs.getTimestamp("date_heure_retour"));
                    mission.setIdVehicule(rs.getInt("id_vehicule"));
                    
                    // Champs de jointure directs
                    mission.setModelVehicule(rs.getString("model_vehicule"));
                    mission.setNbPlaceVehicule(rs.getInt("nb_place_vehicule"));
                    mission.setNomCarburant(rs.getString("nom_carburant"));

                    // Récupérer les passagers pour cette mission
                    mission.setPassagers(findPassengersForMission(mission.getIdReservationVehicule(), conn));
                    missions.add(mission);
                }
            }
        }
        return missions;
    }

    private List<Reservation> findPassengersForMission(int idMission, Connection conn) throws SQLException {
        List<Reservation> passagers = new ArrayList<>();
        String query = """
            SELECT r.*, c.nom_client, l.libelle as libelle_lieu, h.nom_hotel
            FROM details_reservation_client drc
            JOIN reservation_client r ON drc.id_reservation_client = r.id_reservation_client
            JOIN hotel h ON r.id_hotel = h.id_hotel 
            JOIN client c ON r.id_client = c.id_client
            JOIN lieu l ON h.id_lieu = l.id_lieu
            WHERE drc.id_reservation_vehicule = ?
            """;
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idMission);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Reservation res = new Reservation();
                    res.setIdReservation(rs.getInt("id_reservation_client"));
                    res.setNbPassager(rs.getInt("nb_passager"));
                    res.setStatus(rs.getString("status"));
                    res.setDateHeureArrivee(rs.getTimestamp("date_heure_arrivee"));
                    res.setNomClient(rs.getString("nom_client"));
                    res.setLibelleLieu(rs.getString("libelle_lieu"));
                    res.setNomHotel(rs.getString("nom_hotel"));
                    
                    passagers.add(res);
                }
            }

            
        }
        return passagers;
    }
    /**
 * Annule une mission : Libère les passagers et supprime l'assignation.
 * @param idMission L'ID de la reservation_vehicule à supprimer
 */
public void annulerAssignation(int idMission) throws SQLException {
    Connection conn = DatabaseConnection.getConnection();
    try {
        conn.setAutoCommit(false); // Transaction pour garantir l'intégrité

        // 1. Remettre le statut des clients liés à cette mission en 'EN_ATTENTE'
        String queryLiberePassagers = """
            UPDATE reservation_client 
            SET status = 'EN_ATTENTE' 
            WHERE id_reservation_client IN (
                SELECT id_reservation_client FROM details_reservation_client 
                WHERE id_reservation_vehicule = ?
            )
            """;
        
        try (PreparedStatement stmt1 = conn.prepareStatement(queryLiberePassagers)) {
            stmt1.setInt(1, idMission);
            stmt1.executeUpdate();
        }

        // 2. Supprimer les liens dans la table de détails (Clé étrangère)
        String queryDeleteDetails = "DELETE FROM details_reservation_client WHERE id_reservation_vehicule = ?";
        try (PreparedStatement stmt2 = conn.prepareStatement(queryDeleteDetails)) {
            stmt2.setInt(1, idMission);
            stmt2.executeUpdate();
        }

        // 3. Enfin, supprimer la mission (le trajet du véhicule)
        String queryDeleteMission = "DELETE FROM reservation_vehicule WHERE id_reservation_vehicule = ?";
        try (PreparedStatement stmt3 = conn.prepareStatement(queryDeleteMission)) {
            stmt3.setInt(1, idMission);
            stmt3.executeUpdate();
        }

        conn.commit();
    } catch (SQLException e) {
        conn.rollback();
        throw e;
    } finally {
        conn.setAutoCommit(true);
    }
}
}