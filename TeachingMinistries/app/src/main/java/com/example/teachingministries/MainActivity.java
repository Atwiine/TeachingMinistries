package com.example.teachingministries;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingministries.Alphas.OtherTeachersOptions;
import com.example.teachingministries.Alphas.TrackAttendance1;
import com.example.teachingministries.Alphas.add_students;
import com.example.teachingministries.Registrations.Settings;
import com.example.teachingministries.Urls.SessionManager;
import com.example.teachingministries.Urls.Urls;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    LinearLayout linear_alpha1, linear_alpha2, linear_alpha3, linear_alpha4;
    ImageView home, coming_soon, settings;
    TextView name_user,logged_teacher;
    String namesUser, getID, getType = "null";
    SessionManager sessionManager;
    Urls urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(SessionManager.ID);
        namesUser = user.get(SessionManager.FULLNAME);
        getType = user.get(SessionManager.TYPE);
        urls = new Urls();

        if (TextUtils.isEmpty(getType)) {
            getType = "null";
        }

        /**
         * make sure to come and fix this part that crashes at the start*/
//        if (!TextUtils.isEmpty(showReceived)) {
//            showAttend(showReceived);
//            crer.setVisibility(View.GONE);
//            heading.setText(showReceived + " attended participants");
//        }
        linear_alpha1 = findViewById(R.id.linear_alpha1);
        linear_alpha2 = findViewById(R.id.linear_alpha2);
        linear_alpha3 = findViewById(R.id.linear_alpha3);
        linear_alpha4 = findViewById(R.id.linear_alpha4);

        home = findViewById(R.id.home);
        coming_soon = findViewById(R.id.coming_soon);
        settings = findViewById(R.id.settings);
        name_user = findViewById(R.id.name_user);
        logged_teacher = findViewById(R.id.logged_teacher);

        name_user.setText(namesUser);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Settings.class));
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });
        coming_soon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, update.class));
                Toast.makeText(MainActivity.this, "coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * check to see which type of alpha is logged in and then display the correct layout **/

        if (getType.equals("null")) {
//            getType.equals("none");
            Toast.makeText(this, "Teacher profile not set", Toast.LENGTH_SHORT).show();
        }
        switch (getType) {
            case "Alpha one":
                linear_alpha1.setVisibility(View.VISIBLE);
                logged_teacher.setText(getType);
                break;
            case "Alpha two":
                linear_alpha2.setVisibility(View.VISIBLE);
                logged_teacher.setText(getType);
                break;
            case "Alpha three":
                linear_alpha3.setVisibility(View.VISIBLE);
                logged_teacher.setText(getType);
                break;
            case "Alpha four":
                linear_alpha4.setVisibility(View.VISIBLE);
                logged_teacher.setText(getType);
                break;
        }


    }

    public void openTrackAlpha1(View view) {

        startActivity(new Intent(MainActivity.this, TrackAttendance1.class));
    }

    public void openTrackAlpha2(View view) {
        startActivity(new Intent(MainActivity.this, TrackAttendance1.class));

    }

    public void openTrackAlpha3(View view) {

        startActivity(new Intent(MainActivity.this, TrackAttendance1.class));
    }

    public void openTrackAlpha4(View view) {
        startActivity(new Intent(MainActivity.this, TrackAttendance1.class));
    }


    public void openAddStudentsAlpha1(View view) {
        startActivity(new Intent(MainActivity.this, add_students.class));
    }

    public void openSeeOtherTeachersAlpha1(View view) {
        String  er = "check";//attach the selected alpha
        Intent rr = new Intent(MainActivity.this, OtherTeachersOptions.class);
        rr.putExtra("selected_alpha",getType);
        startActivity(rr);
    }
    public void openSeeOtherTechersAlpha2(View view) {
        Intent rr = new Intent(MainActivity.this, OtherTeachersOptions.class);
        rr.putExtra("selected_alpha",getType);
        startActivity(rr);
    }
    public void openSeeOtherTechersAlpha3(View view) {
        Intent rr = new Intent(MainActivity.this, OtherTeachersOptions.class);
        rr.putExtra("selected_alpha",getType);
        startActivity(rr);
    }
    public void openSeeOtherTechersAlpha4(View view) {
        Intent rr = new Intent(MainActivity.this, OtherTeachersOptions.class);
        rr.putExtra("selected_alpha",getType);
        startActivity(rr);
    }

    public void oneseetwo(View view) {
        String  er = "Alpha two";//attach the selected alpha
        Intent rr = new Intent(MainActivity.this, OtherTeachersOptions.class);
        rr.putExtra("selected_alpha",er);
        startActivity(rr);
    }
    public void oneseethree(View view) {
        String  er = "Alpha three";//attach the selected alpha
        Intent rr = new Intent(MainActivity.this, OtherTeachersOptions.class);
        rr.putExtra("selected_alpha",er);
        startActivity(rr);
    }
    public void oneseefour(View view) {
        String  er = "Alpha four";//attach the selected alpha
        Intent rr = new Intent(MainActivity.this, OtherTeachersOptions.class);
        rr.putExtra("selected_alpha",er);
        startActivity(rr);
    }
    public void twoseeone(View view) {
        String  er = "Alpha one";//attach the selected alpha
        Intent rr = new Intent(MainActivity.this, OtherTeachersOptions.class);
        rr.putExtra("selected_alpha",er);
        startActivity(rr);
    }
    public void twoseethree(View view) {
        String  er = "Alpha three";//attach the selected alpha
        Intent rr = new Intent(MainActivity.this, OtherTeachersOptions.class);
        rr.putExtra("selected_alpha",er);
        startActivity(rr);
    }
    public void twoseefour(View view) {
        String  er = "Alpha four";//attach the selected alpha
        Intent rr = new Intent(MainActivity.this, OtherTeachersOptions.class);
        rr.putExtra("selected_alpha",er);
        startActivity(rr);
    }
    public void threeseeone(View view) {
        String  er = "Alpha one";//attach the selected alpha
        Intent rr = new Intent(MainActivity.this, OtherTeachersOptions.class);
        rr.putExtra("selected_alpha",er);
        startActivity(rr);
    }
    public void threeseetwo(View view) {
        String  er = "Alpha two";//attach the selected alpha
        Intent rr = new Intent(MainActivity.this, OtherTeachersOptions.class);
        rr.putExtra("selected_alpha",er);
        startActivity(rr);
    }
    public void threeseefour(View view) {
        String  er = "Alpha four";//attach the selected alpha
        Intent rr = new Intent(MainActivity.this, OtherTeachersOptions.class);
        rr.putExtra("selected_alpha",er);
        startActivity(rr);
    }
    public void fourseeone(View view) {
        String  er = "Alpha one";//attach the selected alpha
        Intent rr = new Intent(MainActivity.this, OtherTeachersOptions.class);
        rr.putExtra("selected_alpha",er);
        startActivity(rr);
    }
    public void fourseetwo(View view) {
        String  er = "Alpha two";//attach the selected alpha
        Intent rr = new Intent(MainActivity.this, OtherTeachersOptions.class);
        rr.putExtra("selected_alpha",er);
        startActivity(rr);
    }
    public void fourseethree(View view) {
        String  er = "Alpha three";//attach the selected alpha
        Intent rr = new Intent(MainActivity.this, OtherTeachersOptions.class);
        rr.putExtra("selected_alpha",er);
        startActivity(rr);
    }
}