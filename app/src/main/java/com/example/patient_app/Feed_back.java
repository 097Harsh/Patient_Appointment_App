package com.example.patient_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class Feed_back extends AppCompatActivity {

    private static final String TAG = "user_dashboard";
    private static final String SHARED_PREFS_NAME = "my_shared_prefs";
    private static final String SESSION_ID_KEY = "session_id";
    private String userId;
    private String sessionId;
    EditText feedbackEditText,feedbackname;
    RatingBar ratingBar;
    Button sendButton;

    // Firebase database reference
    DatabaseReference feedbackRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feed_back);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        feedbackRef = database.getReference("feedback");


        userId = getIntent().getStringExtra("userId");

        // Log the user ID to console
        Log.d(TAG, "User ID: " + userId);
        // Retrieve session ID from Shared Preferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
        sessionId = sharedPreferences.getString(SESSION_ID_KEY, "");

        // Log session ID to console
        Log.d(TAG, "Feed-back page Session ID: " + userId);

        // Initialize views
        feedbackname = findViewById(R.id.edit1);
        feedbackEditText = findViewById(R.id.edit2);
        ratingBar = findViewById(R.id.ratingBar);
        sendButton = findViewById(R.id.button);

        // Set click listener for the send button
        // Set click listener for the send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get feedback message
                String feedbackMessage = feedbackEditText.getText().toString().trim();

                // Get rating
                float rating = ratingBar.getRating();

                // Get name entered by the user
                String feedbackName = feedbackname.getText().toString().trim();

                // Generate unique feedback ID
                String feedbackId = generateFeedbackId();

                // Log print the data
                Log.d(TAG, "Feed-back msg: " + feedbackMessage);
                Log.d(TAG, "Feed-back rating: " + rating);
                Log.d(TAG, "Feed-back f_id: " + feedbackId);
                Log.d(TAG, "Feed-back name: " + feedbackName);

                // Store feedback data into Firebase
                storeFeedbackInFirebase(feedbackId, feedbackMessage, feedbackName, rating, userId);
            }
        });

    }

    private void storeFeedbackInFirebase(String feedbackId, String feedbackMessage, EditText feedbackname, float rating, String userId) {

    }

    private void storeFeedbackInFirebase(String feedbackId, String feedbackMessage, String feedbackName, float rating, String userId) {
        // Create a map to represent the feedback data
        Map<String, Object> feedbackData = new HashMap<>();
        // Get current date and time
        String currentDate = getCurrentDate();
        feedbackData.put("f_id", feedbackId);
        feedbackData.put("name", feedbackName); // Pass the string value of feedbackName, not the EditText
        feedbackData.put("msg", feedbackMessage);
        feedbackData.put("rating", rating);
        feedbackData.put("user_id", userId);
        feedbackData.put("f_date", currentDate); // Add current date

        // Push the feedback data to Firebase
        feedbackRef.push().setValue(feedbackData);

        // Show a toast indicating successful submission
        Toast.makeText(Feed_back.this, "Feedback submitted successfully!", Toast.LENGTH_LONG).show();
        // Redirect back to user_dashboard activity
        redirectToUserDashboard();
    }


    private void redirectToUserDashboard() {
        Intent intent = new Intent(Feed_back.this, user_dashboard.class);
        startActivity(intent);
    }

    // Method to generate a unique feedback ID
    private String generateFeedbackId() {
        return UUID.randomUUID().toString();
    }
    // Method to get current date in the desired format
    private String getCurrentDate() {
        // Get current date and time
        Date currentDate = new Date();

        // Define date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Format the date and return
        return dateFormat.format(currentDate);

    }
}