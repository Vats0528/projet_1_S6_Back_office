package com.projet.model;

public class Client {
    private int idClient;
    private String nomClient;

    public Client() {}

    public Client(int idClient, String nomClient) {
        this.idClient = idClient;
        this.nomClient = nomClient;
    }

    // Getters
    public int getIdClient() {
        return idClient;
    }

    public String getNomClient() {
        return nomClient;
    }

    // Setters
    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }
}
