package com.example.gabbagemonitoringapp.Registrations;

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
import com.example.gabbagemonitoringapp.MainActivity;
import com.example.gabbagemonitoringapp.R;
import com.example.gabbagemonitoringapp.Urls.SessionManager;
import com.example.gabbagemonitoringapp.Urls.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginSecond extends AppCompatActivity {
    Urls urls;
    TextView heading, register;
    EditText names, etPassword;
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
        names = findViewById(R.id.name_login);
        etPassword = findViewById(R.id.etPassword);
        login = findViewById(R.id.button);
        register = findViewById(R.id.reg);


        register.setOnClickListener(v -> {
            Intent intent = new Intent(LoginSecond.this, first_registration.class);
            startActivity(intent);
        });

        login.setOnClickListener(v -> {
            String user = names.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (!user.isEmpty() || !pass.isEmpty()) {
                Login(user, pass);
                startActivity(new Intent(LoginSecond.this,MainActivity.class));
            } else {
                names.setError("Enter your username");
                etPassword.setError("Enter  Password");
            }
        });
    }

    //ADD THE VOLLEY THING

    private void Login(final String name, final String password) {

        final ProgressDialog loading = new ProgressDialog(LoginSecond.this);
        loading.setMessage("logging in please wait...");
        loading.show();

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
                                String username = object.getString("username");
                                String fullname = object.getString("fullname");
                                String password1 = object.getString("password");
                                String type = object.getString("type");//who is using the app

                                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                                sessionManager.createSession(username,  fullname, id, password1,type);
                                        Intent intent = new Intent(LoginSecond.this, CheckFirst.class);
                                        intent.putExtra("username", username);
                                        startActivity(intent);
                                        finish();
                                }
                                loading.dismiss();

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
                params.put("username", name);//user the user name not the full name
                params.put("password", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}