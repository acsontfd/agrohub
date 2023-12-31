package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AccountSettingsActivity extends AppCompatActivity {
    public static final int PICK_IMAGE_REQUEST = 1;

    // Database helper to interact with user data
    private DatabaseHelper dbHelper;

    // TextViews to display user information
    private TextView fullName;
    private TextView username;
    private TextView Email;
    private TextView PhoneNumber;

    // Profile Picture
    private ImageView profileImageView;
    private Uri selectedImageUri; // Declare selectedImageUri
    private Uri existingImageUri; // Store existing image URI



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

    public AccountSettingsActivity() {
    }


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
        profileImageView = findViewById(R.id.profileImageView);

        Confirm = findViewById(R.id.Confirm);
        Cancel = findViewById(R.id.Cancel);
        Edit = findViewById(R.id.Edit);

        // Load current user's data and populate the UI
        Intent intent = getIntent();
        String currentuser = intent.getStringExtra("username");
        String currentUsername = currentuser;

        String sessionToken = SharedPreferencesHelper.getSessionToken(this);
        String storedImageUri = SharedPreferencesHelper.getUserPicturePath(this, "selected");

        // Check if a stored URI exists, then load the profile picture
        if (storedImageUri != null && !storedImageUri.isEmpty()) {
            selectedImageUri = Uri.parse(storedImageUri);
            profileImageView.setImageURI(selectedImageUri);
        }
        User currentUser = dbHelper.getUserByUsername(currentUsername);

        if (currentUser != null) {
            // Display user data
            fullName.setText(currentUser.getFullName());
            username.setText(currentUser.getName());
            Email.setText(currentUser.getEmail());
            PhoneNumber.setText(currentUser.getPhoneNumber());
            existingImageUri = Uri.parse(currentUser.getProfilePicturePath());

            // Populate editable fields
            fullNameEdit.setText(currentUser.getFullName());
            usernameEdit.setText(currentUser.getName());
            EmailEdit.setText(currentUser.getEmail());
            PasswordEdit.setText(currentUser.getPassword());
            PhoneNumberEdit.setText(currentUser.getPhoneNumber());

            if (existingImageUri != null) {
                profileImageView.setImageURI(existingImageUri);
            } else {
                // Set a default placeholder image here
                profileImageView.setImageResource(R.drawable.user);
            }
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

                // Reset profileImageView to the existing image
                if (existingImageUri != null) {
                    profileImageView.setImageURI(existingImageUri);
                }
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

        String profilePicturePath = getRealPathFromURI(selectedImageUri);

        // Create a new User object with the updated data
        User updatedUser = new User(newUsername, newEmail, newPassword, "", newFullName, newPhoneNumber, profilePicturePath);
        updatedUser.setProfilePicturePath(profilePicturePath);

        // Update the user data in the database
        long rowsAffected = dbHelper.updateUser(updatedUser);

        if (rowsAffected > 0) {
            Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show();

            // Update image path if a new image was selected
            if (selectedImageUri != null) {
                String imagePath = getRealPathFromURI(selectedImageUri);

                // Update the User object with the new image path
                dbHelper.updateProfilePicturePath(newUsername, imagePath);
                if (imagePath != null) {
                    SharedPreferencesHelper.saveUserPicturePath(this, "selected", selectedImageUri.toString());

                    // Set the result intent to include the updated profile picture path
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updatedProfilePicturePath", imagePath);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }

            reloadUserData();
        } else {
            Toast.makeText(this, "Failed to update data", Toast.LENGTH_SHORT).show();
        }
        // Disable editing after updating
        setEditingEnabled(false);
    }


    // Method to reload user data and populate the UI
    private void reloadUserData() {
        Intent intent = getIntent();
        String currentuser = intent.getStringExtra("username");
        String currentUsername = currentuser;

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of the selected image
            selectedImageUri = data.getData();

            // Set the selected image to the profileImageView
            profileImageView.setImageURI(selectedImageUri);

            // Update the existingImageUri with the selected image URI
            existingImageUri = selectedImageUri;
            SharedPreferencesHelper.saveUserPicturePath(this, "selected", selectedImageUri.toString());
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor == null) {
            Log.e("ImagePath", "Cursor is null");
            return null;
        }

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(columnIndex);
            Log.d("ImagePath", "Image Path from URI: " + path);
            cursor.close();
            return path;
        } else {
            Log.e("ImagePath", "Cursor.moveToFirst() returned false");
        }

        cursor.close();
        return null;
    }

}