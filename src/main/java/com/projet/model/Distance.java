package com.projet.model;

// Plus besoin de java.sql.Time car la base est en INTEGER

public class Distance {
    private int idDistance;
    private int temps; // Doit être int pour correspondre au SQL INTEGER
    private double distance;
    private int idLieu;
    private int idLieu1;
    
    // Pour les jointures
    private String libelleLieu;
    private String libelleLieu1;

    public Distance() {}

    // --- Getters ---
    public int getIdDistance() { return idDistance; }
    public int getTemps() { return temps; } // Retourne un int
    public double getDistance() { return distance; }
    public int getIdLieu() { return idLieu; }
    public int getIdLieu1() { return idLieu1; }
    public String getLibelleLieu() { return libelleLieu; }
    public String getLibelleLieu1() { return libelleLieu1; }

    // --- Setters ---
    public void setIdDistance(int idDistance) { this.idDistance = idDistance; }
    
    // Setter corrigé pour correspondre au type INTEGER du SQL
    public void setTemps(int temps) { 
        this.temps = temps; 
    }

    public void setDistance(double distance) { this.distance = distance; }
    public void setIdLieu(int idLieu) { this.idLieu = idLieu; }
    public void setIdLieu1(int idLieu1) { this.idLieu1 = idLieu1; }
    public void setLibelleLieu(String libelleLieu) { this.libelleLieu = libelleLieu; }
    public void setLibelleLieu1(String libelleLieu1) { this.libelleLieu1 = libelleLieu1; }
}