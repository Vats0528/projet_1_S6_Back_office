package com.projet.repository;

import com.projet.config.DatabaseConnection;
import com.projet.model.Hotel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelRepository {

    public List<Hotel> findAll() throws SQLException {
        List<Hotel> hotels = new ArrayList<>();
        String query = """
            SELECT h.id_hotel, h.nom_hotel, h.id_lieu, l.libelle as libelle_lieu 
            FROM hotel h
            JOIN lieu l ON h.id_lieu = l.id_lieu
            ORDER BY h.nom_hotel
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                hotels.add(mapResultSet(rs));
            }
        }
        return hotels;
    }

    public Hotel findById(int id) throws SQLException {
        String query = """
            SELECT h.id_hotel, h.nom_hotel, h.id_lieu, l.libelle as libelle_lieu 
            FROM hotel h
            JOIN lieu l ON h.id_lieu = l.id_lieu
            WHERE h.id_hotel = ?
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

    public Hotel save(Hotel hotel) throws SQLException {
        String query = "INSERT INTO hotel (nom_hotel, id_lieu) VALUES (?, ?) RETURNING id_hotel";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, hotel.getNomHotel());
            stmt.setInt(2, hotel.getIdLieu());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    hotel.setIdHotel(rs.getInt("id_hotel"));
                }
            }
        }
        return hotel;
    }

    public Hotel update(Hotel hotel) throws SQLException {
        String query = "UPDATE hotel SET nom_hotel = ?, id_lieu = ? WHERE id_hotel = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, hotel.getNomHotel());
            stmt.setInt(2, hotel.getIdLieu());
            stmt.setInt(3, hotel.getIdHotel());
            stmt.executeUpdate();
        }
        return hotel;
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM hotel WHERE id_hotel = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Hotel mapResultSet(ResultSet rs) throws SQLException {
        Hotel h = new Hotel();
        h.setIdHotel(rs.getInt("id_hotel"));
        h.setNomHotel(rs.getString("nom_hotel"));
        h.setIdLieu(rs.getInt("id_lieu"));
        h.setLibelleLieu(rs.getString("libelle_lieu"));
        return h;
    }
}