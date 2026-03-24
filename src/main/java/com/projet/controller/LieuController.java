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

import com.projet.model.Lieu;
import com.projet.repository.LieuRepository;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class LieuController {

    private LieuRepository lieuRepository = new LieuRepository();

    /**
     * GET /api/lieu - Liste tous les lieux
     */
    @GetMapping("/api/lieu")
    public ResponseEntity<List<Lieu>> getAllLieux() {
        try {
            List<Lieu> lieux = lieuRepository.findAll();
            return ResponseEntity.ok(lieux);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * GET /api/lieu/{id} - Récupère un lieu par ID
     */
    @GetMapping("/api/lieu/{id}")
    public ResponseEntity<Lieu> getLieuById(@PathVariable("id") int id) {
        try {
            Lieu lieu = lieuRepository.findById(id);
            if (lieu != null) {
                return ResponseEntity.ok(lieu);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * POST /api/lieu - Crée un nouveau lieu
     */
    @PostMapping("/api/lieu")
    public ResponseEntity<Lieu> createLieu(@RequestBody Lieu lieu) {
        try {
            Lieu created = lieuRepository.save(lieu);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * PUT /api/lieu/{id} - Met à jour un lieu
     */
    @PutMapping("/api/lieu/{id}")
    public ResponseEntity<Lieu> updateLieu(@PathVariable("id") int id, @RequestBody Lieu lieu) {
        try {
            lieu.setIdLieu(id);
            Lieu updated = lieuRepository.update(lieu);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * DELETE /api/lieu/{id} - Supprime un lieu
     */
    @DeleteMapping("/api/lieu/{id}")
    public ResponseEntity<Void> deleteLieu(@PathVariable("id") int id) {
        try {
            lieuRepository.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

