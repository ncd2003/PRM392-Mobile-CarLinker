package com.example.prm392_mobile_carlinker.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_ROLE = "userRole";
    private static final String KEY_USER_ROLE_STRING = "userRoleString"; // Thêm key mới cho role string
    private static final String KEY_AUTH_TOKEN = "authToken";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // Check if user is logged in
    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Save login session
    public void createLoginSession(int userId, String email, String userName, int userRole, String token) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_NAME, userName);
        editor.putInt(KEY_USER_ROLE, userRole);
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.apply();
    }

    // Save login session với role là string
    public void createLoginSession(int userId, String email, String userName, String userRole, String token) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_ROLE_STRING, userRole);
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.apply();
    }

    // Get user ID
    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    // Get user email
    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, "");
    }

    // Get user name
    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "Người dùng");
    }

    // Get user role
    public int getUserRole() {
        return prefs.getInt(KEY_USER_ROLE, 0);
    }

    // Get user role as string
    public String getUserRoleString() {
        return prefs.getString(KEY_USER_ROLE_STRING, "GUEST");
    }

    // Get auth token
    public String getAuthToken() {
        return prefs.getString(KEY_AUTH_TOKEN, "");
    }

    // Clear login session
    public void logout() {
        editor.clear();
        editor.apply();
    }
}
