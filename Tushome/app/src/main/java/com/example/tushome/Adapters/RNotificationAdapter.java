package com.example.tushome.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tushome.Authors.Requests;
import com.example.tushome.Models.AuthorModel;
import com.example.tushome.Models.RNotificationModel;
import com.example.tushome.R;
import com.example.tushome.Urls.Urls;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class RNotificationAdapter extends RecyclerView.Adapter<RNotificationAdapter.NotificationViewHolder> {
    Context context;
    List<RNotificationModel> mData;
    Urls urls;
    Dialog details;

    public RNotificationAdapter(Context context, List<RNotificationModel> mData) {
        this.context = context;
        this.mData = mData;
        urls = new Urls();
        details = new Dialog(context);
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_reader_notification, null, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, final int position) {

        RNotificationModel farmerModel = mData.get(position);
        holder.title.setText(mData.get(position).getTitle());
        holder.status_notification.setText(mData.get(position).getStatus());
        holder.denied__reason_notification.setText(mData.get(position).getReason());
        holder.id.setText(mData.get(position).getId());
        holder.date_not.setText(mData.get(position).getDate());


        holder.balance_card.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));



        String ss =  holder.status_notification.getText().toString();
        if (ss.equals("denied")){
            holder.denied_order_notification.setVisibility(View.VISIBLE);
            holder.reasons_why_denied.setVisibility(View.VISIBLE);
        }
        else {
            holder.reasons_why_denied.setVisibility(View.GONE);
            holder.success_order_notification.setVisibility(View.VISIBLE);
        }


        holder.reasons_why_denied.setOnClickListener(new View.OnClickListener() {//come out once the status is denied
            @Override
            public void onClick(View v) {
                ////the see button shld come out wen the status is denied
                holder.linear_reason_notification.setVisibility(View.VISIBLE);
                holder.reasons_why_denied.setVisibility(View.GONE);
                holder.hide_reasons_why_denied.setVisibility(View.VISIBLE);
            }
        });


        holder.hide_reasons_why_denied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.linear_reason_notification.setVisibility(View.GONE);
                holder.hide_reasons_why_denied.setVisibility(View.GONE);
                holder.reasons_why_denied.setVisibility(View.VISIBLE);
            }
        });




    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView denied__reason_notification, denied_order_notification, success_order_notification,
                id,title,status_notification,date_not;
        CardView balance_card;
        MaterialCardView hide_reasons_why_denied,reasons_why_denied;
        LinearLayout linear_reason_notification;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            denied__reason_notification = itemView.findViewById(R.id.denied__reason_notification);
            denied_order_notification = itemView.findViewById(R.id.denied_order_notification);
            balance_card = itemView.findViewById(R.id.balance_card);
            id = itemView.findViewById(R.id.id);
            success_order_notification = itemView.findViewById(R.id.success_order_notification);
            hide_reasons_why_denied = itemView.findViewById(R.id.hide_reasons_why_denied);
            reasons_why_denied = itemView.findViewById(R.id.reasons_why_denied);
            linear_reason_notification = itemView.findViewById(R.id.linear_reason_notification);
            status_notification = itemView.findViewById(R.id.status_notification);
            date_not = itemView.findViewById(R.id.date_not);


        }
    }
}
