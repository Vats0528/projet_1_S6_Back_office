package com.projet.model;

public class Hotel {
    private int idHotel;
    private String nomHotel;
    private int idLieu; 
    
    // Champ de jointure (pour l'affichage)
    private String libelleLieu;

    public Hotel() {}

    // Getters & Setters
    public int getIdHotel() { return idHotel; }
    public void setIdHotel(int idHotel) { this.idHotel = idHotel; }

    public String getNomHotel() { return nomHotel; }
    public void setNomHotel(String nomHotel) { this.nomHotel = nomHotel; }

    public int getIdLieu() { return idLieu; }
    public void setIdLieu(int idLieu) { this.idLieu = idLieu; }

    public String getLibelleLieu() { return libelleLieu; }
    public void setLibelleLieu(String libelleLieu) { this.libelleLieu = libelleLieu; }
}