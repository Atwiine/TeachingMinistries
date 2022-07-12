package com.example.teachingministries.Registrations;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.teachingministries.Urls.SessionManager;
import com.example.teachingministries.Urls.Urls;
import com.example.teachingministries.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class first_registration extends AppCompatActivity {

    CardView reg, rules;
    EditText fullname, phone_regist, etPassword, confirm_etPassword, quotes;
    Urls urls;
    SessionManager sessionManager;
    RadioGroup group_alpha;
    RadioButton ra1, ra2, ra3, ra4;
    RelativeLayout show_dp;
    ImageView real_dp;
    TextView already;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_registration);


        sessionManager = new SessionManager(this);
        urls = new Urls();

        already = findViewById(R.id.button17);
        fullname = findViewById(R.id.fullname_regist);
        phone_regist = findViewById(R.id.phone_regist);
        reg = findViewById(R.id.reg);
        show_dp = findViewById(R.id.show_dp);
        real_dp = findViewById(R.id.real_dp);
        group_alpha = findViewById(R.id.group_alpha);


        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(first_registration.this, LoginSecond.class);
                startActivity(intent);
            }
        });


        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String full_name_val = fullname.getText().toString().trim();
                final String phone_regists = phone_regist.getText().toString().trim();


                if (!full_name_val.isEmpty() && !phone_regists.isEmpty()) {

                    int selectedInt = group_alpha.getCheckedRadioButtonId();
                    if (selectedInt == -1) {
                        Toast.makeText(first_registration.this, "Please select alpha", Toast.LENGTH_SHORT).show();
                    } else {
//                            final String ra11 = ra1.getText().toString().trim();
//                            final String ra22 = ra2.getText().toString().trim();
//                            final String ra33 = ra3.getText().toString().trim();

                        RadioButton radioButton1 = (RadioButton) group_alpha.findViewById(selectedInt);

                        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                                first_registration.this);
                        alertDialog2.setTitle("Confirm to proceed to register");
                        alertDialog2.setMessage("Make sure you double check your registration details");
                        alertDialog2.setIcon(R.drawable.ic_warning);
                        alertDialog2.setPositiveButton("YES",
                                (dialog, which) -> {
                                    Register(radioButton1);
//                                    sessionManager.createSession(phone_regists, full_name_val, "id", radioButton1.getText().toString());
//                                    Intent aa = new Intent(first_registration.this, MainActivity.class);
//                                    startActivity(aa);
//                                    finish();
                                });
                        alertDialog2.setNegativeButton("NO",
                                (dialog, which) -> dialog.cancel());
                        alertDialog2.show();
                    }

                } else {
                    Toast.makeText(first_registration.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }

            }

        });


        group_alpha.clearCheck();

        group_alpha.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                ra1 = findViewById(R.id.ra1);
                ra2 = findViewById(R.id.ra2);
                ra3 = findViewById(R.id.ra3);
                ra4 = findViewById(R.id.ra4);
            }
        });

    }


    private void Register(RadioButton radioButton1) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait ...");
        dialog.show();

        final String full_names = this.fullname.getText().toString().trim();
        final String phonee = this.phone_regist.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.FIRSTREG_URL,
                response -> {
                    try {

                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        if (success.equals("1")) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();

                            Intent aa = new Intent(first_registration.this, LoginSecond.class);
                            startActivity(aa);
                            finish();

                        }
                        if (success.equals("2")) {
                            Toast.makeText(getApplicationContext(), "Sorry username already taken, please choose another username and try again.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Registration was unsuccessful, please try again", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Registration continues to fail, please check your internet connection and try again "+ error.toString(), Toast.LENGTH_SHORT).show();
                }
        ) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("fullname", full_names);
                params.put("phone", phonee);
                params.put("type", radioButton1.getText().toString());
                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
