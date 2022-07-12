package com.example.tushome.MessageOptions;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tushome.Adapters.ChatUsersAdapter;
import com.example.tushome.Adapters.PostsAdapter;
import com.example.tushome.Authors.AuthorActivity;
import com.example.tushome.Models.UsersModel;
import com.example.tushome.R;
import com.example.tushome.Reader.MainActivity;
import com.example.tushome.Urls.SessionManager;
import com.example.tushome.Urls.Urls;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PostUsers extends AppCompatActivity {

    RecyclerView recyclerView;
    List<UsersModel> mData;
    PostsAdapter adapter;
    SessionManager sessionManager;
    Urls urls;
    String getID;
    Dialog settings_popup, error_message, no_message, no_internet;
    TextView back_options;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_users);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        settings_popup = new Dialog(this);
        no_internet = new Dialog(this);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(SessionManager.ID);
        String userimage = user.get(SessionManager.IMAGE);
        urls = new Urls();

        error_message = new Dialog(this);
        no_message = new Dialog(this);



        back_options = findViewById(R.id.back_options);
        recyclerView = findViewById(R.id.recyclerview_users_chat);
        mData = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PostsAdapter(this, mData);
        recyclerView.setAdapter(adapter);

        back_options.setText(getIntent().getStringExtra("ere"));


        loadPostUsers();

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadPostUsers() {
        final ProgressDialog progressDialog = new ProgressDialog(PostUsers.this);
        progressDialog.setMessage("Loading.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.GET_POST_USERS,
                response -> {
                    progressDialog.dismiss();
                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONArray tips = new JSONArray(response);
                        if (tips.length() == 0) {
                            no_message = new Dialog(this);
                            no_message.setContentView(R.layout.no_results);
                            MaterialCardView next = no_message.findViewById(R.id.no_results_next);

                            next.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent aa = new Intent(PostUsers.this, PostUsers.class);
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
                            for (int i = 0; i < tips.length(); i++) {
                                JSONObject inputsObjects = tips.getJSONObject(i);

                                String id = inputsObjects.getString("userid");
                                String username = inputsObjects.getString("username");
                                String image = inputsObjects.getString("image");
//                              String total = inputsObjects.getString("total");

                                UsersModel usersModel = new UsersModel(id,
                                        username,
                                        image,
                                        "total",///total unread messages/posts,
                                        ""
                                );
                                mData.add(usersModel);
                            }
                            adapter = new PostsAdapter(PostUsers.this, mData);
                            recyclerView.setAdapter(adapter);
                        }
                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        error_message = new Dialog(this);
                        error_message.setContentView(R.layout.error_message);
                        MaterialCardView tryagains = error_message.findViewById(R.id.tryagain);
                        TextView error_txs = error_message.findViewById(R.id.error_tx);

                        tryagains.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent aa = new Intent(PostUsers.this, PostUsers.class);
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
                }, error -> {
            progressDialog.dismiss();
            error_message = new Dialog(this);
            error_message.setContentView(R.layout.error_message);
            MaterialCardView tryagains = error_message.findViewById(R.id.tryagain);
            TextView error_txs = error_message.findViewById(R.id.error_tx);

            tryagains.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent aa = new Intent(PostUsers.this, PostUsers.class);
                    startActivity(aa);
                    finish();
                    error_message.dismiss();
                }
            });

            error_message.setCancelable(false);
            error_message.setCanceledOnTouchOutside(false);
            Objects.requireNonNull(error_message.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            error_message.show();


        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userid", getID);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }




    public void back(View view) {
        ///check if its the admin or reader then redirect them respectively back
//        startActivity(new Intent(ChatUsers.this, MainActivity.class));
//
        String bb = back_options.getText().toString();
        if (bb.equals("author")) {
            startActivity(new Intent(PostUsers.this, AuthorActivity.class));
            finish();
        } else if (bb.equals("reader")){
            startActivity(new Intent(PostUsers.this, MainActivity.class));
            finish();
        }
    }

    public void openGroupPosts(View view) {
        startActivity(new Intent(PostUsers.this, GroupPost.class));
    }
}