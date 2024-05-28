package com.example.patient_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Doctor_dashboard extends AppCompatActivity {

    private static final String TAG = "user_dashboard";
    private static final String SHARED_PREFS_NAME = "my_shared_prefs";
    private static final String SESSION_ID_KEY = "session_id";
    private String userId;
    private String sessionId;
    private String user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_dashboard);
        userId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("Name"); // Retrieve user's name
        // Log the user ID to console
        Log.d(TAG, "User ID: " + userId);
        // Retrieve session ID from Shared Preferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
        sessionId = sharedPreferences.getString(SESSION_ID_KEY, "");

        // Log session ID to console
        Log.d(TAG, "Session ID: " + userId);
        Log.d(TAG, "User Name: " + userName); // Log the user's name


        // Your existing code
        LinearLayout m_appoinment = findViewById(R.id.m_appoinment);
        LinearLayout m_schedule = findViewById(R.id.m_schedule);
        LinearLayout d_logout = findViewById(R.id.d_logout);

        //Book-Appoinment form intent
        m_appoinment.setOnClickListener(v -> {
            Intent intent = new Intent(Doctor_dashboard.this, doctor_appoinment
                    .class);
            // Pass user ID to the next activity
            intent.putExtra("userId", userId);
            intent.putExtra("username", userName);
            startActivity(intent);
        });

        //Manage-Schedule form intent
        m_schedule.setOnClickListener(v -> {
            Intent intent = new Intent(Doctor_dashboard.this, Manage_schedule
                    .class);
            // Pass user ID to the next activity
            intent.putExtra("userId", userId);
            intent.putExtra("username", userName);
            startActivity(intent);
        });


        // Add click listener for logout
        d_logout.setOnClickListener(v -> logout());
    }
    //lgout method for user
    private void logout() {
        // Clear session data
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SESSION_ID_KEY);
        editor.apply();
        // Log session ID to console
        Log.d(TAG, "Logout successfully!!! ");
        // Redirect to login activity
        Intent intent = new Intent(Doctor_dashboard.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }
}