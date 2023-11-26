package com.example.myapplication;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_STORAGE_PERMISSION = 101;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView registerTextView;
    private CheckBox rememberCheckBox;
    public String currentuser;
    private boolean permissionRequested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.registerTextView);
        rememberCheckBox = findViewById(R.id.rememberCheckBox);

        // Check if a session token exists
        String sessionToken = SharedPreferencesHelper.getSessionToken(this);

        // Check if permissions are granted, if not, request them
        if (!checkPermission() && !permissionRequested) {
            requestStoragePermission();
            permissionRequested = true; // Set flag to avoid multiple requests
        }

        if (sessionToken != null) {
            // Automatically log the user in with the session token
            navigateToMainPage();
            Toast.makeText(this,"Welcome back!",Toast.LENGTH_SHORT).show();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                currentuser = usernameEditText.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter your login credentials.", Toast.LENGTH_SHORT).show();
                } else {
                    LoginHandler loginHandler = new LoginHandler(LoginActivity.this);

                    // Inside your LoginActivity's loginButton click listener
                    if (loginHandler.authenticateUser(username, password)) {
                        // Save session token and username if "Remember Me" is checked
                        if (rememberCheckBox.isChecked()) {
                            String sessionToken = generateSessionToken();
                            SharedPreferencesHelper.saveSessionToken(LoginActivity.this, sessionToken);
                            SharedPreferencesHelper.saveUsername(LoginActivity.this, sessionToken, username);

                            // Retrieve the profile picture path and username for the logged-in user
                            DatabaseHelper dbHelper = new DatabaseHelper(LoginActivity.this);
                            User loggedInUser = dbHelper.getUserByUsername(username);
                            if (loggedInUser != null) {
                                String profilePicturePath = loggedInUser.getProfilePicturePath();
                                if (profilePicturePath != null && !profilePicturePath.isEmpty()) {
                                    SharedPreferencesHelper.saveUserPicturePath(LoginActivity.this, sessionToken, profilePicturePath);
                                } else {
                                    // Handle the case where the profile picture path is empty or null
                                    SharedPreferencesHelper.clearSavedCredentials(LoginActivity.this);
                                    Toast.makeText(LoginActivity.this, "Profile picture path is empty or null.", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // Get the saved username
                                String savedUsername = SharedPreferencesHelper.getUsername(LoginActivity.this, sessionToken);

                                // Continue with your login logic...
                            } else {
                                // Handle the case where the user is not found
                                SharedPreferencesHelper.clearSavedCredentials(LoginActivity.this);
                                Toast.makeText(LoginActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else {
                            // Clear any existing session token if "Remember Me" is unchecked
                            SharedPreferencesHelper.clearSavedCredentials(LoginActivity.this);
                        }

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
        intent.putExtra("username", currentuser);
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

    private boolean checkPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestStoragePermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Storage Permission Needed")
                        .setMessage("This app requires storage permission to function properly.")
                        .setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                    intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                                    startActivityForResult(intent, REQUEST_STORAGE_PERMISSION);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Handle cancellation, you may close the app or display a message
                                finishAffinity();
                            }
                        })
                        .create()
                        .show();
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION:
                if (grantResults.length > 0) {
                    boolean READ_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (READ_EXTERNAL_STORAGE) {
                        // READ_EXTERNAL_STORAGE permission granted
                        // You can handle this case here
                    } else {
                        // READ_EXTERNAL_STORAGE permission not granted
                        Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

}