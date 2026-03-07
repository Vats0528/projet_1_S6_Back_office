package com.projet.model;

public class ParamVehicule {
    private int idParamVehicule;
    private int vitessMoyenne;
    private int tempsAttente;

    public ParamVehicule() {}

    public ParamVehicule(int idParamVehicule, int vitessMoyenne, int tempsAttente) {
        this.idParamVehicule = idParamVehicule;
        this.vitessMoyenne = vitessMoyenne;
        this.tempsAttente = tempsAttente;
    }

    // Getters
    public int getIdParamVehicule() {
        return idParamVehicule;
    }

    public int getVitessMoyenne() {
        return vitessMoyenne;
    }

    public int getTempsAttente() {
        return tempsAttente;
    }

    // Setters
    public void setIdParamVehicule(int idParamVehicule) {
        this.idParamVehicule = idParamVehicule;
    }

    public void setVitessMoyenne(int vitessMoyenne) {
        this.vitessMoyenne = vitessMoyenne;
    }

    public void setTempsAttente(int tempsAttente) {
        this.tempsAttente = tempsAttente;
    }
}

