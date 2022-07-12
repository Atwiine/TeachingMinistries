package com.example.tushome.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.example.tushome.Models.CommentsModel;
import com.example.tushome.Models.RNotificationModel;
import com.example.tushome.R;
import com.example.tushome.Urls.SessionManager;
import com.example.tushome.Urls.Urls;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {
    Context context;
    List<CommentsModel> mData;
    Urls urls;
    Dialog details;
    SessionManager sessionManager;
    String getId, session_contact, contact;

    public CommentsAdapter(Context context, List<CommentsModel> mData) {
        this.context = context;
        this.mData = mData;
        urls = new Urls();
        details = new Dialog(context);
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_comments, null, false);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, final int position) {

        sessionManager = new SessionManager(context);
//        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        contact = user.get(SessionManager.USERNAME);


        CommentsModel commentsModel = mData.get(position);
        holder.id_comment.setText(mData.get(position).getId());
        holder.commentss.setText(mData.get(position).getComments());
        holder.date_not.setText(mData.get(position).getDate());
        holder.comment_fromwho.setText(mData.get(position).getComment_fromwho());
//        holder.balance_card.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        String ae = holder.comment_fromwho.getText().toString();
        if (ae.equals(contact)){
            holder.comment_fromwho.setText("You");
        }

        String imageUrl = urls.https + "user_images/" + commentsModel.getFromimage();
        try {

            Glide.with(context)
                    .load(imageUrl)
                    .into(holder.sender_image);
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder {

        TextView id_comment, commentss,date_not,comment_fromwho;
        CardView balance_card;
ImageView sender_image;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);

            id_comment = itemView.findViewById(R.id.id_comment);
            commentss = itemView.findViewById(R.id.commentss);
            balance_card = itemView.findViewById(R.id.balance_card);
            date_not = itemView.findViewById(R.id.date_not);
            comment_fromwho = itemView.findViewById(R.id.comment_fromwho);
            sender_image = itemView.findViewById(R.id.sender_image);


        }


    }
}
