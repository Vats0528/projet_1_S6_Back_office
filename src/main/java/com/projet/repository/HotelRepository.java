package com.projet.repository;

import com.projet.config.DatabaseConnection;
import com.projet.model.Hotel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelRepository {

    public List<Hotel> findAll() throws SQLException {
        List<Hotel> hotels = new ArrayList<>();
        String query = "SELECT id, libelle FROM hotel ORDER BY libelle";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Hotel hotel = new Hotel();
            hotel.setIdHotel(rs.getInt("id"));
            hotel.setNomHotel(rs.getString("libelle"));
            hotels.add(hotel);
        }
        
        rs.close();
        stmt.close();
        
        return hotels;
    }

    public Hotel findById(int id) throws SQLException {
        String query = "SELECT id, libelle FROM hotel WHERE id = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        
        Hotel hotel = null;
        if (rs.next()) {
            hotel = new Hotel();
            hotel.setIdHotel(rs.getInt("id"));
            hotel.setNomHotel(rs.getString("libelle"));
        }
        
        rs.close();
        stmt.close();
        
        return hotel;
    }

    public Hotel save(Hotel hotel) throws SQLException {
        String query = "INSERT INTO hotel (libelle) VALUES (?) RETURNING id";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, hotel.getNomHotel());
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            hotel.setIdHotel(rs.getInt("id"));
        }
        
        rs.close();
        stmt.close();
        
        return hotel;
    }

    public Hotel update(Hotel hotel) throws SQLException {
        String query = "UPDATE hotel SET libelle = ? WHERE id = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, hotel.getNomHotel());
        stmt.setInt(2, hotel.getIdHotel());
        stmt.executeUpdate();
        stmt.close();
        
        return hotel;
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM hotel WHERE id = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        stmt.executeUpdate();
        stmt.close();
    }
}
