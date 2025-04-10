package com.example.tch057proj.modeles;

public class Categorie {
    private String nom;
    private String imageUrl;

    public Categorie(String nom, String imageUrl) {
        this.nom = nom;
        this.imageUrl = imageUrl;
    }

    public String getNom() {
        return nom;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
