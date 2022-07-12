package com.example.teachingministries.Alphas;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.teachingministries.Adapters.ToBeBaptisedAdapter;
import com.example.teachingministries.Models.Track1Model;
import com.example.teachingministries.R;
import com.example.teachingministries.Students.FileOpen;
import com.example.teachingministries.Students.MissedStudents;
import com.example.teachingministries.Students.PassStudents;
import com.example.teachingministries.Urls.SessionManager;
import com.example.teachingministries.Urls.Urls;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToBeBaptised extends AppCompatActivity {

    String namesUser, getID, getType, showReceived = null;
    SessionManager sessionManager;
    Urls urls;
    ImageView home, coming_soon, settings;
    RecyclerView recyclerView;
    List<Track1Model> mData;
    ToBeBaptisedAdapter adapter;
    TextView error_message_balance, no_message_balance, heading, total;
    LinearLayout b3b, btnGetSelected;
    MaterialCardView trraac, view_baptised, pass_baptised;


    LinearLayout download_results_linear, show_results_linear, wrong_download_results_linear;
    MaterialCardView gradd, loadgradd, getdate_from, getdate_to, scroll_filter,
            export_filter;
    DatePicker datePickerFrom, datePickerTo;
    TextView fromDates, toDates, scrFrom, scrTo, from_results, to_results, show_error, open_folder_show_doc;
    String fileName = "To-be-baptised-alpha-4";
    String fileFinalName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tobebaptised);

        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(SessionManager.ID);
        namesUser = user.get(SessionManager.FULLNAME);
        getType = user.get(SessionManager.TYPE);
        urls = new Urls();

        home = findViewById(R.id.home);
        coming_soon = findViewById(R.id.coming_soon);
        settings = findViewById(R.id.settings);
        trraac = findViewById(R.id.trraac);
        heading = findViewById(R.id.heading);
        b3b = findViewById(R.id.b3b);
//        view_baptised = findViewById(R.id.view_baptised);
        pass_baptised = findViewById(R.id.pass_baptised);


        /*these are the sharing parts*/
        scroll_filter = findViewById(R.id.scroll_filter);
        export_filter = findViewById(R.id.export_filter);
        scrFrom = findViewById(R.id.scrFrom);
        scrTo = findViewById(R.id.scrTo);
        from_results = findViewById(R.id.from_results);
        to_results = findViewById(R.id.to_results);
        show_results_linear = findViewById(R.id.show_results_linear);
        download_results_linear = findViewById(R.id.download_results_linear);
        wrong_download_results_linear = findViewById(R.id.wrong_download_results_linear);
        show_error = findViewById(R.id.show_error);
        open_folder_show_doc = findViewById(R.id.open_folder_show_doc);
        btnGetSelected = findViewById(R.id.btnGetSelected);
        total = findViewById(R.id.total);


        fromDates = (TextView) findViewById(R.id.fromDates);
        toDates = (TextView) findViewById(R.id.toDates);
        datePickerFrom = (DatePicker) findViewById(R.id.datePickerFrom);
        datePickerTo = (DatePicker) findViewById(R.id.datePickerTo);
        getdate_from = (MaterialCardView) findViewById(R.id.getdate_from);
        getdate_to = (MaterialCardView) findViewById(R.id.getdate_to);
        getdate_from.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                fromDates.setText(String.format("%d-%02d-%d", datePickerFrom.getYear(), datePickerFrom.getMonth() + 1, datePickerFrom.getDayOfMonth()));
                Toast.makeText(ToBeBaptised.this, "From: " + fromDates.getText().toString(), Toast.LENGTH_SHORT).show();
                String ff = fromDates.getText().toString();
                if (!ff.isEmpty()) {
                    scrFrom.setVisibility(View.VISIBLE);
                }
            }
        });

        getdate_to.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                toDates.setText(String.format("%d-%02d-%d", datePickerTo.getYear(), datePickerTo.getMonth() + 1, datePickerTo.getDayOfMonth()));
                Toast.makeText(ToBeBaptised.this, "To: " + toDates.getText().toString(), Toast.LENGTH_SHORT).show();
                String ff = toDates.getText().toString();
                if (!ff.isEmpty()) {
                    scrTo.setVisibility(View.VISIBLE);
                }
            }
        });

        btnGetSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wm = "ToBeBaptisedAdapter";
                String tt = "Tobe";
                Intent intent = new Intent(ToBeBaptised.this, SendMessage.class);
                intent.putExtra("whose_message", wm);
                intent.putExtra("go", tt);
                startActivity(intent);
            }
        });


        /**
         * check to see which type of alpha is logged in and then display the correct layout **/
