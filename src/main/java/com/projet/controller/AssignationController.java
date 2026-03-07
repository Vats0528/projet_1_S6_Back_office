package com.projet.controller;

import com.framework.annotation.RestController;
import com.framework.annotation.GetMapping;
import com.framework.annotation.PostMapping;
import com.framework.annotation.RequestParam;
import com.framework.annotation.CrossOrigin;
import com.framework.util.ResponseEntity;
import com.framework.util.HttpStatus;

import com.projet.model.Reservation;
import com.projet.model.Vehicule;
import com.projet.model.ParamVehicule;
import com.projet.model.Distance;
import com.projet.repository.AssignationRepository;

import java.sql.Timestamp;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
public class AssignationController {

    private AssignationRepository assignationRepository = new AssignationRepository();

    /**
     * GET /api/assignation/reservations - Liste les réservations non assignées
     */
    @GetMapping("/api/assignation/reservations")
    public ResponseEntity<List<Reservation>> getReservationsNonAssignees() {
        try {
            List<Reservation> reservations = assignationRepository.getReservationsNonAssignees();
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * POST /api/assignation - Effectue l'assignation des véhicules
     * Paramètre: idAeroport (l'ID du lieu représentant l'aéroport)
     */
    @PostMapping("/api/assignation")
    public ResponseEntity<Map<String, Object>> assignerVehicules(@RequestParam("idAeroport") int idAeroport) {
        try {
            Map<String, Object> result = new HashMap<>();
            List<Map<String, Object>> assignations = new ArrayList<>();
            
            // Récupérer les paramètres globaux (VM et TA)
            ParamVehicule params = assignationRepository.getParametresVehicule();
            if (params == null) {
                result.put("error", "Paramètres du véhicule non configurés");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
            
            int vitessMoyenne = params.getVitessMoyenne();
            int tempsAttente = params.getTempsAttente();
            
            // Récupérer les réservations non assignées
            List<Reservation> reservations = assignationRepository.getReservationsNonAssignees();
            
            if (reservations.isEmpty()) {
                result.put("message", "Aucune réservation à assigner");
                result.put("assignations", assignations);
                return ResponseEntity.ok(result);
            }
            
            // Grouper les réservations par créneau horaire (±TA)
            List<List<Reservation>> groupes = assignationRepository.grouperReservations(reservations, tempsAttente);
            
            // Traiter chaque groupe
            for (List<Reservation> groupe : groupes) {
                // Calculer le total des passengers
                int totalPassagers = groupe.stream().mapToInt(Reservation::getNbPassager).sum();
                
                // Filtrer les véhicules disponibles
                List<Vehicule> vehiculesDispos = assignationRepository.getVehiculesDisponibles(totalPassagers);
                
                if (vehiculesDispos.isEmpty()) {
                    // Pas de véhicule disponible pour ce groupe
                    Map<String, Object> assignationEchouee = new HashMap<>();
                    assignationEchouee.put("reservations", groupe);
                    assignationEchouee.put("error", "Aucun véhicule disponible avec " + totalPassagers + " places");
                    assignations.add(assignationEchouee);
                    continue;
                }
                
                // Choisir le véhicule optimal
                Vehicule vehicule = assignationRepository.choisirVehiculeOptimal(vehiculesDispos);
                
                if (vehicule == null) {
                    Map<String, Object> assignationEchouee = new HashMap<>();
                    assignationEchouee.put("reservations", groupe);
                    assignationEchouee.put("error", "Erreur lors de la sélection du véhicule");
                    assignations.add(assignationEchouee);
                    continue;
                }
                
                // Ordonner les destinations par distance croissante depuis l'aéroport
                List<Reservation> reservationsOrdonnees = assignationRepository.ordonnerParDistanceCroissante(groupe, idAeroport);
                
                // Calculer la date/heure de départ (heure d'arrivée du premier client)
                Timestamp dateHeureDepart = Timestamp.valueOf(reservationsOrdonnees.get(0).getDateHeureArrivee());
                
                // Calculer la date/heure de retour
                Timestamp dateHeureRetour = assignationRepository.calculerDateHeureRetour(
                    dateHeureDepart, reservationsOrdonnees, idAeroport, vitessMoyenne, tempsAttente
                );
                
                // Créer la réservation véhicule
                int idReservationVehicule = assignationRepository.createReservationVehicule(
                    dateHeureDepart, dateHeureRetour, vehicule.getIdVehicule()
                );
                
                // Créer les détails de réservation client et les détails de trajet
                for (int i = 0; i < reservationsOrdonnees.size(); i++) {
                    Reservation reservation = reservationsOrdonnees.get(i);
                    
                    // Créer details_reservation_client
                    assignationRepository.createDetailsReservationClient(
                        reservation.getIdReservation(), 
                        idReservationVehicule
                    );
                    
                    // Créer details_trajet
                    int idLieuDepart = (i == 0) ? idAeroport : reservationsOrdonnees.get(i - 1).getIdLieu();
                    Distance distance = assignationRepository.getDistanceObject(idLieuDepart, reservation.getIdLieu());
                    
                    if (distance != null) {
                        assignationRepository.createDetailsTrajet(
                            distance.getIdDistance(),
                            idReservationVehicule,
                            i + 1 // succession: 1, 2, 3...
                        );
                    }
                }
                
                // Ajouter le trajet retour vers l'aéroport
                if (!reservationsOrdonnees.isEmpty()) {
                    int dernierLieu = reservationsOrdonnees.get(reservationsOrdonnees.size() - 1).getIdLieu();
                    Distance distanceRetour = assignationRepository.getDistanceObject(dernierLieu, idAeroport);
                    
                    if (distanceRetour != null) {
                        assignationRepository.createDetailsTrajet(
                            distanceRetour.getIdDistance(),
                            idReservationVehicule,
                            reservationsOrdonnees.size() + 1
                        );
                    }
                }
                
                // Préparer le résultat pour ce groupe
                Map<String, Object> assignation = new HashMap<>();
                assignation.put("vehicule", vehicule);
                assignation.put("reservations", reservationsOrdonnees);
                assignation.put("date_heure_depart", dateHeureDepart.toString());
                assignation.put("date_heure_retour", dateHeureRetour.toString());
                assignations.add(assignation);
            }
            
            result.put("assignations", assignations);
            result.put("total_groupes", groupes.size());
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}

