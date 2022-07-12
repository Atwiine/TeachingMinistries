package com.example.tushome.Authors;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tushome.Adapters.AllRequestAdapter;
import com.example.tushome.Adapters.RealFeebackAdapter;
import com.example.tushome.Models.AllRequestsModel;
import com.example.tushome.Models.RealFeedbackModel;
import com.example.tushome.R;
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

public class AllRequests extends AppCompatActivity {

    RecyclerView recyclerView;
    List<AllRequestsModel> mData;
    AllRequestAdapter adapter;
    TextView book_title_feedback, book_total_feedback;
    SessionManager sessionManager;
    Urls urls;
    String getID;
    String idd, title;
    Dialog error_message, no_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_request);

        sessionManager = new SessionManager(this);

        error_message = new Dialog(this);
        no_message = new Dialog(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(SessionManager.ID);
        urls = new Urls();


        book_title_feedback = findViewById(R.id.book_title_feedback);
        book_total_feedback = findViewById(R.id.book_total_feedback);

        recyclerView = findViewById(R.id.recyclerview_all_requests);
        mData = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AllRequestAdapter(this, mData);
        recyclerView.setAdapter(adapter);

        Intent ii = getIntent();
        idd = ii.getStringExtra("id");
        title = ii.getStringExtra("title");

        book_title_feedback.setText(title);

        loadAllRequests(idd);

    }


    /*part to show how many feedback this book has*/

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadAllRequests(String idd) {
        final ProgressDialog progressDialog = new ProgressDialog(AllRequests.this);
        progressDialog.setMessage("Loading.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.ALLREQUESTS_URL,
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
                                    Intent aa = new Intent(AllRequests.this, SeeRequestBooks.class);
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

                                String id = inputsObjects.getString("id");
                                String email = inputsObjects.getString("email");
                                String district = inputsObjects.getString("district");
                                String sub_county = inputsObjects.getString("sub_county");
                                String residence = inputsObjects.getString("residence");
                                String soft = inputsObjects.getString("soft");
                                String hard = inputsObjects.getString("hard");
                                String status = inputsObjects.getString("status");

                                AllRequestsModel inputsModel = new AllRequestsModel(id,
                                        email,
                                        district,
                                        sub_county,
                                        residence,
                                        soft,
                                        hard,
                                        status,
                                        ""
                                ,"");
                                mData.add(inputsModel);
                            }
                            adapter = new AllRequestAdapter(AllRequests.this, mData);
                            recyclerView.setAdapter(adapter);

                        }
                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                        error_message = new Dialog(this);
                        error_message.setContentView(R.layout.error_message);
                        MaterialCardView tryagains = error_message.findViewById(R.id.tryagain);
                        TextView error_txs = error_message.findViewById(R.id.error_tx);

                        tryagains.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent aa = new Intent(AllRequests.this, SeeRequestBooks.class);
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
                    Intent aa = new Intent(AllRequests.this, SeeRequestBooks.class);
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
                params.put("bookid", idd);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
        requestQueue.add(stringRequest);
    }


    public void back(View view) {
        startActivity(new Intent(AllRequests.this, SeeRequestBooks.class));
        finish();
    }
}