package com.example.teachingministries.Registrations;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.teachingministries.R;
import com.example.teachingministries.MainActivity;
import com.example.teachingministries.Urls.SessionManager;


public class Settings extends AppCompatActivity {

    CardView reg, rules;
    SessionManager sessionManager;
    ImageView home, coming_soon, settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        sessionManager = new SessionManager(this);
        coming_soon = findViewById(R.id.coming_soon);
        settings = findViewById(R.id.settings);
        home = findViewById(R.id.home);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, Settings.class));
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, MainActivity.class));
            }
        });
        coming_soon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, update.class));
                Toast.makeText(Settings.this, "coming soon", Toast.LENGTH_SHORT).show();
            }
        });


    }


    public void openUpdate(View view) {
        startActivity(new Intent(Settings.this, update.class));
    }

    public void openLogout(View view) {
        sessionManager.logout();
    }
}
