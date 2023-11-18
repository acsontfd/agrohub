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

        // Initialize the database helper
        pdbHelper = new postDatabaseHelper(this);

        // Retrieve all posts from the database
        exampleList = (ArrayList<Post>) pdbHelper.getAllPosts();

        // Set up RecyclerView and its adapter
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        postAdapter = new PostAdapter(this, exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(postAdapter);
    }
}
