package com.projet.repository;

import com.projet.config.DatabaseConnection;
import com.projet.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientRepository {

    public List<Client> findAll() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT id_client, nom_client FROM client ORDER BY nom_client";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Client client = new Client();
            client.setIdClient(rs.getInt("id_client"));
            client.setNomClient(rs.getString("nom_client"));
            clients.add(client);
        }
        
        rs.close();
        stmt.close();
        
        return clients;
    }

    public Client findById(int id) throws SQLException {
        String query = "SELECT id_client, nom_client FROM client WHERE id_client = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        
        Client client = null;
        if (rs.next()) {
            client = new Client();
            client.setIdClient(rs.getInt("id_client"));
            client.setNomClient(rs.getString("nom_client"));
        }
        
        rs.close();
        stmt.close();
        
        return client;
    }

    public Client save(Client client) throws SQLException {
        String query = "INSERT INTO client (nom_client) VALUES (?) RETURNING id_client";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, client.getNomClient());
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            client.setIdClient(rs.getInt("id_client"));
        }
        
        rs.close();
        stmt.close();
        
        return client;
    }
}
