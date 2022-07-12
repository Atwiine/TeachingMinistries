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
import android.text.TextUtils;
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
import com.example.teachingministries.Adapters.Track1Adapter;
import com.example.teachingministries.MainActivity;
import com.example.teachingministries.Models.Track1Model;
import com.example.teachingministries.R;
import com.example.teachingministries.Students.FileOpen;
import com.example.teachingministries.Students.MissedStudents;
import com.example.teachingministries.Students.PassStudents;
import com.example.teachingministries.Urls.SessionManager;
import com.example.teachingministries.Urls.Urls;
import com.google.android.material.card.MaterialCardView;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
import java.util.Map;

public class TrackAttendance1 extends AppCompatActivity {

    private static final Integer READ_EXST = 0;
    private static final Integer WRITE_EXST = 0;
    String namesUser, getID, getType, showReceived = null;
    SessionManager sessionManager;
    Urls urls;
    ImageView home, coming_soon, settings;
    RecyclerView recyclerView;
    ArrayList<Track1Model> mData;
    Track1Adapter adapter;
    TextView error_message_balance, no_message_balance, heading;
    LinearLayout b3b, download_results_linear, show_results_linear, wrong_download_results_linear, btnGetSelected;
    MaterialCardView trraac, gradd, loadgradd, getdate_from, getdate_to, scroll_filter,
            export_filter;
    DatePicker datePickerFrom, datePickerTo;
    TextView fromDates, toDates, total, scrFrom, scrTo, from_results, to_results, show_error, open_folder_show_doc, check__if_selected;
    String name, phone, track;
    public static Cell cell;
    public static Sheet sheet;
    Workbook workbook;
    public static String filepath = "MyFileStorage";
    public static String EXCEL_SHEET_NAME = "Sheet1";
    DownloadManager manager;
    String fileName = "Track-attendance-";
    String fileFinalName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_attendance1);

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
        gradd = findViewById(R.id.gradd);
        loadgradd = findViewById(R.id.loadgradd);
        scroll_filter = findViewById(R.id.scroll_filter);
        export_filter = findViewById(R.id.export_filter);
        btnGetSelected = findViewById(R.id.btnGetSelected);
        total = findViewById(R.id.total);
        scrFrom = findViewById(R.id.scrFrom);
        scrTo = findViewById(R.id.scrTo);
        from_results = findViewById(R.id.from_results);
        to_results = findViewById(R.id.to_results);
        show_results_linear = findViewById(R.id.show_results_linear);
        download_results_linear = findViewById(R.id.download_results_linear);
        wrong_download_results_linear = findViewById(R.id.wrong_download_results_linear);
        show_error = findViewById(R.id.show_error);
        open_folder_show_doc = findViewById(R.id.open_folder_show_doc);
        check__if_selected = findViewById(R.id.check__if_selected);

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
                Toast.makeText(TrackAttendance1.this, "From: " + fromDates.getText().toString(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(TrackAttendance1.this, "To: " + toDates.getText().toString(), Toast.LENGTH_SHORT).show();
                String ff = toDates.getText().toString();
                if (!ff.isEmpty()) {
                    scrTo.setVisibility(View.VISIBLE);
                }
            }
        });


        /**
         * check to see which type of alpha is logged in and then display the correct layout **/
        if ("Alpha four".equals(getType)) {
            trraac.setVisibility(View.GONE);
            gradd.setVisibility(View.VISIBLE);
        }


        /*if this activity is asked to show the received students then run these options*/
        showReceived = getIntent().getStringExtra("selected_alpha");
        if (!TextUtils.isEmpty(showReceived)) {
            heading.setText("Received participants");
            String fromDatess = fromDates.getText().toString().trim();
            String toDatess = toDates.getText().toString().trim();

            loadReceivedStudents(showReceived, fromDatess, toDatess);
//            loadtotalReceivedStudents(showReceived, fromDatess, toDatess);
            b3b.setVisibility(View.GONE);
        }


        error_message_balance = findViewById(R.id.error_message_balance);
        no_message_balance = findViewById(R.id.no_message_balance);

        recyclerView = findViewById(R.id.recyclerview_track1);
        mData = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Track1Adapter(this, mData);
        recyclerView.setAdapter(adapter);

        /*get the dates from filter*/
        String fromDatess = fromDates.getText().toString().trim();
        String toDatess = toDates.getText().toString().trim();

        loadstudentsTrack(fromDatess, toDatess);

        /*check to see if any of the group pplz have been selected*/

        btnGetSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wm = "Track1Adapter";
                String tt = "Trackatt";
                Intent intent = new Intent(TrackAttendance1.this, SendMessage.class);
                intent.putExtra("whose_message", wm);
                intent.putExtra("go", tt);
                startActivity(intent);
            }
        });

