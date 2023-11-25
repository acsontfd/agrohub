package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class postDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "postDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and column names
    private static final String TABLE_POSTS = "posts";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_SERVICE = "service";
    private static final String COLUMN_IMAGE_PATH = "image_path";
    private static final String COLUMN_USERNAME = "username";


    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_POSTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RATING + " REAL, " +
                    COLUMN_SERVICE + " TEXT, " +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_IMAGE_PATH + " TEXT);";


    public postDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
        onCreate(db);
    }

    public long addPostWithImage(Post post, String imagePath) {
        if (post.getRating() == 0.0) {
            // Prevent insertion if any field is empty
            return -1;
        }
        Log.d("DatabaseImagePath", "Image Path: " + imagePath);


        SQLiteDatabase pdb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RATING, post.getRating());
        values.put(COLUMN_USERNAME, post.getUsername());
        values.put(COLUMN_SERVICE, post.getService());
        values.put(COLUMN_IMAGE_PATH, imagePath); // Store the image path
        long result = pdb.insert(TABLE_POSTS, null, values);
        pdb.close();
        return result;
    }
    @SuppressLint("Range")
    public List<Post> getPostsForUser(String currentUser) {
        List<Post> userPosts = new ArrayList<>();
        SQLiteDatabase pdb = this.getReadableDatabase();

        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = { currentUser };

        Cursor cursor = pdb.query(
                TABLE_POSTS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") float rating = cursor.getFloat(cursor.getColumnIndex(COLUMN_RATING));
                @SuppressLint("Range") String service = cursor.getString(cursor.getColumnIndex(COLUMN_SERVICE));
                @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
                String imagePath = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_PATH));

                Post post = new Post();
                post.setUsername(username);
                post.setRating(rating);
                post.setService(service);
                post.setImagePath(imagePath != null ? imagePath : "");

                userPosts.add(post);
            }
            cursor.close();
        }

        pdb.close();
        return userPosts;
    }

    // Retrieve all posts
    public List<Post> getAllPosts() {
        List<Post> postList = new ArrayList<>();
        SQLiteDatabase pdb = this.getReadableDatabase();

        Cursor cursor = pdb.query(
                TABLE_POSTS,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") float rating = cursor.getFloat(cursor.getColumnIndex(COLUMN_RATING));
                @SuppressLint("Range") String service = cursor.getString(cursor.getColumnIndex(COLUMN_SERVICE));
                @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
                @SuppressLint("Range") String imagePath = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_PATH));

                Post post = new Post();
                post.setRating(rating);
                post.setUsername(username);
                post.setService(service);
                post.setImagePath(imagePath != null ? imagePath : ""); // Handle null imagePath

                // Add the post to the list
                postList.add(post);

                // Log the image path to verify
                Log.d("DatabaseImagePath", "Image Path: " + imagePath);
            } while (cursor.moveToNext());
            cursor.close();
        }

        pdb.close();
        return postList;
    }
}