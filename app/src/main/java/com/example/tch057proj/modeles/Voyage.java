package com.example.tch057proj.modeles;

public class Voyage {
    private int id;
    private String destination;
    private String type;
    private String resume;
    private double prix;
    private String image_url;
    private String dateDepart;

    // Constructeurs, getters et setters

    public Voyage() {}

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getDestination() { return destination; }

    public void setDestination(String destination) { this.destination = destination; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getResume() { return resume; }

    public void setResume(String resume) { this.resume = resume; }

    public double getPrix() { return prix; }

    public void setPrix(double prix) { this.prix = prix; }

    public String getImage_url() { return image_url; }

    public void setImageUrl(String image_url) { this.image_url = image_url; }

    public String getDateDepart() { return dateDepart; }

    public void setDateDepart(String dateDepart) { this.dateDepart = dateDepart; }
}
