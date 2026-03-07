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

import com.projet.model.Vehicule;
import com.projet.repository.VehiculeRepository;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class VehiculeController {

    private VehiculeRepository vehiculeRepository = new VehiculeRepository();

    /**
     * GET /api/vehicule - Liste tous les véhicules
     */
    @GetMapping("/api/vehicule")
    public ResponseEntity<List<Vehicule>> getAllVehicules() {
        try {
            List<Vehicule> vehicules = vehiculeRepository.findAll();
            return ResponseEntity.ok(vehicules);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * GET /api/vehicule/{id} - Récupère un véhicule par ID
     */
    @GetMapping("/api/vehicule/{id}")
    public ResponseEntity<Vehicule> getVehiculeById(@PathVariable("id") int id) {
        try {
            Vehicule vehicule = vehiculeRepository.findById(id);
            if (vehicule != null) {
                return ResponseEntity.ok(vehicule);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * GET /api/vehicule/capacite/{places} - Filtre les véhicules par capacité minimale
     */
    @GetMapping("/api/vehicule/capacite/{places}")
    public ResponseEntity<List<Vehicule>> getVehiculesByCapacity(@PathVariable("places") int places) {
        try {
            List<Vehicule> vehicules = vehiculeRepository.findByCapacity(places);
            return ResponseEntity.ok(vehicules);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * POST /api/vehicule - Crée un nouveau véhicule
     */
    @PostMapping("/api/vehicule")
    public ResponseEntity<Vehicule> createVehicule(@RequestBody Vehicule vehicule) {
        try {
            Vehicule created = vehiculeRepository.save(vehicule);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * PUT /api/vehicule/{id} - Met à jour un véhicule
     */
    @PutMapping("/api/vehicule/{id}")
    public ResponseEntity<Vehicule> updateVehicule(@PathVariable("id") int id, @RequestBody Vehicule vehicule) {
        try {
            vehicule.setIdVehicule(id);
            Vehicule updated = vehiculeRepository.update(vehicule);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * DELETE /api/vehicule/{id} - Supprime un véhicule
     */
    @DeleteMapping("/api/vehicule/{id}")
    public ResponseEntity<Void> deleteVehicule(@PathVariable("id") int id) {
        try {
            vehiculeRepository.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

