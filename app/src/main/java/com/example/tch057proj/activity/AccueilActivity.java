package com.example.tch057proj.activity;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tch057proj.R;
import com.example.tch057proj.adapter.VoyageAdapter;
import com.example.tch057proj.dao.ApiClient;
import com.example.tch057proj.dao.VoyageService;
import com.example.tch057proj.modeles.Voyage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccueilActivity extends AppCompatActivity {

    EditText etDestination, etPrixMax, etDate;
    Spinner spinnerType;
    Button btnRechercher;
    RecyclerView recyclerView;
    VoyageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        etDestination = findViewById(R.id.etDestination);
        etPrixMax = findViewById(R.id.etPrixMax);
        etDate = findViewById(R.id.etDate);
        spinnerType = findViewById(R.id.spinnerType);
        btnRechercher = findViewById(R.id.btnRechercher);
        recyclerView = findViewById(R.id.recyclerViewVoyages);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 🎯 Setup du spinner (types de voyages)
        String[] types = {"", "aventure", "culturel", "gastronomique"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);
        spinnerType.setAdapter(spinnerAdapter);

        // 🔍 Action bouton Rechercher
        btnRechercher.setOnClickListener(v -> {
            String destination = etDestination.getText().toString().trim();
            String type = spinnerType.getSelectedItem().toString();
            String prixTexte = etPrixMax.getText().toString().trim();
            String date = etDate.getText().toString().trim();

            Double prixMax = prixTexte.isEmpty() ? null : Double.parseDouble(prixTexte);

            VoyageService service = ApiClient.getClient().create(VoyageService.class);
            Call<List<Voyage>> call = service.getVoyagesFiltrés(destination, type, prixMax, date);

            call.enqueue(new Callback<List<Voyage>>() {
                @Override
                public void onResponse(Call<List<Voyage>> call, Response<List<Voyage>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        adapter = new VoyageAdapter(AccueilActivity.this, response.body());
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(AccueilActivity.this, "Aucun voyage trouvé", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Voyage>> call, Throwable t) {
                    Toast.makeText(AccueilActivity.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
