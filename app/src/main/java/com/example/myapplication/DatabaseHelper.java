package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.w3c.dom.Text;

/**
 * Helper class for managing the SQLite database used for user registration.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Constants for database name, version, table name, and column names

    private static final String DATABASE_NAME = "UserRegistration.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and column names
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_USER_TYPE = "user_type";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_PHONE_NUMBER = "phone_number";
    private static final String COLUMN_PROFILE_PICTURE = "profile_picture";

    // SQL query to create the users table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT UNIQUE, " +
                    COLUMN_EMAIL + " TEXT UNIQUE, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_USER_TYPE + " TEXT, " +
                    COLUMN_FULL_NAME + " TEXT, " +
                    COLUMN_PHONE_NUMBER + " TEXT, " +
                    COLUMN_PROFILE_PICTURE + " TEXT);";

    /**
     * Constructor for DatabaseHelper.
     *
     * @param context Context of the application.
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Override method to create the database tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    // Override method to upgrade the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Method to add a new user to the database
    public long addUser(User user) {
        if (user.getName().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty() ||
                user.getUserType().isEmpty() || user.getFullName().isEmpty() ||
                user.getPhoneNumber().isEmpty()) {
            // Prevent insertion if any field is empty
            return -1;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_TYPE, user.getUserType());
        values.put(COLUMN_FULL_NAME, user.getFullName());
        values.put(COLUMN_PHONE_NUMBER, user.getPhoneNumber());
        values.put(COLUMN_PROFILE_PICTURE, user.getProfilePicturePath());
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result;
    }

    // Method to update an existing user in the database
    public long updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Set values based on the fields you want to update
        values.put(COLUMN_FULL_NAME, user.getFullName());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PHONE_NUMBER, user.getPhoneNumber());
        values.put(COLUMN_NAME, user.getName());

        // Define the WHERE clause to update the specific user
        String whereClause = COLUMN_NAME + " = ?";
        String[] whereArgs = {user.getName()}; // Assuming username is used as the identifier

        // Perform the update
        int rowsAffected = db.update(TABLE_USERS, values, whereClause, whereArgs);
        db.close();

        return rowsAffected;
    }

    public void updateProfilePicturePath(String username, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROFILE_PICTURE, imagePath);

        // Update the profile picture path for the user in the database
        db.update(TABLE_USERS, values, COLUMN_NAME + " = ?", new String[]{username});
        db.close();
    }


    // Method to retrieve a user by their username
    @SuppressLint("Range")
    public User getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        if (username == null) {
            // Handle the case where the username is null
            return null;
        }

        // Define the columns you want to retrieve
        String[] columns = {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_EMAIL,
                COLUMN_PASSWORD,
                COLUMN_USER_TYPE,
                COLUMN_FULL_NAME,
                COLUMN_PHONE_NUMBER,
                COLUMN_PROFILE_PICTURE
        };

        // Define the selection criteria (WHERE clause)
        String selection = COLUMN_NAME + " = ?";
        String[] selectionArgs = {username};

        // Query the database
        Cursor cursor = db.query(
                TABLE_USERS,   // Table to query
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
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
            user.setUserType(cursor.getString(cursor.getColumnIndex(COLUMN_USER_TYPE)));
            user.setFullName(cursor.getString(cursor.getColumnIndex(COLUMN_FULL_NAME)));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NUMBER)));
            user.setProfilePicturePath(cursor.getString(cursor.getColumnIndex(COLUMN_PROFILE_PICTURE)));
        }

        // Close the cursor and database
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return user;
    }

    // Method to retrieve the full name of a user by their username
    @SuppressLint("Range")
    public String getUserFullNameByUsername(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String fullName = "testing";

        if (name != null) {
            Log.d("DB_Debug", "Requested username: " + name); // Add this line for debugging

            String[] columns = {COLUMN_FULL_NAME};
            String selection = COLUMN_NAME + " = ?";
            String[] selectionArgs = {name};

            Cursor cursor = db.query(
                    TABLE_USERS,
                    columns,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    // Move to the next row to start reading from the second row
                    if (cursor.moveToNext()) {
                        fullName = cursor.getString(cursor.getColumnIndex(COLUMN_FULL_NAME));
                        Log.d("DB_Debug", "Retrieved Full Name: " + fullName);
                    } else {
                        Log.d("DB_Debug", "No Full Name Found for username: " + name);
                    }
                    cursor.close();
                } else {
                    Log.d("DB_Debug", "No Full Name Found for username: " + name);
                }
            } else {
                Log.d("DB_Debug", "Cursor is null"); // Add this line for debugging
            }
        } else {
            Log.d("DB_Debug", "Username is null"); // Add this line for debugging
        }

        db.close();
        return fullName;
    }

    // Method to check if a username already exists in the database
    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_NAME + " FROM " + TABLE_USERS + " WHERE " + COLUMN_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Method to check if an email already exists in the database
    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_EMAIL + " FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean deleteUserAccount(String name){
        SQLiteDatabase db = this.getWritableDatabase();

        // Define the WHERE clause
        String selection = "name = ?";
        String[] selectionArgs = {name};

        // Delete the user record from the database
        int deletedRows = db.delete("users", selection, selectionArgs);

        db.close();

        // Return true if at least one row was deleted, indicating successful deletion
        return deletedRows > 0;
    }
}
