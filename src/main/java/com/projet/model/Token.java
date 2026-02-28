package com.projet.model;

public class Token {
    private int idToken ; 
    private String niveau ;
    private String dateExpiration ; //date heure

    public Token() {}

    public Token(int idToken , String niveau, String dateExpiration) {
        this.idToken = idToken ; 
        this.niveau = niveau ; 
        this.dateExpiration = dateExpiration ; 
    }

    //Getters
    public int getIdToken(){
        return idToken ;
    }
    public String getNiveau() {
        return niveau ;
    }
    public String getDateExpiration() {
        return dateExpiration ; 
    }

    // Setters
    public void setIdToken(int idToken){
        this.idToken = idToken ; 
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau ;
    }
    public void setDateExpiration(String dateExpiration){
        this.dateExpiration = dateExpiration ; 
    }
}

