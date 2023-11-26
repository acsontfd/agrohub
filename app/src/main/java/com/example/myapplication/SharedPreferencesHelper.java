package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_SESSION_TOKEN = "session_token";
    private static final String KEY_USER_PICTURE_PATH = "user_picture_path";

    public static void saveUserPicturePath(Context context, String sessionToken, String picturePath) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_PICTURE_PATH + "_" + sessionToken, picturePath);
        editor.apply();
    }

    public static String getUserPicturePath(Context context, String sessionToken) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_PICTURE_PATH + "_" + sessionToken, null);
    }


    public static void saveUsername(Context context, String sessionToken, String username) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME + "_" + sessionToken, username);
        editor.apply();
    }

    public static String getUsername(Context context, String sessionToken) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME + "_" + sessionToken, null);
    }

    public static void saveSessionToken(Context context, String sessionToken) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SESSION_TOKEN, sessionToken);
        editor.apply();
    }

    public static String getSessionToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_SESSION_TOKEN, null);
    }

    public static void clearSavedCredentials(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_PASSWORD);
        editor.remove(KEY_SESSION_TOKEN);
        editor.apply();
    }
}
