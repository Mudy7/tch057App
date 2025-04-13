package com.example.tch057proj.dao;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "session";
    private static final String KEY_CLIENT_ID = "clientId";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveClientId(int clientId) {
        editor.putInt(KEY_CLIENT_ID, clientId);
        editor.apply();
    }

    public int getClientId() {
        return sharedPreferences.getInt(KEY_CLIENT_ID, -1);
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
