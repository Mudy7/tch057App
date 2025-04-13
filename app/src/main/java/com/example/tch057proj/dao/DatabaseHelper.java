package com.example.tch057proj.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "reservations.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_RESERVATION = "reservation";

    public static final String CREATE_TABLE_RESERVATION =
            "CREATE TABLE " + TABLE_RESERVATION + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "client_id INTEGER, " +
                    "voyage_id INTEGER, " +
                    "date_voyage TEXT, " +
                    "nb_places INTEGER, " +
                    "prix_total REAL" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RESERVATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATION);
        onCreate(db);
    }
}
