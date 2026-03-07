package com.projet.model;

import java.sql.Timestamp;

public class ReservationVehicule {
    private int idReservationVehicule;
    private Timestamp dateHeureDepart;
    private Timestamp dateHeureRetour;
    private int idVehicule;
    
    // Pour les jointures
    private String modelVehicule;
    private int nbPlaceVehicule;
    private String nomCarburant;

    public ReservationVehicule() {}

    // Getters
    public int getIdReservationVehicule() {
        return idReservationVehicule;
    }

    public Timestamp getDateHeureDepart() {
        return dateHeureDepart;
    }

    public Timestamp getDateHeureRetour() {
        return dateHeureRetour;
    }

    public int getIdVehicule() {
        return idVehicule;
    }

    public String getModelVehicule() {
        return modelVehicule;
    }

    public int getNbPlaceVehicule() {
        return nbPlaceVehicule;
    }

    public String getNomCarburant() {
        return nomCarburant;
    }

    // Setters
    public void setIdReservationVehicule(int idReservationVehicule) {
        this.idReservationVehicule = idReservationVehicule;
    }

    public void setDateHeureDepart(Timestamp dateHeureDepart) {
        this.dateHeureDepart = dateHeureDepart;
    }

    public void setDateHeureRetour(Timestamp dateHeureRetour) {
        this.dateHeureRetour = dateHeureRetour;
    }

    public void setIdVehicule(int idVehicule) {
        this.idVehicule = idVehicule;
    }

    public void setModelVehicule(String modelVehicule) {
        this.modelVehicule = modelVehicule;
    }

    public void setNbPlaceVehicule(int nbPlaceVehicule) {
        this.nbPlaceVehicule = nbPlaceVehicule;
    }

    public void setNomCarburant(String nomCarburant) {
        this.nomCarburant = nomCarburant;
    }
}

