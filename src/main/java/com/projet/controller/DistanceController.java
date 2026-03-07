package com.projet.controller;

import com.framework.annotation.RestController;
import com.framework.annotation.GetMapping;
import com.framework.annotation.PostMapping;
import com.framework.annotation.PutMapping;
import com.framework.annotation.DeleteMapping;
import com.framework.annotation.PathVariable;
import com.framework.annotation.RequestBody;
import com.framework.annotation.CrossOrigin;
import com.framework.util.ResponseEntity;
import com.framework.util.HttpStatus;

import com.projet.model.Distance;
import com.projet.repository.DistanceRepository;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class DistanceController {

    private DistanceRepository distanceRepository = new DistanceRepository();

    /**
     * GET /api/distance - Liste toutes les distances
     */
    @GetMapping("/api/distance")
    public ResponseEntity<List<Distance>> getAllDistances() {
        try {
            List<Distance> distances = distanceRepository.findAll();
            return ResponseEntity.ok(distances);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * GET /api/distance/{id} - Récupère une distance par ID
     */
    @GetMapping("/api/distance/{id}")
    public ResponseEntity<Distance> getDistanceById(@PathVariable("id") int id) {
        try {
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
     * GET /api/distance/entre/{lieu1}/{lieu2} - Récupère la distance entre deux lieux
     */
    @GetMapping("/api/distance/entre/{lieu1}/{lieu2}")
    public ResponseEntity<List<Distance>> getDistanceBetween(@PathVariable("lieu1") int lieu1, @PathVariable("lieu2") int lieu2) {
        try {
            List<Distance> distances = distanceRepository.findByLieux(lieu1, lieu2);
            return ResponseEntity.ok(distances);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * POST /api/distance - Crée une nouvelle distance
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
     * PUT /api/distance/{id} - Met à jour une distance
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
     * DELETE /api/distance/{id} - Supprime une distance
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

