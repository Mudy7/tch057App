package com.example.tch057proj.modeles;

import java.util.List;

public class Voyage {
    private int id;
    private String nom_voyage;
    private String description;
    private double prix;
    private String destination;
    private String image_url;
    private int duree_jours;
    private List<Trip> trips;
    private String type_de_voyage;
    private String activites_incluses;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom_voyage() {
        return nom_voyage;
    }

    public void setNom_voyage(String nom_voyage) {
        this.nom_voyage = nom_voyage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getDuree_jours() {
        return duree_jours;
    }

    public void setDuree_jours(int duree_jours) {
        this.duree_jours = duree_jours;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public String getType_de_voyage() {
        return type_de_voyage;
    }

    public void setType_de_voyage(String type_de_voyage) {
        this.type_de_voyage = type_de_voyage;
    }

    public String getActivites_incluses() {
        return activites_incluses;
    }

    public void setActivites_incluses(String activites_incluses) {
        this.activites_incluses = activites_incluses;
    }
}

