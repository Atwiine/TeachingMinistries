package com.example.teachingministries.Alphas;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.teachingministries.MainActivity;
import com.example.teachingministries.R;
import com.example.teachingministries.Registrations.Settings;
import com.example.teachingministries.Students.Students;
import com.example.teachingministries.Urls.SessionManager;
import com.example.teachingministries.Urls.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class add_students extends AppCompatActivity {

    CardView reg, rules;
    EditText fullname, phone_regist,remarks_regist
          , confirm_etPassword, quotes;
    Urls urls;
    SessionManager sessionManager;
    Spinner cat;
    RelativeLayout show_dp;
    ImageView real_dp;
    ImageView home, coming_soon, settings;
    String namesUser, getID, getType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_students);


        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(SessionManager.ID);
        namesUser = user.get(SessionManager.FULLNAME);
        getType = user.get(SessionManager.TYPE);
        urls = new Urls();

        fullname = findViewById(R.id.fullname_regist);
        phone_regist = findViewById(R.id.phone_regist);
        reg = findViewById(R.id.reg);
        show_dp = findViewById(R.id.show_dp);
        real_dp = findViewById(R.id.real_dp);
        home = findViewById(R.id.home);
        coming_soon = findViewById(R.id.coming_soon);
        settings = findViewById(R.id.settings);
        remarks_regist = findViewById(R.id.remarks_reg);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(add_students.this, Settings.class));
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(add_students.this, MainActivity.class));
            }
        });
        coming_soon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, update.class));
                Toast.makeText(add_students.this, "coming soon", Toast.LENGTH_SHORT).show();
            }
        });


        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String full_name_val = fullname.getText().toString().trim();
                final String phone_regists = phone_regist.getText().toString().trim();


                if (!full_name_val.isEmpty() && !phone_regists.isEmpty()) {


                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                            add_students.this);
                    alertDialog2.setTitle("Confirm to proceed to register");
                    alertDialog2.setMessage("Make sure you double check your registration details");
                    alertDialog2.setIcon(R.drawable.ic_warning);
                    alertDialog2.setPositiveButton("YES",
                            (dialog, which) -> {
                                Register();
//                                Toast.makeText(add_students.this, "This part wont work because am connected to the internet", Toast.LENGTH_SHORT).show();
                            });
                    alertDialog2.setNegativeButton("NO",
                            (dialog, which) -> dialog.cancel());
                    alertDialog2.show();


                } else {
                    Toast.makeText(add_students.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }

            }

        });


    }


    private void Register() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait ...");
        dialog.show();

        final String full_names = this.fullname.getText().toString().trim();
        final String phonee = this.phone_regist.getText().toString().trim();
        final String remarks = this.remarks_regist.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.STUDENTREG_URL,
                response -> {
                    try {

                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        if (success.equals("1")) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();

                            Intent aa = new Intent(add_students.this, add_students.class);
                            startActivity(aa);
                            finish();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Registration was unsuccessful, please try again", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Registration continues to fail, please check your internet connection and try again", Toast.LENGTH_SHORT).show();
                }
        ) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("fullname", full_names);
                params.put("phone", phonee);
                params.put("userid", getID);
                params.put("teacher", getType);
                params.put("remarks", remarks);
                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void openStudents(View view) {
        startActivity(new Intent(add_students.this, Students.class));

    }
}
