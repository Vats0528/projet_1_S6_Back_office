package com.projet.controller;

import com.framework.annotation.RestController;
import com.framework.annotation.GetMapping;
import com.framework.annotation.PostMapping;
import com.framework.annotation.DeleteMapping;
import com.framework.annotation.PathVariable;
import com.framework.annotation.RequestBody;
import com.framework.annotation.CrossOrigin;
import com.framework.util.ResponseEntity;
import com.framework.util.HttpStatus;

import com.projet.model.Token;
import com.projet.service.TokenService;

@RestController
@CrossOrigin(origins = "*")
public class TokenController {

    private TokenService tokenService = new TokenService();

    /**
     * POST /api/token/generate
     * Génère un nouveau token selon le niveau
     */
    @PostMapping("/api/token/generate")
    public ResponseEntity<Token> generateToken(@RequestBody Token request) {
        try {
            if (request.getNiveau() == null || request.getNiveau().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(null);
            }

            Token created = tokenService.generateToken(request.getNiveau());
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(created);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * GET /api/token/validate/{valeur}
     * Vérifie si un token est valide
     */
    @GetMapping("/api/token/validate/{valeur}")
    public ResponseEntity<Boolean> validateToken(@PathVariable("valeur") String valeur) {
        try {
            boolean isValid = tokenService.validateToken(valeur);
            return ResponseEntity.ok(isValid);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(false);
        }
    }

    /**
     * DELETE /api/token/{valeur}
     * Désactive un token
     */
    @DeleteMapping("/api/token/{valeur}")
    public ResponseEntity<Void> deactivateToken(@PathVariable("valeur") String valeur) {
        try {
            tokenService.deactivateToken(valeur);
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(null);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}