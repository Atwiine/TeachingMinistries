package com.example.tushome.Urls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.tushome.Registrations.CheckFirst;
import com.example.tushome.Registrations.LoginSecond;

import java.util.HashMap;

public class SessionLikes {

    public static final String STATUS = "STATUS";
    public static final String PID = "PID";
    private static final String PREF_NAME = "POST_STATE";
    private static final String SET = "IS_SET";
    public SharedPreferences.Editor editor;
    public Context context;
    SharedPreferences sharedPreferences;
    int PRIVATE_MODE = 0;

    @SuppressLint("CommitPrefEdits")
    public SessionLikes(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String status,String pid) {
        editor.putBoolean(SET, true);
        editor.putString(STATUS, status);
        editor.putString(PID, pid);
        editor.apply();

    }

    public boolean isSet() {
        return sharedPreferences.getBoolean(SET, false);
    }

    public void checkIsSet() {
        if (!this.isSet()) {
         // do something here
        }
    }

//Retrieve the values
// Set<String> set = myScores.getStringSet("key", null);

//Set the values


    public HashMap<String, String> getUserDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(STATUS, sharedPreferences.getString(STATUS, null));
        user.put(PID, sharedPreferences.getString(PID, null));
        return user;
    }


// public Set<String> set = new HashSet<String>(){
//     Set<String> set = new HashSet<String>();
// set.addAll(listOfExistingScores);
// scoreEditor.putStringSet("key", set);
// scoreEditor.commit();
// }

    public void clleaa() {
        editor.clear();
        editor.commit();
    }
}
