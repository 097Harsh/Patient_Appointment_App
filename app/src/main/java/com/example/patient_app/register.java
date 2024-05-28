package com.example.patient_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {

    EditText name, email, contact, password, cpassword;
    Spinner roleSpinner;
    Button submitButton;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        contact = findViewById(R.id.contact);
        password = findViewById(R.id.password);
        cpassword = findViewById(R.id.c_pass);
        roleSpinner = findViewById(R.id.role_spinner);
        submitButton = findViewById(R.id.submit);

        // Initialize Firebase Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = name.getText().toString().trim();
                String useremail = email.getText().toString().trim();
                String usercontact = contact.getText().toString().trim();
                String userpass = password.getText().toString().trim();
                String usercpass = cpassword.getText().toString().trim();
                String role = roleSpinner.getSelectedItem().toString();

                // Validate input
                if (!userpass.equals(usercpass)) {
                    Toast.makeText(register.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Generate unique user_id using Firebase push()
                DatabaseReference newUserRef = databaseReference.push();
                String userId = newUserRef.getKey();

                // Save user details to Firebase
                newUserRef.child("user_id").setValue(userId);
                newUserRef.child("name").setValue(username);
                newUserRef.child("email").setValue(useremail);
                newUserRef.child("contact").setValue(usercontact);
                newUserRef.child("password").setValue(userpass);
                newUserRef.child("role").setValue(role)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                // Display toast message
                                Toast.makeText(register.this, "Registration successful", Toast.LENGTH_SHORT).show();

                                // Redirect to MainActivity
                                Intent intent = new Intent(register.this, MainActivity.class);
                                startActivity(intent);
                                finish(); // Close the current activity
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Display toast message for failure
                                Toast.makeText(register.this, "Failed to register: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
