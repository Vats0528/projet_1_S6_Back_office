package com.projet.repository;

import com.projet.config.DatabaseConnection;
import com.projet.model.Vehicule;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehiculeRepository {

    /**
     * INDISPENSABLE POUR LA SIMULATION :
     * Récupère les véhicules triés par la priorité du carburant.
     * (Ex: Electrique = 1, Hybride = 2, Essence = 3)
     */
    public List<Vehicule> findAllOrderByCarburant() throws SQLException {
        List<Vehicule> vehicules = new ArrayList<>();
        String query = """
            SELECT v.id_vehicule, v.model, v.nb_place, v.id_carburant, c.nom_carburant
                    FROM vehicule v
                    JOIN carburant c ON v.id_carburant = c.id_carburant
                    ORDER BY v.id_carburant ASC, v.nb_place DESC 
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                vehicules.add(mapResultSet(rs));
            }
        }
        return vehicules;
    }

    public List<Vehicule> findAll() throws SQLException {
        List<Vehicule> vehicules = new ArrayList<>();
        String query = """
            SELECT v.id_vehicule, v.model, v.nb_place, v.id_carburant, c.nom_carburant
            FROM vehicule v
            JOIN carburant c ON v.id_carburant = c.id_carburant
            ORDER BY v.model
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                vehicules.add(mapResultSet(rs));
            }
        }
        return vehicules;
    }

    // public Vehicule findById(int id) throws SQLException {
    //     String query = """
    //         SELECT v.id_vehicule, v.model, v.nb_place, v.id_carburant, c.nom_carburant
    //         FROM vehicule v
    //         JOIN carburant c ON v.id_carburant = c.id_carburant
    //         WHERE v.id_vehicule = ?
    //         """;
        
    //     try (Connection conn = DatabaseConnection.getConnection();
    //          PreparedStatement stmt = conn.prepareStatement(query)) {
    //         stmt.setInt(1, id);
    //         try (ResultSet rs = stmt.executeQuery()) {
    //             if (rs.next()) {
    //                 return mapResultSet(rs);
    //             }
    //         }
    //     }
    //     return null;
    // }

    public Vehicule save(Vehicule vehicule) throws SQLException {
        String query = "INSERT INTO vehicule (model, nb_place, id_carburant) VALUES (?, ?, ?) RETURNING id_vehicule";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, vehicule.getModel());
            stmt.setInt(2, vehicule.getNbPlace());
            stmt.setInt(3, vehicule.getIdCarburant());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    vehicule.setIdVehicule(rs.getInt(1));
                }
            }
        }
        return vehicule;
    }

    public void update(Vehicule vehicule) throws SQLException {
        String query = "UPDATE vehicule SET model = ?, nb_place = ?, id_carburant = ? WHERE id_vehicule = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, vehicule.getModel());
            stmt.setInt(2, vehicule.getNbPlace());
            stmt.setInt(3, vehicule.getIdCarburant());
            stmt.setInt(4, vehicule.getIdVehicule());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM vehicule WHERE id_vehicule = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Vehicule mapResultSet(ResultSet rs) throws SQLException {
        Vehicule v = new Vehicule();
        v.setIdVehicule(rs.getInt("id_vehicule"));
        v.setModel(rs.getString("model"));
        v.setNbPlace(rs.getInt("nb_place"));
        v.setIdCarburant(rs.getInt("id_carburant"));
        v.setNomCarburant(rs.getString("nom_carburant"));
        return v;
    }

    /**
     * Recherche les véhicules ayant une capacité suffisante.
     * Trie par consommation pour privilégier l'efficacité.
     */
    public List<Vehicule> findByCapacity(int places) throws SQLException {
        List<Vehicule> vehicules = new ArrayList<>();
        String query = """
            SELECT id_vehicule, immatriculation, modele, capacite, consommation 
            FROM vehicule 
            WHERE capacite >= ? 
            ORDER BY consommation ASC
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, places);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vehicules.add(mapResultSet(rs));
                }
            }
        }
        return vehicules;
    }

    /**
     * Recherche un véhicule spécifique par son ID.
     */
    public Vehicule findById(int id) throws SQLException {
        String query = "SELECT * FROM vehicule WHERE id_vehicule = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        }
        return null;
    }
    public List<Vehicule> findAvailableVehicules(Timestamp debutBesoin, Timestamp finBesoin) throws SQLException {
        List<Vehicule> libres = new ArrayList<>();
        String query = """
            SELECT v.*, c.nom_carburant 
            FROM vehicule v
            JOIN carburant c ON v.id_carburant = c.id_carburant
            WHERE v.id_vehicule NOT IN (
                SELECT rv.id_vehicule 
                FROM reservation_vehicule rv
                WHERE (rv.date_heure_depart < ? AND rv.date_heure_retour > ?)
            )
            """;

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setTimestamp(1, finBesoin);   // Temps 2
            stmt.setTimestamp(2, debutBesoin); // Temps 1
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    libres.add(mapResultSet(rs));
                }
            }
        }
        return libres;
    }
    
    public List<Vehicule> findAvailableWithTA(Timestamp debutBesoin, Timestamp finBesoin, int taMinutes) throws SQLException {
        List<Vehicule> libres = new ArrayList<>();
        
        // On utilise INTERVAL pour décaler le début du besoin de 'taMinutes'
        String query = """
            SELECT v.*, c.nom_carburant 
            FROM vehicule v
            JOIN carburant c ON v.id_carburant = c.id_carburant
            WHERE v.id_vehicule NOT IN (
                SELECT rv.id_vehicule 
                FROM reservation_vehicule rv
                WHERE rv.date_heure_depart < ? 
                AND rv.date_heure_retour > (? + CAST(? || ' minutes' AS INTERVAL))
            )
            """;

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setTimestamp(1, finBesoin);   // Temps 2
            stmt.setTimestamp(2, debutBesoin); // Temps 1
            stmt.setInt(3, taMinutes);         // La marge TA
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    libres.add(mapResultSet(rs));
                }
            }
        }
        return libres;
    }

    /**
     * Récupère le Temps d'Attente (TA) défini dans les paramètres.
     * @return le nombre de minutes (int)
     */
    public int getTempsAttente() throws SQLException {
        int ta = 0;
        String query = "SELECT ta_s FROM param_vehicule LIMIT 1";

        try (Connection conn = com.projet.config.DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next()) {
                ta = rs.getInt("ta_s");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du TA : " + e.getMessage());
            throw e;
        }
        return ta;
    }

    /**
     * Récupère les statistiques d'utilisation des véhicules pour un jour donné.
     * @param date au format "YYYY-MM-DD"
     * @return Une Map (ID_VEHICULE => NOMBRE_DE_TRAJETS)
     */
    public Map<Integer, Integer> getStatsTrajetsParJour(String date) throws SQLException {
        Map<Integer, Integer> stats = new HashMap<>();
        
        String query = """
            SELECT id_vehicule, COUNT(*) as nb_trajets
            FROM reservation_vehicule
            WHERE DATE(date_heure_depart) = ?::date
            GROUP BY id_vehicule
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, date);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idVehicule = rs.getInt("id_vehicule");
                    int nbTrajets = rs.getInt("nb_trajets");
                    stats.put(idVehicule, nbTrajets);
                }
            }
        }
        return stats;
    }

    
}