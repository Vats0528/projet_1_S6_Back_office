package com.projet.model;

public class Hotel {
    private int idHotel;
    private String nomHotel;
    private int idLieu; // Clé étrangère vers la table lieu

    public Hotel() {}

    public Hotel(int idHotel, String nomHotel, int idLieu) {
        this.idHotel = idHotel;
        this.nomHotel = nomHotel;
        this.idLieu = idLieu;
    }

    // Getters
    public int getIdHotel() { return idHotel; }
    public String getNomHotel() { return nomHotel; }
    public int getIdLieu() { return idLieu; }

    // Setters
    public void setIdHotel(int idHotel) { this.idHotel = idHotel; }
    public void setNomHotel(String nomHotel) { this.nomHotel = nomHotel; }
    public void setIdLieu(int idLieu) { this.idLieu = idLieu; }
}