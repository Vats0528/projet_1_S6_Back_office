package com.projet.controller;

import com.framework.annotation.*;
import com.framework.util.ResponseEntity;
import com.framework.util.HttpStatus;

import com.projet.model.ReservationVehicule;
import com.projet.service.SimulationService;
import com.projet.repository.AssignationRepository;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class AssignationController {

    private final SimulationService simulationService = new SimulationService();
    private final AssignationRepository assignationRepository = new AssignationRepository();

    /**
     * 1. SUGGESTION (Simulation pure en mémoire)
     * GET /api/assignations/suggerer/{date}
     */
    @GetMapping("/api/assignations/suggerer/{date}")
    public ResponseEntity<List<ReservationVehicule>> suggerer(@PathVariable("date") String date) {
        try {
            // Correction : La méthode dans SimulationService s'appelle genererSimulation
            List<ReservationVehicule> suggestions = simulationService.genererSimulation(date);
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 2. CONFIRMATION (Persistance réelle en base de données)
     * POST /api/assignations/confirmer
     */
    @PostMapping("/api/assignations/confirmer")
    public ResponseEntity<String> confirmer(@RequestBody List<ReservationVehicule> missions) {
        try {
            if (missions == null || missions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Aucune mission à confirmer.");
            }

            for (ReservationVehicule mission : missions) {
                assignationRepository.confirmerAssignation(mission);
            }
            return ResponseEntity.ok("Missions validées avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la validation : " + e.getMessage());
        }
    }

    /**
     * 3. CONSULTATION (Récupère ce qui a déjà été confirmé)
     * GET /api/assignations/date/{date}
     */
    @GetMapping("/api/assignations/date/{date}")
    public ResponseEntity<List<ReservationVehicule>> getAssignationsByDate(@PathVariable("date") String date) {
        try {
            // Utilise findAssignationsByDate du Repo (que nous avons corrigé plus haut)
            List<ReservationVehicule> missions = assignationRepository.findAssignationsByDate(date);
            return ResponseEntity.ok(missions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 4. ANNULATION (Suppression d'une mission confirmée)
     * DELETE /api/assignations/{id}
     */
    @DeleteMapping("/api/assignations/{id}")
    public ResponseEntity<String> annulerAssignation(@PathVariable("id") int id) {
        try {
            // On appelle la méthode du Repo
            assignationRepository.annulerAssignation(id); 
            return ResponseEntity.ok("Mission annulée.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'annulation");
        }
    }
}