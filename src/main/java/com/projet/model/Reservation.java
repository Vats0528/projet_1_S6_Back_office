package com.projet.model;

import java.sql.Timestamp;

public class Reservation {
    private int idReservation;
    private int nbPassager;
    private String dateHeureArrivee;
    private int idLieu;
    private int idClient;
    
    // Pour les jointures
    private String libelleLieu;
    private String nomClient;

    public Reservation() {}

    public Reservation(int idReservation, int nbPassager, String dateHeureArrivee, 
                       int idLieu, int idClient) {
        this.idReservation = idReservation;
        this.nbPassager = nbPassager;
        this.dateHeureArrivee = dateHeureArrivee;
        this.idLieu = idLieu;
        this.idClient = idClient;
    }

    // Getters
    public int getIdReservation() {
        return idReservation;
    }

    public int getNbPassager() {
        return nbPassager;
    }

    public String getDateHeureArrivee() {
        return dateHeureArrivee;
    }

    public int getIdLieu() {
        return idLieu;
    }

    public int getIdClient() {
        return idClient;
    }

    public String getLibelleLieu() {
        return libelleLieu;
    }

    public String getNomClient() {
        return nomClient;
    }

    // Setters
    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public void setNbPassager(int nbPassager) {
        this.nbPassager = nbPassager;
    }

    public void setDateHeureArrivee(String dateHeureArrivee) {
        this.dateHeureArrivee = dateHeureArrivee;
    }
    
    public void setDateHeureArrivee(Timestamp dateHeureArrivee) {
        this.dateHeureArrivee = dateHeureArrivee != null ? dateHeureArrivee.toString() : null;
    }

    public void setIdLieu(int idLieu) {
        this.idLieu = idLieu;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public void setLibelleLieu(String libelleLieu) {
        this.libelleLieu = libelleLieu;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }
}
