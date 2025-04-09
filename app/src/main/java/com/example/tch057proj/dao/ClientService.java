package com.example.tch057proj.dao;

import com.example.tch057proj.modeles.Client;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ClientService {

    // Ajouter un client
    @POST("clients")
    Call<Client> ajouterClient(@Body Client client);

    // Vérifier les identifiants
    @GET("clients")
    Call<List<Client>> getClientParIdentifiants(
            @Query("email") String email,
            @Query("mdp") String motDePasse
    );
}
