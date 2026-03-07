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

import com.projet.model.ParamVehicule;
import com.projet.repository.ParamVehiculeRepository;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class ParamVehiculeController {

    private ParamVehiculeRepository paramVehiculeRepository = new ParamVehiculeRepository();

    /**
     * GET /api/param-vehicule - Liste tous les paramètres de véhicule
     */
    @GetMapping("/api/param-vehicule")
    public ResponseEntity<List<ParamVehicule>> getAllParamVehicules() {
        try {
            List<ParamVehicule> params = paramVehiculeRepository.findAll();
            return ResponseEntity.ok(params);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * GET /api/param-vehicule/global - Récupère les paramètres globaux (premier enregistrement)
     */
    @GetMapping("/api/param-vehicule/global")
    public ResponseEntity<ParamVehicule> getGlobalParamVehicule() {
        try {
            ParamVehicule param = paramVehiculeRepository.getGlobal();
            if (param != null) {
                return ResponseEntity.ok(param);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * GET /api/param-vehicule/{id} - Récupère un paramètre par ID
     */
    @GetMapping("/api/param-vehicule/{id}")
    public ResponseEntity<ParamVehicule> getParamVehiculeById(@PathVariable("id") int id) {
        try {
            ParamVehicule param = paramVehiculeRepository.findById(id);
            if (param != null) {
                return ResponseEntity.ok(param);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * POST /api/param-vehicule - Crée un nouveau paramètre
     */
    @PostMapping("/api/param-vehicule")
    public ResponseEntity<ParamVehicule> createParamVehicule(@RequestBody ParamVehicule param) {
        try {
            ParamVehicule created = paramVehiculeRepository.save(param);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * PUT /api/param-vehicule/{id} - Met à jour un paramètre
     */
    @PutMapping("/api/param-vehicule/{id}")
    public ResponseEntity<ParamVehicule> updateParamVehicule(@PathVariable("id") int id, @RequestBody ParamVehicule param) {
        try {
            param.setIdParamVehicule(id);
            ParamVehicule updated = paramVehiculeRepository.update(param);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * DELETE /api/param-vehicule/{id} - Supprime un paramètre
     */
    @DeleteMapping("/api/param-vehicule/{id}")
    public ResponseEntity<Void> deleteParamVehicule(@PathVariable("id") int id) {
        try {
            paramVehiculeRepository.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

