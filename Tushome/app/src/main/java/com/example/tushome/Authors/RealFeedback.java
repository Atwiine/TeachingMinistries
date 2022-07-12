package com.example.tushome.Authors;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tushome.Adapters.RealFeebackAdapter;
import com.example.tushome.Models.RealFeedbackModel;
import com.example.tushome.R;
import com.example.tushome.Urls.SessionManager;
import com.example.tushome.Urls.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RealFeedback extends AppCompatActivity {

    RecyclerView recyclerView;
    List<RealFeedbackModel> mData;
    RealFeebackAdapter adapter;
    TextView error_message, no_message, book_title_feedback, book_total_feedback;
    SessionManager sessionManager;
    Urls urls;
    String getID;
    String idd, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_feedback);

        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(SessionManager.ID);
        urls = new Urls();

        no_message = findViewById(R.id.no_message_balance);
        error_message = findViewById(R.id.error_message_balance);
        book_title_feedback = findViewById(R.id.book_title_feedback);
        book_total_feedback = findViewById(R.id.book_total_feedback);

        recyclerView = findViewById(R.id.recyclerview_farmer_products);
        mData = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RealFeebackAdapter(this, mData);
        recyclerView.setAdapter(adapter);

        Intent ii = getIntent();
        idd = ii.getStringExtra("id");
        title = ii.getStringExtra("title");

        book_title_feedback.setText(title);

        numberFeedback(idd);
        loadFeedback(idd);
    }


    /*part to show how many feedback this book has*/

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void numberFeedback(String idd) {
        final ProgressDialog progressDialog = new ProgressDialog(RealFeedback.this);
        progressDialog.setMessage("Loading.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.COUNTFEEDBACK,
                response -> {
                    progressDialog.dismiss();
                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONArray tips = new JSONArray(response);
                        if (tips.length() == 0) {
                            no_message.setVisibility(View.VISIBLE);
                        } else {
                            for (int i = 0; i < tips.length(); i++) {
                                JSONObject inputsObjects = tips.getJSONObject(i);
                                String total = inputsObjects.getString("total");

                                book_total_feedback.setText(total);
                            }

                        }
                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        error_message.setVisibility(View.VISIBLE);
                    }
                }, error -> {
            progressDialog.dismiss();
            error_message.setVisibility(View.VISIBLE);
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


    /*loads the actual feedback*/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadFeedback(String idd) {
        final ProgressDialog progressDialog = new ProgressDialog(RealFeedback.this);
        progressDialog.setMessage("Loading.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.FEEDBACK,
                response -> {
                    progressDialog.dismiss();
                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONArray tips = new JSONArray(response);
                        if (tips.length() == 0) {
                            no_message.setVisibility(View.VISIBLE);
                        } else {
                            for (int i = 0; i < tips.length(); i++) {
                                JSONObject inputsObjects = tips.getJSONObject(i);
//                                String id = inputsObjects.getString("id");
                                String feedback = inputsObjects.getString("feedback");
                                String from_who = inputsObjects.getString("from_who");

                                error_message.setVisibility(View.GONE);
                                RealFeedbackModel inputsModel = new RealFeedbackModel("id",
                                        feedback,
                                        from_who);
                                mData.add(inputsModel);
                            }
                            adapter = new RealFeebackAdapter(RealFeedback.this, mData);
                            recyclerView.setAdapter(adapter);
                        }
                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        error_message.setVisibility(View.VISIBLE);
                    }
                }, error -> {
            progressDialog.dismiss();
            error_message.setVisibility(View.VISIBLE);
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
        startActivity(new Intent(RealFeedback.this, SeeFeedbackBooks.class));
        finish();
    }
}