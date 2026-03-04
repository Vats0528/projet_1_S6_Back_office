package com.projet.service;

import com.projet.model.Token;
import com.projet.repository.TokenRepository;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class TokenService {

    private final TokenRepository tokenRepository = new TokenRepository();
    private final SecureRandom random = new SecureRandom();

    // Génération alphanumérique
    private String generateRandomToken(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }

        return sb.toString();
    }

    // Génération avec vérification unicité
    public Token generateToken(String niveau) throws SQLException {

        String valeur;
        do {
            valeur = generateRandomToken(32);
        } while (tokenRepository.existsByValeur(valeur));

        Token token = new Token();
        token.setValeurToken(valeur);
        token.setNiveau(niveau);
        token.setDateCreation(LocalDateTime.now());
        token.setDateExpiration(LocalDateTime.now().plusHours(24));
        token.setEstActif(true);

        return tokenRepository.save(token);
    }

    // Validation interne
    public boolean validateToken(String valeurToken) throws SQLException {
        return tokenRepository.findValidToken(valeurToken).isPresent();
    }

    public void deactivateToken(String valeurToken) throws Exception {
        tokenRepository.deactivate(valeurToken);
    }
}