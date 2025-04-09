package com.example.tch057proj.modeles;
public class Client {

    private int id;
    private String nom;
    private String prenom;
    private String courriel;
    private int age;
    private String telephone;
    private String adresse;
    private String motDePasse;

    // Constructeur vide (requis pour certaines opérations JSON)
    public Client() {
    }

    // Constructeur complet
    public Client(String nom, String prenom, String courriel, int age, String telephone, String adresse, String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.courriel = courriel;
        this.age = age;
        this.telephone = telephone;
        this.adresse = adresse;
        this.motDePasse = motDePasse;
    }

    // Getters et Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getCourriel() {
        return courriel;
    }

    public void setCourriel(String courriel) {
        this.courriel = courriel;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}