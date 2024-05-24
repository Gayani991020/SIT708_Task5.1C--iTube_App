package com.example.task5_1_itube;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    // UI components and database helper instance
    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button signupButton;
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge display
        EdgeToEdge.enable(this);

        // Set the content view to activity_main.xml
        setContentView(R.layout.activity_main);

        // Adjust padding for system bars for full-screen mode
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        usernameInput = findViewById(R.id.usernameInput); // Input field for username
        passwordInput = findViewById(R.id.passwordInput); // Input field for password
        loginButton = findViewById(R.id.loginButton);     // Button to log in
        signupButton = findViewById(R.id.signupButton);   // Button to sign up

        // Initialize the database helper for user management
        dbHelper = new UserDatabaseHelper(this);

        // Set the login button click listener
        loginButton.setOnClickListener(v -> {
            // Retrieve the username and password entered by the user
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            // Validate the credentials against the database
            if (dbHelper.validateUser(username, password)) {
                // Show a success message
                Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                // Navigate to the MenuActivity
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent); // Start MenuActivity

                // Close the current activity to prevent going back to login
                finish();
            } else {
                // Show an error message for invalid credentials
                Toast.makeText(MainActivity.this, "Invalid username or password.", Toast.LENGTH_SHORT).show();
            }
        });

        // Set the signup button click listener
        signupButton.setOnClickListener(v -> {
            // Navigate to the SignUpActivity
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent); // Start SignUpActivity
        });
    }
}
