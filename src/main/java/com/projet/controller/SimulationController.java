package com.projet.controller;

import com.framework.annotation.RestController;
import com.framework.annotation.GetMapping;
import com.framework.annotation.PostMapping;
import com.framework.annotation.PathVariable;
import com.framework.annotation.RequestBody;
import com.framework.annotation.CrossOrigin;
import com.framework.util.ResponseEntity;
import com.framework.util.HttpStatus;

import com.projet.model.ReservationVehicule;
import com.projet.service.SimulationService;
import com.projet.repository.AssignationRepository;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class SimulationController {

    private final SimulationService simulationService = new SimulationService();
    private final AssignationRepository assignationRepository = new AssignationRepository();

    /**
     * Calcule la suggestion sans rien enregistrer en base.
     * Note : J'ai renommé l'appel en 'genererSimulation' pour correspondre au Service.
     */
    @GetMapping("/api/simulation/suggerer/{date}")
    public ResponseEntity<List<ReservationVehicule>> suggerer(@PathVariable("date") String date) {
        try {
            // Appel de la méthode que nous avons codée ensemble
            List<ReservationVehicule> suggestions = simulationService.genererSimulation(date);
            
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Enregistre réellement les missions choisies par l'utilisateur.
     */
    @PostMapping("/api/simulation/confirmer")
    public ResponseEntity<String> confirmer(@RequestBody List<ReservationVehicule> missions) {
        try {
            if (missions == null || missions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Aucune mission à confirmer");
            }

            for (ReservationVehicule mission : missions) {
                assignationRepository.confirmerAssignation(mission);
            }
            
            return ResponseEntity.ok("Assignations confirmées avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la confirmation : " + e.getMessage());
        }
    }
}