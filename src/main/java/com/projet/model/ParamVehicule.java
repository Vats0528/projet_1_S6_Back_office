package com.projet.model;

public class ParamVehicule {
    private int idParamVehicule;
    private int vitessMoyenne; // Correction : vitess_moyenne en DB (un seul 's')
    private int tempsAttente;
    private int taS;           // Correction : ta_s présent en DB

    public ParamVehicule() {}

    public int getIdParamVehicule() { return idParamVehicule; }
    public void setIdParamVehicule(int id) { this.idParamVehicule = id; }
    public int getVitessMoyenne() { return vitessMoyenne; }
    public void setVitessMoyenne(int v) { this.vitessMoyenne = v; }
    public int getTempsAttente() { return tempsAttente; }
    public void setTempsAttente(int t) { this.tempsAttente = t; }
    public int getTaS() { return taS; }
    public void setTaS(int taS) { this.taS = taS; }
}