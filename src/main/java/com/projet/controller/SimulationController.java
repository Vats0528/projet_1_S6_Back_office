package com.projet.controller;

import com.framework.annotation.*;
import com.framework.util.ResponseEntity;
import com.framework.util.HttpStatus;

import com.projet.model.Reservation;
import com.projet.model.Vehicule;
import com.projet.repository.ReservationRepository;
import com.projet.repository.VehiculeRepository;
import com.projet.repository.ReservationVehiculeRepository;
import com.projet.service.ServiceVehicule;
import com.projet.service.ServiceVehicule.VehiculeMission;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class SimulationController {

    private final ReservationRepository reservationRepo = new ReservationRepository();
    private final VehiculeRepository vehiculeRepo = new VehiculeRepository();
    private final ReservationVehiculeRepository resVehiculeRepo = new ReservationVehiculeRepository();
    private final ServiceVehicule serviceVehicule = new ServiceVehicule();

    /**
     * Lance la simulation complète pour une date donnée.
     * POST /api/simulation/generer/{date}
     */
    @PostMapping("/api/simulation/generer/{date}")
    public ResponseEntity<String> genererSimulation(@PathVariable("date") String date) {
        try {
            // 1. Récupérer les regroupements (Liste de Listes)
            List<List<Reservation>> tousLesGroupes = reservationRepo.regrouperReservations(date);
            
            if (tousLesGroupes.isEmpty()) {
                return ResponseEntity.ok("Aucune réservation en attente pour cette date.");
            }

            int missionsCrees = 0;

            // 2. Traiter chaque regroupement un par un (Regroupement par Regroupement)
            for (List<Reservation> groupe : tousLesGroupes) {
                
                // A. Définir le créneau horaire (Basé sur le dernier arrivé du groupe)
                String heureMax = serviceVehicule.getHeureMax(groupe);
                Timestamp debut = Timestamp.valueOf(heureMax);
                // On estime un retour à +2h pour la recherche de disponibilité
                Timestamp fin = new Timestamp(debut.getTime() + (120 * 60 * 1000)); 

                // B. Chercher les véhicules REELLEMENT libres à ce moment
                List<Vehicule> libres = vehiculeRepo.findAvailableVehicules(debut, fin);
                
                // C. Récupérer les stats de trajets mis à jour (pour l'équilibre du parc)
                Map<Integer, Integer> stats = vehiculeRepo.getStatsTrajetsParJour(date);

                // D. SIMULATION : Répartir les passagers dans les véhicules
                List<VehiculeMission> missionsDuGroupe = serviceVehicule.simulerAssignation(groupe, libres, stats);

                // E. PERSISTANCE : Enregistrer chaque mission en base
                for (VehiculeMission m : missionsDuGroupe) {
                    // Ici on pourrait affiner debut/fin avec les distances
                    resVehiculeRepo.saveMission(m, debut.toString(), fin.toString());
                    missionsCrees++;
                }
            }

            return ResponseEntity.ok("Simulation terminée : " + missionsCrees + " missions créées.");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erreur lors de la simulation : " + e.getMessage());
        }
    }
}