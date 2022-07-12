package com.example.tushome.Registrations;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tushome.R;
import com.example.tushome.Urls.SessionManager;
import com.example.tushome.Urls.Urls;
import com.google.android.material.card.MaterialCardView;

import java.util.HashMap;

public class Error_account extends AppCompatActivity {

    TextView reason_suspend, reason_deny, username_error;
    MaterialCardView call_suspend, call_deny;
    LinearLayout linear_suspended_error, linear_denied_error;
    SessionManager sessionManager;
    Urls urls;
    String getID;
    ImageView reader_dp_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_account);

        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(SessionManager.ID);
        urls = new Urls();


        reason_suspend = findViewById(R.id.reason_suspend);
        reason_deny = findViewById(R.id.reason_deny);
        username_error = findViewById(R.id.username_error);
        call_suspend = findViewById(R.id.call_suspend);
        call_deny = findViewById(R.id.call_deny);
        linear_suspended_error = findViewById(R.id.linear_suspended_error);
        linear_denied_error = findViewById(R.id.linear_denied_error);


        reason_suspend.setText(getIntent().getStringExtra("reason"));
        reason_deny.setText(getIntent().getStringExtra("reason"));
        username_error.setText(getIntent().getStringExtra("username"));
        String action = getIntent().getStringExtra("action");


        reader_dp_error = findViewById(R.id.reader_dp_error);
        String imgurl = getIntent().getStringExtra("image_url");

        Glide.with(this)
                .load(imgurl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .fallback(R.drawable.ic_launcher_background)
                .into(reader_dp_error);


        if (action.contains("suspended")) {
            linear_denied_error.setVisibility(View.GONE);
            linear_suspended_error.setVisibility(View.VISIBLE);
        } else if (action.contains("denied")) {
            linear_suspended_error.setVisibility(View.GONE);
            linear_denied_error.setVisibility(View.VISIBLE);
        }


        call_suspend.setOnClickListener(view -> callUs());

        call_deny.setOnClickListener(view -> callUs());

    }

    public void callUs() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:0754840755"));
        try {
            startActivity(callIntent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    public void back(View view) {
        startActivity(new Intent(Error_account.this, LoginSecond.class));
        finish();
    }
}