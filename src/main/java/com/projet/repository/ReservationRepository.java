package com.projet.repository;

import com.projet.config.DatabaseConnection;
import com.projet.model.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepository {

    public List<Reservation> findAll() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = """
            SELECT r.id_reservation_client, r.nb_passager, r.date_heure_arrivee, 
                   r.id_hotel, r.id_client, h.nom_hotel, c.nom_client
            FROM reservation_client r
            JOIN hotel h ON r.id_hotel = h.id_hotel
            JOIN client c ON r.id_client = c.id_client
            ORDER BY r.date_heure_arrivee DESC
            """;
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Reservation reservation = mapResultSet(rs);
            reservations.add(reservation);
        }
        
        rs.close();
        stmt.close();
        
        return reservations;
    }

    public List<Reservation> findByDate(String date) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = """
            SELECT r.id_reservation_client, r.nb_passager, r.date_heure_arrivee, 
                   r.id_hotel, r.id_client, h.nom_hotel, c.nom_client
            FROM reservation_client r
            JOIN hotel h ON r.id_hotel = h.id_hotel
            JOIN client c ON r.id_client = c.id_client
            WHERE DATE(r.date_heure_arrivee) = ?::date
            ORDER BY r.date_heure_arrivee DESC
            """;
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, date);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Reservation reservation = mapResultSet(rs);
            reservations.add(reservation);
        }
        
        rs.close();
        stmt.close();
        
        return reservations;
    }

    public Reservation findById(int id) throws SQLException {
        String query = """
            SELECT r.id_reservation_client, r.nb_passager, r.date_heure_arrivee, 
                   r.id_hotel, r.id_client, h.nom_hotel, c.nom_client
            FROM reservation_client r
            JOIN hotel h ON r.id_hotel = h.id_hotel
            JOIN client c ON r.id_client = c.id_client
            WHERE r.id_reservation_client = ?
            """;
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        
        Reservation reservation = null;
        if (rs.next()) {
            reservation = mapResultSet(rs);
        }
        
        rs.close();
        stmt.close();
        
        return reservation;
    }

    public Reservation save(Reservation reservation) throws SQLException {
        String query = """
            INSERT INTO reservation_client (nb_passager, date_heure_arrivee, id_hotel, id_client) 
            VALUES (?, ?::timestamp, ?, ?) 
            RETURNING id_reservation_client
            """;
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, reservation.getNbPassager());
        stmt.setString(2, reservation.getDateHeureArrivee());
        stmt.setInt(3, reservation.getIdHotel());
        stmt.setInt(4, reservation.getIdClient());
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            reservation.setIdReservation(rs.getInt("id_reservation_client"));
        }
        
        rs.close();
        stmt.close();
        
        return reservation;
    }

    public Reservation update(Reservation reservation) throws SQLException {
        String query = """
            UPDATE reservation_client 
            SET nb_passager = ?, date_heure_arrivee = ?::timestamp, id_hotel = ?, id_client = ?
            WHERE id_reservation_client = ?
            """;
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, reservation.getNbPassager());
        stmt.setString(2, reservation.getDateHeureArrivee());
        stmt.setInt(3, reservation.getIdHotel());
        stmt.setInt(4, reservation.getIdClient());
        stmt.setInt(5, reservation.getIdReservation());
        stmt.executeUpdate();
        
        stmt.close();
        
        return reservation;
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM reservation_client WHERE id_reservation_client = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        stmt.executeUpdate();
        
        stmt.close();
    }

    private Reservation mapResultSet(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setIdReservation(rs.getInt("id_reservation_client"));
        reservation.setNbPassager(rs.getInt("nb_passager"));
        reservation.setDateHeureArrivee(rs.getTimestamp("date_heure_arrivee"));
        reservation.setIdHotel(rs.getInt("id_hotel"));
        reservation.setIdClient(rs.getInt("id_client"));
        reservation.setNomHotel(rs.getString("nom_hotel"));
        reservation.setNomClient(rs.getString("nom_client"));
        return reservation;
    }
}
