package com.example.myapplication;

import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddPostActivity extends AppCompatActivity {

    Toast toast;
    private Button postButton;
    private EditText serviceEditText;
    private RatingBar ratingBar;
    private postDatabaseHelper pdbHelper;
    private DatabaseHelper dbHelper;
    TextView fullNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        // Initializing views and database helpers
        ratingBar = findViewById(R.id.ratingBar);
        serviceEditText = findViewById(R.id.serviceEditText);
        postButton = findViewById(R.id.postButton);

        pdbHelper = new postDatabaseHelper(this);
        dbHelper = new DatabaseHelper(this);
        fullNameText = findViewById(R.id.fullNameText);

        // Getting logged-in username from intent and retrieving full name
        Intent intent = getIntent();
        String currentuser = intent.getStringExtra("username");
        fullNameText.setText(currentuser);

        // Button to select farm with toast notification
        ImageButton farm = findViewById(R.id.imageButtonFarm);
        farm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast = Toast.makeText(AddPostActivity.this, "Select Farm", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        // Button click to add a post and review
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new post object with entered service and rating
                Post post = new Post();
                String service = serviceEditText.getText().toString();
                float rating = ratingBar.getRating();
                post.setService(service);
                post.setRating(rating);

                // Add the post to the database
                long userPost = pdbHelper.addPost(post);

                // Handle post insertion status
                if (userPost != -1) {
                    toast = Toast.makeText(AddPostActivity.this, "Your Post and Review has been posted", Toast.LENGTH_SHORT);
                    toast.show();
                    // Navigate to CommunityActivity after successful post addition
                    Intent intent = new Intent(getApplicationContext(), CommunityActivity.class);
                    intent.putExtra("username",currentuser);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                } else {
                    // Handle insertion failure
                    toast = Toast.makeText(AddPostActivity.this, "Failed to save post", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}
