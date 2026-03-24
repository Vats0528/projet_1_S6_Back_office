package com.projet.repository;

import com.projet.config.DatabaseConnection;
import com.projet.model.ParamVehicule;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParamVehiculeRepository {

    /**
     * Utilisée par le SimulationService pour obtenir les réglages de simulation.
     * J'ai renommé getGlobal en findFirst pour correspondre au service.
     */
    public ParamVehicule findFirst() throws SQLException {
        String query = "SELECT id_param_vehicule, vitess_moyenne, temps_attente FROM param_vehicule LIMIT 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return mapResultSet(rs);
            }
        }
        return null;
    }

    public List<ParamVehicule> findAll() throws SQLException {
        List<ParamVehicule> params = new ArrayList<>();
        String query = "SELECT * FROM param_vehicule";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                params.add(mapResultSet(rs));
            }
        }
        return params;
    }

    public void update(ParamVehicule param) throws SQLException {
        String query = """
            UPDATE param_vehicule 
            SET vitess_moyenne = ?, temps_attente = ?
            WHERE id_param_vehicule = ?
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, param.getVitessMoyenne());
            stmt.setInt(2, param.getTempsAttente());
            stmt.setInt(3, param.getIdParamVehicule());
            stmt.executeUpdate();
        }
    }

    // Extraction de la logique de mapping pour éviter la répétition
    private ParamVehicule mapResultSet(ResultSet rs) throws SQLException {
        ParamVehicule param = new ParamVehicule();
        param.setIdParamVehicule(rs.getInt("id_param_vehicule"));
        param.setVitessMoyenne(rs.getInt("vitess_moyenne"));
        param.setTempsAttente(rs.getInt("temps_attente"));
        return param;
    }
}