package com.example.tch057proj.modeles;

public class SeatsUpdate {
    private int nb_places_disponibles;

    public SeatsUpdate(int nb_places_disponibles) {
        this.nb_places_disponibles = nb_places_disponibles;
    }

    public int getNb_places_disponibles() {
        return nb_places_disponibles;
    }

    public void setNb_places_disponibles(int nb_places_disponibles) {
        this.nb_places_disponibles = nb_places_disponibles;
    }
}

