package com.example.tch057proj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.tch057proj.R;
import com.example.tch057proj.modeles.Trip;
import com.example.tch057proj.modeles.Voyage;
import com.example.tch057proj.dao.ApiClient;
import com.example.tch057proj.dao.VoyageService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoyageDetailActivity extends AppCompatActivity {

    private Voyage voyage;
    private TextView tvDestination, tvDescription, tvPrice, tvDuration, tvActivities, tvLocation, tvAvailableSeats;
    private Button btnBookNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voyage_detail);


        tvDestination = findViewById(R.id.tvDestination);
        tvDescription = findViewById(R.id.tvDescription);
        tvPrice = findViewById(R.id.tvPrice);
        tvDuration = findViewById(R.id.tvDuration);
        tvActivities = findViewById(R.id.tvActivities);
        tvLocation = findViewById(R.id.tvLocation);
        tvAvailableSeats = findViewById(R.id.tvAvailableSeats);
        tvLocation = findViewById(R.id.tvLocation);

        btnBookNow = findViewById(R.id.btnBookNow);


        int voyageId = getIntent().getIntExtra("VOYAGE_ID", -1);

        if (voyageId != -1) {
            fetchVoyageDetails(voyageId);
        } else {
            Toast.makeText(this, "No Voyage ID received", Toast.LENGTH_SHORT).show();
        }


        btnBookNow.setOnClickListener(v -> {

            Intent intent = new Intent(VoyageDetailActivity.this, ReserverActivity.class);


            intent.putExtra("VOYAGE_ID", voyage.getId());

            startActivity(intent);
        });


        findViewById(R.id.btnBack).setOnClickListener(v -> {

            finish();
        });
    }

    private void fetchVoyageDetails(int voyageId) {
        VoyageService service = ApiClient.getClient().create(VoyageService.class);
        Call<List<Voyage>> call = service.getTousLesVoyages();

        call.enqueue(new Callback<List<Voyage>>() {
            @Override
            public void onResponse(Call<List<Voyage>> call, Response<List<Voyage>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Voyage> voyages = response.body();
                    for (Voyage v : voyages) {
                        if (v.getId() == voyageId) {
                            voyage = v;
                            setUpVoyageDetails();
                            break;
                        }
                    }
                } else {
                    Toast.makeText(VoyageDetailActivity.this, "Failed to fetch voyage details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Voyage>> call, Throwable t) {
                Toast.makeText(VoyageDetailActivity.this, "Error fetching voyage details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpVoyageDetails() {
        tvDestination.setText(voyage.getNom_voyage());
        tvDescription.setText(voyage.getDescription());
        tvPrice.setText(String.valueOf("$ " + voyage.getPrix()));
        tvDuration.setText(voyage.getDuree_jours() + " jours");
        tvLocation.setText(voyage.getDestination());
        tvActivities.setText(voyage.getActivites_incluses());

        Glide.with(this)
                .load(voyage.getImage_url())
                .into((ImageView) findViewById(R.id.imageVoyage));


        List<Trip> trips = voyage.getTrips();
        if (trips != null && !trips.isEmpty()) {
            tvAvailableSeats.setText(String.valueOf(trips.get(0).getNb_places_disponibles()));
        } else {
            tvAvailableSeats.setText("pas de place disponible");
        }
    }
}
