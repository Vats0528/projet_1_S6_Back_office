package com.projet.model;

import java.sql.Timestamp;

public class Reservation {
    private int idReservation;
    private int nbPassager;
    private String dateHeureArrivee;
    private int idHotel;
    private int idClient;
    
    // Pour les jointures
    private String nomHotel;
    private String nomClient;

    public Reservation() {}

    public Reservation(int idReservation, int nbPassager, String dateHeureArrivee, 
                       int idHotel, int idClient) {
        this.idReservation = idReservation;
        this.nbPassager = nbPassager;
        this.dateHeureArrivee = dateHeureArrivee;
        this.idHotel = idHotel;
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

    public int getIdHotel() {
        return idHotel;
    }

    public int getIdClient() {
        return idClient;
    }

    public String getNomHotel() {
        return nomHotel;
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

    public void setIdHotel(int idHotel) {
        this.idHotel = idHotel;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public void setNomHotel(String nomHotel) {
        this.nomHotel = nomHotel;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }
}
