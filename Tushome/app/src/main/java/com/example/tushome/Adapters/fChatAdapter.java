package com.example.tushome.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tushome.Models.fChatModel;
import com.example.tushome.R;
import com.example.tushome.Urls.SessionManager;

import java.util.HashMap;
import java.util.List;

public class fChatAdapter extends RecyclerView.Adapter<fChatAdapter.ChatViewHolder> {

    Context fcontext;
    List<fChatModel> fmData;
    SessionManager sessionManager;
    String getId, session_contact, contact;


    public fChatAdapter(Context fcontext, List<fChatModel> fmData) {
        this.fcontext = fcontext;
        this.fmData = fmData;

    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(fcontext).inflate(R.layout.item_fchat, null, false);
        return new ChatViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, final int position) {
        fChatModel farmer_model = fmData.get(position);
        holder.fmessage.setText(fmData.get(position).getMessage());
        holder.ftime.setText(fmData.get(position).getTime());
        holder.fsender_no.setText(fmData.get(position).getSender());
        holder.freciever_no.setText(fmData.get(position).getReceiver());
//        handle session manager
        sessionManager = new SessionManager(fcontext);
//        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        contact = user.get(SessionManager.USERNAME);

        holder.fsession_no.setText(contact);
        holder.fcard_sms_farmer.setAnimation(AnimationUtils.loadAnimation(fcontext, R.anim.fade_scale_animation));

    }

    @Override
    public int getItemCount() {
        return fmData.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView fmessage, ftime, fsession_no, fsender_no, freciever_no;
        Button details;
        CardView card_sms_farmer, fcard_sms_farmer;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);


            fmessage = itemView.findViewById(R.id.fsms);
            ftime = itemView.findViewById(R.id.ftime);
            fsession_no = itemView.findViewById(R.id.fsession_no);
            fsender_no = itemView.findViewById(R.id.fsender_no);
            freciever_no = itemView.findViewById(R.id.freciever_no);
            fcard_sms_farmer = itemView.findViewById(R.id.fcard_sms_farmer);

            if (fsession_no.getText().toString().equals(freciever_no.getText().toString())) {
                fcard_sms_farmer.setVisibility(View.VISIBLE);
//            fcard_sms_farmer.setCardBackgroundColor(Color.WHITE);
            }


        }

    }
}
