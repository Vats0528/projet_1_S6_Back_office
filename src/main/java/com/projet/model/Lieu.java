package com.projet.model;

public class Lieu {
    private int idLieu ; 
    private String code ;
    private String libelle ; 


    public Lieu () {}

    public Lieu (int idLieu  , String code , String libelle ) {
        this.idLieu  = idLieu ; 
        this.code    = code   ; 
        this.libelle = libelle;
    }

    //Getters
    public int getIdLieu() {
        return idLieu ; 
    }

    public String getCode() {
        return code ; 
    }
    public String getlibelle(){
        return libelle ; 
    }

    //Setters
    public void setIdLieu(int idLieu) {
        this.idLieu = idLieu ;
    }

    public void setIdCode(String code){
        this.code = code ; 
    }
    public void setLiebelle(String libelle){
        this.libelle = libelle ; 
    }

}
