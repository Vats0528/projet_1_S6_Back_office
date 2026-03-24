package com.projet.service;

import com.projet.model.*;
import com.projet.repository.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SimulationService {
    private final ReservationRepository resRepo = new ReservationRepository();
    private final VehiculeRepository vehiculeRepo = new VehiculeRepository();
    private final DistanceRepository distanceRepo = new DistanceRepository();
    private final ParamVehiculeRepository paramRepo = new ParamVehiculeRepository();

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<ReservationVehicule> genererSimulation(String date) throws SQLException {
        // 1. Charger les données (On prend les réservations EN_ATTENTE triées par heure)
        List<Reservation> reservations = resRepo.findUnassignedByDate(date);
        // On récupère les véhicules triés par priorité écologique (Electrique -> Hybride -> Diesel)
        List<Vehicule> vehiculesDispo = vehiculeRepo.findAllOrderByCarburant();
        ParamVehicule params = paramRepo.findFirst();

        List<ReservationVehicule> planning = new ArrayList<>();

        // 2. Grouper les réservations par Lieu (Destination)
        Map<Integer, List<Reservation>> groupesParLieu = grouperParLieu(reservations);

        for (Map.Entry<Integer, List<Reservation>> entry : groupesParLieu.entrySet()) {
            int idLieu = entry.getKey();
            List<Reservation> clientsDuLieu = entry.getValue();

            // Tant qu'il reste des clients pour ce lieu, on remplit des véhicules
            while (!clientsDuLieu.isEmpty()) {
                // On cherche le meilleur véhicule disponible pour le premier groupe de clients
                int nbNecessaire = clientsDuLieu.get(0).getNbPassager();
                Vehicule vChoisi = trouverMeilleurVehicule(vehiculesDispo, nbNecessaire);

                if (vChoisi == null) break; // Plus de véhicules disponibles

                // Créer la mission
                ReservationVehicule mission = new ReservationVehicule();
                mission.setIdVehicule(vChoisi.getIdVehicule());
                mission.setModelVehicule(vChoisi.getModel());
                mission.setNomCarburant(vChoisi.getNomCarburant());
                mission.setNbPlaceVehicule(vChoisi.getNbPlace());

                // Remplir le véhicule (Optimisation de l'espace)
                remplirVehicule(mission, clientsDuLieu, vChoisi.getNbPlace());

                // Calculer les horaires
                calculerHoraires(mission, idLieu, params);

                planning.add(mission);
                // On retire ce véhicule des dispos pour cette simulation
                vehiculesDispo.remove(vChoisi);
            }
        }
        return planning;
    }

    
    private Map<Integer, List<Reservation>> grouperParLieu(List<Reservation> reservations) {
        Map<Integer, List<Reservation>> map = new HashMap<>();
        for (Reservation r : reservations) {
            map.computeIfAbsent(r.getIdLieu(), k -> new ArrayList<>()).add(r);
        }
        return map;
    }

    private void remplirVehicule(ReservationVehicule mission, List<Reservation> clients, int capacite) {
        int placesRestantes = capacite;
        Iterator<Reservation> it = clients.iterator();
        while (it.hasNext()) {
            Reservation r = it.next();
            if (r.getNbPassager() <= placesRestantes) {
                mission.getPassagers().add(r);
                placesRestantes -= r.getNbPassager();
                it.remove(); // Client assigné, on le retire de la liste d'attente
            }
        }
    }

private void calculerHoraires(ReservationVehicule mission, int idLieu, ParamVehicule params) throws SQLException {
    // 1. Sécurité Date : On nettoie le ".0" éventuel de PostgreSQL
    String dateBrute = mission.getPassagers().get(0).getDateHeureArrivee();
    if (dateBrute.contains(".")) {
        dateBrute = dateBrute.split("\\.")[0];
    }
    
    LocalDateTime heureArrivee = LocalDateTime.parse(dateBrute, formatter);

    // 2. Sécurité Distance : On cherche la distance vers l'aéroport (id_lieu = 1)
    // On met une valeur par défaut de 15.0km si la base ne répond pas
    double distanceKm = 15.0; 
    try {
        Distance dist = distanceRepo.findByLieu(idLieu); 
        if (dist != null) {
            distanceKm = dist.getDistance();
        }
    } catch (Exception e) {
        System.out.println("Erreur recup distance pour lieu " + idLieu + ", utilisation valeur par défaut");
    }

    // 3. Sécurité Paramètres : Si la table param est vide
    int vitesse = (params != null) ? params.getVitessMoyenne() : 20;
    int attente = (params != null) ? params.getTempsAttente() : 30;

    // 4. Calculs
    int dureeTrajetMinutes = (int) ((distanceKm / vitesse) * 60);

    // Départ = Arrivée - Temps d'attente (ex: 08:30 - 30min = 08:00)
    LocalDateTime depart = heureArrivee.minusMinutes(attente);
    
    // Retour = Départ + (Aller + Retour + 10min décharge)
    LocalDateTime retour = depart.plusMinutes((dureeTrajetMinutes * 2) + 10); 

    mission.setDateHeureDepart(depart.format(formatter));
    mission.setDateHeureRetour(retour.format(formatter));
}

    private Vehicule trouverMeilleurVehicule(List<Vehicule> vehicules, int nbPassagers) {
        for (Vehicule v : vehicules) {
            if (v.getNbPlace() >= nbPassagers) return v;
        }
        return null;
    }
}