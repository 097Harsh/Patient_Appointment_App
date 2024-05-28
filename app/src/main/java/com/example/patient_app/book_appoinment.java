package com.example.patient_app;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class book_appoinment extends AppCompatActivity {
    private static final String TAG = "user_dashboard";
    private static final String SHARED_PREFS_NAME = "my_shared_prefs";
    private static final String SESSION_ID_KEY = "session_id";
    private TextView mDisplayDate;
    Spinner spinnerv;
    DatabaseReference spinnerref;
    private DatePickerDialog.OnDateSetListener mDateSetListerner;

    private  String status ="Pending";
    private String user_id;
    private String sessionId;

    private Spinner spinner;
    private ArrayList<String> doctorsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_appoinment);

        user_id = getIntent().getStringExtra("userId");

        // Log the user ID to console
        Log.d(TAG, "User ID: " + user_id);
        // Retrieve session ID from Shared Preferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
        sessionId = sharedPreferences.getString(SESSION_ID_KEY, "");

        // Log session ID to console
        Log.d(TAG, "book-appoinment page Session ID: " + user_id);


        TextView mDisplayDate = (TextView) findViewById(R.id.date);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        book_appoinment.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListerner,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });
        mDateSetListerner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyyy:" + day + "/" + month + "/" + year);
                String date = day + "/" + month + "/" + year;
                mDisplayDate.setText(date);
                Log.d(TAG, "string: "+date);
            }
        };

        //spineer data

        doctorsList = new ArrayList<>();
        // Query to fetch users with the role Doctor
        Spinner spinner = findViewById(R.id.simple_list_item_1); // Ensure R.id.spinner matches the ID in your layout XML

        Query query = FirebaseDatabase.getInstance().getReference("users").orderByChild("role").equalTo("Doctor");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                doctorsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Assuming the doctor's name is stored under a child called "name"
                    String doctorName = snapshot.child("name").getValue(String.class);
                    doctorsList.add(doctorName);
                }
                // Populate the spinner with the list of doctors
                ArrayAdapter<String> adapter = new ArrayAdapter<>(book_appoinment.this, android.R.layout.simple_list_item_1, doctorsList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter); // Make sure spinner is referencing the correct Spinner object
                Log.d(TAG, "Doctor Name: "+doctorsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error fetching doctors: " + databaseError.getMessage());
            }
        });

        //appoinment store from fire-base
        // Button click listener to book appointment
        Button bookAppointmentButton = findViewById(R.id.button);
        bookAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected doctor's name from the spinner
                String selectedDoctor = spinner.getSelectedItem().toString();

                // Get the patient's name from the EditText
                EditText patientNameEditText = findViewById(R.id.name);
                String patientName = patientNameEditText.getText().toString();

                // Get the message from the EditText
                EditText messageEditText = findViewById(R.id.edit1);
                String message = messageEditText.getText().toString();
                Spinner timeSlotSpinner = findViewById(R.id.time_slot_spinner); // Initialize the Spinner for time slots

                // Inside the OnClickListener for booking appointment button
                // Get the selected time slot from the Spinner
                String selectedTimeSlot = timeSlotSpinner.getSelectedItem().toString();

                // Get the selected date from the TextView
                String appointmentDate = mDisplayDate.getText().toString();

                // Generate a unique appointment ID (you can use a timestamp or any other unique identifier)
                String appointmentId = String.valueOf(System.currentTimeMillis());
                // Log session ID to console
                Log.d(TAG, "book-appoinment page Session ID: " + user_id);

                // Save appointment data to Firebase
                DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference("appointments");


                // Create an Appointment object with all details except user_id
                Appointment appointment = new Appointment(appointmentId,status, patientName, null, message, selectedDoctor, appointmentDate,selectedTimeSlot);

                // Convert Appointment object to a Map<String, Object>
                Map<String, Object> appointmentMap = new HashMap<>();
                appointmentMap.put("appointmentId", appointment.getAppointmentId());
                appointmentMap.put("patientName", appointment.getPatientName());
                // Set the user_id externally
                appointment.setUser_id(user_id);
                appointmentMap.put("user_id", appointment.getuser_id());
                appointmentMap.put("status", appointment.getStatus());
                appointmentMap.put("message", appointment.getMessage());
                appointmentMap.put("doctorName", appointment.getDoctorName());
                appointmentMap.put("appointmentDate", appointment.getAppointmentDate());
                // Add other appointment details to the map
                appointmentMap.put("timeSlot", appointment.getTimeSlot()); // Add selected time slot to the map
                //Push the appointment data to Firebase
                appointmentsRef.push().setValue(appointmentMap);

                // Display a success message or perform any other action
                Toast.makeText(book_appoinment.this, "Appointment booked successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}