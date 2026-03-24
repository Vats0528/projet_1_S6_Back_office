package com.projet.repository;

import com.projet.config.DatabaseConnection;
import com.projet.model.Lieu;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LieuRepository {

    public List<Lieu> findAll() throws SQLException {
        List<Lieu> lieux = new ArrayList<>();
        String query = "SELECT id_lieu, code, libelle FROM lieu ORDER BY libelle";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Lieu lieu = new Lieu();
            lieu.setIdLieu(rs.getInt("id_lieu"));
            lieu.setCode(rs.getString("code"));
            lieu.setLibelle(rs.getString("libelle"));
            lieux.add(lieu);
        }
        
        rs.close();
        stmt.close();
        
        return lieux;
    }

    public Lieu findById(int id) throws SQLException {
        String query = "SELECT id_lieu, code, libelle FROM lieu WHERE id_lieu = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        
        Lieu lieu = null;
        if (rs.next()) {
            lieu = new Lieu();
            lieu.setIdLieu(rs.getInt("id_lieu"));
            lieu.setCode(rs.getString("code"));
            lieu.setLibelle(rs.getString("libelle"));
        }
        
        rs.close();
        stmt.close();
        
        return lieu;
    }

    public Lieu save(Lieu lieu) throws SQLException {
        String query = "INSERT INTO lieu (code, libelle) VALUES (?, ?) RETURNING id_lieu";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, lieu.getCode());
        stmt.setString(2, lieu.getLibelle());
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            lieu.setIdLieu(rs.getInt("id_lieu"));
        }
        
        rs.close();
        stmt.close();
        
        return lieu;
    }

    public Lieu update(Lieu lieu) throws SQLException {
        String query = "UPDATE lieu SET code = ?, libelle = ? WHERE id_lieu = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, lieu.getCode());
        stmt.setString(2, lieu.getLibelle());
        stmt.setInt(3, lieu.getIdLieu());
        stmt.executeUpdate();
        stmt.close();
        
        return lieu;
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM lieu WHERE id_lieu = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        stmt.executeUpdate();
        stmt.close();
    }
}

