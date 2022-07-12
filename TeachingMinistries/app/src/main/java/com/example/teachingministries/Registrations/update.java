package com.example.teachingministries.Registrations;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

public class update extends AppCompatActivity {

    TextView id, category;
    EditText fullname, phone_regist;
    SessionManager sessionManager;
    String getId, number, names, imagesDp;
    Urls urls;
    LinearLayout show_detss;
    CardView update_account;
    ImageView user_dets_image;
    Dialog ss_card, ee_card;
    Dialog error_message, no_message;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        error_message = new Dialog(this);
        no_message = new Dialog(this);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        number = user.get(SessionManager.CONTACT);
        names = user.get(SessionManager.FULLNAME);
        urls = new Urls();
        ss_card = new Dialog(this);
        ee_card = new Dialog(this);

        update_account = findViewById(R.id.update_account);
        fullname = findViewById(R.id.fullname_regist);
        phone_regist = findViewById(R.id.phone_regist);


        update_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String full_name_val = fullname.getText().toString().trim();
//                final String usernames = username.getText().toString().trim();
//                String pass = etPassword.getText().toString().trim();
//
//                if (!full_name_val.isEmpty() && !usernames.isEmpty() && !pass.isEmpty()) {


                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        update.this);
                alertDialog2.setTitle("Confirm to proceed to update account");
                alertDialog2.setMessage("Make sure you double check your account details");
                alertDialog2.setIcon(R.drawable.ic_warning);
                alertDialog2.setPositiveButton("YES",
                        (dialog, which) -> {
                            SaveEditDetail();
                        });
                alertDialog2.setNegativeButton("NO",
                        (dialog, which) -> dialog.cancel());
                alertDialog2.show();


//                } else {
//                    Toast.makeText(update.this, "All fields are required", Toast.LENGTH_SHORT).show();
//                }
            }

        });


        getUserDetail();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getUserDetail();
    }


    private void getUserDetail() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait as we fetch your details");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.USERDETS_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONArray users = new JSONArray(response);
                            if (users.length() == 0) {
                                dialog.dismiss();
                                Toast.makeText(update.this, "No results found", Toast.LENGTH_SHORT).show();
                            } else {
                                for (int i = 0; i < users.length(); i++) {
                                    dialog.dismiss();
                                    JSONObject object = users.getJSONObject(i);

                                    String ids = object.getString("userid");
                                    String fullnames = object.getString("fullname");
                                    String password1 = object.getString("phone");

                                    fullname.setText(fullnames);
                                    phone_regist.setText(password1);

                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();

                            Toast.makeText(update.this, "Error something went wrong", Toast.LENGTH_SHORT).show();

                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(update.this, "Error something went wrong, please try again", Toast.LENGTH_SHORT).show();


                    }
                }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userid", getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void SaveEditDetail() {


        final String full_names = this.fullname.getText().toString().trim();
        final String pass = this.phone_regist.getText().toString().trim();


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating profile, please wait...");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.EDITACCOUNT,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                sessionManager.logout();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();

                            Toast.makeText(update.this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Toast.makeText(update.this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();


                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fullname", full_names);
                params.put("phone", pass);
                params.put("userid", getId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    public void goback(View view) {
        startActivity(new Intent(update.this, MainActivity.class));
    }
}