package com.example.tushome.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tushome.Models.ChatModel;
import com.example.tushome.R;
import com.example.tushome.Urls.SessionManager;
import com.example.tushome.Urls.Urls;

import java.util.HashMap;
import java.util.List;


public class SingleChatAdapter extends RecyclerView.Adapter<SingleChatAdapter.ChatViewHolder> {

    Context context;
    List<ChatModel> mData;
    SessionManager sessionManager;
    String getId, session_contact, contact;
    Urls urls;

    public SingleChatAdapter(Context context, List<ChatModel> mData) {
        this.context = context;
        this.mData = mData;
        urls = new Urls();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_chat, null, false);
        return new ChatViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, final int position) {
        //        handle session manager
        sessionManager = new SessionManager(context);
//        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        contact = user.get(SessionManager.USERNAME);

        holder.session_no.setText(contact);


        ChatModel chatModel = mData.get(position);
//        holder.message.setText(mData.get(position).getMessage());
        holder.sender_no.setText(mData.get(position).getSender());
        holder.receiver_no.setText(mData.get(position).getReceiver());

//        if (chatModel.getSender().equals(contact) || chatModel.getRecieverID().equals(getId)) {

//            if (chatModel.getSender().equals(contact)) {
//            holder.sender_no.setText("You");/// from me
//            holder.sender_no.setTextColor(Color.BLACK);
//                holder.card_sender.setVisibility(View.VISIBLE);
//                holder.card_receiver.setVisibility(View.GONE);
//                holder.sender_message.setText(mData.get(position).getMessage());
//                holder.sender_time.setText(mData.get(position).getTime());

                /*et default images incase the image fails to load*/

                /*for loading sender images*/
//                String imageUrl = urls.https + "user_images/" + chatModel.getImage();
//                try {
//
//                    Glide.with(context)
//                            .load(imageUrl)
//                            .placeholder(R.drawable.ic_launcher_background)
//                            .error(R.drawable.ic_launcher_background)
//                            .fallback(R.drawable.ic_launcher_background)
//                            .into(holder.sender_image);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }else
             if (chatModel.getReceiver().equals(contact)) {

                 holder.card_sender.setVisibility(View.VISIBLE);
                 holder.card_receiver.setVisibility(View.GONE);
                 holder.sender_message.setText(mData.get(position).getMessage());
                 holder.sender_time.setText(mData.get(position).getTime());

                 /*for loading sender images*/
                 String imageUrls = urls.https + "user_images/" + chatModel.getImage();
                 try {

                     Glide.with(context)
                             .load(imageUrls)
                             .placeholder(R.drawable.ic_launcher_background)
                             .error(R.drawable.ic_launcher_background)
                             .fallback(R.drawable.ic_launcher_background)
                             .into(holder.sender_image);
                 } catch (Exception e) {
                     e.printStackTrace();
                 }

             }
            else {





                 holder.card_receiver.setVisibility(View.VISIBLE);
                 holder.card_sender.setVisibility(View.GONE);
                 holder.receiver_message.setText(mData.get(position).getMessage()); ///gets message
                 holder.receiver_time.setText(mData.get(position).getTime()); ///gets time

//                 holder.card_sender.setVisibility(View.VISIBLE);
//                 holder.card_receiver.setVisibility(View.GONE);
//                 holder.sender_message.setText(mData.get(position).getMessage());
//                 holder.sender_time.setText(mData.get(position).getTime());

                 /*et default images incase the image fails to load*/

                 /*for loading sender images*/
                 String imageUrlss = urls.https + "user_images/" + chatModel.getImage();
                 try {

                     Glide.with(context)
                             .load(imageUrlss)
                             .placeholder(R.drawable.ic_launcher_background)
                             .error(R.drawable.ic_launcher_background)
                             .fallback(R.drawable.ic_launcher_background)
                             .into(holder.receiver_image);
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
            }
//        }
//        else {
//
//        }


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView receiver_message, receiver_time, session_no, sender_no, receiver_no, sender_message, sender_time;
        RelativeLayout card_sender, fcard_sms_farmer, card_receiver;
        ImageView sender_image, receiver_image;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            receiver_message = itemView.findViewById(R.id.receiver_message);
            receiver_time = itemView.findViewById(R.id.receiver_time);
            session_no = itemView.findViewById(R.id.session_no);
            sender_no = itemView.findViewById(R.id.sender_no);
            receiver_no = itemView.findViewById(R.id.receiver_no);
            card_sender = itemView.findViewById(R.id.card_sender);
            card_receiver = itemView.findViewById(R.id.card_receiver);
            sender_message = itemView.findViewById(R.id.sender_message);
            sender_time = itemView.findViewById(R.id.sender_time);
            sender_image = itemView.findViewById(R.id.sender_image);
            receiver_image = itemView.findViewById(R.id.receiver_image);
        }

    }
}
