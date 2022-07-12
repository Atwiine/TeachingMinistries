package com.example.tushome.Authors;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.tushome.R;
import com.example.tushome.Urls.SessionManager;
import com.example.tushome.Urls.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OtherBookDetails extends AppCompatActivity {


    TextView book_title, book_summary, book_author_name, book_id;
    ImageView img_dets;
    LinearLayout linear_onlineorder, linear_hard_copy, linear_feedback, linear_ordering;
    //    EditText feedback_provided, online_order_email;
    String getID;
    Urls urls;
    SessionManager sessionManager;
    ScrollView scroll_dets;
    Dialog online_order, send_feedback, successss, order_now, hard_copy_orders, ss_card, ee_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_book_details);

        online_order = new Dialog(this);
        send_feedback = new Dialog(this);
        successss = new Dialog(this);
        order_now = new Dialog(this);
        hard_copy_orders = new Dialog(this);
        ss_card = new Dialog(this);
        ee_card = new Dialog(this);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(SessionManager.ID);
        urls = new Urls();

        img_dets = findViewById(R.id.img_dets);
        book_title = findViewById(R.id.book_title);
        book_summary = findViewById(R.id.book_summary);
        book_author_name = findViewById(R.id.book_author_name);
        book_id = findViewById(R.id.book_id);

        scroll_dets = findViewById(R.id.scroll_dets);

        linear_onlineorder = findViewById(R.id.linear_onlineorder);
        linear_hard_copy = findViewById(R.id.linear_hard_copy);
        linear_feedback = findViewById(R.id.linear_feedback);
        linear_ordering = findViewById(R.id.linear_ordering);

