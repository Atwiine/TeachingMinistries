package com.example.tushome.Registrations;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tushome.Authors.AuthorActivity;
import com.example.tushome.R;
import com.example.tushome.Reader.MainActivity;
import com.example.tushome.Urls.SessionManager;
import com.example.tushome.Urls.Urls;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

            } else {
                names.setError("Enter your username");
                etPassword.setError("Enter  Password");
            }
        });
    }

    private void Login(final String name, final String password) {

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
                                String username = object.getString("username");
                                String fullname = object.getString("fullname");
                                String password1 = object.getString("password");
                                String category = object.getString("category");
                                String image = object.getString("image");
                                String status = object.getString("status");
                                String suspended = object.getString("suspended");
                                String reason = object.getString("reason");


                                sessionManager.createSession(username, "contact", fullname, id, password1, category, image);

                                if (category.equals("author") || category.equals("Author")){
                                    category = "Author";
                                }

                                if (category.equals("Reader")) {
                                    Intent intent = new Intent(LoginSecond.this, MainActivity.class);
                                    intent.putExtra("username", username);
                                    intent.putExtra("image_url", urls.https + "user_images/" + image);
                                    startActivity(intent);
                                    finish();
//                                    Toast.makeText(this, "path is " + urls.https + "user_images/" + image , Toast.LENGTH_SHORT).show();
                                } else if (category.equals("Author")) {


                                    if (status.equals("pending")) {
                                        Toast.makeText(this, "pending login", Toast.LENGTH_SHORT).show();
                                        not_confirmed = new Dialog(this);
                                        not_confirmed.setContentView(R.layout.not_confirmed);
                                        MaterialCardView next = not_confirmed.findViewById(R.id.not_confirmed_account);

                                        next.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                not_confirmed.dismiss();
                                            }
                                        });

                                        not_confirmed.setCancelable(false);
                                        not_confirmed.setCanceledOnTouchOutside(false);
                                        Objects.requireNonNull(not_confirmed.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        not_confirmed.show();


                                    } else if (status.equals("confirmed") && suspended.equals("activated")) {
                                        Toast.makeText(this, "confirmed login", Toast.LENGTH_SHORT).show();
                                        sessionManager.createSession(username, "contact", fullname, id, password1, category, image);


                                        Intent intent = new Intent(LoginSecond.this, AuthorActivity.class);
                                        intent.putExtra("username", username);
                                        intent.putExtra("image_url", urls.https + "user_images/" + image);
                                        startActivity(intent);
                                        finish();
                                    } else if (status.equals("denied")) {

//                                        Toast.makeText(this, "denied login", Toast.LENGTH_SHORT).show();
//                                        denied_access = new Dialog(this);
//                                        denied_access.setContentView(R.layout.denied_access);
//                                        MaterialCardView contact_denied_access = denied_access.findViewById(R.id.contact_denied_access);
//
//                                        contact_denied_access.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
//                                                callIntent.setData(Uri.parse("tel:0754840755"));
//                                                try {
//                                                    startActivity(callIntent);
//                                                } catch (SecurityException e) {
//                                                    e.printStackTrace();
//                                                }
//                                                denied_access.dismiss();
//                                            }
//                                        });
//
//                                        denied_access.setCancelable(false);
//                                        denied_access.setCanceledOnTouchOutside(false);
//                                        Objects.requireNonNull(denied_access.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                                        denied_access.show();


                                        /*remember to come back and fix this part*/

//                                        sessionManager.createSession(username, "contact", fullname, id, password1, category, image);

//
                                        Intent intent = new Intent(LoginSecond.this, Error_account.class);
                                        action = "denied";
                                        intent.putExtra("username", username);
                                        intent.putExtra("reason", reason);
                                        intent.putExtra("action", action);
                                        intent.putExtra("image_url", urls.https + "user_images/" + image);
                                        startActivity(intent);
                                        finish();
//
//                                        Intent intent = new Intent(LoginSecond.this, AuthorActivity.class);
//                                        intent.putExtra("username", username);
//                                        intent.putExtra("image_url", urls.https + "user_images/" + image);
//                                        startActivity(intent);
//                                        finish();


                                    } else if (status.equals("confirmed") && suspended.equals("deactivated")) {
                                        Toast.makeText(this, "deactivated login", Toast.LENGTH_SHORT).show();

                                       /* suspended_access = new Dialog(this);
                                        suspended_access.setContentView(R.layout.suspended_access);
                                        MaterialCardView contact_denied_access = suspended_access.findViewById(R.id.contact_suspended_access);

                                        contact_denied_access.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                                callIntent.setData(Uri.parse("tel:0754840755"));
                                                try {
                                                    startActivity(callIntent);
                                                } catch (SecurityException e) {
                                                    e.printStackTrace();
                                                }
                                                suspended_access.dismiss();
                                            }
                                        });

                                        suspended_access.setCancelable(false);
                                        suspended_access.setCanceledOnTouchOutside(false);
                                        Objects.requireNonNull(suspended_access.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        suspended_access.show();*/

                                        Intent intent = new Intent(LoginSecond.this, Error_account.class);
                                        action = "suspended";
                                        intent.putExtra("username", username);
                                        intent.putExtra("reason", reason);
                                        intent.putExtra("action", action);
                                        intent.putExtra("image_url", urls.https + "user_images/" + image);
                                        startActivity(intent);
                                        finish();


                                    }
//                                    Toast.makeText(this, "path is " + urls.https + "user_images/" + image, Toast.LENGTH_SHORT).show();

                                }
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
                params.put("username", name);//user the user name not the full name
                params.put("password", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}