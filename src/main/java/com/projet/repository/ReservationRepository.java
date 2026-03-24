package com.projet.repository;

import com.projet.config.DatabaseConnection;
import com.projet.model.Reservation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class ReservationRepository {

    // On centralise la base de la requête pour éviter les répétitions
    private final String BASE_QUERY = """
        SELECT r.id_reservation_client, r.nb_passager, r.date_heure_arrivee, r.status,
               r.id_hotel, r.id_client, h.nom_hotel, h.id_lieu, l.libelle as libelle_lieu, c.nom_client
        FROM reservation_client r
        JOIN hotel h ON r.id_hotel = h.id_hotel
        JOIN lieu l ON h.id_lieu = l.id_lieu
        JOIN client c ON r.id_client = c.id_client
        """;

    public List<Reservation> findAll() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = BASE_QUERY + " ORDER BY r.date_heure_arrivee DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                reservations.add(mapResultSet(rs));
            }
        }
        return reservations;
    }

    public List<Reservation> findByDate(String date) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        // Utilisation de DATE() pour comparer uniquement la partie jour
        String query = BASE_QUERY + " WHERE DATE(r.date_heure_arrivee) = ?::date ORDER BY r.date_heure_arrivee ASC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, date);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(mapResultSet(rs));
                }
            }
        }
        return reservations;
    }

    public Reservation save(Reservation reservation) throws SQLException {
        String query = """
            INSERT INTO reservation_client (nb_passager, date_heure_arrivee, id_hotel, id_client, status) 
            VALUES (?, ?::timestamp, ?, ?, ?) 
            RETURNING id_reservation_client
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, reservation.getNbPassager());
            stmt.setString(2, reservation.getDateHeureArrivee());
            stmt.setInt(3, reservation.getIdHotel()); // Utilise l'idHotel de l'objet
            stmt.setInt(4, reservation.getIdClient());
            stmt.setString(5, reservation.getStatus());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    reservation.setIdReservation(rs.getInt(1));
                }
            }
        }
        return reservation;
    }

    private Reservation mapResultSet(ResultSet rs) throws SQLException {
        Reservation res = new Reservation();
        res.setIdReservation(rs.getInt("id_reservation_client"));
        res.setNbPassager(rs.getInt("nb_passager"));
        
        // Utilise notre setter "bridge" qui accepte un Timestamp et stocke une String
        res.setDateHeureArrivee(rs.getTimestamp("date_heure_arrivee"));
        
        res.setStatus(rs.getString("status"));
        res.setIdHotel(rs.getInt("id_hotel"));
        res.setIdClient(rs.getInt("id_client"));
        res.setIdLieu(rs.getInt("id_lieu"));
        
        // Jointures
        res.setNomHotel(rs.getString("nom_hotel"));
        res.setLibelleLieu(rs.getString("libelle_lieu"));
        res.setNomClient(rs.getString("nom_client"));
        
        return res;
    }


    public List<Reservation> findUnassignedByDate(String date) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        // On ajoute le filtre "status = 'EN_ATTENTE'"
        String query = """
            SELECT r.id_reservation_client, r.nb_passager, r.date_heure_arrivee, r.status,
                   r.id_hotel, r.id_client, h.nom_hotel, h.id_lieu, l.libelle as libelle_lieu, c.nom_client
            FROM reservation_client r
            JOIN hotel h ON r.id_hotel = h.id_hotel
            JOIN lieu l ON h.id_lieu = l.id_lieu
            JOIN client c ON r.id_client = c.id_client
            WHERE r.status = 'EN_ATTENTE' 
            AND DATE(r.date_heure_arrivee) = ?::date
            ORDER BY r.date_heure_arrivee ASC
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, date);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(mapResultSet(rs));
                }
            }
        }
        return reservations;
    }

    /**
     * Recherche une réservation par son ID avec toutes les jointures 
     * (pour afficher le nom de l'hôtel, du lieu et du client).
     */
    public Reservation findById(int id) throws SQLException {
        String query = """
        SELECT r.id_reservation_client, r.nb_passager, r.date_heure_arrivee, r.status,
                    r.id_hotel, r.id_client, h.nom_hotel, l.libelle as libelle_lieu, c.nom_client, h.id_lieu
                FROM reservation_client r
                JOIN hotel h ON r.id_hotel = h.id_hotel
                JOIN lieu l ON h.id_lieu = l.id_lieu
                JOIN client c ON r.id_client = c.id_client
                WHERE r.id_reservation_client = ?
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

    /**
     * Sauvegarde une nouvelle réservation.
     * Correction : On accepte l'idHotel en paramètre pour la clé étrangère.
     */
    public Reservation save(Reservation reservation, int idHotel) throws SQLException {
        String query = """
            INSERT INTO reservation_client (nb_passager, date_heure_arrivee, id_hotel, id_client, status) 
            VALUES (?, ?::timestamp, ?, ?, ?) RETURNING id_reservation_client
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, reservation.getNbPassager());
            stmt.setString(2, reservation.getDateHeureArrivee());
            stmt.setInt(3, idHotel);
            stmt.setInt(4, reservation.getIdClient());
            stmt.setString(5, reservation.getStatus());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    reservation.setIdReservation(rs.getInt(1));
                }
            }
        }
        return reservation;
    }

    /**
     * Supprime une réservation.
     */
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM reservation_client WHERE id_reservation_client = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Mise à jour du statut (utilisée par le PUT du controller).
     */
    public void updateStatus(int id, String status) throws SQLException {
        String query = "UPDATE reservation_client SET status = ? WHERE id_reservation_client = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }


    /**
 * Récupère le Temps d'Attente (TA) en minutes depuis la table param_vehicule.
 */
public int getTempsAttente() throws SQLException {
    int ta = 0;
    String query = "SELECT ta_s FROM param_vehicule LIMIT 1";
    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {
        if (rs.next()) {
            ta = rs.getInt("ta_s");
        }
    }
    return ta;
}

/**
 * Regroupe les réservations "EN_ATTENTE" par fenêtre de temps (TA).
 * @return Une liste de listes (Regroupement 1, Regroupement 2, etc.)
 */
public List<List<Reservation>> regrouperReservations1(String date) throws SQLException {
        // 1. On récupère les réservations non traitées triées par heure ASC
        List<Reservation> reservations = findUnassignedByDate(date);
        
        // 2. On récupère le paramètre TA
        int taMinutes = getTempsAttente();
        
        List<List<Reservation>> resultatFinal = new ArrayList<>();

        // 3. Boucle de regroupement
        while (!reservations.isEmpty()) {
            List<Reservation> groupeActuel = new ArrayList<>();
            
            // La première est toujours la référence (la plus tôt)
            Reservation ref = reservations.get(0);
            groupeActuel.add(ref);
            
            // Calcul de l'heure limite : Heure Arrivée Ref + TA
            Timestamp tsRef = Timestamp.valueOf(ref.getDateHeureArrivee());
            long limiteMillis = tsRef.getTime() + (taMinutes * 60 * 1000);
            
            // On cherche les autres réservations qui rentrent dans cette fenêtre
            // On commence à l'indice 1 (la suivante)
            for (int i = 1; i < reservations.size(); i++) {
                Reservation suivante = reservations.get(i);
                Timestamp tsSuivante = Timestamp.valueOf(suivante.getDateHeureArrivee());
                
                if (tsSuivante.getTime() <= limiteMillis) {
                    groupeActuel.add(suivante);
                } else {
                    // Comme c'est trié par date ASC, si celle-ci dépasse, 
                    // les suivantes dépasseront aussi.
                    break;
                }
            }
            
            // On ajoute le groupe au résultat
            resultatFinal.add(groupeActuel);
            
            // ON retire toutes les réservations du groupe actuel de la liste principale
            // pour que la boucle 'while' puisse trouver la prochaine 'Heure Min'
            reservations.removeAll(groupeActuel);
        }
        
        return resultatFinal;
    }




/**
 * Regroupe les réservations par fenêtre de temps (TA) 
 * ET trie chaque groupe par nombre de passagers décroissant.
 */
public List<List<Reservation>> regrouperReservations(String date) throws SQLException {
    List<Reservation> reservations = findUnassignedByDate(date);
    int taMinutes = getTempsAttente();
    List<List<Reservation>> resultatFinal = new ArrayList<>();

    while (!reservations.isEmpty()) {
        List<Reservation> groupeActuel = new ArrayList<>();
        
        // 1. Définir la fenêtre de temps
        Reservation ref = reservations.get(0);
        groupeActuel.add(ref);
        
        Timestamp tsRef = Timestamp.valueOf(ref.getDateHeureArrivee());
        long limiteMillis = tsRef.getTime() + (taMinutes * 60 * 1000);
        
        for (int i = 1; i < reservations.size(); i++) {
            Reservation suivante = reservations.get(i);
            Timestamp tsSuivante = Timestamp.valueOf(suivante.getDateHeureArrivee());
            
            if (tsSuivante.getTime() <= limiteMillis) {
                groupeActuel.add(suivante);
            } else {
                break;
            }
        }

        // --- NOUVEAUTÉ : TRI PAR NOMBRE DE PASSAGERS DÉCROISSANT ---
        // On trie le groupe avant de l'ajouter au résultat final
        Collections.sort(groupeActuel, new Comparator<Reservation>() {
            @Override
            public int compare(Reservation r1, Reservation r2) {
                // Pour un tri décroissant : r2 comparé à r1
                return Integer.compare(r2.getNbPassager(), r1.getNbPassager());
            }
        });
        // ---------------------------------------------------------

        resultatFinal.add(groupeActuel);
        reservations.removeAll(groupeActuel);
    }
    
    return resultatFinal;
}




}