package com.projet.repository;

import com.projet.config.DatabaseConnection;
import com.projet.model.Carburant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarburantRepository {

    public List<Carburant> findAll() throws SQLException {
        List<Carburant> carburants = new ArrayList<>();
        String query = "SELECT id_carburant, nom_carburant FROM carburant ORDER BY nom_carburant";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Carburant carburant = new Carburant();
            carburant.setIdCarburant(rs.getInt("id_carburant"));
            carburant.setNomCarburant(rs.getString("nom_carburant"));
            carburants.add(carburant);
        }
        
        rs.close();
        stmt.close();
        
        return carburants;
    }

    public Carburant findById(int id) throws SQLException {
        String query = "SELECT id_carburant, nom_carburant FROM carburant WHERE id_carburant = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        
        Carburant carburant = null;
        if (rs.next()) {
            carburant = new Carburant();
            carburant.setIdCarburant(rs.getInt("id_carburant"));
            carburant.setNomCarburant(rs.getString("nom_carburant"));
        }
        
        rs.close();
        stmt.close();
        
        return carburant;
    }

    public Carburant save(Carburant carburant) throws SQLException {
        String query = "INSERT INTO carburant (nom_carburant) VALUES (?) RETURNING id_carburant";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, carburant.getNomCarburant());
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            carburant.setIdCarburant(rs.getInt("id_carburant"));
        }
        
        rs.close();
        stmt.close();
        
        return carburant;
    }

    public Carburant update(Carburant carburant) throws SQLException {
        String query = "UPDATE carburant SET nom_carburant = ? WHERE id_carburant = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, carburant.getNomCarburant());
        stmt.setInt(2, carburant.getIdCarburant());
        stmt.executeUpdate();
        stmt.close();
        
        return carburant;
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM carburant WHERE id_carburant = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        stmt.executeUpdate();
        stmt.close();
    }
}

