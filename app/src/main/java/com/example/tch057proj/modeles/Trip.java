package com.example.tch057proj.modeles;

public class Trip {
    private String date;
    private int nb_places_disponibles;


    public Trip(String date, int nb_places_disponibles) {
        this.date = date;
        this.nb_places_disponibles = nb_places_disponibles;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNb_places_disponibles() {
        return nb_places_disponibles;
    }

    public void setNb_places_disponibles(int nb_places_disponibles) {
        this.nb_places_disponibles = nb_places_disponibles;
    }
}
