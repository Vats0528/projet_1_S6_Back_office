package com.projet.repository;

import com.projet.config.DatabaseConnection;
import com.projet.model.*;

import java.sql.*;
import java.util.*;

public class AssignationRepository {

    private ReservationRepository reservationRepository = new ReservationRepository();
    private VehiculeRepository vehiculeRepository = new VehiculeRepository();
    private DistanceRepository distanceRepository = new DistanceRepository();
    private ParamVehiculeRepository paramVehiculeRepository = new ParamVehiculeRepository();

    /**
     * Récupère les réservations non assignées (sans entrée dans details_reservation_client)
     */
    public List<Reservation> getReservationsNonAssignees() throws SQLException {
        String query = """
            SELECT r.id_reservation_client, r.nb_passager, r.date_heure_arrivee, 
                   r.id_lieu, r.id_client, l.libelle as libelle_lieu, c.nom_client
            FROM reservation_client r
            JOIN lieu l ON r.id_lieu = l.id_lieu
            JOIN client c ON r.id_client = c.id_client
            WHERE r.id_reservation_client NOT IN (
                SELECT id_reservation_client FROM details_reservation_client
            )
            ORDER BY r.date_heure_arrivee
            """;
        
        List<Reservation> reservations = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Reservation reservation = new Reservation();
            reservation.setIdReservation(rs.getInt("id_reservation_client"));
            reservation.setNbPassager(rs.getInt("nb_passager"));
            reservation.setDateHeureArrivee(rs.getTimestamp("date_heure_arrivee"));
            reservation.setIdLieu(rs.getInt("id_lieu"));
            reservation.setIdClient(rs.getInt("id_client"));
            reservation.setLibelleLieu(rs.getString("libelle_lieu"));
            reservation.setNomClient(rs.getString("nom_client"));
            reservations.add(reservation);
        }
        
        rs.close();
        stmt.close();
        
