package com.example.tch057proj.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.tch057proj.modeles.Reservation;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public ReservationDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void insertReservation(Reservation reservation) {
        ContentValues values = new ContentValues();
        values.put("client_id", reservation.getClientId());
        values.put("voyage_id", reservation.getVoyageId());
        values.put("date_voyage", reservation.getDateVoyage());
        values.put("nb_places", reservation.getNbPlaces());
        values.put("prix_total", reservation.getPrixTotal());
        db.insert(DatabaseHelper.TABLE_RESERVATION, null, values);
    }

    public List<Reservation> getReservationsByClient(int clientId) {
        List<Reservation> reservations = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_RESERVATION,
                null, "client_id = ?",
                new String[]{String.valueOf(clientId)},
                null, null, null);

        while (cursor.moveToNext()) {
            reservations.add(new Reservation(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("client_id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("voyage_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("date_voyage")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("nb_places")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("prix_total"))
            ));
        }
        cursor.close();
        return reservations;
    }
}
