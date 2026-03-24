package com.projet.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ReservationVehicule {
    private int idReservationVehicule;
    private String titre; // Correction : Ajouté car présent en DB
    private String dateHeureDepart; 
    private String dateHeureRetour; 
    private int idVehicule;
    
    // Jointures
    private String modelVehicule;
    private int nbPlaceVehicule;
    private String nomCarburant;

    private List<Reservation> passagers = new ArrayList<>();

    public ReservationVehicule() {}

    // --- Getters & Setters ---
    public int getIdReservationVehicule() { return idReservationVehicule; }
    public void setIdReservationVehicule(int id) { this.idReservationVehicule = id; }
    
    public String getTitre() { return titre; }
    public void setTitre(String t) { this.titre = t; }

    public String getDateHeureDepart() { return dateHeureDepart; }
    public void setDateHeureDepart(String d) { this.dateHeureDepart = d; }
    public void setDateHeureDepart(Timestamp ts) { 
        this.dateHeureDepart = (ts != null) ? ts.toString() : null; 
    }

    public String getDateHeureRetour() { return dateHeureRetour; }
    public void setDateHeureRetour(String r) { this.dateHeureRetour = r; }
    public void setDateHeureRetour(Timestamp ts) { 
        this.dateHeureRetour = (ts != null) ? ts.toString() : null; 
    }

    public int getIdVehicule() { return idVehicule; }
    public void setIdVehicule(int id) { this.idVehicule = id; }
    public String getModelVehicule() { return modelVehicule; }
    public void setModelVehicule(String m) { this.modelVehicule = m; }
    public int getNbPlaceVehicule() { return nbPlaceVehicule; }
    public void setNbPlaceVehicule(int n) { this.nbPlaceVehicule = n; }
    public String getNomCarburant() { return nomCarburant; }
    public void setNomCarburant(String n) { this.nomCarburant = n; }
    public List<Reservation> getPassagers() { return passagers; }
    public void setPassagers(List<Reservation> p) { this.passagers = p; }
}