/** FIND A WAY TO HIDE THE ABOVE BUTTON IF NO STUDENT IS SELECTED
 *  THEN EXPORTING AND ADDING THE FILTER TO ALL THE OTHER PARTS
 *  AND PROPER IMAGES/ ICONS AND IF SO ANIMATIONS */

//        registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadstudentsTrack(String fromDatess, String toDatess) {
        final ProgressDialog progressDialog = new ProgressDialog(TrackAttendance1.this);
        progressDialog.setMessage("Loading registered participants, please wait....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.TRACK_REGISTERED_STUDENTS,
                response -> {
                    progressDialog.dismiss();
                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONArray tips = new JSONArray(response);
                        if (tips.length() == 0) {

                            no_message_balance.setVisibility(View.VISIBLE);
                            trraac.setVisibility(View.GONE);
                            gradd.setVisibility(View.GONE);
                            total.setText("0");

                            if (getType.equals("Alpha four")) {
                                loadgradd.setVisibility(View.VISIBLE);
                            }
                        } else {
                            for (int i = 0; i < tips.length(); i++) {
                                JSONObject inputsObjects = tips.getJSONObject(i);

                                String id = inputsObjects.getString("id");
                                name = inputsObjects.getString("names");
                                phone = "0" + inputsObjects.getString("phone");
                                track = inputsObjects.getString("attendance");
                                String teacher = inputsObjects.getString("teacher");
                                String remarks = inputsObjects.getString("remarks");
                                String totals = inputsObjects.getString("total");
                                total.setText(totals);
                                if (track.equals("null")) {
                                    track = "Attendance not taken yet";
                                }
                                Track1Model inputsModel = new Track1Model(id,
                                        name, phone, track, teacher, remarks
                                );
                                mData.add(inputsModel);
                            }
                            adapter = new Track1Adapter(TrackAttendance1.this, mData);
                            recyclerView.setAdapter(adapter);
                        }
                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        error_message_balance.setVisibility(View.VISIBLE);
                        // Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            progressDialog.dismiss();
            error_message_balance.setVisibility(View.VISIBLE);
            //Toast.makeText(this, "Something went wrong, check your connection and try again please try again", Toast.LENGTH_SHORT).show();

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


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadReceivedStudents(String showReceived, String fromDatess, String toDatess) {
        final ProgressDialog progressDialog = new ProgressDialog(TrackAttendance1.this);
        progressDialog.setMessage("Loading received participants, please wait....");
        progressDialog.show();
        String fromDatesss = fromDates.getText().toString().trim();
        String toDatesss = toDates.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.LOAD_RECEIVED_STUDENTS,
                response -> {
                    progressDialog.dismiss();
                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONArray tips = new JSONArray(response);
                        if (tips.length() == 0) {

                            no_message_balance.setVisibility(View.VISIBLE);

                            b3b.setVisibility(View.GONE);
                        } else {
                            for (int i = 0; i < tips.length(); i++) {
                                JSONObject inputsObjects = tips.getJSONObject(i);

                                String id = inputsObjects.getString("id");
                                String name = inputsObjects.getString("names");
                                String phone = inputsObjects.getString("phone");
                                String teacher = inputsObjects.getString("teacher");
                                String remarks = inputsObjects.getString("remarks");
                                String totals = inputsObjects.getString("total");
                                total.setText(totals);
                                Track1Model inputsModel = new Track1Model(id,
                                        name, phone, " track", teacher, remarks
                                );
                                mData.add(inputsModel);
                            }
                            adapter = new Track1Adapter(TrackAttendance1.this, mData);
                            recyclerView.setAdapter(adapter);
                        }
                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        error_message_balance.setVisibility(View.VISIBLE);
                        // Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            progressDialog.dismiss();
            error_message_balance.setVisibility(View.VISIBLE);
            //Toast.makeText(this, "Something went wrong, check your connection and try again please try again", Toast.LENGTH_SHORT).show();

        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                params.put("userid", getID);
                params.put("teacher", showReceived);
                params.put("todate", toDatess);
                params.put("fromdate", fromDatess);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void openPassStudents(View view) {
        startActivity(new Intent(TrackAttendance1.this, PassStudents.class));

    }

    public void back(View view) {
        startActivity(new Intent(TrackAttendance1.this, MainActivity.class));
        finish();
    }

    public void openMissedStudents(View view) {
        startActivity(new Intent(TrackAttendance1.this, MissedStudents.class));
    }

    public void openGraduateStudents(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, you want to pass the attended participants to graduation");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
//                        sendstudentsGraduation();
                        final ProgressDialog progressDialog = new ProgressDialog(TrackAttendance1.this);
                        progressDialog.setMessage("Sending attended participants to graduation, please wait....");
                        progressDialog.show();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.PASS_ATTENDED_STUDENTS_GRADUATION,
                                response -> {
                                    progressDialog.dismiss();
                                    try {
                                        Log.i("tagconvertstr", "[" + response + "]");
                                        JSONObject object = new JSONObject(response);
                                        String success = object.getString("success");
                                        if (success.equals("1")) {
                                            Toast.makeText(TrackAttendance1.this, "Participants sent  successfully", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(TrackAttendance1.this, Graduation.class));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(TrackAttendance1.this, "" + e.toString(), Toast.LENGTH_LONG).show();
                                    }
                                }, error -> {
                            progressDialog.dismiss();
                            Toast.makeText(TrackAttendance1.this, "" + error.toString(), Toast.LENGTH_LONG).show();

//            Toast.makeText(this, "Participants not sent successfully, please check your connection and try again", Toast.LENGTH_LONG).show();
                        }) {
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                String aa = "attended";
                                params.put("userid", getID);
                                params.put("attended", aa);
                                params.put("teacher", getType);
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(TrackAttendance1.this);
                        requestQueue.add(stringRequest);
                    }

                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    /**
     * for getting the selected dates and then sending them to the databases  and then the following are for the filtering and exporting and
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
            export_filter.setVisibility(View.VISIBLE);
            download_results_linear.setVisibility(View.GONE);
            wrong_download_results_linear.setVisibility(View.GONE);
            show_results_linear.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(showReceived)) {
                heading.setText("Received participants");
                loadReceivedStudents(showReceived, fromDatess, toDatess);
                b3b.setVisibility(View.GONE);
                export_filter.setVisibility(View.GONE);
            } else {
                export_filter.setVisibility(View.VISIBLE);
                download_results_linear.setVisibility(View.GONE);
                wrong_download_results_linear.setVisibility(View.GONE);
                show_results_linear.setVisibility(View.VISIBLE);
                loadstudentsTrack(fromDatess, toDatess);
            }

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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.EXPORT_TRACK_STUDENTS_4,
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

                    } catch (JSONException e) {
                        e.printStackTrace();
                        wrong_download_results_linear.setVisibility(View.VISIBLE);
//                        show_error.setText(e.toString());
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
            switch (getType) {
                case "Alpha one": {
                    Uri downloadUri = Uri.parse(urls.download_track1);
                    DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                            .setDestinationInExternalPublicDir(imageStorageDir + File.separator, fileFinalName)
                            .setTitle(fileFinalName).setDescription(getString(R.string.save_img))
                            .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                            .setAllowedOverRoaming(true)// Set if download is allowed on roaming network
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    dm.enqueue(request);

                    open_folder_show_doc.setVisibility(View.VISIBLE);
                    break;
                }
                case "Alpha two": {
                    Uri downloadUri = Uri.parse(urls.download_track2);
                    DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                            .setDestinationInExternalPublicDir(imageStorageDir + File.separator, fileFinalName)
                            .setTitle(fileFinalName).setDescription(getString(R.string.save_img))
                            .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                            .setAllowedOverRoaming(true)// Set if download is allowed on roaming network
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    dm.enqueue(request);

                    open_folder_show_doc.setVisibility(View.VISIBLE);
                    break;
                }
                case "Alpha three": {
                    Uri downloadUri = Uri.parse(urls.download_track3);
                    DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                            .setDestinationInExternalPublicDir(imageStorageDir + File.separator, fileFinalName)
                            .setTitle(fileFinalName).setDescription(getString(R.string.save_img))
                            .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                            .setAllowedOverRoaming(true)// Set if download is allowed on roaming network
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    dm.enqueue(request);

                    open_folder_show_doc.setVisibility(View.VISIBLE);
                    break;
                }
                case "Alpha four": {
                    Uri downloadUri = Uri.parse(urls.download_track4);
                    DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                            .setDestinationInExternalPublicDir(imageStorageDir + File.separator, fileFinalName)
                            .setTitle(fileFinalName).setDescription(getString(R.string.save_img))
                            .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                            .setAllowedOverRoaming(true)// Set if download is allowed on roaming network
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    dm.enqueue(request);

                    open_folder_show_doc.setVisibility(View.VISIBLE);
                    break;
                }
            }


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
        String fromDatess = " ";
        String toDatess = " ";
        loadstudentsTrack(fromDatess, toDatess);
    }


    public void viewGraduateStudents(View view) {
        startActivity(new Intent(TrackAttendance1.this, Graduation.class));
    }

    /**
     * opens the downloaded file
     */
    public void open_intents(View view) {
        File myFile = new File("/storage/emulated/0/Documents" + File.separator + fileFinalName);
        try {
            FileOpen.openFile(TrackAttendance1.this, myFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}