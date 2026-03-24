package com.projet.model;

import java.sql.Time;

public class Distance {
    private int idDistance;
    private String temps; // Passé en String pour éviter l'erreur 500
    private double distance;
    private int idLieu;
    private int idLieu1;
    
    // Pour les jointures
    private String libelleLieu;
    private String libelleLieu1;

    public Distance() {}

    // --- Getters ---
    public int getIdDistance() { return idDistance; }
    public String getTemps() { return temps; }
    public double getDistance() { return distance; }
    public int getIdLieu() { return idLieu; }
    public int getIdLieu1() { return idLieu1; }
    public String getLibelleLieu() { return libelleLieu; }
    public String getLibelleLieu1() { return libelleLieu1; }

    // --- Setters ---
    public void setIdDistance(int idDistance) { this.idDistance = idDistance; }
    public void setDistance(double distance) { this.distance = distance; }
    public void setIdLieu(int idLieu) { this.idLieu = idLieu; }
    public void setIdLieu1(int idLieu1) { this.idLieu1 = idLieu1; }
    public void setLibelleLieu(String libelleLieu) { this.libelleLieu = libelleLieu; }
    public void setLibelleLieu1(String libelleLieu1) { this.libelleLieu1 = libelleLieu1; }

    // Setters pour le temps (Overloading)
    public void setTemps(String temps) { 
        this.temps = temps; 
    }
    
    public void setTemps(Time t) {
        this.temps = (t != null) ? t.toString() : null;
    }
}