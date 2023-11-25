package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {
    private EditText fullNameEditText;
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText phoneNumberEditText;
    private RadioGroup userTypeRadioGroup;
    private Button registerButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        fullNameEditText = findViewById(R.id.fullNameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        userTypeRadioGroup = findViewById(R.id.userTypeRadioGroup);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);

        registerButton = findViewById(R.id.registerButton);

        dbHelper = new DatabaseHelper(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String name = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String fullName = fullNameEditText.getText().toString();
                String phoneNumber = phoneNumberEditText.getText().toString();

                // Get the selected user type (Customer or Vendor)
                int selectedRadioButtonId = userTypeRadioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                String userType = selectedRadioButton.getText().toString();

                // Check if the username or email already exists
                if (dbHelper.isUsernameExists(name) || dbHelper.isEmailExists(email)) {
                    // Username or email is already used
                    Toast.makeText(RegistrationActivity.this, "Username or email is already in use.", Toast.LENGTH_LONG).show();
                } else {
                    // Create a User object
                    User user = new User(name, email, password, userType, fullName, phoneNumber);

                    // Add the user to the database
                    long result = dbHelper.addUser(user);

                    if (result != -1) {
                        // Registration successful
                        Toast.makeText(RegistrationActivity.this, "Registration successful as " + userType, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Registration failed
                        Toast.makeText(RegistrationActivity.this, "Registration failed, please fill in all fields.", Toast.LENGTH_LONG).show();
                    }
                }
            }

        });
    }
        @Override
        public void onBackPressed() {
            super.onBackPressed();
            navigateToLogin();
        }

        private void navigateToLogin() {
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

}
