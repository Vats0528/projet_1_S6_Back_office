package com.projet.service;

import com.projet.config.DatabaseConnection;
import com.projet.model.Reservation;
import com.projet.model.Vehicule;
import com.projet.repository.ReservationRepository;
import com.projet.repository.ReservationVehiculeRepository;
import com.projet.repository.VehiculeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.sql.Timestamp;

public class ServiceVehicule {
    private final ReservationRepository reservationRepo = new ReservationRepository();
    private final VehiculeRepository vehiculeRepo = new VehiculeRepository();
    private final ReservationVehiculeRepository resVehiculeRepo = new ReservationVehiculeRepository();

    // Classe interne pour structurer le résultat avant la persistance
    public static class VehiculeMission {
        public Vehicule vehicule;
        public List<Reservation> reservationsAssocies = new ArrayList<>();
        public int placesRestantes;

        public VehiculeMission(Vehicule v) {
            this.vehicule = v;
            this.placesRestantes = v.getNbPlace();
        }
    }

    /**
     * Retourne l'heure d'arrivée la plus basse (le premier arrivé) 
     * dans une liste de réservations.
     */
    public String getHeureMin(List<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) {
            return null;
        }

        String min = reservations.get(0).getDateHeureArrivee();

        for (Reservation res : reservations) {
            //compareTo fonctionne parfaitement sur le format "YYYY-MM-DD HH:MM:SS"
            if (res.getDateHeureArrivee().compareTo(min) < 0) {
                min = res.getDateHeureArrivee();
            }
        }
        return min;
    }

    /**
     * Retourne l'heure d'arrivée la plus haute (le dernier arrivé) 
     * dans une liste de réservations.
     */
    public String getHeureMax(List<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) {
            return null;
        }

        String max = reservations.get(0).getDateHeureArrivee();

        for (Reservation res : reservations) {
            if (res.getDateHeureArrivee().compareTo(max) > 0) {
                max = res.getDateHeureArrivee();
            }
        }
        return max;
    }

    /**
     * Calcule le nombre total de passagers dans un groupe.
     * Utile pour choisir le véhicule plus tard.
     */
    public int getTotalPassagers(List<Reservation> reservations) {
        int total = 0;
        for (Reservation res : reservations) {
            total += res.getNbPassager();
        }
        return total;
    }


    public Vehicule choisirMeilleurVehicule(List<Vehicule> tousLesVehicules, int nbPassagers, Map<Integer, Integer> statsTrajets) {
        
        // --- CRITÈRE 1 : CAPACITÉ SUFFISANTE ---
        // On ne garde que les véhicules pouvant contenir tous les passagers
        List<Vehicule> eligibleCapacite = tousLesVehicules.stream()
                .filter(v -> v.getNbPlace() >= nbPassagers)
                .collect(Collectors.toList());

        if (eligibleCapacite.isEmpty()) return null; // Aucun véhicule assez grand

        // --- CRITÈRE 2 : CAPACITÉ LA PLUS PROCHE (Optimisation du remplissage) ---
        // On trouve la capacité minimale parmi les éligibles (ex: si on a 10 passagers, on préfère 12 places à 50 places)
        int capaciteMinTrouvee = eligibleCapacite.stream()
                .mapToInt(Vehicule::getNbPlace)
                .min()
                .getAsInt();

        List<Vehicule> meilleursCapacite = eligibleCapacite.stream()
                .filter(v -> v.getNbPlace() == capaciteMinTrouvee)
                .collect(Collectors.toList());

        if (meilleursCapacite.size() == 1) return meilleursCapacite.get(0);

        // --- CRITÈRE 3 : NOMBRE DE TRAJETS MINIMUM ---
        // On regarde combien de fois chaque véhicule a déjà roulé aujourd'hui
        int minTrajets = meilleursCapacite.stream()
                .mapToInt(v -> statsTrajets.getOrDefault(v.getIdVehicule(), 0))
                .min()
                .getAsInt();

        List<Vehicule> moinsUtilises = meilleursCapacite.stream()
                .filter(v -> statsTrajets.getOrDefault(v.getIdVehicule(), 0) == minTrajets)
                .collect(Collectors.toList());

        if (moinsUtilises.size() == 1) return moinsUtilises.get(0);

        // --- CRITÈRE 4 : TYPE DE CARBURANT (Priorité 1: Diesel, Priorité 2: Essence) ---
        // Tentative Diesel (ID 1)
        List<Vehicule> diesel = moinsUtilises.stream()
                .filter(v -> v.getIdCarburant() == 1)
                .collect(Collectors.toList());
        if (!diesel.isEmpty()) return diesel.get(0); // On prend le premier diesel trouvé

        // Tentative Essence (ID 2)
        List<Vehicule> essence = moinsUtilises.stream()
                .filter(v -> v.getIdCarburant() == 2)
                .collect(Collectors.toList());
        if (!essence.isEmpty()) return essence.get(0);

        // --- CRITÈRE 5 : RANDOM ---
        // Si on arrive ici, c'est qu'ils ont le même nombre de trajets et un carburant "autre"
        Random rand = new Random();
        return moinsUtilises.get(rand.nextInt(moinsUtilises.size()));
    }


    public List<VehiculeMission> simulerAssignation(List<Reservation> reservationsGroupees, 
                                                   List<Vehicule> vehiculesLibres, 
                                                   Map<Integer, Integer> statsTrajets) {
        
        List<VehiculeMission> missionsGenerees = new ArrayList<>();

        for (Reservation res : reservationsGroupees) {
            boolean assigne = false;

            // 1. Tenter d'ajouter à un véhicule DÉJÀ PRIS (Optimisation du remplissage)
            for (VehiculeMission mission : missionsGenerees) {
                if (mission.placesRestantes >= res.getNbPassager()) {
                    mission.reservationsAssocies.add(res);
                    mission.placesRestantes -= res.getNbPassager();
                    assigne = true;
                    break; 
                }
            }

            // 2. Si non assigné, chercher un NOUVEAU véhicule parmi les libres
            if (!assigne) {
                // On filtre les véhicules libres pour n'envoyer que ceux qui ne sont pas encore dans missionsGenerees
                List<Vehicule> vraisLibres = vehiculesLibres.stream()
                    .filter(v -> missionsGenerees.stream().noneMatch(m -> m.vehicule.getIdVehicule() == v.getIdVehicule()))
                    .toList();

                Vehicule choisi = choisirMeilleurVehicule(vraisLibres, res.getNbPassager(), statsTrajets);
                
                if (choisi != null) {
                    VehiculeMission nouvelleMission = new VehiculeMission(choisi);
                    nouvelleMission.reservationsAssocies.add(res);
                    nouvelleMission.placesRestantes -= res.getNbPassager();
                    missionsGenerees.add(nouvelleMission);
                    assigne = true;
                } else {
                    res.setStatus("NON_ASSIGNEE"); // Marquer pour l'utilisateur
                }
            }
        }
        return missionsGenerees;
    }


    public void enregistrerMissionComplete(VehiculeMission mission, String dateDepart, String dateRetour) throws SQLException {
    String sqlMission = "INSERT INTO reservation_vehicule (date_heure_depart, date_heure_retour, id_vehicule, titre) VALUES (?::timestamp, ?::timestamp, ?, ?) RETURNING id_reservation_vehicule";
    String sqlLiaison = "INSERT INTO details_reservation_client (id_reservation_client, id_reservation_vehicule) VALUES (?, ?)";
    String sqlUpdateRes = "UPDATE reservation_client SET status = 'CONFIRME' WHERE id_reservation_client = ?";

    try (Connection conn = DatabaseConnection.getConnection()) {
        conn.setAutoCommit(false); // Pour tout annuler si une insertion échoue
        try {
            int idMission;
            try (PreparedStatement st = conn.prepareStatement(sqlMission)) {
                st.setString(1, dateDepart);
                st.setString(2, dateRetour);
                st.setInt(3, mission.vehicule.getIdVehicule());
                st.setString(4, "Transfert Groupe " + mission.vehicule.getModel());
                ResultSet rs = st.executeQuery();
                rs.next();
                idMission = rs.getInt(1);
            }

            for (Reservation r : mission.reservationsAssocies) {
                // Créer le lien
                try (PreparedStatement st = conn.prepareStatement(sqlLiaison)) {
                    st.setInt(1, r.getIdReservation());
                    st.setInt(2, idMission);
                    st.executeUpdate();
                }
                // Update le statut du client
                try (PreparedStatement st = conn.prepareStatement(sqlUpdateRes)) {
                    st.setInt(1, r.getIdReservation());
                    st.executeUpdate();
                }
            }
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        }
    }
    
}


 public void lancerSimulationGlobale(String date) throws SQLException {
    // 1. Obtenir les regroupements (Liste de Listes)
    List<List<Reservation>> tousLesGroupes = reservationRepo.regrouperReservations(date);
    
    for (List<Reservation> groupe : tousLesGroupes) {
        // A. Trouver l'heure Max d'arrivée du groupe pour savoir quand le véhicule doit être là
        String heureMax = getHeureMax(groupe);
        Timestamp debut = Timestamp.valueOf(heureMax);
        Timestamp fin = new Timestamp(debut.getTime() + (2 * 60 * 60 * 1000)); // Exemple +2h

        // B. Chercher les véhicules REELLEMENT libres à ce moment précis
        List<Vehicule> libres = vehiculeRepo.findAvailableVehicules(debut, fin);
        
        // C. Obtenir les stats de trajet pour le critère de sélection
        Map<Integer, Integer> stats = vehiculeRepo.getStatsTrajetsParJour(date);

        // D. SIMULATION : Répartir les passagers du groupe dans les véhicules libres
        List<VehiculeMission> missions = simulerAssignation(groupe, libres, stats);

        // E. PERSISTANCE : Enregistrer chaque mission générée pour ce groupe
        for (VehiculeMission m : missions) {
            resVehiculeRepo.saveMission(m, debut.toString(), fin.toString());
        }
    }
}

 


}