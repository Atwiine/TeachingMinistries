package com.example.tushome.Registrations;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tushome.Authors.AuthorActivity;
import com.example.tushome.R;
import com.example.tushome.Reader.MainActivity;
import com.example.tushome.Urls.SessionManager;
import com.example.tushome.Urls.Urls;

import java.util.HashMap;

public class CheckFirst extends AppCompatActivity {

    Urls urls;
    SessionManager sessionManager;
    String getID, cate = "";
    TextView ccc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_first);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(SessionManager.ID);
        String cate = user.get(SessionManager.CATEGORY);
        urls = new Urls();

        ccc = findViewById(R.id.ccc);
        ccc.setText(cate);
        String ok = ccc.getText().toString();

//        if (TextUtils.isEmpty(cate)){

        if (ok.contains("Author")) {
            startActivity(new Intent(CheckFirst.this, AuthorActivity.class));
            finish();
        } else if (ok.contains("Reader")) {
            startActivity(new Intent(CheckFirst.this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(CheckFirst.this, LoginSecond.class));
            finish();
        }
//            Toast.makeText(this, "empty shit", Toast.LENGTH_SHORT).show();

//        }
//        else {
//            startActivity(new Intent(CheckFirst.this, LoginSecond.class));
//            finish();
////            Toast.makeText(this, "not empty shit", Toast.LENGTH_SHORT).show();
//
//        }


    }


}