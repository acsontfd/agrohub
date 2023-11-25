package com.example.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;

public class CommunityActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private postDatabaseHelper pdbHelper;
    private TextView fullNameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        BottomNavigationView bnv = findViewById(R.id.bottom_Navigation);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);

        recyclerView = findViewById(R.id.recyclerView);
        pdbHelper = new postDatabaseHelper(this);
        fullNameText = findViewById(R.id.fullNameText);

        Intent intent = getIntent();
        String currentuser = intent.getStringExtra("username");
        fullNameText.setText(currentuser);

        // Retrieve posts from the database and set up RecyclerView
        List<Post> postList = pdbHelper.getAllPosts();

        for (Post post : postList) {
            String imagePath = post.getImagePath();
            // Log the image path to verify it's correct
            Log.d("DatabaseImagePath", "Image Path: " + imagePath);
        }
        postAdapter = new PostAdapter(this, postList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(postAdapter);

        bnv.setSelectedItemId(R.id.community);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddPostActivity.class);
                intent.putExtra("username", currentuser);
                startActivity(intent);
            }
        });

        final int[] currentPageId = {R.id.community};

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id != currentPageId[0]) {
                    Intent intent = null;

                    if (id == R.id.community) {
                        return true;
                    } else if (id == R.id.profile) {
                        intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        intent.putExtra("username", currentuser);
                    } else if (id == R.id.home) {
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("username", currentuser);
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
    }

}
