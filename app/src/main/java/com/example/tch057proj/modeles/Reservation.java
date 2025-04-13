package com.example.tch057proj.modeles;

public class Reservation {
    private int id;
    private int clientId;
    private int voyageId;
    private String dateVoyage;
    private int nbPlaces;
    private double prixTotal;

    public Reservation(int id, int clientId, int voyageId, String dateVoyage, int nbPlaces, double prixTotal) {
        this.id = id;
        this.clientId = clientId;
        this.voyageId = voyageId;
        this.dateVoyage = dateVoyage;
        this.nbPlaces = nbPlaces;
        this.prixTotal = prixTotal;
    }

    // Getters et Setters
    public int getId() { return id; }
    public int getClientId() { return clientId; }
    public int getVoyageId() { return voyageId; }
    public String getDateVoyage() { return dateVoyage; }
    public int getNbPlaces() { return nbPlaces; }
    public double getPrixTotal() { return prixTotal; }
}
