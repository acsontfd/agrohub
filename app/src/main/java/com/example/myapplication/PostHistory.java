package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class PostHistory extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Post> exampleList = new ArrayList<>();
    PostAdapter postAdapter;
    postDatabaseHelper pdbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_history);

        final int[] currentPageId = {R.id.history}; // Initialize with the current page

        Intent intent = getIntent();
        String currentuser = intent.getStringExtra("username");
        pdbHelper = new postDatabaseHelper(this);

        String sessionToken = SharedPreferencesHelper.getSessionToken(this);
        String rememberusername = SharedPreferencesHelper.getUsername(this, sessionToken);

        // Retrieve all posts from the database
        exampleList = (ArrayList<Post>) pdbHelper.getPostsForUser(currentuser);

        if (currentuser != null) {
            exampleList = (ArrayList<Post>) pdbHelper.getPostsForUser(currentuser);

            // Set up RecyclerView and its adapter
            mRecyclerView = findViewById(R.id.recyclerView);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            postAdapter = new PostAdapter(this, exampleList);

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(postAdapter);
        } else {
            exampleList = (ArrayList<Post>) pdbHelper.getPostsForUser(rememberusername);

            // Set up RecyclerView and its adapter
            mRecyclerView = findViewById(R.id.recyclerView);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            postAdapter = new PostAdapter(this, exampleList);

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(postAdapter);
        }
    }
}
