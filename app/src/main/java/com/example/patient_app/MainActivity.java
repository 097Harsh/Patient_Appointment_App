package com.example.patient_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText useremail, user_password;
    Button login, register;
    Spinner roleSpinner;
    private FirebaseAuth mAuth;
    DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        useremail = findViewById(R.id.email);
        user_password = findViewById(R.id.password);
        login = findViewById(R.id.loginButton);
        register = findViewById(R.id.register);
        roleSpinner = findViewById(R.id.roleSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        // Get reference to the "users" node in your Firebase Realtime Database
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        // Check if the user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();


        login.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View v) {
                String email = useremail.getText().toString().trim();
                String password = user_password.getText().toString().trim();
                String role = roleSpinner.getSelectedItem().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(MainActivity.this, "Login Successfully.", Toast.LENGTH_SHORT).show();

                    loginuser();
                    // String userData = "Email: " + email + "\nPassword: " + password + "\nRole: " + role;
                    // Toast.makeText(MainActivity.this, userData, Toast.LENGTH_LONG).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, register.class);
                startActivity(intent);
            }
        });
    }

    private void loginuser() {
        final String email = useremail.getText().toString().trim();
        final String password = user_password.getText().toString().trim();
        final String selectedRole = roleSpinner.getSelectedItem().toString();

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userEmail = userSnapshot.child("email").getValue(String.class);
                    String userPassword = userSnapshot.child("password").getValue(String.class);
                    String role = userSnapshot.child("role").getValue(String.class);
                    String userName = userSnapshot.child("name").getValue(String.class); // Fetching the user's name
                    String userId = userSnapshot.child("user_id").getValue(String.class);

                    if (userEmail != null && userEmail.equals(email) && userPassword != null && userPassword.equals(password)) {
                        // If email and password match
                        if (role != null && role.equalsIgnoreCase(selectedRole)) {
                            // Redirect the user based on role
                            switch (role) {
                                case "Admin":
                                    Intent intent = new Intent(MainActivity.this, admin_dashboard.class);
                                    // Pass user ID to the next activity
                                    intent.putExtra("userId", userId);
                                    startActivity(intent);
                                    break;
                                case "Doctor":
                                    intent = new Intent(MainActivity.this, Doctor_dashboard.class);
                                    // Pass user ID to the next activity
                                    intent.putExtra("userId", userId);
                                    intent.putExtra("Name", userName);
                                    startActivity(intent);
                                    break;
                                case "Users":
                                    intent = new Intent(MainActivity.this, user_dashboard.class);
                                    // Pass user ID to the next activity
                                    intent.putExtra("userId", userId);
                                    intent.putExtra("Name", userName);
                                    startActivity(intent);
                                    break;
                                default:
                                    Toast.makeText(MainActivity.this, "Invalid role", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            finish(); // Finish the current activity to prevent returning to login
                            return;
                        } else {
                            Toast.makeText(MainActivity.this, "User does not have access to this role", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                // If no matching user found or incorrect credentials
                Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MainActivity", "Error fetching users", databaseError.toException());
            }
        });
    }



}