package com.projet.model;

public class Carburant {
    private int idCarburant;
    private String nomCarburant;

    public Carburant() {}

    public Carburant(int idCarburant, String nomCarburant) {
        this.idCarburant = idCarburant;
        this.nomCarburant = nomCarburant;
    }

    // Getters
    public int getIdCarburant() {
        return idCarburant;
    }

    public String getNomCarburant() {
        return nomCarburant;
    }

    // Setters
    public void setIdCarburant(int idCarburant) {
        this.idCarburant = idCarburant;
    }

    public void setNomCarburant(String nomCarburant) {
        this.nomCarburant = nomCarburant;
    }
}

