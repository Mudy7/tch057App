package com.example.tch057proj.activity;

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tch057proj.R;
import com.example.tch057proj.adapter.ReservationAdapter;
import com.example.tch057proj.dao.ReservationDAO;
import com.example.tch057proj.dao.SessionManager;
import com.example.tch057proj.modeles.Reservation;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ListView listView;
    private ReservationDAO reservationDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listView = findViewById(R.id.listViewReservations);
        reservationDAO = new ReservationDAO(this);

        SessionManager sessionManager = new SessionManager(HistoryActivity.this);
        int clientId = sessionManager.getClientId();

        List<Reservation> reservations = reservationDAO.getReservationsByClient(clientId);


        ReservationAdapter adapter = new ReservationAdapter(this, reservations);
        listView.setAdapter(adapter);
    }
}
