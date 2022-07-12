package com.example.tushome.Registrations;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.bumptech.glide.Glide;
import com.example.tushome.Preferences.SettingsActivity;
import com.example.tushome.R;
import com.example.tushome.Urls.SessionManager;
import com.example.tushome.Urls.Urls;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class update extends AppCompatActivity {

    TextView id, category;
    EditText fullname, username, etPassword;
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
        imagesDp = user.get(SessionManager.IMAGE);
        number = user.get(SessionManager.CONTACT);
        names = user.get(SessionManager.FULLNAME);
        urls = new Urls();
        ss_card = new Dialog(this);
        ee_card = new Dialog(this);

        update_account = findViewById(R.id.update_account);
        fullname = findViewById(R.id.fullname_regist);
        username = findViewById(R.id.username_regist);
        etPassword = findViewById(R.id.etPassword);

        user_dets_image = findViewById(R.id.user_dets_image);
        category = findViewById(R.id.category);
        id = findViewById(R.id.id);

//        find out what to do when the image of the user is null

        if (user_dets_image == null) {
            Glide.with(update.this)
                    .load(R.drawable.ic_user)
                    .into(user_dets_image);
        }else {

            Glide.with(update.this)
                    .load(urls.https + "user_images/" + imagesDp)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .fallback(R.drawable.ic_launcher_background)
                    .into(user_dets_image);

        }
//        Handler handler =  new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (user_dets_image.getDrawable() == null){
////                    Toast.makeText(update.this, "image empty", Toast.LENGTH_SHORT).show();
//                    user_dets_image.setBackgroundResource(R.drawable.ic_user);
//                }else {
//                    Toast.makeText(update.this, "image not empty", Toast.LENGTH_SHORT).show();
//                }
//            }
//        },500);


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
                            SaveEditDetail(getStringImage(bitmap));
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
    protected void onResume() {
        super.onResume();
        getUserDetail();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                user_dets_image.setImageBitmap(bitmap);
                getStringImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
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
                                no_message = new Dialog(update.this);
                                no_message.setContentView(R.layout.no_results);
                                MaterialCardView next = no_message.findViewById(R.id.no_results_next);

                                next.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent aa = new Intent(update.this, update.class);
                                        startActivity(aa);
                                        finish();
                                        no_message.dismiss();
                                    }
                                });

                                no_message.setCancelable(false);
                                no_message.setCanceledOnTouchOutside(false);
                                Objects.requireNonNull(no_message.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                no_message.show();
                            } else {
                                for (int i = 0; i < users.length(); i++) {
                                    dialog.dismiss();
                                    JSONObject object = users.getJSONObject(i);

                                    String ids = object.getString("userid");
                                    String usernames = object.getString("username");
                                    String fullnames = object.getString("fullname");
                                    String password1 = object.getString("password");
                                    String categorys = object.getString("category");
                                    String image = object.getString("image");

                                    fullname.setText(fullnames);
                                    username.setText(usernames);
                                    etPassword.setText(password1);
                                    id.setText(ids);
                                    category.setText(categorys);
                                    category.setText(categorys);


                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();

                            error_message = new Dialog(update.this);
                            error_message.setContentView(R.layout.error_message);
                            MaterialCardView tryagains = error_message.findViewById(R.id.tryagain);
                            TextView error_txs = error_message.findViewById(R.id.error_tx);

                            tryagains.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent aa = new Intent(update.this, update.class);
                                    startActivity(aa);
                                    finish();
                                    error_message.dismiss();
                                }
                            });

                            error_message.setCancelable(false);
                            error_message.setCanceledOnTouchOutside(false);
                            Objects.requireNonNull(error_message.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            error_message.show();
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        error_message = new Dialog(update.this);
                        error_message.setContentView(R.layout.error_message);
                        MaterialCardView tryagains = error_message.findViewById(R.id.tryagain);
                        TextView error_txs = error_message.findViewById(R.id.error_tx);

                        tryagains.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent aa = new Intent(update.this, update.class);
                                startActivity(aa);
                                finish();
                                error_message.dismiss();
                            }
                        });

                        error_message.setCancelable(false);
                        error_message.setCanceledOnTouchOutside(false);
                        Objects.requireNonNull(error_message.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        error_message.show();

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


    private void SaveEditDetail(String photo) {


        final String full_names = this.fullname.getText().toString().trim();
        final String usernn = this.username.getText().toString().trim();
        final String pass = this.etPassword.getText().toString().trim();
        final String categg = this.category.getText().toString();
        final String uid = this.id.getText().toString();


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
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

                                sessionManager.createSession(usernn, "contact"
                                        , full_names, uid, pass, categg, photo);

                                ss_card.setContentView(R.layout.right);
                                CardView dismiss = ss_card.findViewById(R.id.ss_card);
                                TextView success_type = ss_card.findViewById(R.id.tx_success);

                                success_type.setText("Update was sent successfully");
                                dismiss.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        ss_card.dismiss();
                                        sessionManager.logout();
                                    }
                                });

                                ss_card.setCancelable(false);
                                ss_card.setCanceledOnTouchOutside(false);
                                Objects.requireNonNull(ss_card.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                ss_card.show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();

                            ee_card.setContentView(R.layout.error);
                            CardView dismiss = ee_card.findViewById(R.id.ee_card);
                            TextView success_type = ee_card.findViewById(R.id.tx_error);

                            success_type.setText("Update not sent successful, please try again");
                            dismiss.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ee_card.dismiss();
                                }
                            });

                            ee_card.setCancelable(false);
                            ee_card.setCanceledOnTouchOutside(false);
                            Objects.requireNonNull(ee_card.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            ee_card.show();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        ee_card.setContentView(R.layout.error);
                        CardView dismiss = ee_card.findViewById(R.id.ee_card);
                        TextView success_type = ee_card.findViewById(R.id.tx_error);

                        success_type.setText("Update not sent successful,please check your network and try again");
                        dismiss.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ee_card.dismiss();
                            }
                        });

                        ee_card.setCancelable(false);
                        ee_card.setCanceledOnTouchOutside(false);
                        Objects.requireNonNull(ee_card.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        ee_card.show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fullname", full_names);
                params.put("username", usernn);
                params.put("password", pass);
                params.put("catergory", categg);
                params.put("image", photo);
                params.put("userid", getId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap = ((BitmapDrawable) user_dets_image.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }


    public void goback(View view) {
        startActivity(new Intent(update.this, SettingsActivity.class));
    }
}