package com.example.tushome.Authors;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.tushome.Preferences.SettingsActivity;
import com.example.tushome.R;
import com.example.tushome.MessageOptions.Chat;
import com.example.tushome.Urls.SessionManager;
import com.example.tushome.Urls.Urls;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AuthorActivity extends AppCompatActivity {

    TextView error_message, no_message;
    SessionManager sessionManager;
    Urls urls;
    String getID,IMAGE;
    Dialog settings_popup;
    ImageView reader_dp;
    Dialog not_confirmed, denied_access, suspended_access,no_internet;
    boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);

        not_confirmed = new Dialog(this);
        denied_access = new Dialog(this);
        suspended_access = new Dialog(this);
        no_internet = new Dialog(this);


        settings_popup = new Dialog(this);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(SessionManager.ID);
        IMAGE = user.get(SessionManager.IMAGE);
        urls = new Urls();

        no_message = findViewById(R.id.no_message_balance);
        error_message = findViewById(R.id.error_message_balance);

        reader_dp = findViewById(R.id.reader_dp);
        String imgurl = getIntent().getStringExtra("image_url");

        Glide.with(this)
                .load(urls.https + "user_images/" +IMAGE)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .fallback(R.drawable.ic_launcher_background)
                .into(reader_dp);


        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ){
            connected = true;
            checkLogin(getID);
        }else {
            connected = false;
            no_internet = new Dialog(this);
            no_internet.setContentView(R.layout.no_internet);
            MaterialCardView no_conn = no_internet.findViewById(R.id.no_conn);

            no_conn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    System.exit(0);
                    no_internet.dismiss();
                }
            });

            no_internet.setCancelable(false);
            no_internet.setCanceledOnTouchOutside(false);
            Objects.requireNonNull(no_internet.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            no_internet.show();
        }





    }


    private void checkLogin(final String userid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.CHECKLOGIN_URL,
                response -> {
                    Log.i("tagconvertstr", "[" + response + "]");
                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("login");
                        if (success.equals("1")) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                Log.i("tagconvertstr", "[" + response + "]");
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("userid");
                                String status = object.getString("status");
                                String suspended = object.getString("suspended");


                                if (status.equals("denied")) {
                                    Toast.makeText(this, "account denied", Toast.LENGTH_SHORT).show();
                                    denied_access = new Dialog(this);
                                    denied_access.setContentView(R.layout.denied_access);
                                    MaterialCardView contact_denied_access = denied_access.findViewById(R.id.contact_denied_access);
                                    MaterialCardView ok_denied_access = denied_access.findViewById(R.id.ok_denied_access);


                                    ok_denied_access.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            sessionManager.logout();
                                            denied_access.dismiss();
                                        }
                                    });


                                    contact_denied_access.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            sessionManager.logout();
                                            denied_access.dismiss();
                                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                            callIntent.setData(Uri.parse("tel:0754840755"));
                                            try {
                                                startActivity(callIntent);
                                            } catch (SecurityException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                    denied_access.setCancelable(false);
                                    denied_access.setCanceledOnTouchOutside(false);
                                    Objects.requireNonNull(denied_access.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    denied_access.show();


                                } else if (status.equals("confirmed") && suspended.equals("deactivated")) {
                                    Toast.makeText(this, "account deactivated", Toast.LENGTH_SHORT).show();

                                    suspended_access = new Dialog(this);
                                    suspended_access.setContentView(R.layout.suspended_access);
                                    MaterialCardView contact_suspended_access = suspended_access.findViewById(R.id.contact_suspended_access);
                                    MaterialCardView ok_suspended_access = denied_access.findViewById(R.id.ok_suspended_access);

                                    ok_suspended_access.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            sessionManager.logout();
                                            suspended_access.dismiss();
                                        }
                                    });

                                    contact_suspended_access.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            sessionManager.logout();
                                            suspended_access.dismiss();
                                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                            callIntent.setData(Uri.parse("tel:0754840755"));
                                            try {
                                                startActivity(callIntent);
                                            } catch (SecurityException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                    suspended_access.setCancelable(false);
                                    suspended_access.setCanceledOnTouchOutside(false);
                                    Objects.requireNonNull(suspended_access.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    suspended_access.show();


                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },

                error -> {
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userid", userid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void addBook(View view) {
        startActivity(new Intent(AuthorActivity.this, UploadBooks.class));
    }

    public void myBooks(View view) {
        startActivity(new Intent(AuthorActivity.this, AuthorsBooks.class));

    }

    public void otherBooks(View view) {
        startActivity(new Intent(AuthorActivity.this, OthersBooks.class));

    }

    public void viewRequests(View view) {
        startActivity(new Intent(AuthorActivity.this, SeeRequestBooks.class));

    }

    public void viewFeedbacks(View view) {
        startActivity(new Intent(AuthorActivity.this, SeeFeedbackBooks.class));

    }

    public void viewChat(View view) {

        String a = "author";
        Intent ss = new Intent(AuthorActivity.this, Chat.class);
        ss.putExtra("author", a);
        startActivity(ss);
//        see if there could be anything fansy from author to chat room
    }

    public void logout(View view) {

   /*     settings_popup = new Dialog(this);
        settings_popup.setContentView(R.layout.popup_settings);
        LinearLayout linear_account = settings_popup.findViewById(R.id.linear_account);
        LinearLayout appinfo = settings_popup.findViewById(R.id.appinfo);
        LinearLayout about = settings_popup.findViewById(R.id.about);
        LinearLayout linear_logout = settings_popup.findViewById(R.id.linear_logout);
        ImageButton close_popup = settings_popup.findViewById(R.id.close_popup);

        close_popup.setOnClickListener(v -> settings_popup.dismiss());
        linear_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          Intent intent = new Intent(AuthorActivity.this, update.class);
                startActivity(intent);
            }
        });
        appinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthorActivity.this, app_information.class);
                startActivity(intent);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthorActivity.this, About_us.class);
                startActivity(intent);
            }
        });
        linear_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        AuthorActivity.this);
                alertDialog2.setTitle("Confirm to proceed");
                alertDialog2.setMessage("Are you sure you want to logout");
                alertDialog2.setIcon(R.drawable.ic_warning);
                alertDialog2.setPositiveButton("YES",
                        (dialog, which) -> sessionManager.logout());
                alertDialog2.setNegativeButton("NO",
                        (dialog, which) -> dialog.cancel());
                alertDialog2.show();
            }
        });


        settings_popup.setCancelable(false);
        settings_popup.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(settings_popup.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        settings_popup.show();
*/

        SettingsActivity settingsActivity = new SettingsActivity();
        settingsActivity.show(getSupportFragmentManager(),
                "ModalBottomSheet");
    }

    public void goAccount(View view) {
        Toast.makeText(this, "coming soon !!!", Toast.LENGTH_SHORT).show();
    }
}