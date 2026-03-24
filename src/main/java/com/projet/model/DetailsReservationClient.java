package com.projet.model;

public class DetailsReservationClient {
    private int idDetails;
    private int idReservationClient;
    private int idReservationVehicule;

    public DetailsReservationClient() {}

    public DetailsReservationClient(int idDetails, int idReservationClient, int idReservationVehicule) {
        this.idDetails = idDetails;
        this.idReservationClient = idReservationClient;
        this.idReservationVehicule = idReservationVehicule;
    }

    // --- Getters ---
    public int getIdDetails() {
        return idDetails;
    }

    public int getIdReservationClient() {
        return idReservationClient;
    }

    public int getIdReservationVehicule() {
        return idReservationVehicule;
    }

    // --- Setters ---
    public void setIdDetails(int idDetails) {
        this.idDetails = idDetails;
    }

    public void setIdReservationClient(int idReservationClient) {
        this.idReservationClient = idReservationClient;
    }

    public void setIdReservationVehicule(int idReservationVehicule) {
        this.idReservationVehicule = idReservationVehicule;
    }
}