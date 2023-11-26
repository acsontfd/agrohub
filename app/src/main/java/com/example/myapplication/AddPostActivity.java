package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Rating;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class AddPostActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    Toast toast;
    private Button postButton;
    private EditText serviceEditText;
    private RatingBar ratingBar;
    private ImageButton selectImage;
    private ImageView postImage;
    private Uri selectedImageUri;
    private Bitmap bitmap;
    private postDatabaseHelper pdbHelper;
    private DatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        // Initializing views and database helpers
        ratingBar = findViewById(R.id.ratingBar);
        serviceEditText = findViewById(R.id.serviceEditText);
        postButton = findViewById(R.id.postButton);
        selectImage = findViewById(R.id.openGallery);
        postImage = findViewById(R.id.postImage);

        pdbHelper = new postDatabaseHelper(this);
        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        String currentuser = intent.getStringExtra("username");
        String sessionToken = SharedPreferencesHelper.getSessionToken(this);
        String profilePicturePath = SharedPreferencesHelper.getUserPicturePath(this, sessionToken);
        String rememberusername = SharedPreferencesHelper.getUsername(this,sessionToken);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast = Toast.makeText(AddPostActivity.this, "Select Image", Toast.LENGTH_SHORT);
                toast.show();
                openGallery();
            }
        });

        // Button click to add a post
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String currentuser = intent.getStringExtra("username");
                String rememberuser;
                if(currentuser != null){
                    rememberuser = currentuser;
                }else{
                    rememberuser = rememberusername;
                }
                String service = serviceEditText.getText().toString();
                float rating = ratingBar.getRating();
                String imagePath = null;
                Log.d("ImagePath", "Image Path: " + imagePath);

                if (selectedImageUri != null) {
                    // Get the actual file path from the URI
                    imagePath = getRealPathFromURI(selectedImageUri);
                }

                // Fetch the user from the database
                User user = dbHelper.getUserByUsername(rememberuser);

                if (user != null) {
                    String username = user.getName();

                    // Create a new Post object with the entered service, rating, and image path
                    Post post = new Post();
                    post.setService(service);
                    post.setRating(rating);
                    post.setUsername(username);
                    post.setImagePath(imagePath); // Set the image path to the Post object

                    // Add the post to the database
                    long userPost = pdbHelper.addPostWithImage(post, imagePath);

                    // Handle post insertion status
                    if (userPost != -1) {
                        Toast.makeText(AddPostActivity.this, "Your Post and Review have been posted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), CommunityActivity.class);
                        intent.putExtra("username", currentuser);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    } else {
                        Toast.makeText(AddPostActivity.this, "Failed to save post", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle the case where the user is null
                    Toast.makeText(AddPostActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private void openGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                postImage.setImageBitmap(bitmap);

                // Get the actual file path from the URI
                String imagePath = getRealPathFromURI(selectedImageUri);
                Log.d("ImagePath", "Image Path: " + imagePath);

                // Load the image path into the database
                // ... call your database insertion method here with the imagePath parameter
            } catch (IOException e) {
                e.printStackTrace();
            }
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
