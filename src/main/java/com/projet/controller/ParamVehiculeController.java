package com.projet.controller;

import com.framework.annotation.*;
import com.framework.util.ResponseEntity;
import com.framework.util.HttpStatus;

import com.projet.model.ParamVehicule;
import com.projet.repository.ParamVehiculeRepository;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class ParamVehiculeController {

    private final ParamVehiculeRepository paramVehiculeRepository = new ParamVehiculeRepository();

    /**
     * GET /api/param-vehicule/global
     * Récupère les paramètres utilisés par le simulateur.
     */
    @GetMapping("/api/param-vehicule/global")
    public ResponseEntity<ParamVehicule> getGlobalParamVehicule() {
        try {
            // Correction : On appelle findFirst() au lieu de getGlobal()
            ParamVehicule param = paramVehiculeRepository.findFirst();
            if (param != null) {
                return ResponseEntity.ok(param);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/api/param-vehicule")
    public ResponseEntity<List<ParamVehicule>> getAllParamVehicules() {
        try {
            return ResponseEntity.ok(paramVehiculeRepository.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/api/param-vehicule/{id}")
    public ResponseEntity<ParamVehicule> updateParamVehicule(@PathVariable("id") int id, @RequestBody ParamVehicule param) {
        try {
            param.setIdParamVehicule(id);
            // On s'assure que le Repo a bien une méthode update qui renvoie l'objet ou void
            paramVehiculeRepository.update(param); 
            return ResponseEntity.ok(param);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // Les autres méthodes (POST, DELETE) sont correctes si elles existent dans ton Repo,
    // mais pour un simulateur, on ne manipule souvent qu'une seule ligne de paramètres.
}