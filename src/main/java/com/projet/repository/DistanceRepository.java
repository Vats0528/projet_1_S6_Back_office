package com.projet.repository;

import com.projet.config.DatabaseConnection;
import com.projet.model.Distance;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DistanceRepository {

    public List<Distance> findAll() throws SQLException {
        List<Distance> distances = new ArrayList<>();
        String query = """
            SELECT d.id_distance, d.temps, d.distance, d.id_lieu, d.id_lieu_1,
                   l1.libelle as libelle_lieu, l2.libelle as libelle_lieu_1
            FROM distance d
            JOIN lieu l1 ON d.id_lieu = l1.id_lieu
            JOIN lieu l2 ON d.id_lieu_1 = l2.id_lieu
            ORDER BY d.distance
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                distances.add(mapResultSet(rs));
            }
        }
        return distances;
    }

    public Distance findById(int id) throws SQLException {
        String query = """
            SELECT d.id_distance, d.temps, d.distance, d.id_lieu, d.id_lieu_1,
                   l1.libelle as libelle_lieu, l2.libelle as libelle_lieu_1
            FROM distance d
            JOIN lieu l1 ON d.id_lieu = l1.id_lieu
            JOIN lieu l2 ON d.id_lieu_1 = l2.id_lieu
            WHERE d.id_distance = ?
            """;
        
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

    public Distance save(Distance distance) throws SQLException {
        String query = """
            INSERT INTO distance (temps, distance, id_lieu, id_lieu_1) 
            VALUES (?, ?, ?, ?) RETURNING id_distance
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, distance.getTemps()); // INTEGER en SQL
            stmt.setDouble(2, distance.getDistance());
            stmt.setInt(3, distance.getIdLieu());
            stmt.setInt(4, distance.getIdLieu1());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    distance.setIdDistance(rs.getInt(1));
                }
            }
        }
        return distance;
    }

    public Distance update(Distance distance) throws SQLException {
        String query = """
            UPDATE distance 
            SET temps = ?, distance = ?, id_lieu = ?, id_lieu_1 = ?
            WHERE id_distance = ?
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, distance.getTemps()); // INTEGER en SQL
            stmt.setDouble(2, distance.getDistance());
            stmt.setInt(3, distance.getIdLieu());
            stmt.setInt(4, distance.getIdLieu1());
            stmt.setInt(5, distance.getIdDistance());
            
            stmt.executeUpdate();
        }
        return distance;
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM distance WHERE id_distance = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Distance mapResultSet(ResultSet rs) throws SQLException {
        Distance d = new Distance();
        d.setIdDistance(rs.getInt("id_distance"));
        d.setTemps(rs.getInt("temps")); // Récupère le INTEGER du SQL
        d.setDistance(rs.getDouble("distance"));
        d.setIdLieu(rs.getInt("id_lieu"));
        d.setIdLieu1(rs.getInt("id_lieu_1"));
        d.setLibelleLieu(rs.getString("libelle_lieu"));
        d.setLibelleLieu1(rs.getString("libelle_lieu_1"));
        return d;
    }

    public List<Distance> findByLieux(int idLieu1, int idLieu2) throws SQLException {
        List<Distance> distances = new ArrayList<>();
        String query = """
            SELECT d.id_distance, d.temps, d.distance, d.id_lieu, d.id_lieu_1,
                   l1.libelle as libelle_lieu, l2.libelle as libelle_lieu_1
            FROM distance d
            JOIN lieu l1 ON d.id_lieu = l1.id_lieu
            JOIN lieu l2 ON d.id_lieu_1 = l2.id_lieu
            WHERE (d.id_lieu = ? AND d.id_lieu_1 = ?) 
               OR (d.id_lieu = ? AND d.id_lieu_1 = ?)
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idLieu1);
            stmt.setInt(2, idLieu2);
            stmt.setInt(3, idLieu2);
            stmt.setInt(4, idLieu1);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    distances.add(mapResultSet(rs)); // mapResultSet gère déjà le int temps
                }
            }
        }
        return distances;
    }
}