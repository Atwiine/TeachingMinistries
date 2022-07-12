package com.example.tushome.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tushome.Authors.Requests;
import com.example.tushome.Authors.SeeRequestBooks;
import com.example.tushome.Models.AllRequestsModel;
import com.example.tushome.R;
import com.example.tushome.Urls.SessionManager;
import com.example.tushome.Urls.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AllRequestAdapter extends RecyclerView.Adapter<AllRequestAdapter.RequestViewHolder> {
    Context context;
    List<AllRequestsModel> mData;
    Urls urls;
    SessionManager sessionManager;
    String getID;
    Dialog ss_card, ee_card;

    public AllRequestAdapter(Context context, List<AllRequestsModel> mData) {
        this.context = context;
        this.mData = mData;
        urls = new Urls();
        ss_card = new Dialog(context);
        ee_card = new Dialog(context);
        sessionManager = new SessionManager(context);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(SessionManager.ID);
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_all_requests, null, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, final int position) {


        AllRequestsModel farmerModel = mData.get(position);

        holder.reader_name_request.setText(mData.get(position).getReader_name());
        holder.id_request.setText(mData.get(position).getId());

        holder.balance_card.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        holder.status_request.setText(mData.get(position).getStatus());

        String sta = holder.status_request.getText().toString();

        /*add the status for the author's actions and then add it the fetching php file(***)
        * meanwhile write the updating php files for these cards once pressed(****)
        * also when fetching the requests add the readers name (***) */

        if (sta.equals("order-confirmed")) {
            holder.please_confirm_req.setVisibility(View.GONE);
            holder.linear_options_req.setVisibility(View.GONE);
            holder.linear_action_req.setVisibility(View.VISIBLE);
            holder.success_order_req.setVisibility(View.VISIBLE);
        } else if (sta.equals("order-denied")) {
            holder.please_confirm_req.setVisibility(View.GONE);
            holder.linear_options_req.setVisibility(View.GONE);
            holder.linear_action_req.setVisibility(View.VISIBLE);
            holder.deny_order_req.setVisibility(View.VISIBLE);
        }


        holder.confirm_card_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String bid = holder.id_request.getText().toString();
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setTitle("Confirm order Book")
                        .setMessage("Are you sure you want to confirm this order")
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_warning)
                        .setPositiveButton("YES", (dialog, which) -> {
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.CONFIRM_REQUEST,
                                    response -> {
                                        try {
                                            Log.i("tagconvertstr", "[" + response + "]");
                                            JSONObject object = new JSONObject(response);
                                            String success = object.getString("success");
                                            if (success.equals("1")) {
                                                Log.i("tagconvertstr", "[" + response + "]");

                                                ss_card = new Dialog(context);
                                                ss_card.setContentView(R.layout.right);
                                                CardView dismiss = ss_card.findViewById(R.id.ss_card);
                                                TextView success_type = ss_card.findViewById(R.id.tx_success);

                                                success_type.setText("Order was confirmed successfully");
                                                dismiss.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(context, SeeRequestBooks.class);
                                                        context.startActivity(intent);
                                                        ((Activity) context).finish();
//                                                        linear_options_req
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
                                            Toast.makeText(context, "Order not confirmed, please try again " + e.getMessage(), Toast.LENGTH_LONG).show();
                                            ee_card = new Dialog(context);
                                            ee_card.setContentView(R.layout.error);
                                            CardView dismiss = ee_card.findViewById(R.id.ee_card);
                                            TextView success_type = ee_card.findViewById(R.id.tx_error);

                                            success_type.setText("Order not confirmed, please try again");
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
                                ee_card = new Dialog(context);
                                ee_card.setContentView(R.layout.error);
                                CardView dismiss = ee_card.findViewById(R.id.ee_card);
                                TextView success_type = ee_card.findViewById(R.id.tx_error);

                                success_type.setText("Order not confirmed, please check your network and try again");
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
                                    params.put("authorid", getID);
                                    params.put("id", bid);
                                    return params;

                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(context);
                            requestQueue.add(stringRequest);
                        })
                        .setNegativeButton("NO", (dialog, which) -> dialog.dismiss());
                //Creating dialog box
                android.app.AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        holder.deny_card_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String bid = holder.id_request.getText().toString();
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setTitle("Confirm order Book")
                        .setMessage("Are you sure you want to deny this order")
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_warning)
                        .setPositiveButton("YES", (dialog, which) -> {
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.DENY_REQUEST,
                                    response -> {
                                        try {
                                            Log.i("tagconvertstr", "[" + response + "]");
                                            JSONObject object = new JSONObject(response);
                                            String success = object.getString("success");
                                            if (success.equals("1")) {
                                                Log.i("tagconvertstr", "[" + response + "]");

                                                ss_card = new Dialog(context);
                                                ss_card.setContentView(R.layout.right);
                                                CardView dismiss = ss_card.findViewById(R.id.ss_card);
                                                TextView success_type = ss_card.findViewById(R.id.tx_success);

                                                success_type.setText("Order was denied successfully");
                                                dismiss.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(context, SeeRequestBooks.class);
                                                        context.startActivity(intent);
                                                        ((Activity) context).finish();
//                                                        linear_options_req
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
                                            Toast.makeText(context, "Order not denied, please try again " + e.getMessage(), Toast.LENGTH_LONG).show();
                                            ee_card = new Dialog(context);
                                            ee_card.setContentView(R.layout.error);
                                            CardView dismiss = ee_card.findViewById(R.id.ee_card);
                                            TextView success_type = ee_card.findViewById(R.id.tx_error);

                                            success_type.setText("Order not denied, please try again");
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
                                ee_card = new Dialog(context);
                                ee_card.setContentView(R.layout.error);
                                CardView dismiss = ee_card.findViewById(R.id.ee_card);
                                TextView success_type = ee_card.findViewById(R.id.tx_error);

                                success_type.setText("Order not denied, please check your network and try again");
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

                                    params.put("authorid", getID);
                                    params.put("id", bid);
                                    return params;

                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(context);
                            requestQueue.add(stringRequest);
                        })
                        .setNegativeButton("NO", (dialog, which) -> dialog.dismiss());
                //Creating dialog box
                android.app.AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {

        TextView reader_name_request, id_request, status_request, please_confirm_req, success_order_req, deny_order_req;
        CardView balance_card, confirm_card_request, deny_card_request;
        LinearLayout linear_options_req, linear_action_req;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            reader_name_request = itemView.findViewById(R.id.reader_name_request);
            id_request = itemView.findViewById(R.id.id_request);
            balance_card = itemView.findViewById(R.id.balance_card);
            confirm_card_request = itemView.findViewById(R.id.confirm_card_request);
            deny_card_request = itemView.findViewById(R.id.deny_card_request);
            linear_options_req = itemView.findViewById(R.id.linear_options_req);
            status_request = itemView.findViewById(R.id.status_request);
            linear_action_req = itemView.findViewById(R.id.linear_action_req);
            please_confirm_req = itemView.findViewById(R.id.please_confirm_req);
            success_order_req = itemView.findViewById(R.id.success_order_req);
            deny_order_req = itemView.findViewById(R.id.deny_order_req);

        }
    }
}
