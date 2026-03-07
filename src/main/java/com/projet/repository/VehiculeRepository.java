package com.projet.repository;

import com.projet.config.DatabaseConnection;
import com.projet.model.Vehicule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculeRepository {

    public List<Vehicule> findAll() throws SQLException {
        List<Vehicule> vehicules = new ArrayList<>();
        String query = """
            SELECT v.id_vehicule, v.model, v.nb_place, v.id_carburant, c.nom_carburant
            FROM vehicule v
            JOIN carburant c ON v.id_carburant = c.id_carburant
            ORDER BY v.model
            """;
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Vehicule vehicule = mapResultSet(rs);
            vehicules.add(vehicule);
        }
        
        rs.close();
        stmt.close();
        
        return vehicules;
    }

    public Vehicule findById(int id) throws SQLException {
        String query = """
            SELECT v.id_vehicule, v.model, v.nb_place, v.id_carburant, c.nom_carburant
            FROM vehicule v
            JOIN carburant c ON v.id_carburant = c.id_carburant
            WHERE v.id_vehicule = ?
            """;
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        
        Vehicule vehicule = null;
        if (rs.next()) {
            vehicule = mapResultSet(rs);
        }
        
        rs.close();
        stmt.close();
        
        return vehicule;
    }

    public List<Vehicule> findByCapacity(int minPlaces) throws SQLException {
        List<Vehicule> vehicules = new ArrayList<>();
        String query = """
            SELECT v.id_vehicule, v.model, v.nb_place, v.id_carburant, c.nom_carburant
            FROM vehicule v
            JOIN carburant c ON v.id_carburant = c.id_carburant
            WHERE v.nb_place >= ?
            ORDER BY v.nb_place
            """;
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, minPlaces);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Vehicule vehicule = mapResultSet(rs);
            vehicules.add(vehicule);
        }
        
        rs.close();
        stmt.close();
        
        return vehicules;
    }

    public Vehicule save(Vehicule vehicule) throws SQLException {
        String query = """
            INSERT INTO vehicule (model, nb_place, id_carburant) 
            VALUES (?, ?, ?) RETURNING id_vehicule
            """;
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, vehicule.getModel());
        stmt.setInt(2, vehicule.getNbPlace());
        stmt.setInt(3, vehicule.getIdCarburant());
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            vehicule.setIdVehicule(rs.getInt("id_vehicule"));
        }
        
        rs.close();
        stmt.close();
        
        return vehicule;
    }

    public Vehicule update(Vehicule vehicule) throws SQLException {
        String query = """
            UPDATE vehicule 
            SET model = ?, nb_place = ?, id_carburant = ?
            WHERE id_vehicule = ?
            """;
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, vehicule.getModel());
        stmt.setInt(2, vehicule.getNbPlace());
        stmt.setInt(3, vehicule.getIdCarburant());
        stmt.setInt(4, vehicule.getIdVehicule());
        stmt.executeUpdate();
        stmt.close();
        
        return vehicule;
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM vehicule WHERE id_vehicule = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        stmt.executeUpdate();
        stmt.close();
    }

    private Vehicule mapResultSet(ResultSet rs) throws SQLException {
        Vehicule vehicule = new Vehicule();
        vehicule.setIdVehicule(rs.getInt("id_vehicule"));
        vehicule.setModel(rs.getString("model"));
        vehicule.setNbPlace(rs.getInt("nb_place"));
        vehicule.setIdCarburant(rs.getInt("id_carburant"));
        vehicule.setNomCarburant(rs.getString("nom_carburant"));
        return vehicule;
    }
}

