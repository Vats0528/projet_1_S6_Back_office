package com.projet.controller;

import com.framework.annotation.*;
import com.framework.util.ResponseEntity;
import com.framework.util.HttpStatus;
import com.projet.dto.MissionAffichageDTO;
import com.projet.model.Lieu;
import com.projet.repository.ReservationVehiculeRepository;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class ReservationVehiculeController {

    private final ReservationVehiculeRepository reservationVehiculeRepository = new ReservationVehiculeRepository();

    /**
     * GET /api/reservation-vehicule/{id}/trajet
     * Retourne l'itinéraire complet (liste de lieux) d'une mission spécifique.
     * Indice 0 = Départ, Indices suivants = Escales et Arrivée finale.
     */
    @GetMapping("/api/reservation-vehicule/{id}/trajet")
    public ResponseEntity<List<Lieu>> getItineraireMission(@PathVariable("id") int id) {
        try {
            // Appel de la fonction que nous avons analysée dans le Repository
            List<Lieu> itineraire = reservationVehiculeRepository.getTrajetVehicule(id);

            if (itineraire.isEmpty()) {
                // Si aucune donnée n'est trouvée dans details_trajet pour cet ID
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }

            return ResponseEntity.ok(itineraire);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Endpoint pour récupérer le planning complet d'une date donnée.
     */
    @GetMapping("/api/missions/planning/{date}")
    public ResponseEntity<List<MissionAffichageDTO>> getPlanningParDate(@PathVariable("date") String date) {
        try {
            List<MissionAffichageDTO> planning = reservationVehiculeRepository.getMissionsParDate(date);
            
            if (planning == null || planning.isEmpty()) {
                // CORRECTION : On utilise .body(null) au lieu de .build() 
                // pour que le type de retour soit bien ResponseEntity<List<MissionAffichageDTO>>
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }

            return ResponseEntity.ok(planning);

        } catch (Exception e) {
            e.printStackTrace();
            // CORRECTION : Même chose ici pour l'erreur 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}