package com.projet.model;

import java.sql.Time;

public class Distance {
    private int idDistance;
    private Time temps;
    private double distance;
    private int idLieu;
    private int idLieu1;
    
    // Pour les jointures
    private String libelleLieu;
    private String libelleLieu1;

    public Distance() {}

    public Distance(int idDistance, Time temps, double distance, int idLieu, int idLieu1) {
        this.idDistance = idDistance;
        this.temps = temps;
        this.distance = distance;
        this.idLieu = idLieu;
        this.idLieu1 = idLieu1;
    }

    // Getters
    public int getIdDistance() {
        return idDistance;
    }

    public Time getTemps() {
        return temps;
    }

    public double getDistance() {
        return distance;
    }

    public int getIdLieu() {
        return idLieu;
    }

    public int getIdLieu1() {
        return idLieu1;
    }

    public String getLibelleLieu() {
        return libelleLieu;
    }

    public String getLibelleLieu1() {
        return libelleLieu1;
    }

    // Setters
    public void setIdDistance(int idDistance) {
        this.idDistance = idDistance;
    }

    public void setTemps(Time temps) {
        this.temps = temps;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setIdLieu(int idLieu) {
        this.idLieu = idLieu;
    }

    public void setIdLieu1(int idLieu1) {
        this.idLieu1 = idLieu1;
    }

    public void setLibelleLieu(String libelleLieu) {
        this.libelleLieu = libelleLieu;
    }

    public void setLibelleLieu1(String libelleLieu1) {
        this.libelleLieu1 = libelleLieu1;
    }
}

