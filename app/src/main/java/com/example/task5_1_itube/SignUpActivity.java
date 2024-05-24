package com.example.task5_1_itube;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    // UI elements for user input
    private EditText fullNameEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button createAccButton; // Button to create a new account
    private UserDatabaseHelper dbHelper; // Database helper for user data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup); // Set the layout for the signup activity

        // Bind UI elements to variables
        fullNameEditText = findViewById(R.id.FullNameEditText); // EditText for full name input
        usernameEditText = findViewById(R.id.UsernameEditText); // EditText for username input
        passwordEditText = findViewById(R.id.passwordEditText); // EditText for password input
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText); // EditText for confirming password
        createAccButton = findViewById(R.id.createAccButton); // Button to create a new account

        // Initialize the database helper instance
        dbHelper = new UserDatabaseHelper(this);

        // Set a click listener for the "Create Account" button
        createAccButton.setOnClickListener(v -> {
            // Retrieve user input from the EditText fields
            String fullName = fullNameEditText.getText().toString().trim();
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            // Perform basic validation
            if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                // If any field is empty, show a toast message and return
                Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if the password and confirm password fields match
            if (!password.equals(confirmPassword)) {
                // If passwords do not match, show a toast message and return
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Attempt to add the new user to the database
            if (dbHelper.addUser(username, password)) {
                // If the account is created successfully, show a toast message
                Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();

                // Redirect to the login screen
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent); // Start the login activity
                finish(); // Close the current activity

            } else {
                // If the username already exists, show a toast message
                Toast.makeText(this, "Username already exists.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
