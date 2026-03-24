package com.projet.controller;

import com.framework.annotation.*;
import com.framework.util.ResponseEntity;
import com.framework.util.HttpStatus;

import com.projet.model.Vehicule;
import com.projet.repository.VehiculeRepository;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class VehiculeController {

    private final VehiculeRepository vehiculeRepository = new VehiculeRepository();

    /**
     * GET /api/vehicule
     * Liste tous les véhicules, idéalement triés par efficacité (moins polluants en premier).
     */
    @GetMapping("/api/vehicule")
    public ResponseEntity<List<Vehicule>> getAllVehicules() {
        try {
            // Utilise la méthode findAll que nous avons optimisée
            List<Vehicule> vehicules = vehiculeRepository.findAll();
            return ResponseEntity.ok(vehicules);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * GET /api/vehicule/capacite/{places}
     * Utile si le front-end veut filtrer manuellement.
     */
    @GetMapping("/api/vehicule/capacite/{places}")
    public ResponseEntity<List<Vehicule>> getVehiculesByCapacity(@PathVariable("places") int places) {
        try {
            // Vérifie que cette méthode existe dans ton VehiculeRepository
            List<Vehicule> vehicules = vehiculeRepository.findByCapacity(places);
            return ResponseEntity.ok(vehicules);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

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

    @PutMapping("/api/vehicule/{id}")
    public ResponseEntity<Vehicule> updateVehicule(@PathVariable("id") int id, @RequestBody Vehicule vehicule) {
        try {
            vehicule.setIdVehicule(id);
            vehiculeRepository.update(vehicule);
            return ResponseEntity.ok(vehicule);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

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