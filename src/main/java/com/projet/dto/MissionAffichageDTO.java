package com.projet.dto;

import com.projet.model.Reservation;
import com.projet.model.Vehicule;
import java.util.ArrayList;
import java.util.List;

public class MissionAffichageDTO {
    public int idMission;
    public Vehicule vehicule;
    public String dateDepart;
    public String dateRetour;
    public String titre;
    public List<Reservation> passagers = new ArrayList<>();

    public MissionAffichageDTO() {}

    public MissionAffichageDTO(int id, Vehicule v, String dep, String ret, String t) {
        this.idMission = id;
        this.vehicule = v;
        this.dateDepart = dep;
        this.dateRetour = ret;
        this.titre = t;
    }
}