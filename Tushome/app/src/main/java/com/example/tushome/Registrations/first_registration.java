package com.example.tushome.Registrations;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tushome.R;
import com.example.tushome.Urls.SessionManager;
import com.example.tushome.Urls.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class first_registration extends AppCompatActivity {

    CardView reg , rules;
    EditText fullname, username, etPassword, confirm_etPassword,quotes;
    Urls urls;
    SessionManager sessionManager;
    Spinner cat;
    RelativeLayout show_dp;
    ImageView real_dp;
    private Bitmap bitmap;
    TextView already;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_registration);


        sessionManager = new SessionManager(this);
        urls = new Urls();

        cat = findViewById(R.id.category);
        already = findViewById(R.id.button17);
        fullname = findViewById(R.id.fullname_regist);
        username = findViewById(R.id.username_regist);
        etPassword = findViewById(R.id.etPassword);
        reg = findViewById(R.id.reg);
        confirm_etPassword = findViewById(R.id.confirm_etPassword);
        show_dp = findViewById(R.id.show_dp);
        real_dp = findViewById(R.id.real_dp);
        quotes = findViewById(R.id.quotes);


//        if (){
//            Toast.makeText(this, "shit is empty", Toast.LENGTH_SHORT).show();
//        }


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
                final String usernames = username.getText().toString().trim();
                String pass = etPassword.getText().toString().trim();
                String confirm_pass = confirm_etPassword.getText().toString().trim();
                String qqq = quotes.getText().toString().trim();


                if (!(real_dp.getDrawable() == null)) {
                    if (!full_name_val.isEmpty() && !usernames.isEmpty() && !pass.isEmpty()
                            && !confirm_pass.isEmpty()) {

                        /* checking if password and confirm password aint equal*/
                        if (pass.equals(confirm_pass)) {


                            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                                    first_registration.this);
                            alertDialog2.setTitle("Confirm to proceed to register");
                            alertDialog2.setMessage("Make sure you double check your registration details");
                            alertDialog2.setIcon(R.drawable.ic_warning);
                            alertDialog2.setPositiveButton("YES",
                                    (dialog, which) -> {
                                        Register(getStringImage(bitmap));
                                    });
                            alertDialog2.setNegativeButton("NO",
                                    (dialog, which) -> dialog.cancel());
                            alertDialog2.show();

                        } else {
                            Toast.makeText(first_registration.this, "password fields do not match", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(first_registration.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(first_registration.this, "Select a profile picture", Toast.LENGTH_SHORT).show();

                }
            }

        });


    }


    public void uploadDP(View view) {
        chooseFile();
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a profile picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                show_dp.setVisibility(View.VISIBLE);
                real_dp.setImageBitmap(bitmap);
                getStringImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }


    private void Register(String photo) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait ...");
        dialog.show();

        final String full_names = this.fullname.getText().toString().trim();
        final String usernn = this.username.getText().toString().trim();
        final String pass = this.etPassword.getText().toString().trim();
        final String categg = this.cat.getSelectedItem().toString();
final String qqq = quotes.getText().toString().trim();

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

                        }if (success.equals("2")) {
                            Toast.makeText(getApplicationContext(),"Sorry username already taken, please choose another username and try again.",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), "Registration continues to fail, please check your internet connection and try again", Toast.LENGTH_SHORT).show();
                }
        ) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("fullname", full_names);
                params.put("username", usernn);
                params.put("password", pass);
                params.put("catergory", categg);
                params.put("image", photo);
                params.put("quote", qqq);
                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }

    public void backLogin(View view) {
        startActivity(new Intent(first_registration.this,LoginSecond.class));
    }
}
