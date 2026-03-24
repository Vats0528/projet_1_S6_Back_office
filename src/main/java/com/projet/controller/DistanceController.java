package com.projet.controller;

import com.framework.annotation.*;
import com.framework.util.ResponseEntity;
import com.framework.util.HttpStatus;

import com.projet.model.Distance;
import com.projet.repository.DistanceRepository;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class DistanceController {

    private final DistanceRepository distanceRepository = new DistanceRepository();

    /**
     * GET /api/distance
     * Liste toutes les distances enregistrées.
     */
    @GetMapping("/api/distance")
    public ResponseEntity< List<Distance>> getAllDistances() {
        try {
            List<Distance> distances = distanceRepository.findAll() ; 
            return ResponseEntity.ok(distances);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * GET /api/distance/{id}
     * Récupère une distance spécifique par son ID.
     */
    @GetMapping("/api/distance/{id}")
    public ResponseEntity<Distance> getDistanceById(@PathVariable("id") int id) {
        try {
            // Note: Assure-toi que findById est bien implémenté dans ton repo
            Distance distance = distanceRepository.findById(id);
            if (distance != null) {
                return ResponseEntity.ok(distance);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * GET /api/distance/entre/{lieu1}/{lieu2}
     * Récupère la distance entre deux points.
     */
    @GetMapping("/api/distance/entre/{lieu1}/{lieu2}")
    public ResponseEntity<List<Distance>> getDistanceBetween(@PathVariable("lieu1") int lieu1, @PathVariable("lieu2") int lieu2) {
        try {
            // Utilise la méthode findByLieux que nous avons corrigée dans le repository
            List<Distance> distances = distanceRepository.findByLieux(lieu1, lieu2);
            return ResponseEntity.ok(distances);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * POST /api/distance
     * Ajoute un nouveau trajet (temps/distance) en base.
     */
    @PostMapping("/api/distance")
    public ResponseEntity<Distance> createDistance(@RequestBody Distance distance) {
        try {
            Distance created = distanceRepository.save(distance);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * PUT /api/distance/{id}
     * Met à jour les informations d'un trajet existant.
     */
    @PutMapping("/api/distance/{id}")
    public ResponseEntity<Distance> updateDistance(@PathVariable("id") int id, @RequestBody Distance distance) {
        try {
            distance.setIdDistance(id);
            Distance updated = distanceRepository.update(distance);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * DELETE /api/distance/{id}
     * Supprime un trajet.
     */
    @DeleteMapping("/api/distance/{id}")
    public ResponseEntity<Void> deleteDistance(@PathVariable("id") int id) {
        try {
            distanceRepository.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}