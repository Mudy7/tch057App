package com.example.tch057proj.activity;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tch057proj.R;
import com.example.tch057proj.dao.ApiClient;
import com.example.tch057proj.dao.ClientService;
import com.example.tch057proj.dao.ClientService;
import com.example.tch057proj.modeles.Client;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InscriptionActivity extends AppCompatActivity {

    EditText etNom, etPrenom, etCourriel, etAge, etTelephone, etAdresse, etMotDePasse;
    Button btnInscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        etNom = findViewById(R.id.etNom);
        etPrenom = findViewById(R.id.etPrenom);
        etCourriel = findViewById(R.id.etCourriel);
        etAge = findViewById(R.id.etAge);
        etTelephone = findViewById(R.id.etTelephone);
        etAdresse = findViewById(R.id.etAdresse);
        etMotDePasse = findViewById(R.id.etMotDePasse);
        btnInscription = findViewById(R.id.btnInscription);

        btnInscription.setOnClickListener(v -> {
            String nom = etNom.getText().toString();
            String prenom = etPrenom.getText().toString();
            String courriel = etCourriel.getText().toString();
            int age = Integer.parseInt(etAge.getText().toString());
            String telephone = etTelephone.getText().toString();
            String adresse = etAdresse.getText().toString();
            String motDePasse = etMotDePasse.getText().toString();

            Client utilisateur = new Client(nom, prenom, courriel, age, telephone, adresse, motDePasse);

            ClientService service = ApiClient.getClient().create(ClientService.class);
            Call<Client> call = service.ajouterClient(utilisateur);

            call.enqueue(new Callback<Client>() {
                @Override
                public void onResponse(Call<Client> call, Response<Client> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(InscriptionActivity.this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
                        finish(); // revenir à l’écran précédent
                    } else {
                        Toast.makeText(InscriptionActivity.this, "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Client> call, Throwable t) {
                    Toast.makeText(InscriptionActivity.this, "Échec : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
