package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView registerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.registerTextView);

        // Check if a session token exists
        String sessionToken = SharedPreferencesHelper.getSessionToken(this);

        if (sessionToken != null) {
            // Automatically log the user in with the session token
            navigateToMainPage();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter your login credentials.", Toast.LENGTH_SHORT).show();
                } else {
                    LoginHandler loginHandler = new LoginHandler(LoginActivity.this);

                    if (loginHandler.authenticateUser(username, password)) {
                        // Save a session token upon successful login
                        String sessionToken = generateSessionToken();
                        SharedPreferencesHelper.saveSessionToken(LoginActivity.this, sessionToken);

                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        navigateToMainPage();
                    } else {
                        Toast.makeText(LoginActivity.this, "Incorrect Credentials.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRegister();
            }
        });
    }

    /**
     * Generates a unique session token.
     *
     * @return A unique session token.
     */
    private String generateSessionToken() {
        // Generate a unique session token (e.g., using UUID.randomUUID() or another method)
        return "YourGeneratedSessionToken";
    }

    /**
     * Navigates the user to the main page (MainActivity).
     */
    private void navigateToMainPage() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Navigates the user to the registration page (RegistrationActivity).
     */
    private void navigateToRegister() {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
        finish();
    }
}
