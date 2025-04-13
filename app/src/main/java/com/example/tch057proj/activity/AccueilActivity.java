package com.example.tch057proj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tch057proj.R;
import com.example.tch057proj.adapter.MiniVoyageAdapter;
import com.example.tch057proj.adapter.VoyageAdapter;
import com.example.tch057proj.adapter.CategorieAdapter;
import com.example.tch057proj.dao.ApiClient;
import com.example.tch057proj.dao.VoyageService;
import com.example.tch057proj.modeles.Voyage;
import com.example.tch057proj.modeles.Categorie;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccueilActivity extends AppCompatActivity {

    EditText etDestination, etPrixMax, etDate;
    Spinner spinnerType;
    RecyclerView recyclerView, recyclerViewCategories, recyclerViewRandom;
    VoyageAdapter adapter;
    CategorieAdapter categorieAdapter;
    LinearLayout filterSection;
    ImageButton btnToggleFilters, btnHistory;
    TextView tvCategorieTitle, tvEmptyMessage, tvRandomTitle;


    private boolean categorySelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        etDestination = findViewById(R.id.etDestination);
        etPrixMax = findViewById(R.id.etPrixMax);
        etDate = findViewById(R.id.etDate);
        spinnerType = findViewById(R.id.spinnerType);
        recyclerView = findViewById(R.id.recyclerViewVoyages);
        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        recyclerViewRandom = findViewById(R.id.recyclerViewRandomVoyages);
        tvCategorieTitle = findViewById(R.id.tvCategorieTitle);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);
        tvRandomTitle = findViewById(R.id.tvRandomTitle);
        filterSection = findViewById(R.id.filterSection);
        btnToggleFilters = findViewById(R.id.btnToggleFilters);
        btnHistory = findViewById(R.id.btnHistory);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewRandom.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        String[] types = {"Tous les Catégorie", "Aventure", "Culturel", "Bien-être"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);
        spinnerType.setAdapter(spinnerAdapter);

        List<Categorie> categories = Arrays.asList(
                new Categorie("Culturel", "https://images.pexels.com/photos/31418360/pexels-photo-31418360/free-photo-of-serene-view-of-mount-fuji-with-cherry-blossoms.jpeg"),
                new Categorie("Aventure", "https://images.pexels.com/photos/9021404/pexels-photo-9021404.jpeg"),
                new Categorie("Bien-être", "https://images.pexels.com/photos/994605/pexels-photo-994605.jpeg"),
                new Categorie("Nature", "https://images.pexels.com/photos/147411/italy-mountains-dawn-daybreak-147411.jpeg")
        );


        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(AccueilActivity.this, HistoryActivity.class);
            startActivity(intent);
        });



        categorieAdapter = new CategorieAdapter(this, categories, selectedType -> {
            categorySelected = true;
            spinnerType.setSelection(getSpinnerIndex(spinnerType, selectedType));
            recyclerViewCategories.setVisibility(View.GONE);
            tvCategorieTitle.setVisibility(View.GONE);
            lancerRecherche();
        });

        recyclerViewCategories.setAdapter(categorieAdapter);

        btnToggleFilters.setOnClickListener(v -> {
            filterSection.setVisibility(filterSection.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });

        etDestination.setOnEditorActionListener(searchOnEnter);
        etPrixMax.setOnEditorActionListener(searchOnEnter);
        etDate.setOnEditorActionListener(searchOnEnter);

        fetchRandomVoyages();
    }

    private void lancerRecherche() {
        recyclerViewCategories.setVisibility(View.GONE);
        tvCategorieTitle.setVisibility(View.GONE);
        recyclerViewRandom.setVisibility(View.GONE);
        tvRandomTitle.setVisibility(View.GONE);

        String destination = etDestination.getText().toString().trim();
        String prixTexte = etPrixMax.getText().toString().trim();
        String date = etDate.getText().toString().trim();

        String type = spinnerType.getSelectedItem().toString();
        if (type.equals("Tous les Catégorie")) type = null;

        destination = destination.isEmpty() ? null : destination;
        date = date.isEmpty() ? null : date;
        Double prixMax = prixTexte.isEmpty() ? null : Double.parseDouble(prixTexte);

        VoyageService service = ApiClient.getClient().create(VoyageService.class);
        Call<List<Voyage>> call = service.getVoyagesFiltrés(destination, type, prixMax, date);

        call.enqueue(new Callback<List<Voyage>>() {
            @Override
            public void onResponse(Call<List<Voyage>> call, Response<List<Voyage>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Voyage> voyages = response.body();
                    if (voyages.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        tvEmptyMessage.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        tvEmptyMessage.setVisibility(View.GONE);


                        VoyageAdapter adapter = new VoyageAdapter(AccueilActivity.this, voyages, voyage -> {
                            Intent intent = new Intent(AccueilActivity.this, VoyageDetailActivity.class);
                            intent.putExtra("VOYAGE_ID", voyage.getId());
                            startActivity(intent);
                        });
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
                    tvEmptyMessage.setVisibility(View.VISIBLE);
                }
                categorySelected = false;
            }

            @Override
            public void onFailure(Call<List<Voyage>> call, Throwable t) {
                recyclerView.setVisibility(View.GONE);
                tvEmptyMessage.setText("Erreur : " + t.getMessage());
                tvEmptyMessage.setVisibility(View.VISIBLE);
                categorySelected = false;
            }
        });
    }

    private void fetchRandomVoyages() {
        VoyageService service = ApiClient.getClient().create(VoyageService.class);
        Call<List<Voyage>> call = service.getTousLesVoyages();

        call.enqueue(new Callback<List<Voyage>>() {
            @Override
            public void onResponse(Call<List<Voyage>> call, Response<List<Voyage>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<Voyage> all = response.body();
                    Collections.shuffle(all);
                    List<Voyage> random = all.subList(0, Math.min(5, all.size()));

                    MiniVoyageAdapter miniAdapter = new MiniVoyageAdapter(AccueilActivity.this, random, new MiniVoyageAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Voyage voyage) {
                            Intent intent = new Intent(AccueilActivity.this, VoyageDetailActivity.class);
                            intent.putExtra("VOYAGE_ID", voyage.getId());
                            startActivity(intent);
                        }
                    });

                    recyclerViewRandom.setAdapter(miniAdapter);

                    tvRandomTitle.setVisibility(View.VISIBLE);
                    recyclerViewRandom.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Voyage>> call, Throwable t) {
            }
        });
    }

    private final TextView.OnEditorActionListener searchOnEnter = (v, actionId, event) -> {
        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
            lancerRecherche();
            return true;
        }
        return false;
    };

    private int getSpinnerIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0;
    }
}
