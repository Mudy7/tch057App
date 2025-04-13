package com.example.tch057proj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tch057proj.R;
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

    private Voyage voyage;
    private TextView tvPrice, tvAvailableSeats, tvTotalPrice;
    private Spinner spinnerDate;
    private EditText etSeats;
    private Button btnConfirm;
    private ImageButton btnBack;
    private boolean isAvailabilityChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserver);

        tvPrice = findViewById(R.id.tvPriceLabel);
        tvAvailableSeats = findViewById(R.id.tvAvailableSeats);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        spinnerDate = findViewById(R.id.spinnerDate);
        etSeats = findViewById(R.id.etSeats);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnBack = findViewById(R.id.btnBack);

        btnConfirm.setText("Vérifier la disponibilité");


        int voyageId = getIntent().getIntExtra("VOYAGE_ID", -1);

        if (voyageId != -1) {
            fetchVoyageDetails(voyageId);
        } else {
            Toast.makeText(this, "Voyage ID non reçu", Toast.LENGTH_SHORT).show();
        }


        btnConfirm.setOnClickListener(v -> {
            String seatsInput = etSeats.getText().toString().trim();

            if (!seatsInput.isEmpty()) {
                int selectedSeats = Integer.parseInt(seatsInput);
                String tripDate = voyage.getTrips().get(spinnerDate.getSelectedItemPosition()).getDate();
                int availableSeats = voyage.getTrips().get(spinnerDate.getSelectedItemPosition()).getNb_places_disponibles();

                if (!isAvailabilityChecked) {
                    if (selectedSeats <= availableSeats) {
                        double totalPrice = selectedSeats * voyage.getPrix();
                        tvTotalPrice.setText("Prix total: " + totalPrice + " $");
                        btnConfirm.setText("Confirmer la Reservation");
                        isAvailabilityChecked = true;

                    } else {
                        Toast.makeText(this, "Pas assez de places disponibles", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Trip selectedTrip = voyage.getTrips().get(spinnerDate.getSelectedItemPosition());
                    selectedTrip.setNb_places_disponibles(availableSeats - selectedSeats);


                    VoyageService service = ApiClient.getClient().create(VoyageService.class);
                    Call<Voyage> call = service.updateVoyage(voyage.getId(), voyage);
                    call.enqueue(new Callback<Voyage>() {
                        @Override
                        public void onResponse(Call<Voyage> call, Response<Voyage> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(ReserverActivity.this, "Réservation complète", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(ReserverActivity.this, AccueilActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(ReserverActivity.this, "Réservation incomplète", Toast.LENGTH_SHORT).show();
                            }

                            isAvailabilityChecked = false;
                            btnConfirm.setText("Vérifier la disponibilité");
                        }

                        @Override
                        public void onFailure(Call<Voyage> call, Throwable t) {
                            Toast.makeText(ReserverActivity.this, "Erreur lors de la réservation", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            } else {
                Toast.makeText(this, "Veuillez entrer le nombre de places", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> {
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
                            setUpReserverDetails();
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

    private void setUpReserverDetails() {
        tvPrice.setText("Prix par siège: " + voyage.getPrix() + " $");

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

    private String formatDateString(String dateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.FRENCH);
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

            if (tripDateMillis >= currentTime) {
                spinnerDate.setSelection(i);
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
