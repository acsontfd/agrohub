package com.example.myapplication;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {
    private EditText fullNameEditText;
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText phoneNumberEditText;
    private ImageView profilePicture;
    private RadioGroup userTypeRadioGroup;
    private Button addProfilePicButton,registerButton;
    private static final int PICK_IMAGE_REQUEST = 1; // Request code for image selection
    private Uri selectedImageUri; // Declare selectedImageUri here
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        fullNameEditText = findViewById(R.id.fullNameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        userTypeRadioGroup = findViewById(R.id.userTypeRadioGroup);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        profilePicture = findViewById(R.id.profileImageView);
        addProfilePicButton = findViewById(R.id.addProfilePictureButton);

        registerButton = findViewById(R.id.registerButton);

        dbHelper = new DatabaseHelper(this);

        addProfilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to open gallery for image selection
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String name = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String fullName = fullNameEditText.getText().toString();
                String phoneNumber = phoneNumberEditText.getText().toString();
                String profilePicturePath = (selectedImageUri != null) ? getRealPathFromURI(selectedImageUri) : "";

                // Get the selected user type (Customer or Vendor)
                int selectedRadioButtonId = userTypeRadioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                String userType = selectedRadioButton.getText().toString();

                // Check if the username or email already exists
                if (dbHelper.isUsernameExists(name) || dbHelper.isEmailExists(email)) {
                    // Username or email is already used
                    Toast.makeText(RegistrationActivity.this, "Username or email is already in use.", Toast.LENGTH_LONG).show();
                } else {
                    // Create a User object
                    User user = new User(name, email, password, userType, fullName, phoneNumber, profilePicturePath);

                    // Add the user to the database
                    long result = dbHelper.addUser(user);

                    if (result != -1) {
                        // Registration successful
                        Toast.makeText(RegistrationActivity.this, "Registration successful as " + userType, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Registration failed
                        Toast.makeText(RegistrationActivity.this, "Registration failed, please fill in all fields.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();

            // Set the selected image to the ImageView
            profilePicture.setImageURI(selectedImageUri);

            // Get the image path from the selected image URI
            String imagePath = getRealPathFromURI(selectedImageUri);

            // Now, imagePath contains the path of the selected image which you can save to your User object or database
        }
    }

    // Method to get real path from URI
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

        @Override
        public void onBackPressed() {
            super.onBackPressed();
            navigateToLogin();
        }

        private void navigateToLogin() {
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

}
