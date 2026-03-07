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

import com.projet.model.Carburant;
import com.projet.repository.CarburantRepository;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class CarburantController {

    private CarburantRepository carburantRepository = new CarburantRepository();

    /**
     * GET /api/carburant - Liste tous les carburants
     */
    @GetMapping("/api/carburant")
    public ResponseEntity<List<Carburant>> getAllCarburants() {
        try {
            List<Carburant> carburants = carburantRepository.findAll();
            return ResponseEntity.ok(carburants);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * GET /api/carburant/{id} - Récupère un carburant par ID
     */
    @GetMapping("/api/carburant/{id}")
    public ResponseEntity<Carburant> getCarburantById(@PathVariable("id") int id) {
        try {
            Carburant carburant = carburantRepository.findById(id);
            if (carburant != null) {
                return ResponseEntity.ok(carburant);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * POST /api/carburant - Crée un nouveau carburant
     */
    @PostMapping("/api/carburant")
    public ResponseEntity<Carburant> createCarburant(@RequestBody Carburant carburant) {
        try {
            Carburant created = carburantRepository.save(carburant);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * PUT /api/carburant/{id} - Met à jour un carburant
     */
    @PutMapping("/api/carburant/{id}")
    public ResponseEntity<Carburant> updateCarburant(@PathVariable("id") int id, @RequestBody Carburant carburant) {
        try {
            carburant.setIdCarburant(id);
            Carburant updated = carburantRepository.update(carburant);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * DELETE /api/carburant/{id} - Supprime un carburant
     */
    @DeleteMapping("/api/carburant/{id}")
    public ResponseEntity<Void> deleteCarburant(@PathVariable("id") int id) {
        try {
            carburantRepository.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

