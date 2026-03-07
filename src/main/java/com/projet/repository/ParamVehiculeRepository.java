package com.projet.repository;

import com.projet.config.DatabaseConnection;
import com.projet.model.ParamVehicule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParamVehiculeRepository {

    public List<ParamVehicule> findAll() throws SQLException {
        List<ParamVehicule> params = new ArrayList<>();
        String query = "SELECT id_param_vehicule, vitess_moyenne, temps_attente FROM param_vehicule";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            ParamVehicule param = new ParamVehicule();
            param.setIdParamVehicule(rs.getInt("id_param_vehicule"));
            param.setVitessMoyenne(rs.getInt("vitess_moyenne"));
            param.setTempsAttente(rs.getInt("temps_attente"));
            params.add(param);
        }
        
        rs.close();
        stmt.close();
        
        return params;
    }

    public ParamVehicule findById(int id) throws SQLException {
        String query = "SELECT id_param_vehicule, vitess_moyenne, temps_attente FROM param_vehicule WHERE id_param_vehicule = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        
        ParamVehicule param = null;
        if (rs.next()) {
            param = new ParamVehicule();
            param.setIdParamVehicule(rs.getInt("id_param_vehicule"));
            param.setVitessMoyenne(rs.getInt("vitess_moyenne"));
            param.setTempsAttente(rs.getInt("temps_attente"));
        }
        
        rs.close();
        stmt.close();
        
        return param;
    }

    public ParamVehicule getGlobal() throws SQLException {
        String query = "SELECT id_param_vehicule, vitess_moyenne, temps_attente FROM param_vehicule LIMIT 1";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        
        ParamVehicule param = null;
        if (rs.next()) {
            param = new ParamVehicule();
            param.setIdParamVehicule(rs.getInt("id_param_vehicule"));
            param.setVitessMoyenne(rs.getInt("vitess_moyenne"));
            param.setTempsAttente(rs.getInt("temps_attente"));
        }
        
        rs.close();
        stmt.close();
        
        return param;
    }

    public ParamVehicule save(ParamVehicule param) throws SQLException {
        String query = """
            INSERT INTO param_vehicule (vitess_moyenne, temps_attente) 
            VALUES (?, ?) RETURNING id_param_vehicule
            """;
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, param.getVitessMoyenne());
        stmt.setInt(2, param.getTempsAttente());
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            param.setIdParamVehicule(rs.getInt("id_param_vehicule"));
        }
        
        rs.close();
        stmt.close();
        
        return param;
    }

    public ParamVehicule update(ParamVehicule param) throws SQLException {
        String query = """
            UPDATE param_vehicule 
            SET vitess_moyenne = ?, temps_attente = ?
            WHERE id_param_vehicule = ?
            """;
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, param.getVitessMoyenne());
        stmt.setInt(2, param.getTempsAttente());
        stmt.setInt(3, param.getIdParamVehicule());
        stmt.executeUpdate();
        stmt.close();
        
        return param;
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM param_vehicule WHERE id_param_vehicule = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        stmt.executeUpdate();
        stmt.close();
    }
}

