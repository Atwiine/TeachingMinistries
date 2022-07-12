package com.example.tushome.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.example.tushome.MessageOptions.ChatUsers;
import com.example.tushome.MessageOptions.SinglesPost;
import com.example.tushome.Models.UsersModel;
import com.example.tushome.R;
import com.example.tushome.MessageOptions.SinglesChat;
import com.example.tushome.Urls.SessionManager;
import com.example.tushome.Urls.Urls;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChatUsersAdapter extends RecyclerView.Adapter<ChatUsersAdapter.ChatUserViewHolder> {
    Context context;
    List<UsersModel> mData;
    Urls urls;
    SessionManager sessionManager;
    String getId;

    public ChatUsersAdapter(Context context, List<UsersModel> mData) {
        this.context = context;
        this.mData = mData;
        urls = new Urls();
        sessionManager = new SessionManager(context);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public ChatUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_chat_user, null, false);
        return new ChatUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatUserViewHolder holder, final int position) {

        UsersModel usersModel = mData.get(position);
        holder.cName.setText(mData.get(position).getName());
        holder.cID.setText(mData.get(position).getId());
        holder.cPreviousMessage.setText(mData.get(position).getQuote());
//        holder.balance_card.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        String imageUrl = urls.https + "user_images/" + usersModel.getImage();
        try {

            Glide.with(context)
                    .load(imageUrl)
                    .into(holder.cImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*run the counting the number of new messages*/
        holder.loadChatUsersNOMessages();
        holder.loadChatUsersLastMessages();





    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ChatUserViewHolder extends RecyclerView.ViewHolder {

        TextView cName, cID,actual_likes_no,cPreviousMessage,cdate;
        CardView balance_card;
        ImageView cImage;
        RelativeLayout re_likes_no;

        public ChatUserViewHolder(@NonNull View itemView) {
            super(itemView);

            cName = itemView.findViewById(R.id.cName);
            balance_card = itemView.findViewById(R.id.balance_card);
            cID = itemView.findViewById(R.id.cID);
            cImage = itemView.findViewById(R.id.cImage);
            actual_likes_no = itemView.findViewById(R.id.actual_likes_no);
            cPreviousMessage = itemView.findViewById(R.id.cPreviousMessage);
            re_likes_no = itemView.findViewById(R.id.re_likes_no);
            cdate = itemView.findViewById(R.id.cdate);

            balance_card.setOnClickListener(view -> {

                String no = actual_likes_no.getText().toString();
                if (no.equals("0")){
                    Intent request = new Intent(context, SinglesChat.class);
                    request.putExtra("username", mData.get(getAdapterPosition()).getName());
                    request.putExtra("id", mData.get(getAdapterPosition()).getId());
                    request.putExtra("image_url", urls.https + "user_images/" + mData.get(getAdapterPosition()).getImage());
                    context.startActivity(request);
                }else {
                    String neee = "new";
                    Intent request = new Intent(context, SinglesChat.class);
                    request.putExtra("username", mData.get(getAdapterPosition()).getName());
                    request.putExtra("new", neee);
                    request.putExtra("id", mData.get(getAdapterPosition()).getId());
                    request.putExtra("image_url", urls.https + "user_images/" + mData.get(getAdapterPosition()).getImage());
                    context.startActivity(request);
                }

            });


        }


        private void loadChatUsersNOMessages() {

            String crr = cID.getText().toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.GET_NO_NEW_MESSAGES,
                    response -> {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONArray tips = new JSONArray(response);
                            if (tips.length() == 0) {

                                actual_likes_no.setText("0");
                                cPreviousMessage.setText("N/A");
                            } else {
                                for (int i = 0; i < tips.length(); i++) {
                                    JSONObject inputsObjects = tips.getJSONObject(i);

                                    String total = inputsObjects.getString("total");
                                    String userid = inputsObjects.getString("userid");
                                    String receiver_id = inputsObjects.getString("receiver_id");
                                    // cPreviousMessage.setText(mmm);
                                    actual_likes_no.setText(total);

                                    /*handle the showing of the new messages*/
//                                    String rt = actual_likes_no.getText().toString();
//                                    if (total.equals("0")){
//                                         re_likes_no.setVisibility(View.GONE);
//                                    }else {
//                                        re_likes_no.setVisibility(View.VISIBLE);
//                                    }

                                    /*again using this the userid that send the counting 
                                    options and then if its equal to the current logged in 
                                    user then hide the counting thing*/
                                   /*if (getId.equals(userid)){
//                                       re_likes_no.setVisibility(View.GONE);
                                       if (total.equals("0")){
                                           re_likes_no.setVisibility(View.GONE);
                                       }else {
                                           re_likes_no.setVisibility(View.VISIBLE);
                                       }
                                   }else if (getId.equals(receiver_id)){
//                                       re_likes_no.setVisibility(View.VISIBLE);
                                       if (total.equals("0")){
                                           re_likes_no.setVisibility(View.GONE);
                                       }else {
                                           re_likes_no.setVisibility(View.VISIBLE);
                                       }
                                   }*/


                                    if (!userid.equals("0")){
                                        if (total.equals("0")){
                                            re_likes_no.setVisibility(View.GONE);
                                        }else {
                                            re_likes_no.setVisibility(View.VISIBLE);
                                        }
                                    }else {
                                        re_likes_no.setVisibility(View.GONE);
                                    }


                                }

                            }
                        } catch (JSONException e) {
                            Toast.makeText(context, "we " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }, error -> {
                Toast.makeText(context, "wer " + error.toString(), Toast.LENGTH_SHORT).show();

            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("userid", getId);
                    params.put("receiver_id", crr);
                    return params;

                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        }
        private void loadChatUsersLastMessages() {

            String crr = cID.getText().toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.GET_LAST_MESSAGES,
                    response -> {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONArray tips = new JSONArray(response);
                            if (tips.length() == 0) {

                                actual_likes_no.setText("0");
//                                cPreviousMessage.setText("N/A");
                                cPreviousMessage.setVisibility(View.GONE);
                                cdate.setVisibility(View.GONE);
                            } else {
                                for (int i = 0; i < tips.length(); i++) {
                                    JSONObject inputsObjects = tips.getJSONObject(i);

                                    String datee = inputsObjects.getString("time");
                                     String mmm = inputsObjects.getString("message");
                                    cPreviousMessage.setVisibility(View.VISIBLE);
                                    cdate.setVisibility(View.VISIBLE);
                                    cPreviousMessage.setText(mmm);
                                    cdate.setText(datee);


                                }

                            }
                        } catch (JSONException e) {
                            Toast.makeText(context, "we " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }, error -> {
                Toast.makeText(context, "wer " + error.toString(), Toast.LENGTH_SHORT).show();

            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("userid", crr);
                    return params;

                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        }
    }
}
