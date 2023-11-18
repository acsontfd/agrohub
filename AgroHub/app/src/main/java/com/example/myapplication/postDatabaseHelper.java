package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_POSTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RATING + " REAL, " +
                    COLUMN_SERVICE + " TEXT);";

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

    public long addPost(Post post) {

        if (post.getRating() == 0.0 && post.getService().isEmpty()){
            // Prevent insertion if any field is empty
            return -1;
        }
        SQLiteDatabase pdb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RATING, post.getRating());
        values.put(COLUMN_SERVICE, post.getService());
        long result = pdb.insert(TABLE_POSTS, null, values);
        pdb.close();
        return result;
    }
    @SuppressLint("Range")
    public Post getPostbyid (int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Post post = null;

        // Define the columns you want to retrieve
        String[] columns = {
                COLUMN_ID,
                COLUMN_RATING,
                COLUMN_SERVICE
        };

        // Define the selection criteria (WHERE clause)
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        // Query the database
        Cursor cursor = db.query(
                TABLE_POSTS,   // Table to query
                columns,       // Columns to return
                selection,     // Columns for the WHERE clause
                selectionArgs, // Values for the WHERE clause
                null,          // Don't group the rows
                null,          // Don't filter by row groups
                null,          // The sort order
                null           // No limit
        );

        if (cursor != null && cursor.moveToFirst()) {
            // User found, populate a User object
            post = new Post();
            post.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            post.setRating(cursor.getFloat(cursor.getColumnIndex(COLUMN_RATING)));
            post.setService(cursor.getString(cursor.getColumnIndex(COLUMN_SERVICE)));
        }

        // Close the cursor and database
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return post;
    }

    // Retrieve all posts
    public List<Post> getAllPosts() {
        List<Post> postList = new ArrayList<>();
        SQLiteDatabase pdb = this.getReadableDatabase();

        Cursor cursor = pdb.query(
                TABLE_POSTS,     // Table to query
                null,            // Columns to return (null retrieves all columns)
                null,            // Columns for the WHERE clause
                null,            // Values for the WHERE clause
                null,            // Don't group the rows
                null,            // Don't filter by row groups
                null             // The sort order
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Extract data from the cursor and create a Post object
                @SuppressLint("Range") float rating = cursor.getFloat(cursor.getColumnIndex(COLUMN_RATING));
                @SuppressLint("Range") String service = cursor.getString(cursor.getColumnIndex(COLUMN_SERVICE));

                Post post = new Post();
                post.setRating(rating);
                post.setService(service);

                // Add the post to the list
                postList.add(post);
            }
            cursor.close();
        }

        pdb.close();
        return postList;
    }
}