//        if ("Alpha four".equals(getType)) {
//            trraac.setVisibility(View.VISIBLE);
//        }


        error_message_balance = findViewById(R.id.error_message_balance);
        no_message_balance = findViewById(R.id.no_message_balance);
        total = findViewById(R.id.total);

        recyclerView = findViewById(R.id.recyclerview_graduation);
        mData = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ToBeBaptisedAdapter(this, mData);
        recyclerView.setAdapter(adapter);

//        loadtotalTBBaptisedstudentsTrack();
        /*get the dates from filter*/
        String fromDatess = fromDates.getText().toString().trim();
        String toDatess = toDates.getText().toString().trim();

        loadTBBaptisedstudentsTrack(fromDatess, toDatess);


    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadTBBaptisedstudentsTrack(String fromDatess, String toDatess) {
        final ProgressDialog progressDialog = new ProgressDialog(ToBeBaptised.this);
        progressDialog.setMessage("Loading to be baptised participants, please wait....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.LOAD_TBBAPTISED_STUDENTS,
                response -> {
                    progressDialog.dismiss();
                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONArray tips = new JSONArray(response);
                        if (tips.length() == 0) {

                            no_message_balance.setVisibility(View.VISIBLE);
                            pass_baptised.setVisibility(View.GONE);
                            view_baptised.setVisibility(View.VISIBLE);

                        } else {
                            for (int i = 0; i < tips.length(); i++) {
                                JSONObject inputsObjects = tips.getJSONObject(i);

                                String id = inputsObjects.getString("id");
                                String name = inputsObjects.getString("names");
                                String phone = "0" + inputsObjects.getString("phone");
                                String track = inputsObjects.getString("baptised");
                                String teacher = inputsObjects.getString("teacher");
                                String remarks = inputsObjects.getString("remarks");
                                String totals = inputsObjects.getString("total");
                                total.setText(totals);
                                if (track.equals("yes")) {
                                    track = "Baptised";
                                } else {
                                    track = "Not baptised";
                                }
                                Track1Model inputsModel = new Track1Model(id,
                                        name, phone, track, teacher, remarks
                                );
                                mData.add(inputsModel);
                            }
                            adapter = new ToBeBaptisedAdapter(ToBeBaptised.this, mData);
                            recyclerView.setAdapter(adapter);
                        }
                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        error_message_balance.setVisibility(View.VISIBLE);
                        no_message_balance.setVisibility(View.GONE);
//                         Toast.makeText(this, "Somet " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            progressDialog.dismiss();
            error_message_balance.setVisibility(View.VISIBLE);
            no_message_balance.setVisibility(View.GONE);
//            Toast.makeText(this, "Sond try agn " + error.toString(), Toast.LENGTH_SHORT).show();

        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userid", getID);
                params.put("teacher", getType);
                params.put("todate", toDatess);
                params.put("fromdate", fromDatess);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void openPassStudents(View view) {
        startActivity(new Intent(ToBeBaptised.this, PassStudents.class));

    }

    public void back(View view) {
        startActivity(new Intent(ToBeBaptised.this, Graduation.class));
        finish();
    }

    public void openMissedStudents(View view) {
        startActivity(new Intent(ToBeBaptised.this, MissedStudents.class));
    }


    public void openBaptisedStudents(View view) {
        startActivity(new Intent(ToBeBaptised.this, BaptisedStudents.class));
    }

//    public void openBasptisedStudents(View view) {
//        startActivity(new Intent(Graduation.this, BaptisedStudents.class));
//    }

    public void openToBeBasptisedStudents(View view) {

    }


    /**
     * for getting the selected dates and then sending them to the databases
     * and then the following are for the filtering and exporting and
     * downloading
     **/
    public void sendFilterDates(View view) {

        String fromDatesss = fromDates.getText().toString().trim();
        String toDatesss = toDates.getText().toString().trim();

        /*check to see if all the dates are selected*/
        if (fromDatesss.isEmpty() && toDatesss.isEmpty()) {
            Toast.makeText(this, "Please select the start date and the end date", Toast.LENGTH_SHORT).show();
            scroll_filter.setVisibility(View.GONE);

        } else if (fromDatesss.isEmpty() || toDatesss.isEmpty()) {
            Toast.makeText(this, "Please select the start date and the end date", Toast.LENGTH_SHORT).show();
            scroll_filter.setVisibility(View.GONE);
        } else {
            adapter.clear();

            String fromDatess = fromDates.getText().toString().trim();
            String toDatess = toDates.getText().toString().trim();
            from_results.setText(fromDatess);
            to_results.setText(toDatess);

            loadTBBaptisedstudentsTrack(fromDatess, toDatess);
            export_filter.setVisibility(View.VISIBLE);
            download_results_linear.setVisibility(View.GONE);
            wrong_download_results_linear.setVisibility(View.GONE);
            show_results_linear.setVisibility(View.VISIBLE);
            scroll_filter.setVisibility(View.GONE);


        }

    }

    public void open_filer_option(View view) {
        scroll_filter.setVisibility(View.VISIBLE);
    }

    public void close_filter(View view) {
        scroll_filter.setVisibility(View.GONE);
    }

    public void open_filer_export(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, you want to export these filtered results to an excel file");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String fromDatess = fromDates.getText().toString().trim();
                        String toDatess = toDates.getText().toString().trim();
                        exporttrackStudents(fromDatess, toDatess);
                    }

                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void exporttrackStudents(String fromDatess, String toDatess) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.EXPORT_TO_BE_BAPTISED,
                response -> {

                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONArray tips = new JSONArray(response);

                        for (int i = 0; i < tips.length(); i++) {
                            JSONObject inputsObjects = tips.getJSONObject(i);

                            String teacher = inputsObjects.getString("teacher");
                            show_results_linear.setVisibility(View.GONE);
                            wrong_download_results_linear.setVisibility(View.GONE);
                            if (teacher.length() != 0) {
                                exportFile();
                                download_results_linear.setVisibility(View.VISIBLE);
                            }
                        }
//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        wrong_download_results_linear.setVisibility(View.VISIBLE);
                    }
                }, error -> {
            wrong_download_results_linear.setVisibility(View.VISIBLE);
//            show_error.setText(error.toString());
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userid", getID);
                params.put("teacher", getType);
                params.put("todate", toDatess);
                params.put("fromdate", fromDatess);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void exportFile() {

        try {
            File imageStorageDir = new File(Environment.getExternalStorageDirectory() + "/Documents");
            if (!imageStorageDir.exists()) {
                imageStorageDir.mkdirs();
            }
            String imgExtension = ".xls";

            String date = DateFormat.getDateTimeInstance().format(new Date());
            @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
            fileFinalName = fileName + getType + "-" + timeStamp.replace(" ", " ").
                    replace(":", ":").replace(".", ".") + imgExtension;
            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(urls.download_tobebaptised);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setDestinationInExternalPublicDir(imageStorageDir + File.separator, fileFinalName)
                    .setTitle(fileFinalName).setDescription(getString(R.string.save_img))
                    .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                    .setAllowedOverRoaming(true)// Set if download is allowed on roaming network
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            dm.enqueue(request);

            open_folder_show_doc.setVisibility(View.VISIBLE);
        } catch (IllegalStateException ex) {
            Toast.makeText(getApplicationContext(), "Storage Error", Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Unable to save file, please check your connection and try again", Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }

    }

    public void close_export(View view) {
        export_filter.setVisibility(View.GONE);
        adapter.clear();
        String fromDatess = "";
        String toDatess = "";
        loadTBBaptisedstudentsTrack(fromDatess, toDatess);
    }

    /**
     * opens the downloaded file
     */
    public void open_intents(View view) {
        File myFile = new File("/storage/emulated/0/Documents" + File.separator + fileFinalName);
        try {
            FileOpen.openFile(ToBeBaptised.this, myFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}