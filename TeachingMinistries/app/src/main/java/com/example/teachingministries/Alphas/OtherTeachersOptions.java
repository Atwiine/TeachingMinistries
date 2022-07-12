package com.example.teachingministries.Alphas;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingministries.MainActivity;
import com.example.teachingministries.R;
import com.example.teachingministries.Registrations.Settings;
import com.example.teachingministries.Students.MissedStudents;
import com.example.teachingministries.Students.PassStudents;
import com.example.teachingministries.Students.Students;
import com.example.teachingministries.Urls.SessionManager;
import com.example.teachingministries.Urls.Urls;
import com.google.android.material.card.MaterialCardView;

import java.util.HashMap;

public class OtherTeachersOptions extends AppCompatActivity {

    LinearLayout linear_alpha1, linear_alpha2, linear_alpha3, linear_alpha4;
    ImageView home, coming_soon, settings;
    TextView name_user,selected_alpha;
    String namesUser, getID, getType,showTeacher;
    SessionManager sessionManager;
    Urls urls;
    MaterialCardView card_received_students,card_reg_students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_teachers_options);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(SessionManager.ID);
        namesUser = user.get(SessionManager.FULLNAME);
        getType = user.get(SessionManager.TYPE);
        urls = new Urls();


        linear_alpha1 = findViewById(R.id.linear_alpha1);
        card_received_students = findViewById(R.id.card_received_students);
        card_reg_students = findViewById(R.id.card_reg_students);
        selected_alpha = findViewById(R.id.selected_alpha);

        if (getType.equals("Alpha one")){
            card_reg_students.setVisibility(View.GONE);
            card_received_students.setVisibility(View.VISIBLE);
        }else {
            card_received_students.setVisibility(View.GONE);
            card_reg_students.setVisibility(View.VISIBLE);
        }

        /*handle the asking teacher and bring their type*/
        showTeacher = getIntent().getStringExtra("selected_alpha");
        if (!TextUtils.isEmpty(showTeacher)) {
            selected_alpha.setText(showTeacher);
            if (showTeacher.equals("Alpha one")) {
                card_reg_students.setVisibility(View.VISIBLE);
                card_received_students.setVisibility(View.GONE);
            } else {
                card_reg_students.setVisibility(View.GONE);
                card_received_students.setVisibility(View.VISIBLE);

            }
        }

        home = findViewById(R.id.home);
        coming_soon = findViewById(R.id.coming_soon);
        settings = findViewById(R.id.settings);
//        name_user = findViewById(R.id.name_user);
//
//        name_user.setText(namesUser);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OtherTeachersOptions.this, Settings.class));
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OtherTeachersOptions.this, MainActivity.class));
            }
        });
        coming_soon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, update.class));
                Toast.makeText(OtherTeachersOptions.this, "coming soon", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void openRegisterStudentsAlpha1(View view) {
/* should only work for other alphas not alpha one
* and shld also show the registered students*/
        String  er = "Alpha one";
        Intent rr = new Intent(OtherTeachersOptions.this, Students.class);
        rr.putExtra("selected_alpha",er);
        startActivity(rr);
    }

    public void openReceivedStudentsAlpha1(View view) {
/*show the students that the selected alpha received*/

        String  er = "check";//attach the selected alpha
        Intent rr = new Intent(OtherTeachersOptions.this, Students.class);
        rr.putExtra("selected_alpha",showTeacher);
        startActivity(rr);

    }

    public void openPresentStudents(View view) {
        /*show the students that the were present in the selected alpha class*/

        String  er = "check";//attach the selected alpha
        Intent rr = new Intent(OtherTeachersOptions.this, PassStudents.class);
        rr.putExtra("selected_alpha",showTeacher);
        startActivity(rr);
    }

    public void openMissedStudents(View view) {
        /*show the students that the were absent in the selected alpha class*/

        String  er = "check";//attach the selected alpha
        Intent rr = new Intent(OtherTeachersOptions.this, MissedStudents.class);
        rr.putExtra("selected_alpha",showTeacher);
        startActivity(rr);

    }


    public void back(View view) {
        startActivity(new Intent(OtherTeachersOptions.this, MainActivity.class));
        finish();
    }
}