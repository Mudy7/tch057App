package com.example.tch057proj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tch057proj.R;
import com.example.tch057proj.dao.ReservationDAO;
import com.example.tch057proj.modeles.Reservation;
import com.example.tch057proj.modeles.SeatsUpdate;
import com.example.tch057proj.modeles.Trip;
import com.example.tch057proj.modeles.Voyage;
import com.example.tch057proj.dao.ApiClient;
import com.example.tch057proj.dao.VoyageService;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReserverActivity extends AppCompatActivity {

    private Voyage voyage;  // Object representing the selected Voyage
    private TextView tvPrice, tvAvailableSeats, tvTotalPrice;
    private Spinner spinnerDate; // Spinner for selecting a trip date
    private EditText etSeats; // EditText for inputting the number of seats to book
    private Button btnConfirm; // Button to confirm the booking
    private ImageButton btnBack;  // Back button, ImageButton type
    private boolean isAvailabilityChecked = false; // Flag to track the button state


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserver);

        // Initialize the views
        tvPrice = findViewById(R.id.tvPriceLabel);
        tvAvailableSeats = findViewById(R.id.tvAvailableSeats);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        spinnerDate = findViewById(R.id.spinnerDate);
        etSeats = findViewById(R.id.etSeats);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnBack = findViewById(R.id.btnBack); // Initialize the ImageButton for Back

        // Set initial button text to "Vérifier la disponibilité"
        btnConfirm.setText("Vérifier la disponibilité");

        // Get the passed voyageId from the previous activity
        int voyageId = getIntent().getIntExtra("VOYAGE_ID", -1);

        if (voyageId != -1) {
            fetchVoyageDetails(voyageId);  // Fetch voyage details based on the ID
        } else {
            Toast.makeText(this, "Voyage ID non reçu", Toast.LENGTH_SHORT).show();
        }

        // Confirm Button OnClick Listener
        // Inside your btnConfirm.setOnClickListener:
        btnConfirm.setOnClickListener(v -> {
            // Get the number of seats from the EditText
            String seatsInput = etSeats.getText().toString().trim();

            if (!seatsInput.isEmpty()) {
                int selectedSeats = Integer.parseInt(seatsInput);  // Convert input to int
                String tripDate = voyage.getTrips().get(spinnerDate.getSelectedItemPosition()).getDate();  // Get selected trip date
                int availableSeats = voyage.getTrips().get(spinnerDate.getSelectedItemPosition()).getNb_places_disponibles();

                if (!isAvailabilityChecked) {
                    // Check if the selected number of seats is available
                    if (selectedSeats <= availableSeats) {
                        // Calculate total price and update the text
                        double totalPrice = selectedSeats * voyage.getPrix(); // Calculate total price
                        tvTotalPrice.setText("Prix total: " + totalPrice + " $");

                        // Change the button text to "Confirmer la Reservation"
                        btnConfirm.setText("Confirmer la Reservation");

                        // Update the flag to indicate that availability has been checked
                        isAvailabilityChecked = true;

                    } else {
                        Toast.makeText(this, "Pas assez de places disponibles", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // If the button was clicked a second time, make the API call to update available seats
                    Trip selectedTrip = voyage.getTrips().get(spinnerDate.getSelectedItemPosition());
                    selectedTrip.setNb_places_disponibles(availableSeats - selectedSeats); // Update available seats

                    // Now send the updated voyage object with the modified trips
                    VoyageService service = ApiClient.getClient().create(VoyageService.class);
                    Call<Voyage> call = service.updateVoyage(voyage.getId(), voyage); // Send the entire updated voyage object
                    call.enqueue(new Callback<Voyage>() {
                        @Override
                        public void onResponse(Call<Voyage> call, Response<Voyage> response) {
                            if (response.isSuccessful()) {
                                // Once updated, display reservation complete message
                                Toast.makeText(ReserverActivity.this, "Réservation complète", Toast.LENGTH_SHORT).show();

                                SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
                                int clientId = sharedPreferences.getInt("clientId", -1);


                                ReservationDAO reservationDAO = new ReservationDAO(ReserverActivity.this);
                                reservationDAO.insertReservation(new Reservation(
                                        0,  // id auto-incrémenté
                                        clientId,  // récupéré via SharedPreferences
                                        voyage.getId(),
                                        tripDate,
                                        selectedSeats,
                                        selectedSeats * voyage.getPrix()
                                ));


                                // After successful reservation, go back to the AccueilActivity (Home screen)
                                Intent intent = new Intent(ReserverActivity.this, AccueilActivity.class);
                                startActivity(intent);
                                finish(); // Finish this activity to prevent the user from coming back to it
                            } else {
                                // In case of an issue with updating the server
                                Toast.makeText(ReserverActivity.this, "Réservation incomplète", Toast.LENGTH_SHORT).show();
                            }

                            // Reset the flag and button text for future bookings
                            isAvailabilityChecked = false;
                            btnConfirm.setText("Vérifier la disponibilité");
                        }

                        @Override
                        public void onFailure(Call<Voyage> call, Throwable t) {
                            // In case of a network failure
                            Toast.makeText(ReserverActivity.this, "Erreur lors de la réservation", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            } else {
                Toast.makeText(this, "Veuillez entrer le nombre de places", Toast.LENGTH_SHORT).show();
            }
        });




        // Back Button OnClick Listener
        btnBack.setOnClickListener(v -> {
            // Finish the activity and go back to the previous screen
            finish();  // This closes the current activity and returns to the previous one
        });
    }

    // Fetch Voyage details based on the Voyage ID
    private void fetchVoyageDetails(int voyageId) {
        VoyageService service = ApiClient.getClient().create(VoyageService.class);
        Call<List<Voyage>> call = service.getTousLesVoyages();  // Fetch all voyages

        call.enqueue(new Callback<List<Voyage>>() {
            @Override
            public void onResponse(Call<List<Voyage>> call, Response<List<Voyage>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Voyage> voyages = response.body();
                    for (Voyage v : voyages) {
                        if (v.getId() == voyageId) {  // Find the selected voyage based on ID
                            voyage = v;
                            setUpReserverDetails();  // Set up Reserver details once the data is fetched
                            break;
                        }
                    }
                } else {
                    Toast.makeText(ReserverActivity.this, "Échec de la récupération des détails du voyage", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Voyage>> call, Throwable t) {
                Toast.makeText(ReserverActivity.this, "Erreur lors de la récupération des détails du voyage", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Set up ReserverActivity details in the UI
    private void setUpReserverDetails() {
        tvPrice.setText("Prix par siège: " + voyage.getPrix() + " $");

        // Set up the Spinner with available trip dates
        List<Trip> trips = voyage.getTrips();
        ArrayAdapter<Trip> adapter = new ArrayAdapter<Trip>(this, android.R.layout.simple_spinner_item, trips) {
            @Override
            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                Trip trip = getItem(position);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                if (trip != null) {
                    String formattedDate = formatDateString(trip.getDate());
                    textView.setText(formattedDate);
                }
                return view;
            }

            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                Trip trip = getItem(position);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                if (trip != null) {
                    String formattedDate = formatDateString(trip.getDate());
                    textView.setText(formattedDate);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDate.setAdapter(adapter);

        selectNearestDate(trips);

        // Display the available seats for the first trip
        spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int availableSeats = trips.get(position).getNb_places_disponibles();
                tvAvailableSeats.setText("Places disponibles: " + availableSeats);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    // Convert date string to a more readable format
    private String formatDateString(String dateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.FRENCH); // Set to French locale
        try {
            Date date = inputFormat.parse(dateString);
            if (date != null) {
                return outputFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }

    private void selectNearestDate(List<Trip> trips) {
        long currentTime = System.currentTimeMillis();

        for (int i = 0; i < trips.size(); i++) {
            Trip trip = trips.get(i);
            long tripDateMillis = convertDateToMillis(trip.getDate());

            // Check if the trip date is after or equal to the current date
            if (tripDateMillis >= currentTime) {
                spinnerDate.setSelection(i);  // Automatically select the nearest date
                break;
            }
        }
    }

    private long convertDateToMillis(String dateString) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = format.parse(dateString);
            if (date != null) {
                return date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
