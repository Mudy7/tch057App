package com.example.tch057proj.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.tch057proj.R;
import com.example.tch057proj.adapter.TripAdapter;
import com.example.tch057proj.modeles.Voyage;
import com.example.tch057proj.modeles.Trip;
import com.example.tch057proj.dao.ApiClient;
import com.example.tch057proj.dao.VoyageService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoyageDetailActivity extends AppCompatActivity {

    private Voyage voyage; // Object representing the selected Voyage
    private TextView tvDestination, tvDescription, tvPrice, tvSelectDate, tvAvailableSeats;
    private Spinner spinnerDate; // Spinner for selecting a trip date
    private EditText etSeats; // EditText for inputting the number of seats to book
    private Button btnBookNow; // Button to initiate the booking process

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voyage_detail);

        // Initialize the views
        tvDestination = findViewById(R.id.tvDestination);
        tvDescription = findViewById(R.id.tvDescription);
        tvPrice = findViewById(R.id.tvPrice);
        tvSelectDate = findViewById(R.id.tvSelectDate);
        spinnerDate = findViewById(R.id.spinnerDate);
        tvAvailableSeats = findViewById(R.id.tvAvailableSeats);
        etSeats = findViewById(R.id.etSeats);
        btnBookNow = findViewById(R.id.btnBookNow);

        // Get the Voyage ID passed from the previous activity
        int voyageId = getIntent().getIntExtra("VOYAGE_ID", -1);  // Get Voyage ID

        if (voyageId != -1) {
            fetchVoyageDetails(voyageId);  // Fetch details based on the Voyage ID
        } else {
            Toast.makeText(this, "No Voyage ID received", Toast.LENGTH_SHORT).show();
        }

        // Book Now Button OnClick Listener
        btnBookNow.setOnClickListener(v -> {
            // Get the number of seats from the EditText
            int selectedSeats = Integer.parseInt(etSeats.getText().toString());
            // Get the selected trip's available seats
            int availableSeats = voyage.getTrips().get(spinnerDate.getSelectedItemPosition()).getNb_places_disponibles();

            // Check if the selected number of seats is available
            if (selectedSeats <= availableSeats) {
                // Proceed with booking logic (e.g., update server, show confirmation)
                Toast.makeText(this, "Booking successful", Toast.LENGTH_SHORT).show();
                // Update the available seats after booking
                voyage.getTrips().get(spinnerDate.getSelectedItemPosition()).setNb_places_disponibles(availableSeats - selectedSeats);
            } else {
                // Inform the user that not enough seats are available
                Toast.makeText(this, "Not enough seats available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Fetch Voyage details based on the Voyage ID
    private void fetchVoyageDetails(int voyageId) {
        VoyageService service = ApiClient.getClient().create(VoyageService.class);
        Call<Voyage> call = service.getVoyageById(voyageId);  // Make an API call to fetch voyage by ID

        call.enqueue(new Callback<Voyage>() {
            @Override
            public void onResponse(Call<Voyage> call, Response<Voyage> response) {
                if (response.isSuccessful() && response.body() != null) {
                    voyage = response.body();
                    setUpVoyageDetails();  // Set up Voyage details once the data is fetched
                } else {
                    Toast.makeText(VoyageDetailActivity.this, "Failed to fetch voyage details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Voyage> call, Throwable t) {
                Toast.makeText(VoyageDetailActivity.this, "Error fetching voyage details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Set up Voyage details in the UI
    private void setUpVoyageDetails() {
        tvDestination.setText(voyage.getDestination());
        tvDescription.setText(voyage.getDescription());
        tvPrice.setText("Prix: " + voyage.getPrix() + " $");

        Glide.with(this)
                .load(voyage.getImage_url())
                .into((ImageView) findViewById(R.id.imageVoyage));

        // Set up the Spinner with available trip dates
        List<Trip> trips = voyage.getTrips();
        TripAdapter adapter = new TripAdapter(this, trips);
        spinnerDate.setAdapter(adapter);

        // Set the number of available seats based on the selected date
        spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int availableSeats = trips.get(position).getNb_places_disponibles();
                tvAvailableSeats.setText("Places disponibles: " + availableSeats);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle if nothing is selected
            }
        });
    }
}
