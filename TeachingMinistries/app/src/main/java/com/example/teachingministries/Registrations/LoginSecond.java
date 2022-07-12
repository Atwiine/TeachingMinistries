package com.example.teachingministries.Registrations;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.teachingministries.MainActivity;
import com.example.teachingministries.R;
import com.example.teachingministries.Urls.SessionManager;
import com.example.teachingministries.Urls.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginSecond extends AppCompatActivity {
    Urls urls;
    TextView heading, register;
    EditText etPhone;
    CardView login;
    SessionManager sessionManager;
    Dialog not_confirmed, denied_access, suspended_access;
    String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_second);
        sessionManager = new SessionManager(this);
        urls = new Urls();

        not_confirmed = new Dialog(this);
        denied_access = new Dialog(this);
        suspended_access = new Dialog(this);


        heading = findViewById(R.id.textView);
        etPhone = findViewById(R.id.etPhone);
        login = findViewById(R.id.button);
        register = findViewById(R.id.reg);


        register.setOnClickListener(v -> {
            /*remember to check after testing*/
            Intent intent = new Intent(LoginSecond.this, first_registration.class);
            startActivity(intent);

        });

        login.setOnClickListener(v -> {
            String etPhones = etPhone.getText().toString().trim();

            if (!etPhones.isEmpty()) {
                Login(etPhones);
//                Toast.makeText(this, "Due to testing this part wont work", Toast.LENGTH_SHORT).show();
            } else {
                etPhone.setError("Enter your phone");
            }
        });
    }


    private void Login(final String etPhones) {

        final ProgressDialog loading = new ProgressDialog(LoginSecond.this);
        loading.setMessage("logging in please wait...");
        loading.show();
//        login.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.LOGIN_URL,
                response -> {
                    Log.i("tagconvertstr", "[" + response + "]");
                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("login");
                        if (success.equals("1")) {
                            Toast.makeText(LoginSecond.this, "login success", Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Log.i("tagconvertstr", "[" + response + "]");
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("userid");
                                String contact = object.getString("phone");
                                String fullname = object.getString("fullname");
                                String type = object.getString("type");

                                sessionManager.createSession(contact, fullname, id, type);

                                Intent intent = new Intent(LoginSecond.this, MainActivity.class);
                                intent.putExtra("username", fullname);
                                startActivity(intent);
                                finish();

                                loading.dismiss();


                            }
                        } else if (success.equals("0")) {
                            loading.dismiss();
//                            login.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginSecond.this, "login was error account not found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        loading.dismiss();
//                        login.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginSecond.this, "failed to login, please try again ", Toast.LENGTH_LONG).show();
                    }
                },

                error -> {
                    loading.dismiss();
//                    login.setVisibility(View.VISIBLE);
                    Toast.makeText(LoginSecond.this, "login error please check your internet connection and try again ", Toast.LENGTH_SHORT).show();
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", etPhones);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}