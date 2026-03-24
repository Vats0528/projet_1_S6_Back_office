package com.projet.model;

public class Vehicule {
    private int idVehicule;
    private String model;
    private int nbPlace;
    private int idCarburant;
    
    // Pour les jointures
    private String nomCarburant;

    public Vehicule() {}

    public Vehicule(int idVehicule, String model, int nbPlace, int idCarburant) {
        this.idVehicule = idVehicule;
        this.model = model;
        this.nbPlace = nbPlace;
        this.idCarburant = idCarburant;
    }

    // Getters
    public int getIdVehicule() {
        return idVehicule;
    }

    public String getModel() {
        return model;
    }

    public int getNbPlace() {
        return nbPlace;
    }

    public int getIdCarburant() {
        return idCarburant;
    }

    public String getNomCarburant() {
        return nomCarburant;
    }

    // Setters
    public void setIdVehicule(int idVehicule) {
        this.idVehicule = idVehicule;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setNbPlace(int nbPlace) {
        this.nbPlace = nbPlace;
    }

    public void setIdCarburant(int idCarburant) {
        this.idCarburant = idCarburant;
    }

    public void setNomCarburant(String nomCarburant) {
        this.nomCarburant = nomCarburant;
    }
}

