package com.projet.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.projet.config.DatabaseConnection;
import com.projet.dto.MissionAffichageDTO;
import com.projet.model.Lieu;
import com.projet.model.Reservation;
import com.projet.model.Vehicule;
import com.projet.service.ServiceVehicule.VehiculeMission;

public class ReservationVehiculeRepository {

    public List<Lieu> getTrajetVehicule(int idReservationVehicule) throws SQLException {
        List<Lieu> itineraire = new ArrayList<>();
        // Cette requête récupère :
        // 1. Le départ du premier segment (succession = 1)
        // 2. Puis toutes les destinations (id_lieu_1) dans l'ordre
        String query = """
            SELECT l.id_lieu, l.code, l.libelle, dt.succession
            FROM details_trajet dt
            JOIN distance d ON dt.id_distance = d.id_distance
            JOIN lieu l ON (
                (dt.succession = 1 AND l.id_lieu = d.id_lieu) -- Départ du 1er trajet
                OR 
                l.id_lieu = d.id_lieu_1 -- Destinations de tous les segments
            )
            WHERE dt.id_reservation_vehicule = ?
            ORDER BY dt.succession ASC, (l.id_lieu = d.id_lieu_1) ASC
            """;

        try (Connection conn = com.projet.config.DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, idReservationVehicule);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    itineraire.add(new Lieu(
                        rs.getInt("id_lieu"),
                        rs.getString("code"),
                        rs.getString("libelle")
                    ));
                }
            }
        }
        return itineraire;
    }

    /**
     * Calcule la durée totale d'une mission en minutes.
     * Somme des temps de chaque segment du trajet.
     */
    public int getDureeTotaleMissionT(int idReservationVehicule) throws SQLException {
        int dureeTotale = 0;
        String query = """
            SELECT SUM(d.temps) as total 
            FROM details_trajet dt
            JOIN distance d ON dt.id_distance = d.id_distance
            WHERE dt.id_reservation_vehicule = ?
            """;

        try (Connection conn = com.projet.config.DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, idReservationVehicule);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    dureeTotale = rs.getInt("total");
                }
            }
        }
        return dureeTotale;
    }

        /**
     * Calcule la durée théorique en minutes basée sur la distance 
     * et la vitesse moyenne définie dans param_vehicule.
     */
    public int getDureeTheorique(int idReservationVehicule) throws SQLException {
        double minutesTotale = 0;
        
        String query = """
            SELECT 
                SUM(d.distance) as total_km,
                (SELECT vitess_moyenne FROM param_vehicule LIMIT 1) as v_moyenne
            FROM details_trajet dt
            JOIN distance d ON dt.id_distance = d.id_distance
            WHERE dt.id_reservation_vehicule = ?
            """;

        try (Connection conn = com.projet.config.DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, idReservationVehicule);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double km = rs.getDouble("total_km");
                    int vitesse = rs.getInt("v_moyenne");
                    
                    if (vitesse > 0) {
                        // (Distance / Vitesse) donne des heures. * 60 donne des minutes.
                        minutesTotale = (km / vitesse) * 60;
                    }
                }
            }
        }
        return (int) Math.round(minutesTotale);
    }

    /**
     * Enregistre une mission complète (Véhicule + Liste de réservations clients)
     * Utilise une transaction SQL pour garantir l'intégrité des données.
     */
    public void saveMission(VehiculeMission mission, String dateDepart, String dateRetour) throws SQLException {
        String sqlMission = """
            INSERT INTO reservation_vehicule (date_heure_depart, date_heure_retour, id_vehicule, titre) 
            VALUES (?::timestamp, ?::timestamp, ?, ?) RETURNING id_reservation_vehicule
            """;
        
        String sqlLiaison = "INSERT INTO details_reservation_client (id_reservation_client, id_reservation_vehicule) VALUES (?, ?)";
        
        String sqlUpdateStatus = "UPDATE reservation_client SET status = 'CONFIRME' WHERE id_reservation_client = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // Début de la transaction
            
            try {
                // 1. Insertion de la mission (La tête de la réservation véhicule)
                int idMission;
                try (PreparedStatement stmt = conn.prepareStatement(sqlMission)) {
                    stmt.setString(1, dateDepart);
                    stmt.setString(2, dateRetour);
                    stmt.setInt(3, mission.vehicule.getIdVehicule());
                    stmt.setString(4, "Transfert Aéroport - " + mission.vehicule.getModel());
                    
                    ResultSet rs = stmt.executeQuery();
                    rs.next();
                    idMission = rs.getInt(1);
                }

                // 2. Pour chaque réservation dans ce véhicule
                for (Reservation res : mission.reservationsAssocies) {
                    // Créer le lien dans la table de détails
                    try (PreparedStatement stmt = conn.prepareStatement(sqlLiaison)) {
                        stmt.setInt(1, res.getIdReservation());
                        stmt.setInt(2, idMission);
                        stmt.executeUpdate();
                    }
                    
                    // Mettre à jour le statut du client pour qu'il ne soit plus "EN_ATTENTE"
                    try (PreparedStatement stmt = conn.prepareStatement(sqlUpdateStatus)) {
                        stmt.setInt(1, res.getIdReservation());
                        stmt.executeUpdate();
                    }
                }

                conn.commit(); // Valider toutes les étapes
            } catch (SQLException e) {
                conn.rollback(); // Annuler tout si une erreur survient
                throw e;
            }
        }
    }

    /**
     * Optionnel : Pour voir les missions créées (CRUD)
     */
    public void deleteMission(int idMission) throws SQLException {
        String query = "DELETE FROM reservation_vehicule WHERE id_reservation_vehicule = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idMission);
            stmt.executeUpdate();
        }
    }

    public List<MissionAffichageDTO> getMissionsParDate(String date) throws SQLException {
    List<MissionAffichageDTO> missions = new ArrayList<>();
    String query = """
        SELECT rv.*, v.model, v.nb_place, v.id_carburant 
        FROM reservation_vehicule rv
        JOIN vehicule v ON rv.id_vehicule = v.id_vehicule
        WHERE DATE(rv.date_heure_depart) = ?::date
        ORDER BY rv.date_heure_depart ASC
        """;

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        
        stmt.setString(1, date);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Vehicule v = new Vehicule();
            v.setIdVehicule(rs.getInt("id_vehicule"));
            v.setModel(rs.getString("model"));
            v.setNbPlace(rs.getInt("nb_place"));

            MissionAffichageDTO dto = new MissionAffichageDTO(
                rs.getInt("id_reservation_vehicule"),
                v,
                rs.getTimestamp("date_heure_depart").toString(),
                rs.getTimestamp("date_heure_retour").toString(),
                rs.getString("titre")
            );

            // Charger les passagers pour cette mission spécifique
            dto.passagers = findPassagersPourMission(dto.idMission, conn);
            missions.add(dto);
        }
    }
    return missions;
}

private List<Reservation> findPassagersPourMission(int idMission, Connection conn) throws SQLException {
    List<Reservation> liste = new ArrayList<>();
    
    // CORRECTION : On joint la table client pour avoir le nom_client
    String sql = """
        SELECT r.*, c.nom_client 
        FROM reservation_client r
        JOIN client c ON r.id_client = c.id_client
        JOIN details_reservation_client d ON r.id_reservation_client = d.id_reservation_client
        WHERE d.id_reservation_vehicule = ?
        """;

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, idMission);
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Reservation r = new Reservation();
                r.setIdReservation(rs.getInt("id_reservation_client"));
                r.setNbPassager(rs.getInt("nb_passager"));
                
                // Maintenant cette ligne fonctionnera car nom_client est dans le SELECT
                r.setNomClient(rs.getString("nom_client")); 
                
                liste.add(r);
            }
        }
    }
    return liste;
}

    

}
