package com.projet.repository;

import com.projet.config.DatabaseConnection;
import com.projet.model.Hotel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelRepository {

    public List<Hotel> findAll() throws SQLException {
        List<Hotel> hotels = new ArrayList<>();
        String query = "SELECT id_hotel, nom_hotel FROM hotel ORDER BY nom_hotel";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Hotel hotel = new Hotel();
            hotel.setIdHotel(rs.getInt("id_hotel"));
            hotel.setNomHotel(rs.getString("nom_hotel"));
            hotels.add(hotel);
        }
        
        rs.close();
        stmt.close();
        
        return hotels;
    }

    public Hotel findById(int id) throws SQLException {
        String query = "SELECT id_hotel, nom_hotel FROM hotel WHERE id_hotel = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        
        Hotel hotel = null;
        if (rs.next()) {
            hotel = new Hotel();
            hotel.setIdHotel(rs.getInt("id_hotel"));
            hotel.setNomHotel(rs.getString("nom_hotel"));
        }
        
        rs.close();
        stmt.close();
        
        return hotel;
    }

    public Hotel save(Hotel hotel) throws SQLException {
        String query = "INSERT INTO hotel (nom_hotel) VALUES (?) RETURNING id_hotel";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, hotel.getNomHotel());
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            hotel.setIdHotel(rs.getInt("id_hotel"));
        }
        
        rs.close();
        stmt.close();
        
        return hotel;
    }
}
