package com.projet.model;

import java.sql.Timestamp;

public class Reservation {
    private int idReservation;
    private int nbPassager;
    private String dateHeureArrivee;
    private String status; 
    private int idHotel;
    private int idClient;
    
    
    // Jointures
    private String libelleLieu;
    private String nomHotel;
    private String nomClient;
    private int idLieu;

    public Reservation() {}

    // --- Getters & Setters Standard ---
    public int getIdReservation() { return idReservation; }
    public void setIdReservation(int id) { this.idReservation = id; }
    public int getNbPassager() { return nbPassager; }
    public void setNbPassager(int nb) { this.nbPassager = nb; }
    public String getDateHeureArrivee() { return dateHeureArrivee; }
    public void setDateHeureArrivee(String d) { this.dateHeureArrivee = d; }
    
    // Le "Bridge" pour le Repository (très important)
    public void setDateHeureArrivee(Timestamp ts) { 
        this.dateHeureArrivee = (ts != null) ? ts.toString() : null; 
    }

    public String getStatus() { return status; }
    public void setStatus(String s) { this.status = s; }
    public int getIdHotel() { return idHotel; }
    public void setIdHotel(int id) { this.idHotel = id; }
    public int getIdClient() { return idClient; }
    public void setIdClient(int id) { this.idClient = id; }
    public String getLibelleLieu() { return libelleLieu; }
    public void setLibelleLieu(String l) { this.libelleLieu = l; }
    public String getNomHotel() { return nomHotel; }
    public void setNomHotel(String n) { this.nomHotel = n; }
    public String getNomClient() { return nomClient; }
    public void setNomClient(String n) { this.nomClient = n; }

    public int getIdLieu() {
        return idLieu;
    }
    
    // 3. Ajoute le Setter (C'est lui qui manque !)
    public void setIdLieu(int idLieu) {
        this.idLieu = idLieu;
    }

    
}