package com.projet.model;

public class DetailsReservationClient {
    // Suppression de idDetails car absent du SQL
    private int idReservationClient;
    private int idReservationVehicule;

    public DetailsReservationClient() {}

    // Constructeur mis à jour
    public DetailsReservationClient(int idReservationClient, int idReservationVehicule) {
        this.idReservationClient = idReservationClient;
        this.idReservationVehicule = idReservationVehicule;
    }

    // --- Getters ---
    public int getIdReservationClient() {
        return idReservationClient;
    }

    public int getIdReservationVehicule() {
        return idReservationVehicule;
    }

    // --- Setters ---
    public void setIdReservationClient(int idReservationClient) {
        this.idReservationClient = idReservationClient;
    }

    public void setIdReservationVehicule(int idReservationVehicule) {
        this.idReservationVehicule = idReservationVehicule;
    }
}