//        feedback_provided = findViewById(R.id.feedback_provided);
//        online_order_email = findViewById(R.id.online_order_email);

        book_summary.setText(getIntent().getStringExtra("preview"));
        book_title.setText(getIntent().getStringExtra("title"));
        book_author_name.setText(getIntent().getStringExtra("author"));
        book_id.setText(getIntent().getStringExtra("id"));


        String imgurl = getIntent().getStringExtra("image_url");

        Glide.with(this)
                .load(imgurl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .fallback(R.drawable.ic_launcher_background)
                .into(img_dets);
    }


    public void back(View view) {
        startActivity(new Intent(OtherBookDetails.this, AuthorActivity.class));
        finish();
    }


    public void handCopyOrder(View view) {
        order_now.dismiss();

        hard_copy_orders = new Dialog(this);
        hard_copy_orders.setContentView(R.layout.hard_copy_orders);
        CardView sendHardCopyOrder = hard_copy_orders.findViewById(R.id.sendHardCopyOrder);
        EditText hard_district = hard_copy_orders.findViewById(R.id.hard_district);
        EditText hard_subcounty = hard_copy_orders.findViewById(R.id.hard_subcounty);
        EditText hard_residence = hard_copy_orders.findViewById(R.id.hard_residence);
        ImageButton close_popup = hard_copy_orders.findViewById(R.id.close_popup);

        close_popup.setOnClickListener(v -> online_order.dismiss());

        sendHardCopyOrder.setOnClickListener(v1 -> {
            String hd = hard_district.getText().toString().trim();
            String hs = hard_subcounty.getText().toString().trim();
            String hr = hard_residence.getText().toString().trim();
            if (hd.equals("") && hs.equals("") && hr.equals("")) {
                hard_district.setError("District required");
                hard_subcounty.setError("Sub county required");
                hard_residence.setError("Residence address required");
            } else {
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        OtherBookDetails.this);
                alertDialog2.setTitle("Confirm to proceed");
                alertDialog2.setMessage("Make sure to double check your email address");
                alertDialog2.setIcon(R.drawable.ic_warning);
                alertDialog2.setPositiveButton("YES",
                        (dialog, which) -> sendHardAddressProvided(hd, hs, hr));
                alertDialog2.setNegativeButton("NO",
                        (dialog, which) -> dialog.cancel());
                alertDialog2.show();
            }
        });
        hard_copy_orders.setCancelable(false);
        hard_copy_orders.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(online_order.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        hard_copy_orders.show();
    }

    public void orderOnline(View view) {
//        linear_hard_copy.setVisibility(View.GONE);
//        linear_onlineorder.setVisibility(View.VISIBLE);


        online_order = new Dialog(this);
        online_order.setContentView(R.layout.online_orders);
        CardView online_send = online_order.findViewById(R.id.online_send);
        EditText online_order_email = online_order.findViewById(R.id.online_order_email);
        ImageButton close_popup = online_order.findViewById(R.id.close_popup);

        close_popup.setOnClickListener(v -> online_order.dismiss());

        online_send.setOnClickListener(v1 -> {
            String oo = online_order_email.getText().toString().trim();
            if (oo.equals("")) {
                online_order_email.setError("Email address required");
            } else {
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        OtherBookDetails.this);
                alertDialog2.setTitle("Confirm to proceed");
                alertDialog2.setMessage("Make sure to double check your email address");
                alertDialog2.setIcon(R.drawable.ic_warning);
                alertDialog2.setPositiveButton("YES",
                        (dialog, which) -> sendEmailProvided(oo));
                alertDialog2.setNegativeButton("NO",
                        (dialog, which) -> dialog.cancel());
                alertDialog2.show();
            }
        });
        online_order.setCancelable(false);
        online_order.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(online_order.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        online_order.show();


    }

    public void feedback(View view) {
//        scroll_dets.fullScroll(View.FOCUS_DOWN);
//        linear_ordering.setVisibility(View.GONE);
//        linear_feedback.setVisibility(View.VISIBLE);


        send_feedback = new Dialog(this);
        send_feedback.setContentView(R.layout.feedback_send);
        CardView send_feedbacks = send_feedback.findViewById(R.id.send_feedback);
        EditText feedback_provided = send_feedback.findViewById(R.id.feedback_provided);
        ImageButton close_popup = send_feedback.findViewById(R.id.close_popup);

        close_popup.setOnClickListener(v -> send_feedback.dismiss());
        send_feedbacks.setOnClickListener(v1 -> {
            String ff = feedback_provided.getText().toString().trim();
            if (ff.equals("")) {
                feedback_provided.setError("Feedback required");
            } else {
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        OtherBookDetails.this);
                alertDialog2.setTitle("Confirm to proceed");
                alertDialog2.setMessage("Make sure to double check your feedback");
                alertDialog2.setIcon(R.drawable.ic_warning);
                alertDialog2.setPositiveButton("YES",
                        (dialog, which) -> sendFeedbackProvided(ff));
                alertDialog2.setNegativeButton("NO",
                        (dialog, which) -> dialog.cancel());
                alertDialog2.show();
            }
        });
        send_feedback.setCancelable(false);
        send_feedback.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(send_feedback.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        send_feedback.show();

    }

  /*  public void order(View view) {
        order_now = new Dialog(this);
        order_now.setContentView(R.layout.order_now);
//        CardView send_feedbacks = send_feedback.findViewById(R.id.send_feedback);
//        CardView send_feedbacks = send_feedback.findViewById(R.id.send_feedback);
        ImageButton close_popup = order_now.findViewById(R.id.close_popup);

        close_popup.setOnClickListener(v -> order_now.dismiss());
//        send_feedbacks.setOnClickListener(v1 -> {
//
//        });
        order_now.setCancelable(false);
        order_now.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(order_now.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        order_now.show();

    }*/

    public void order(View view) {
        order_now = new Dialog(this);
        order_now.setContentView(R.layout.order_now);
        CardView orderOnline = order_now.findViewById(R.id.orderOnline);
        CardView handCopyOrder = order_now.findViewById(R.id.handCopyOrder);
        ImageButton close_popup = order_now.findViewById(R.id.close_popup);

        close_popup.setOnClickListener(v -> order_now.dismiss());
        handCopyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_now.dismiss();

                hard_copy_orders.setContentView(R.layout.hard_copy_orders);
                CardView sendHardCopyOrder = hard_copy_orders.findViewById(R.id.sendHardCopyOrder);
                EditText hard_district = hard_copy_orders.findViewById(R.id.hard_district);
                EditText hard_subcounty = hard_copy_orders.findViewById(R.id.hard_subcounty);
                EditText hard_residence = hard_copy_orders.findViewById(R.id.hard_residence);
                ImageButton close_popup = hard_copy_orders.findViewById(R.id.close_popup);

                close_popup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hard_copy_orders.dismiss();
                    }
                });


                sendHardCopyOrder.setOnClickListener(v1 -> {
                    String hd = hard_district.getText().toString().trim();
                    String hs = hard_subcounty.getText().toString().trim();
                    String hr = hard_residence.getText().toString().trim();
                    if (hd.equals("") && hs.equals("") && hr.equals("")) {
                        hard_district.setError("District required");
                        hard_subcounty.setError("Sub county required");
                        hard_residence.setError("Residence address required");
                    } else {
                        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                                OtherBookDetails.this);
                        alertDialog2.setTitle("Confirm to proceed");
                        alertDialog2.setMessage("Make sure to double check your email address");
                        alertDialog2.setIcon(R.drawable.ic_warning);
                        alertDialog2.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendHardAddressProvided(hd, hs, hr);
                                hard_copy_orders.dismiss();
                            }
                        });
                        alertDialog2.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                hard_copy_orders.dismiss();
                            }
                        });
                        alertDialog2.show();
                    }
                });
                hard_copy_orders.setCancelable(false);
                hard_copy_orders.setCanceledOnTouchOutside(false);
                Objects.requireNonNull(hard_copy_orders.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                hard_copy_orders.show();
            }
        });

        orderOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_now.dismiss();

                online_order.setContentView(R.layout.online_orders);
                CardView online_send = online_order.findViewById(R.id.online_send);
                EditText online_order_email = online_order.findViewById(R.id.online_order_email);
                ImageButton close_popup = online_order.findViewById(R.id.close_popup);

                close_popup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        online_order.dismiss();
                    }
                });

                online_send.setOnClickListener(v1 -> {
                    String oo = online_order_email.getText().toString().trim();
                    if (oo.equals("")) {
                        online_order_email.setError("Email address required");
                    } else {
                        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                                OtherBookDetails.this);
                        alertDialog2.setTitle("Confirm to proceed");
                        alertDialog2.setMessage("Make sure to double check your email address");
                        alertDialog2.setIcon(R.drawable.ic_warning);
                        alertDialog2.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendEmailProvided(oo);
                                online_order.dismiss();
                            }
                        });
                        alertDialog2.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                online_order.dismiss();
                            }
                        });
                        alertDialog2.show();
                    }
                });
                online_order.setCancelable(false);
                online_order.setCanceledOnTouchOutside(false);
                Objects.requireNonNull(online_order.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                online_order.show();

            }
        });

        order_now.setCancelable(false);
        order_now.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(order_now.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        order_now.show();

    }


    /*FEEDBACK SECTION*/
    private void sendFeedbackProvided(String fd) {
        final ProgressDialog dialog = new ProgressDialog(OtherBookDetails.this);
        dialog.setMessage("Please wait...");
        dialog.show();

        final String book_titles = book_title.getText().toString();
        final String bb = book_id.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.SEND_FEEDBACK,
                response -> {
                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONObject object = new JSONObject(response);
                        String success = object.getString("success");
                        if (success.equals("1")) {
                            dialog.dismiss();
                            Toast.makeText(OtherBookDetails.this, "Feedback sent successfully", Toast.LENGTH_LONG).show();
                            send_feedback.dismiss();

                            ss_card = new Dialog(this);
                            ss_card.setContentView(R.layout.right);
                            CardView dismiss = ss_card.findViewById(R.id.ss_card);
                            TextView success_type = ss_card.findViewById(R.id.tx_success);

                            success_type.setText("Feedback was sent successfully");
                            dismiss.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    Intent as = new Intent(OtherBookDetails.this,OtherBookDetails.class);
//                                    startActivity(as);
//                                    finish();
                                    ss_card.dismiss();
                                }
                            });

                            ss_card.setCancelable(false);
                            ss_card.setCanceledOnTouchOutside(false);
                            Objects.requireNonNull(ss_card.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            ss_card.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
//                        Toast.makeText(OtherBookDetails.this, "Feedback not sent successful, please try again ", Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                        ee_card = new Dialog(this);
                        ee_card.setContentView(R.layout.error);
                        CardView dismiss = ee_card.findViewById(R.id.ee_card);
                        TextView success_type = ee_card.findViewById(R.id.tx_error);

                        success_type.setText("Feedback not sent successful, please try again");
                        dismiss.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                    Intent as = new Intent(BookDetails.this,MainActivity.class);
//                                    startActivity(as);
//                                    finish();
                                ee_card.dismiss();
                            }
                        });

                        ee_card.setCancelable(false);
                        ee_card.setCanceledOnTouchOutside(false);
                        Objects.requireNonNull(ee_card.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        ee_card.show();

                    }
                }, error -> {
//            Toast.makeText(OtherBookDetails.this, "Feedback not sent successful, please check your network and try again", Toast.LENGTH_LONG).show();
            dialog.dismiss();

            ee_card = new Dialog(this);
            ee_card.setContentView(R.layout.error);
            CardView dismiss = ee_card.findViewById(R.id.ee_card);
            TextView success_type = ee_card.findViewById(R.id.tx_error);

            success_type.setText("Feedback not sent successful, please check your network and try again");
            dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                                    Intent as = new Intent(BookDetails.this,MainActivity.class);
//                                    startActivity(as);
//                                    finish();
                    ee_card.dismiss();
                }
            });

            ee_card.setCancelable(false);
            ee_card.setCanceledOnTouchOutside(false);
            Objects.requireNonNull(ee_card.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ee_card.show();

        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("feedback", fd);
                params.put("userid", getID);
                params.put("bookid", bb);
                params.put("book_title", book_titles);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /*ONLINE ORDER SECTION
type id = 1 thats for soft copy orders
    */
    private void sendEmailProvided(String oo) {
        final ProgressDialog dialog = new ProgressDialog(OtherBookDetails.this);
        dialog.setMessage("Please wait...");
        dialog.show();

        final String book_titles = book_title.getText().toString();
        final String bb = book_id.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.SEND_ONLINE_EMAIL_ORDER,
                response -> {
                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONObject object = new JSONObject(response);
                        String success = object.getString("success");
                        if (success.equals("1")) {
                            dialog.dismiss();
//                            Toast.makeText(BookDetails.this, "Order sent successfully", Toast.LENGTH_LONG).show();
//                            feedback_provided.getText().clear();
                            online_order.dismiss();

                            ss_card = new Dialog(this);
                            ss_card.setContentView(R.layout.right);
                            CardView dismiss = ss_card.findViewById(R.id.ss_card);
                            TextView success_type = ss_card.findViewById(R.id.tx_success);

                            success_type.setText("Order was sent successfully");
                            dismiss.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    Intent as = new Intent(BookDetails.this,MainActivity.class);
//                                    startActivity(as);
//                                    finish();
                                    ss_card.dismiss();
                                }
                            });

                            ss_card.setCancelable(false);
                            ss_card.setCanceledOnTouchOutside(false);
                            Objects.requireNonNull(ss_card.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            ss_card.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
//                        Toast.makeText(BookDetails.this, "Order not sent successful, please try again ", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        ee_card = new Dialog(this);
                        ee_card.setContentView(R.layout.error);
                        CardView dismiss = ee_card.findViewById(R.id.ee_card);
                        TextView success_type = ee_card.findViewById(R.id.tx_error);

                        success_type.setText("Order not sent successful, please try again");
                        dismiss.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                    Intent as = new Intent(BookDetails.this,MainActivity.class);
//                                    startActivity(as);
//                                    finish();
                                ee_card.dismiss();
                            }
                        });

                        ee_card.setCancelable(false);
                        ee_card.setCanceledOnTouchOutside(false);
                        Objects.requireNonNull(ee_card.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        ee_card.show();

                    }
                }, error -> {
//            Toast.makeText(BookDetails.this, "Order not sent successful, please check your network and try again", Toast.LENGTH_LONG).show();
            dialog.dismiss();
            ee_card = new Dialog(this);
            ee_card.setContentView(R.layout.error);
            CardView dismiss = ee_card.findViewById(R.id.ee_card);
            TextView success_type = ee_card.findViewById(R.id.tx_error);

            success_type.setText("Order not sent successful, please check your network and try again");
            dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                                    Intent as = new Intent(BookDetails.this,MainActivity.class);
//                                    startActivity(as);
//                                    finish();
                    ee_card.dismiss();
                }
            });

            ee_card.setCancelable(false);
            ee_card.setCanceledOnTouchOutside(false);
            Objects.requireNonNull(ee_card.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ee_card.show();

        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                String identify = "1";
                params.put("online_order", oo);
                params.put("userid", getID);
                params.put("bookid", bb);
                params.put("type_identifier", identify);
                params.put("book_title", book_titles);

                ////make your table
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void sendHardAddressProvided(String hd, String hs, String hr) {
        final ProgressDialog dialog = new ProgressDialog(OtherBookDetails.this);
        dialog.setMessage("Please wait...");
        dialog.show();

        final String book_titles = book_title.getText().toString();
        final String bb = book_id.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.SEND_HARD_COPY_ORDER,
                response -> {
                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONObject object = new JSONObject(response);
                        String success = object.getString("success");
                        if (success.equals("1")) {
                            dialog.dismiss();
//                            Toast.makeText(BookDetails.this, "Order sent successfully", Toast.LENGTH_LONG).show();
//                            feedback_provided.getText().clear();
                            online_order.dismiss();

                            ss_card = new Dialog(this);
                            ss_card.setContentView(R.layout.right);
                            CardView dismiss = ss_card.findViewById(R.id.ss_card);
                            TextView success_type = ss_card.findViewById(R.id.tx_success);

                            success_type.setText("Order was sent successfully");
                            dismiss.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    Intent as = new Intent(BookDetails.this,MainActivity.class);
//                                    startActivity(as);
//                                    finish();
                                    ss_card.dismiss();
                                }
                            });

                            ss_card.setCancelable(false);
                            ss_card.setCanceledOnTouchOutside(false);
                            Objects.requireNonNull(ss_card.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            ss_card.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
//                        Toast.makeText(BookDetails.this, "Order not sent successful, please try again ", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        ee_card = new Dialog(this);
                        ee_card.setContentView(R.layout.error);
                        CardView dismiss = ee_card.findViewById(R.id.ee_card);
                        TextView success_type = ee_card.findViewById(R.id.tx_error);

                        success_type.setText("Order not sent successful, please try again");
                        dismiss.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                    Intent as = new Intent(BookDetails.this,MainActivity.class);
//                                    startActivity(as);
//                                    finish();
                                ee_card.dismiss();
                            }
                        });

                        ee_card.setCancelable(false);
                        ee_card.setCanceledOnTouchOutside(false);
                        Objects.requireNonNull(ee_card.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        ee_card.show();

                    }
                }, error -> {
//            Toast.makeText(BookDetails.this, "Order not sent successful, please check your network and try again", Toast.LENGTH_LONG).show();
            dialog.dismiss();
            ee_card = new Dialog(this);
            ee_card.setContentView(R.layout.error);
            CardView dismiss = ee_card.findViewById(R.id.ee_card);
            TextView success_type = ee_card.findViewById(R.id.tx_error);

            success_type.setText("Order not sent successful, please check your network and try again");
            dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                                    Intent as = new Intent(BookDetails.this,MainActivity.class);
//                                    startActivity(as);
//                                    finish();
                    ee_card.dismiss();
                }
            });

            ee_card.setCancelable(false);
            ee_card.setCanceledOnTouchOutside(false);
            Objects.requireNonNull(ee_card.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ee_card.show();

        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                String identify = "2";
                params.put("district", hd);
                params.put("sub_county", hs);
                params.put("residence", hr);
                params.put("userid", getID);
                params.put("bookid", bb);
                params.put("type_identifier", identify);
                params.put("book_title", book_titles);


                ////make your table
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}