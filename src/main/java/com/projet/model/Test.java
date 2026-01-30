package com.projet.model;

import java.sql.Date;

public class Test {
    private int id;
    private String text;
    private String date;

    public Test() {}

    public Test(int id, String text, String date) {
        this.id = id;
        this.text = text;
        this.date = date;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    // Setter pour accepter java.sql.Date depuis le repository
    public void setDate(Date date) {
        this.date = date != null ? date.toString() : null;
    }
}