        return reservations;
    }

    /**
     * Groupe les réservations par créneau horaire (± temps d'attente)
     */
    public List<List<Reservation>> grouperReservations(List<Reservation> reservations, int tempsAttente) {
        List<List<Reservation>> groupes = new ArrayList<>();
        
        if (reservations.isEmpty()) {
            return groupes;
        }
        
        List<Reservation> groupeActuel = new ArrayList<>();
        Timestamp heureReference = reservations.get(0).getDateHeureArrivee();
        
        for (Reservation reservation : reservations) {
            Timestamp heureArrivee = reservation.getDateHeureArrivee();
            
            // Vérifier si la réservation est dans le créneau ± tempsAttente
            long diffMinutes = Math.abs(heureArrivee.getTime() - heureReference.getTime()) / (1000 * 60);
            
            if (diffMinutes <= tempsAttente) {
                groupeActuel.add(reservation);
            } else {
                if (!groupeActuel.isEmpty()) {
                    groupes.add(new ArrayList<>(groupeActuel));
                }
                groupeActuel = new ArrayList<>();
                groupeActuel.add(reservation);
                heureReference = heureArrivee;
            }
        }
        
        if (!groupeActuel.isEmpty()) {
            groupes.add(groupeActuel);
        }
        
        return groupes;
    }

    /**
     * Filtre les véhicules disponibles par capacité
     */
    public List<Vehicule> getVehiculesDisponibles(int nbPassagersTotal) throws SQLException {
        return vehiculeRepository.findByCapacity(nbPassagersTotal);
    }

    /**
     * Choisit le véhicule optimal (moins de trajets déjà assignés)
     */
    public Vehicule choisirVehiculeOptimal(List<Vehicule> vehiculesDisponibles) throws SQLException {
        if (vehiculesDisponibles.isEmpty()) {
            return null;
        }
        
        // Compter le nombre de réservations existantes pour chaque véhicule
        Map<Integer, Integer> countMap = new HashMap<>();
        
        for (Vehicule v : vehiculesDisponibles) {
            String query = """
                SELECT COUNT(*) as count FROM reservation_vehicule 
                WHERE id_vehicule = ?
                """;
            
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, v.getIdVehicule());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                countMap.put(v.getIdVehicule(), rs.getInt("count"));
            } else {
                countMap.put(v.getIdVehicule(), 0);
            }
            
            rs.close();
            stmt.close();
        }
        
        // Trier par nombre de réservations croissantes
        vehiculesDisponibles.sort((v1, v2) -> 
            countMap.get(v1.getIdVehicule()) - countMap.get(v2.getIdVehicule())
        );
        
        return vehiculesDisponibles.get(0);
    }

    /**
     * Ordonne les destinations par distance croissante depuis l'aéroport
     */
    public List<Reservation> ordonnerParDistanceCroissante(List<Reservation> reservations, int idAeroport) throws SQLException {
        if (reservations.size() <= 1) {
            return reservations;
        }
        
        // Calculer la distance de chaque réservation depuis l'aéroport
        Map<Reservation, Double> distanceMap = new HashMap<>();
        
        for (Reservation reservation : reservations) {
            double distance = getDistanceEntreLieux(idAeroport, reservation.getIdLieu());
            distanceMap.put(reservation, distance);
        }
        
        // Trier par distance croissante
        List<Reservation> trie = new ArrayList<>(reservations);
        trie.sort((r1, r2) -> 
            Double.compare(distanceMap.get(r1), distanceMap.get(r2))
        );
        
        return trie;
    }

    /**
     * Récupère la distance entre deux lieux
     */
    private double getDistanceEntreLieux(int idLieu1, int idLieu2) throws SQLException {
        List<Distance> distances = distanceRepository.findByLieux(idLieu1, idLieu2);
        
        if (!distances.isEmpty()) {
            return distances.get(0).getDistance();
        }
        
        return Double.MAX_VALUE;
    }

    /**
     * Crée une réservation véhicule
     */
    public int createReservationVehicule(Timestamp dateHeureDepart, Timestamp dateHeureRetour, int idVehicule) throws SQLException {
        String query = """
            INSERT INTO reservation_vehicule (date_heure_depart, date_heure_retour, id_vehicule) 
            VALUES (?, ?, ?) RETURNING id_reservation_vehicule
            """;
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setTimestamp(1, dateHeureDepart);
        stmt.setTimestamp(2, dateHeureRetour);
        stmt.setInt(3, idVehicule);
        ResultSet rs = stmt.executeQuery();
        
        int idReservationVehicule = 0;
        if (rs.next()) {
            idReservationVehicule = rs.getInt("id_reservation_vehicule");
        }
        
        rs.close();
        stmt.close();
        
        return idReservationVehicule;
    }

    /**
     * Crée les détails de réservation client
     */
    public void createDetailsReservationClient(int idReservationClient, int idReservationVehicule) throws SQLException {
        String query = """
            INSERT INTO details_reservation_client (id_reservation_client, id_reservation_vehicule) 
            VALUES (?, ?)
            """;
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, idReservationClient);
        stmt.setInt(2, idReservationVehicule);
        stmt.executeUpdate();
        
        stmt.close();
    }

    /**
     * Crée les détails du trajet
     */
    public void createDetailsTrajet(int idDistance, int idReservationVehicule, int succession) throws SQLException {
        String query = """
            INSERT INTO details_trajet (id_distance, id_reservation_vehicule, succession) 
            VALUES (?, ?, ?)
            """;
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, idDistance);
        stmt.setInt(2, idReservationVehicule);
        stmt.setInt(3, succession);
        stmt.executeUpdate();
        
        stmt.close();
    }

    /**
     * Récupère la distance entre deux lieux et retourne l'objet Distance
     */
    public Distance getDistanceObject(int idLieu1, int idLieu2) throws SQLException {
        List<Distance> distances = distanceRepository.findByLieux(idLieu1, idLieu2);
        
        if (!distances.isEmpty()) {
            return distances.get(0);
        }
        
        return null;
    }

    /**
     * Récupère les paramètres globaux du véhicule
     */
    public ParamVehicule getParametresVehicule() throws SQLException {
        return paramVehiculeRepository.getGlobal();
    }

    /**
     * Calcule la date/heure de retour
     * date_heure_retour = date_heure_depart + Σ(temps_trajet_i) + Σ(temps_attente) + temps_retour_aeroport
     */
    public Timestamp calculerDateHeureRetour(Timestamp dateHeureDepart, List<Reservation> reservations, 
                                              int idAeroport, int vitessMoyenne, int tempsAttente) throws SQLException {
        
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dateHeureDepart.getTime());
        
        // Pour chaque arrêt (sauf le dernier), ajouter le temps de trajet et le temps d'attente
        for (int i = 0; i < reservations.size(); i++) {
            Reservation reservation = reservations.get(i);
            
            // Calculer le temps de trajet depuis le lieu précédent (ou aéroport pour le premier)
            int idLieuDepart = (i == 0) ? idAeroport : reservations.get(i - 1).getIdLieu();
            double distance = getDistanceEntreLieux(idLieuDepart, reservation.getIdLieu());
            double tempsTrajetHeures = distance / vitessMoyenne;
            long tempsTrajetMinutes = (long) (tempsTrajetHeures * 60);
            
            // Ajouter le temps de trajet
            cal.add(Calendar.MINUTE, (int)tempsTrajetMinutes);
            
            // Ajouter le temps d'attente (sauf pour le dernier arrêt)
            if (i < reservations.size() - 1) {
                cal.add(Calendar.MINUTE, tempsAttente);
            }
        }
        
        // Ajouter le trajet retour vers l'aéroport (depuis le dernier lieu)
        if (!reservations.isEmpty()) {
            int dernierLieu = reservations.get(reservations.size() - 1).getIdLieu();
            double distanceRetour = getDistanceEntreLieux(dernierLieu, idAeroport);
            double tempsRetourHeures = distanceRetour / vitessMoyenne;
            long tempsRetourMinutes = (long) (tempsRetourHeures * 60);
            
            cal.add(Calendar.MINUTE, (int)tempsRetourMinutes);
        }
        
        return new Timestamp(cal.getTimeInMillis());
    }
}

