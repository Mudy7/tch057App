package com.example.tch057proj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tch057proj.R;
import com.example.tch057proj.dao.ApiClient;
import com.example.tch057proj.dao.ClientService;
import com.example.tch057proj.dao.SessionManager;
import com.example.tch057proj.modeles.Client;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText etCourriel, etMotDePasse;
    Button btnConnexion;
    TextView tvInscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCourriel = findViewById(R.id.etCourriel);
        etMotDePasse = findViewById(R.id.etMotDePasse);
        btnConnexion = findViewById(R.id.btnConnexion);
        tvInscription = findViewById(R.id.tvInscription);

        // 🔗 Lien vers l'écran d'inscription
        tvInscription.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, InscriptionActivity.class);
            startActivity(intent);
        });

        // 🔐 Connexion
        btnConnexion.setOnClickListener(v -> {
            String courriel = etCourriel.getText().toString().trim();
            String motDePasse = etMotDePasse.getText().toString().trim();

            if (courriel.isEmpty() || motDePasse.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            ClientService service = ApiClient.getClient().create(ClientService.class);
            Call<List<Client>> call = service.getClientParIdentifiants(courriel, motDePasse);

            call.enqueue(new Callback<List<Client>>() {
                @Override
                public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                        Client clientConnecte = response.body().get(0);
                        int clientId = clientConnecte.getId();

                        SessionManager sessionManager = new SessionManager(MainActivity.this);
                        sessionManager.saveClientId(clientId);

                        Log.d("SESSION", "clientId sauvegardé : " + clientConnecte.getId());

                        Toast.makeText(MainActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, AccueilActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }

                @Override
                public void onFailure(Call<List<Client>> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
