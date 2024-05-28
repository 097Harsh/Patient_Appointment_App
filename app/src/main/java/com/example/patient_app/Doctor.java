package com.example.patient_app;

import static androidx.constraintlayout.widget.StateSet.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class Doctor extends AppCompatActivity {
    private static final String SHARED_PREFS_NAME = "my_shared_prefs";
    private static final String SESSION_ID_KEY = "session_id";
    private String userId;
    private String sessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        userId = getIntent().getStringExtra("userId");

        // Log the user ID to console
        Log.d(TAG, "User ID: " + userId);
        // Retrieve session ID from Shared Preferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
        sessionId = sharedPreferences.getString(SESSION_ID_KEY, "");

        // Log session ID to console
        Log.d(TAG, "Session ID: " + userId);

    }
}