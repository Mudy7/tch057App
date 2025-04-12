package com.example.tch057proj.dao;

import com.example.tch057proj.modeles.SeatsUpdate;
import com.example.tch057proj.modeles.Voyage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Body;
import retrofit2.http.Query;

public interface VoyageService {
    @GET("voyages")
    Call<List<Voyage>> getVoyagesFiltrés(
            @Query("destination_like") String destination,
            @Query("type_de_voyage") String type,
            @Query("prix_lte") Double maxPrix,
            @Query("dateDepart_like") String date
    );

    @GET("voyages/{id}")
    Call<Voyage> getVoyageById(@Path("id") int id);


    @GET("voyages")
    Call<List<Voyage>> getTousLesVoyages();

    @PUT("voyages/{voyageId}")
    Call<Voyage> updateVoyage(@Path("voyageId") int voyageId, @Body Voyage updatedVoyage);

}
