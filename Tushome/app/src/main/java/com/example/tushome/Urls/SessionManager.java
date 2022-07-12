package com.example.tushome.Urls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.tushome.Registrations.CheckFirst;
import com.example.tushome.Registrations.LoginSecond;

import java.util.HashMap;

public class SessionManager {
    public static final String USERNAME = "USERNAME";
    public static final String CONTACT = "CONTACT";
    public static final String FULLNAME = "FULLNAME";
    public static final String ID = "ID";
    public static final String CATEGORY = "CATEGORY";
    public static final String IMAGE = "IMAGE";
    public static final String PASS = "PASSWORD";
    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public SharedPreferences.Editor editor;
    public Context context;
    SharedPreferences sharedPreferences;
    int PRIVATE_MODE = 0;

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String username, String contact, String fullname, String id, String pass, String category
            , String image) {
        editor.putBoolean(LOGIN, true);
        editor.putString(USERNAME, username);
        editor.putString(CONTACT, contact);
        editor.putString(FULLNAME, fullname);
        editor.putString(ID, id);
        editor.putString(PASS, pass);
        editor.putString(CATEGORY, category);
        editor.putString(IMAGE, image);
        editor.apply();

    }

    public boolean isLogin() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin() {
        if (!this.isLogin()) {
            Intent i = new Intent(context, LoginSecond.class);
            context.startActivity(i);
            ((CheckFirst) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(USERNAME, sharedPreferences.getString(USERNAME, null));
        user.put(CONTACT, sharedPreferences.getString(CONTACT, null));
        user.put(FULLNAME, sharedPreferences.getString(FULLNAME, null));
        user.put(ID, sharedPreferences.getString(ID, null));
        user.put(PASS, sharedPreferences.getString(PASS, null));
        user.put(CATEGORY, sharedPreferences.getString(CATEGORY, null));
        user.put(IMAGE, sharedPreferences.getString(IMAGE, null));

        return user;
    }

    public void logout() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, CheckFirst.class);
        context.startActivity(i);
    }

}
