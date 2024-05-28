package com.example.patient_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;


public class admin_dashboard extends AppCompatActivity {

    private static final String TAG = "admin_dashboard";
    private static final String SHARED_PREFS_NAME = "my_shared_prefs";
    private static final String SESSION_ID_KEY = "session_id";

    private String userId;
    private String sessionId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);



        userId = getIntent().getStringExtra("userId");

        // Log the user ID to console
        Log.d(TAG, "User ID: " + userId);
        // Retrieve session ID from Shared Preferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
        sessionId = sharedPreferences.getString(SESSION_ID_KEY, "");

        // Log session ID to console
        Log.d(TAG, "Session ID: " + userId);

        // Your existing code
        LinearLayout manageAppointmentsLayout = findViewById(R.id.manageAppointmentsLayout);
        LinearLayout manageFeedbacksLayout = findViewById(R.id.manageFeedbacksLayout);
        LinearLayout manageUsersLayout = findViewById(R.id.manageUsersLayout);
      //  LinearLayout manageDoctorsLayout = findViewById(R.id.manageDoctorsLayout);
        LinearLayout logoutLayout = findViewById(R.id.logoutLayout);


        //Manage-feed-back intent
        manageFeedbacksLayout.setOnClickListener(v -> {
            startActivity(new Intent(admin_dashboard.this, Manage_Feed_back.class));
        });
        //Manage-Users only
        manageUsersLayout.setOnClickListener(v -> {
            startActivity(new Intent(admin_dashboard.this, ManageUsersActivity.class));
        });
        //Manage-Appoinment only
        manageAppointmentsLayout.setOnClickListener(v -> {
            startActivity(new Intent(admin_dashboard.this, Manage_Appoinment.class));
        });

        // Add click listener for logout
        logoutLayout.setOnClickListener(v -> logout());
    }
    private void logout() {
        // Clear session data
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SESSION_ID_KEY);
        editor.apply();
        // Log session ID to console
        Log.d(TAG, "Logout successfully!!! ");
        // Redirect to login activity
        Intent intent = new Intent(admin_dashboard.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

}
