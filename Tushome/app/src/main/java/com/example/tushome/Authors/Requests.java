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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tushome.Adapters.AllRequestAdapter;
import com.example.tushome.Models.AllRequestsModel;
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

public class Requests extends AppCompatActivity {

    RecyclerView recyclerView;
    List<AllRequestsModel> mData;
    AllRequestAdapter adapter;
    SessionManager sessionManager;
    Urls urls;
    String getID, idd;
    Dialog details;
    TextView title, preview, author, id, soft_copy_orders, hard_copy_orders, total_copies;
    CardView balance_card;
    ImageView product_image;
    Dialog error_message, no_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        sessionManager = new SessionManager(this);
        details = new Dialog(this);
        error_message = new Dialog(this);
        no_message = new Dialog(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(SessionManager.ID);
        urls = new Urls();


        title = findViewById(R.id.title);
        preview = findViewById(R.id.preview);
        author = findViewById(R.id.author);
        balance_card = findViewById(R.id.balance_card);
        id = findViewById(R.id.id);
        product_image = findViewById(R.id.product_image);
        soft_copy_orders = findViewById(R.id.soft_copy_orders);
        hard_copy_orders = findViewById(R.id.hard_copy_orders);
        total_copies = findViewById(R.id.total_copies);

        recyclerView = findViewById(R.id.recyclerview_all_requests);
        mData = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AllRequestAdapter(this, mData);
        recyclerView.setAdapter(adapter);

        Intent ii = getIntent();
        idd = ii.getStringExtra("id");


        balance_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String too = total_copies.getText().toString();
                String haaa = hard_copy_orders.getText().toString();
                String soo = soft_copy_orders.getText().toString();


                double tt = Double.parseDouble(too);
                double hh = Double.parseDouble(haaa);
                double ss = Double.parseDouble(soo);

//                    double tt = 2489000;
//                    double hh = 145777;
//                    double ss = 5780000;

                if (too.equals("null")) {
                    too = "0";
                }


                if (tt > 1000 && hh > 1000 && ss > 1000) {


//                    double tt = Double.parseDouble(too);
//                    double hh = Double.parseDouble(haaa);
//                    double ss = Double.parseDouble(soo);


                    String numberStringTotal = "";
                    String numberStringSoft = "";
                    String numberStringHard = "";


                    /*total copies part*/
                    if (Math.abs(tt / 1000000) > 1) {
                        numberStringTotal = (tt / 1000000) + "m";
//                        Toast.makeText(context, "1 " + numberStringTotal, Toast.LENGTH_SHORT).show();
                        total_copies.setText(numberStringTotal);

                    } else if (Math.abs(tt / 1000) > 1) {
                        numberStringTotal = (tt / 1000) + "k";
//                        Toast.makeText(context, "2 " + numberStringTotal, Toast.LENGTH_SHORT).show();
                        total_copies.setText(numberStringTotal);
                    } else {
                        numberStringTotal = String.valueOf(tt);
//                        Toast.makeText(context, "3 " + numberStringTotal, Toast.LENGTH_SHORT).show();
                        total_copies.setText(numberStringTotal);
                    }

                    /*hard copy part*/
                    if (Math.abs(hh / 1000000) > 1) {
                        numberStringHard = (hh / 1000000) + "m";
//                        Toast.makeText(context, "1 " + numberStringHard, Toast.LENGTH_SHORT).show();
                        hard_copy_orders.setText(numberStringHard);

                    } else if (Math.abs(hh / 1000) > 1) {
                        numberStringHard = (hh / 1000) + "k";
//                        Toast.makeText(context, "2 " + numberStringHard, Toast.LENGTH_SHORT).show();
                        hard_copy_orders.setText(numberStringHard);
                    } else {
                        numberStringHard = String.valueOf(hh);
//                        Toast.makeText(context, "3 " + numberStringHard, Toast.LENGTH_SHORT).show();
                        hard_copy_orders.setText(numberStringHard);
                    }

                    /*soft copy part*/
                    if (Math.abs(ss / 1000000) > 1) {
                        numberStringSoft = (ss / 1000000) + "m";
//                        Toast.makeText(context, "1 " + numberStringSoft, Toast.LENGTH_SHORT).show();
                        soft_copy_orders.setText(numberStringSoft);

                    } else if (Math.abs(ss / 1000) > 1) {
                        numberStringSoft = (ss / 1000) + "k";
//                        Toast.makeText(context, "2 " + numberStringSoft, Toast.LENGTH_SHORT).show();
                        soft_copy_orders.setText(numberStringSoft);
                    } else {
                        numberStringSoft = String.valueOf(ss);
//                        Toast.makeText(context, "3 " + numberStringSoft, Toast.LENGTH_SHORT).show();
                        soft_copy_orders.setText(numberStringSoft);
                    }

                    details = new Dialog(Requests.this);
                    details.setContentView(R.layout.request_dets);
                    CardView go_home = details.findViewById(R.id.go_home);
                    TextView soft_copy_dets = details.findViewById(R.id.soft_copy_dets);
                    TextView hard_copy_dets = details.findViewById(R.id.hard_copy_dets);
                    TextView total_dets = details.findViewById(R.id.total_dets);

                    total_dets.setText(numberStringTotal);
                    hard_copy_dets.setText(numberStringHard);
                    soft_copy_dets.setText(numberStringSoft);

                    go_home.setOnClickListener(v1 -> {
                        details.dismiss();
                    });
                    details.setCancelable(false);
                    details.setCanceledOnTouchOutside(false);
                    Objects.requireNonNull(details.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    details.show();

                }
            }
        });


        loadOthersBooks();
        loadAllRequests(idd);
    }

    /* get the book id here somehow bookid*/

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadOthersBooks() {
        final ProgressDialog progressDialog = new ProgressDialog(Requests.this);
        progressDialog.setMessage("Loading.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.REQUESTS_BOOKS,
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
                                    Intent aa = new Intent(Requests.this, SeeRequestBooks.class);
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
                                String titles = inputsObjects.getString("title");
                                String totalRequests = inputsObjects.getString("total");
                                String onlineRequests = inputsObjects.getString("soft");
                                String hardRequests = inputsObjects.getString("hard");

                                title.setText(titles);
                                soft_copy_orders.setText(onlineRequests);
                                hard_copy_orders.setText(hardRequests);
                                total_copies.setText(totalRequests);

                            }

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
                                Intent aa = new Intent(Requests.this, SeeRequestBooks.class);
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
                    Intent aa = new Intent(Requests.this, SeeRequestBooks.class);
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
                params.put("authorid", getID);
                params.put("bookid", idd);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
        requestQueue.add(stringRequest);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadAllRequests(String idd) {
        final ProgressDialog progressDialog = new ProgressDialog(Requests.this);
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
                                    Intent aa = new Intent(Requests.this, SeeRequestBooks.class);
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
                                String status = inputsObjects.getString("author_action");
                               String reader_name = inputsObjects.getString("reader_name");

                                AllRequestsModel inputsModel = new AllRequestsModel(id,
                                        email,
                                        district,
                                        sub_county,
                                        residence,
                                        soft,
                                        hard,
                                        status,
                                        "total_requests",
                                        reader_name);
                                mData.add(inputsModel);
                            }
                            adapter = new AllRequestAdapter(Requests.this, mData);
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
                                Intent aa = new Intent(Requests.this, SeeRequestBooks.class);
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
                    Intent aa = new Intent(Requests.this, SeeRequestBooks.class);
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
        startActivity(new Intent(Requests.this, SeeRequestBooks.class));
        finish();
    }
}