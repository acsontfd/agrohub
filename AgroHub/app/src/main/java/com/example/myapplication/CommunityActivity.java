package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CommunityActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private postDatabaseHelper pdbHelper;
    private DatabaseHelper dbHelper;
    TextView fullNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        // Initializing views and helpers
        BottomNavigationView bnv = findViewById(R.id.bottom_Navigation);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.recyclerView);
        pdbHelper = new postDatabaseHelper(this);
        dbHelper = new DatabaseHelper(this);
        fullNameText = findViewById(R.id.fullNameText);

        // Retrieve logged-in username from intent and fetch full name from the database
        Intent intent = getIntent();
        String currentuser = intent.getStringExtra("username");
        fullNameText.setText(currentuser);

        // Retrieve posts from the database and set up RecyclerView
        List<Post> postList = pdbHelper.getAllPosts();
        postAdapter = new PostAdapter(this, postList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(postAdapter);

        bnv.setSelectedItemId(R.id.community);

        // Button to add a new post
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddPostActivity.class);
                intent.putExtra("username",currentuser);
                startActivity(intent);
            }
        });

        final int[] currentPageId = {R.id.community}; // Initialize with the current page

        // Bottom Navigation setup to switch between activities
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id != currentPageId[0]) {
                    Intent intent = null;

                    // Handle navigation based on selected item
                    if (id == R.id.community) {
                        return true; // Prevent reloading the same page
                    } else if (id == R.id.profile) {
                        intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        intent.putExtra("username",currentuser);
                    } else if (id == R.id.home) {
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("username",currentuser);
                    }

                    // Start the selected activity and update current page
                    if (intent != null) {
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish(); // Close the current activity
                        currentPageId[0] = id; // Update the current page
                        return true;
                    }
                }

                return false;
            }
        });
    }
}
