package com.example.tushome.Reader;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.bumptech.glide.Glide;
import com.example.tushome.Adapters.ReaderAdapter;
import com.example.tushome.MessageOptions.ChatUsers;
import com.example.tushome.MessageOptions.PostUsers;
import com.example.tushome.Models.ReaderModel;
import com.example.tushome.Preferences.SettingsActivity;
import com.example.tushome.R;
import com.example.tushome.Urls.SessionManager;
import com.example.tushome.Urls.Urls;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<ReaderModel> mData;
    ReaderAdapter adapter;
    SessionManager sessionManager;
    Urls urls;
    String getID;
    String search_cat = "";
    BottomNavigationView bottomNavigationView;
    EditText search;
    Dialog settings_popup, error_message, no_message,no_internet;
    ImageView reader_dp;
    boolean connected = false;
    Spinner bookcategory;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        bookcategory = findViewById(R.id.bookcategory);
        reader_dp = findViewById(R.id.reader_dp);

        String imgurl = getIntent().getStringExtra("image_url");

        Glide.with(this)
                .load(urls.https + "user_images/" +userimage)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .fallback(R.drawable.ic_launcher_background)
                .into(reader_dp);


        recyclerView = findViewById(R.id.recyclerview_releases);
        mData = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReaderAdapter(this, mData);
        recyclerView.setAdapter(adapter);


        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ){
            connected = true;
            loadBooks(search_cat);
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




        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setFocusableInTouchMode(true);
                search.setFocusable(true);
            }
        });
        search.setFocusableInTouchMode(false);
        search.setFocusable(false);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });






        bookcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedProduct = parent.getItemAtPosition(position).toString();
                switch (selectedProduct) {
                    case "Fiction":
                        search_cat = "Fiction";
                        loadBooks(search_cat);
                        break;
                    case "Poetry":
                        search_cat = "Poetry";
                        loadBooks(search_cat);
                        break;
                    case "Action and Adventure":
                        search_cat = "Action and Adventure";
                        loadBooks(search_cat);
                        break;
                    case "Classic":
                        search_cat = "Classic";
                        loadBooks(search_cat);
                        break;
                    case "Children's books":
                        search_cat = "Children's books";
                        loadBooks(search_cat);
                        break;
                    case "Crime":
                        search_cat = "Crime";
                        loadBooks(search_cat);
                        break;
                    case "Drama":
                        search_cat = "Drama";
                        loadBooks(search_cat);
                        break;
                    case "Romance":
                        search_cat = "Romance";
                        loadBooks(search_cat);
                        break;
                    case "Book categories":
                        search_cat = "";
                        loadBooks(search_cat);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadBooks(String search_cat) {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.READERBOOKS_URL,
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
                                    Intent aa = new Intent(MainActivity.this, MainActivity.class);
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

                                String id = inputsObjects.getString("bookid");
                                String title = inputsObjects.getString("title");
                                String image = inputsObjects.getString("image");
                                String preview = inputsObjects.getString("preview");
                                String author = inputsObjects.getString("author");

                                ReaderModel inputsModel = new ReaderModel(id,
                                        title,
                                        image,
                                        preview,
                                        author
                                );
                                mData.add(inputsModel);
                            }
                            adapter = new ReaderAdapter(MainActivity.this, mData);
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
                                Intent aa = new Intent(MainActivity.this, MainActivity.class);
                                startActivity(aa);
                                finish();
                                error_message.dismiss();
                            }
                        });

                        error_message.setCancelable(false);
                        error_message.setCanceledOnTouchOutside(false);
                        Objects.requireNonNull(error_message.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        error_message.show();
                        Toast.makeText(MainActivity.this, "ero" + e.toString(), Toast.LENGTH_SHORT).show();
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
                    Intent aa = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(aa);
                    finish();
                    error_message.dismiss();
                }
            });

            error_message.setCancelable(false);
            error_message.setCanceledOnTouchOutside(false);
            Objects.requireNonNull(error_message.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            error_message.show();
            Toast.makeText(MainActivity.this, "erores" + error.toString(), Toast.LENGTH_SHORT).show();

        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("category", MainActivity.this.search_cat);// use the category to search for the books and if
//                none is selected then get all the books
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
        requestQueue.add(stringRequest);
    }

    private void filter(String text) {
        ArrayList<ReaderModel> filteredList = new ArrayList<>();

        for (ReaderModel item : mData) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList);
    }

    public void openSettings(View view) {
        /*settings_popup = new Dialog(this);
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
                //                Intent intent = new Intent(SettingsActivity.this, app_information.class);
//                startActivity(intent);
            }
        });
        appinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(SettingsActivity.this, app_information.class);
//                startActivity(intent);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(SettingsActivity.this, app_information.class);
//                startActivity(intent);
            }
        });
        linear_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        MainActivity.this);
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
        settings_popup.show();*/


        SettingsActivity settingsActivity = new SettingsActivity();
        settingsActivity.show(getSupportFragmentManager(),
                "ModalBottomSheet");

    }

    public void openChat(View view) {
        String ere = "reader";
        Intent ss = new Intent(MainActivity.this,ChatUsers.class);
        ss.putExtra("ere",ere);
        startActivity(ss);
    }

    public void openNotifactions(View view) {
        startActivity(new Intent(MainActivity.this, ReaderNotification.class));
    }

    public void openPost(View view) {
        String ere = "reader";
        Intent ss = new Intent(MainActivity.this,PostUsers.class);
        ss.putExtra("ere",ere);
        startActivity(ss);
    }
}