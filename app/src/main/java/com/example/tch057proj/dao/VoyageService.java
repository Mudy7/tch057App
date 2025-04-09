package com.example.tch057proj.dao;

import com.example.tch057proj.modeles.Voyage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VoyageService {
    @GET("voyages")
    Call<List<Voyage>> getVoyagesFiltrés(
            @Query("destination_like") String destination,
            @Query("type_like") String type,
            @Query("prix_lte") Double maxPrix,
            @Query("dateDepart_like") String date
    );

    @GET("voyages")
    Call<List<Voyage>> getTousLesVoyages();
}
