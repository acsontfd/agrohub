package com.example.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {
    private Button logout, settings;
    private Dialog confirmationDialog;
    private Button posthistory;
    private DatabaseHelper dbHelper;

    TextView fullNameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize UI elements and database helper
        BottomNavigationView bnv = findViewById(R.id.bottom_Navigation);
        logout = findViewById(R.id.logout);
        posthistory = findViewById(R.id.history);
        settings = findViewById(R.id.settings);
        fullNameText = findViewById(R.id.fullNameText);
        dbHelper = new DatabaseHelper(this);

        // Retrieve the username from the Intent
        Intent intent = getIntent();
        String currentuser = intent.getStringExtra("username");
        fullNameText.setText(currentuser);


        // Set up a confirmation dialog for logout
        confirmationDialog = new Dialog(this);
        confirmationDialog.setContentView(R.layout.dialog_logout_confirm);

        Button btnLogout = confirmationDialog.findViewById(R.id.dialog_confirm);
        Button btnCancel = confirmationDialog.findViewById(R.id.dialog_cancel);

        bnv.setSelectedItemId(R.id.profile);

        final int[] currentPageId = {R.id.profile}; // Initialize with the current page

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
                startActivity(new Intent(getApplicationContext(),PostHistory.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
