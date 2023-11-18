package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AccountSettingsActivity extends AppCompatActivity {

    // Database helper to interact with user data
    private DatabaseHelper dbHelper;

    // TextViews to display user information
    private TextView fullName;
    private TextView username;
    private TextView Email;
    private TextView PhoneNumber;

    // EditText fields for editing user information
    private EditText fullNameEdit;
    private EditText usernameEdit;
    private EditText EmailEdit;
    private EditText PasswordEdit;
    private EditText PhoneNumberEdit;

    // Buttons for user interaction
    private Button Confirm;
    private Button Cancel;
    private Button Edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Find views by their respective IDs
        fullName = findViewById(R.id.fullName);
        username = findViewById(R.id.username);
        Email = findViewById(R.id.email);
        PhoneNumber = findViewById(R.id.phoneNumber);

        fullNameEdit = findViewById(R.id.fullNameEdit);
        usernameEdit = findViewById(R.id.usernameEdit);
        EmailEdit = findViewById(R.id.emailEdit);
        PasswordEdit = findViewById(R.id.passwordEdit);
        PhoneNumberEdit = findViewById(R.id.phoneNumberEdit);

        Confirm = findViewById(R.id.Confirm);
        Cancel = findViewById(R.id.Cancel);
        Edit = findViewById(R.id.Edit);

        // Load current user's data and populate the UI
        String currentUsername = "test";
        User currentUser = dbHelper.getUserByUsername(currentUsername);
        if (currentUser != null) {
            // Display user data
            fullName.setText(currentUser.getFullName());
            username.setText(currentUser.getName());
            Email.setText(currentUser.getEmail());
            PhoneNumber.setText(currentUser.getPhoneNumber());

            // Populate editable fields
            fullNameEdit.setText(currentUser.getFullName());
            usernameEdit.setText(currentUser.getName());
            EmailEdit.setText(currentUser.getEmail());
            PasswordEdit.setText(currentUser.getPassword());
            PhoneNumberEdit.setText(currentUser.getPhoneNumber());
        }

        // Disable editing initially
        setEditingEnabled(false);

        // Enable editing on Edit button click
        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditingEnabled(true);
            }
        });

        // Update user data on Confirm button click
        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserData();
            }
        });

        // Cancel editing and reload user data on Cancel button click
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadUserData();
                setEditingEnabled(false);
            }
        });
    }

    // Method to enable/disable editing of fields
    private void setEditingEnabled(boolean isEnabled) {
        // Enable/disable editable fields and adjust visibility
        fullNameEdit.setEnabled(isEnabled);
        EmailEdit.setEnabled(isEnabled);
        PasswordEdit.setEnabled(isEnabled);
        PhoneNumberEdit.setEnabled(isEnabled);

        // Adjust visibility of fields based on editing mode
        fullNameEdit.setVisibility(isEnabled ? View.VISIBLE : View.GONE);
        PasswordEdit.setVisibility(isEnabled ? View.VISIBLE : View.GONE);
        EmailEdit.setVisibility(isEnabled ? View.VISIBLE : View.GONE);
        PhoneNumberEdit.setVisibility(isEnabled ? View.VISIBLE : View.GONE);

        // Adjust visibility of non-editable fields
        fullName.setVisibility(isEnabled ? View.GONE : View.VISIBLE);
        username.setVisibility(isEnabled ? View.GONE : View.VISIBLE);
        Email.setVisibility(isEnabled ? View.GONE : View.VISIBLE);
        PhoneNumber.setVisibility(isEnabled ? View.GONE : View.VISIBLE);

        // Adjust visibility of buttons
        Confirm.setVisibility(isEnabled ? View.VISIBLE : View.GONE);
        Cancel.setVisibility(isEnabled ? View.VISIBLE : View.GONE);
        Edit.setVisibility(isEnabled ? View.GONE : View.VISIBLE);
    }

    // Method to update user data in the database
    private void updateUserData() {
        String newFullName = fullNameEdit.getText().toString();
        String newUsername = usernameEdit.getText().toString();
        String newEmail = EmailEdit.getText().toString();
        String newPassword = PasswordEdit.getText().toString();
        String newPhoneNumber = PhoneNumberEdit.getText().toString();

        // Create a new User object with the updated data
        User updatedUser = new User(newUsername, newEmail, newPassword, "", newFullName, newPhoneNumber);

        // Update the user data in the database
        long rowsAffected = dbHelper.updateUser(updatedUser);

        if (rowsAffected > 0) {
            Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show();
            reloadUserData();
        } else {
            Toast.makeText(this, "Failed to update data", Toast.LENGTH_SHORT).show();
        }
        // Disable editing after updating
        setEditingEnabled(false);
    }

    // Method to reload user data and populate the UI
    private void reloadUserData() {
        String currentUsername = "test";

        User currentUser = dbHelper.getUserByUsername(currentUsername);

        if (currentUser != null) {
            fullName.setText(currentUser.getFullName());
            username.setText(currentUser.getName());
            Email.setText(currentUser.getEmail());
            PhoneNumber.setText(currentUser.getPhoneNumber());

            fullNameEdit.setText(currentUser.getFullName());
            usernameEdit.setText(currentUser.getName());
            EmailEdit.setText(currentUser.getEmail());
            PasswordEdit.setText(currentUser.getPassword());
            PhoneNumberEdit.setText(currentUser.getPhoneNumber());
        }
    }
}
