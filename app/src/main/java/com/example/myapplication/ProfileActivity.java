package com.example.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {
    private Button logout, settings, delete;
    private Dialog confirmationDialog;
    private Dialog deleteAccountConfirmation;
    private Button posthistory;
    private DatabaseHelper dbHelper;
    TextView fullNameText;
    ImageView profilePic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize UI elements and database helper
        BottomNavigationView bnv = findViewById(R.id.bottom_Navigation);
        profilePic = findViewById(R.id.profilePic);
        logout = findViewById(R.id.logout);
        posthistory = findViewById(R.id.history);
        settings = findViewById(R.id.settings);
        fullNameText = findViewById(R.id.fullNameText);
        delete = findViewById(R.id.delete);
        dbHelper = new DatabaseHelper(this);

        // Retrieve the username from the Intent
        Intent intent = getIntent();
        String currentuser = intent.getStringExtra("username");
        fullNameText.setText(currentuser);

        // Get profile picture path associated with the session token
        String sessionToken = SharedPreferencesHelper.getSessionToken(this);
        String profilePicturePath = SharedPreferencesHelper.getUserPicturePath(this, sessionToken);

        // Set the profile picture if available
        if (profilePicturePath != null && !profilePicturePath.isEmpty()) {
            Uri profilePictureUri = Uri.parse(profilePicturePath);
            profilePic.setImageURI(profilePictureUri);
        } else {
            profilePic.setImageResource(R.drawable.user); // Set a default image
        }


        // Set up a confirmation dialog for logout
        confirmationDialog = new Dialog(this);
        confirmationDialog.setContentView(R.layout.dialog_logout_confirm);

        // Set up a confirmation dialog for delete account
        deleteAccountConfirmation = new Dialog(this);
        deleteAccountConfirmation.setContentView(R.layout.delete_account_confirmation);

        Button btnLogout = confirmationDialog.findViewById(R.id.dialog_confirm);
        Button btnCancel = confirmationDialog.findViewById(R.id.dialog_cancel);

        Button buttonDelete = deleteAccountConfirmation.findViewById(R.id.dialog_Confirm);
        Button buttonCancel = deleteAccountConfirmation.findViewById(R.id.dialog_Cancel);

        bnv.setSelectedItemId(R.id.profile);

        final int[] currentPageId = {R.id.profile}; // Initialize with the current page

        User user = dbHelper.getUserByUsername(currentuser);
        if (user != null) {
            profilePicturePath = user.getProfilePicturePath();
            if (profilePicturePath != null && !profilePicturePath.isEmpty()) {
                // Load and display the image in the ImageView
                Uri profilePictureUri = Uri.parse(profilePicturePath);
                profilePic.setImageURI(profilePictureUri);
            } else {
                // Set a default image or placeholder if the profile picture path is empty or null
                profilePic.setImageResource(R.drawable.user);
            }
        }


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isDeleted = dbHelper.deleteUserAccount(currentuser);
                if (isDeleted) {
                    // Clear the session token and credentials when the user logs out
                    SharedPreferencesHelper.clearSavedCredentials(ProfileActivity.this);

                    // Account deleted successfully
                    Toast.makeText(ProfileActivity.this, "Account deleted successfully!", Toast.LENGTH_SHORT).show();
                    //Navigate to the login screen
                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                    deleteAccountConfirmation.dismiss();
                }
                else {
                    // Account deletion failed
                    Toast.makeText(ProfileActivity.this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Pop on delete account
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccountConfirmation.show();
            }
        });

        // Cancel button for popup dialog
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccountConfirmation.dismiss();
            }
        });

        // Code for BottomNavigationView and other listeners
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id != currentPageId[0]) {
                    Intent intent = null;

                    if (id == R.id.community) {
                        intent = new Intent(getApplicationContext(), CommunityActivity.class);
                        intent.putExtra("username",currentuser);
                    } else if (id == R.id.profile) {
                        return true; // Prevent reloading the same page
                    } else if (id == R.id.home) {
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("username",currentuser);
                    }

                    if (intent != null) {
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        currentPageId[0] = id;
                        return true;
                    }
                }

                return false;
            }
        });

        // Log out button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the session token and credentials when the user logs out
                SharedPreferencesHelper.clearSavedCredentials(ProfileActivity.this);

                Toast.makeText(ProfileActivity.this, "Logout Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

                // Dismiss the dialog
                confirmationDialog.dismiss();
            }
        });

        // Post history button click event
        posthistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostHistory.class);
                intent.putExtra("username",currentuser);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDialog.show();
            }
        });

        // Cancel button for popup dialog
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDialog.dismiss();
            }
        });


        // Settings button click event
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AccountSettingsActivity.class);
                intent.putExtra("username", currentuser);
                startActivity(intent);
            }
        });
    }
}
