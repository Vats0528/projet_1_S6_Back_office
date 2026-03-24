package com.projet.model;
import java.time.LocalDateTime;

public class Token {

    private int idToken;
    private String valeurToken;
    private String niveau;
    private LocalDateTime dateCreation;
    private LocalDateTime dateExpiration;
    private boolean estActif;

    public Token() {}

    public Token(int idToken, String valeurToken, String niveau,
                 LocalDateTime dateCreation, LocalDateTime dateExpiration,
                 boolean estActif) {
        this.idToken = idToken;
        this.valeurToken = valeurToken;
        this.niveau = niveau;
        this.dateCreation = dateCreation;
        this.dateExpiration = dateExpiration;
        this.estActif = estActif;
    }

    // ===== Getters =====

    public int getIdToken() {
        return idToken;
    }

    public String getValeurToken() {
        return valeurToken;
    }

    public String getNiveau() {
        return niveau;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public LocalDateTime getDateExpiration() {
        return dateExpiration;
    }

    public boolean isEstActif() {
        return estActif;
    }

    // ===== Setters =====

    public void setIdToken(int idToken) {
        this.idToken = idToken;
    }

    public void setValeurToken(String valeurToken) {
        this.valeurToken = valeurToken;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setDateExpiration(LocalDateTime dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public void setEstActif(boolean estActif) {
        this.estActif = estActif;
    }
}