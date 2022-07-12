package com.example.tushome.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.tushome.Models.CommentsModel;
import com.example.tushome.Models.PostModel;
import com.example.tushome.R;
import com.example.tushome.Urls.SessionLikes;
import com.example.tushome.Urls.SessionManager;
import com.example.tushome.Urls.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ChatViewHolder> {

    Context context;
    List<PostModel> mData;
    SessionLikes sessionLikes;
    SessionManager sessionManager;
    String getId, sendLikes, idPost, contact,imagess;
    Urls urls;
    Dialog comments_popup;
    String statuss, postIDD;

    public PostAdapter(Context context, List<PostModel> mData) {
        this.context = context;
        this.mData = mData;
        urls = new Urls();
        comments_popup = new Dialog(context);
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_post, null, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

        sessionManager = new SessionManager(context);
//        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        contact = user.get(SessionManager.USERNAME);

        PostModel postModel = mData.get(position);


        holder.card_postss.setVisibility(View.VISIBLE);
        holder.card_postss_receive.setVisibility(View.GONE);
        holder.pName.setText(mData.get(position).getUsername());
        holder.pId.setText(mData.get(position).getPostID());
        holder.pTime.setText(mData.get(position).getTime());
        holder.pmessage.setText(mData.get(position).getMessage());
        holder.plikes.setText(mData.get(position).getLikes());
//        holder.pcomments.setText(mData.get(position).getComments());


///check to see if the sender is equal to the logged in user
        if (postModel.getUsername().equals(contact)) {
//holder.pName.setText("You");
//            holder.view_owner_post.setVisibility(View.VISIBLE);
//            holder.re_owner.setVisibility(View.VISIBLE);
//            holder.card_postss.setBackgroundColor(ContextCompat.getColor(context,R.color.purple_700));
//            holder.pName.setTextColor(ContextCompat.getColor(context,R.color.white));
//            holder.pTime.setTextColor(ContextCompat.getColor(context,R.color.white));
//            holder.pmessage.setTextColor(ContextCompat.getColor(context,R.color.white));
//            holder.plikes.setTextColor(ContextCompat.getColor(context,R.color.white));
//            holder.pcomments.setTextColor(ContextCompat.getColor(context,R.color.white));
//            holder.vei_white.setVisibility(View.VISIBLE);
//            holder.vei_blue.setVisibility(View.GONE);

            holder.card_postss.setVisibility(View.GONE);
            holder.card_postss_receive.setVisibility(View.VISIBLE);
            holder.rpName.setText(mData.get(position).getUsername());
            holder.rpId.setText(mData.get(position).getPostID());
            holder.rpTime.setText(mData.get(position).getTime());
            holder.rpmessage.setText(mData.get(position).getMessage());
            holder.rplikes.setText(mData.get(position).getLikes());


            String wewe = holder.rpmessage.getText().toString();
            if (wewe.equals("")) {
                holder.rpmessage.setVisibility(View.GONE);
            }


            /*for loading sender images*/

            String imageUrl = urls.https + "user_images/" + postModel.getUserimage();
            try {

                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .fallback(R.drawable.ic_launcher_background)
                        .into(holder.rpimage);
            } catch (Exception e) {
                e.printStackTrace();
            }



            /*loading the poster's image*/
            String pos = postModel.getPostimage();
            if (pos.equals("null")) {
                holder.rpost_image.setVisibility(View.GONE);
            } else {

                String imageUrlss = urls.https + "post_images/" + postModel.getPostimage();
                try {

                    Glide.with(context)
                            .load(imageUrlss)
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_background)
                            .fallback(R.drawable.ic_launcher_background)
                            .into(holder.rpost_image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
        String wewe = holder.pmessage.getText().toString();
        if (wewe.equals("")) {
            holder.pmessage.setVisibility(View.GONE);
        }


        /*for loading sender images*/

        String imageUrl = urls.https + "user_images/" + postModel.getUserimage();
        try {

            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .fallback(R.drawable.ic_launcher_background)
                    .into(holder.pimage);
        } catch (Exception e) {
            e.printStackTrace();
        }



        /*loading the poster's image*/
        String pos = postModel.getPostimage();
        if (pos.equals("null")) {
            holder.post_image.setVisibility(View.GONE);
        } else {

            String imageUrlss = urls.https + "post_images/" + postModel.getPostimage();
            try {

                Glide.with(context)
                        .load(imageUrlss)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .fallback(R.drawable.ic_launcher_background)
                        .into(holder.post_image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }






//
//        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        boolean isButtonPressed = preferences.getBoolean("button_1_pressed",false);
//        boolean isDisButtonPressed = preferences.getBoolean("button_2_pressed",false);
//        if (isButtonPressed){

        /*sessionLikes = new SessionLikes(context);

        HashMap<String, String> users = sessionLikes.getUserDetail();
        statuss = users.get(SessionLikes.STATUS);
        postIDD = users.get(SessionLikes.PID);

        idPost = holder.pId.getText().toString();

        if (postIDD.isEmpty()) {
            Toast.makeText(context, "shit is empty is  ", Toast.LENGTH_SHORT).show();
//            holder.like_post.setVisibility(View.GONE);
        } else {
            if (postIDD.equals(idPost)) {

                if (statuss.equals("like")) {
                    holder.like_post.setVisibility(View.GONE);
                    holder.dislike_post.setVisibility(View.VISIBLE);
//            Toast.makeText(context, "status is  " + statuss, Toast.LENGTH_SHORT).show();
                } else if (statuss.equals("dislike")) {
                    holder.dislike_post.setVisibility(View.GONE);
                    holder.like_post.setVisibility(View.VISIBLE);
//                Toast.makeText(context, "status is  " + statuss, Toast.LENGTH_SHORT).show();
                }
            }*/


        //read the prefrences
        SharedPreferences pref = context.getSharedPreferences("Button", MODE_PRIVATE);
        String state = pref.getString(String.valueOf(position)+"pressed", "no");


        if(state.equals("yes")){

//the button must be pressed, make it pressed (check mark icon)

//change the icon
            holder.like_post.setVisibility(View.GONE);
            holder.dislike_post.setVisibility(View.VISIBLE);
            holder.rlike_post.setVisibility(View.GONE);
            holder.rdislike_post.setVisibility(View.VISIBLE);

        }else if(state.equals("no")){
//the button must not be pressed, make it not pressed (default add icon)

//change the icon
            holder.dislike_post.setVisibility(View.GONE);
            holder.like_post.setVisibility(View.VISIBLE);
            holder.rdislike_post.setVisibility(View.GONE);
            holder.rlike_post.setVisibility(View.VISIBLE);
        }







        //handle the like part
        holder.like_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String totalLikes = holder.plikes.getText().toString();
                idPost = holder.pId.getText().toString();
                ;
                int finalLikes = Integer.parseInt(totalLikes);
                int addings = 1 + finalLikes;
                sendLikes = String.valueOf(addings);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.UPDATE_LIKES_POST,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.i("tagconvertstr", "[" + response + "]");
                                    JSONObject object = new JSONObject(response);
                                    String success = object.getString("success");
                                    if (success.equals("1")) {
                                        Toast.makeText(context, "Confirmation successful", Toast.LENGTH_LONG).show();
                                        String yt = "like";
                                        idPost = holder.pId.getText().toString();
//                                            sessionLikes.createSession(yt, idPost);
//                                            Toast.makeText(context, "post id " + postIDD, Toast.LENGTH_LONG).show();
//
//                                            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//                                            preferences.edit().putBoolean("button_1_pressed",true).apply();

                                        //save to preferences
                                        SharedPreferences.Editor editor = context.getSharedPreferences("Button", MODE_PRIVATE).edit();
                                        editor.putString(String.valueOf(position)+ "pressed", "yes");
                                        editor.apply();

                                        Toast.makeText(context, "", Toast.LENGTH_SHORT).show();

                                        holder.like_post.setVisibility(View.GONE);
                                        holder.dislike_post.setVisibility(View.VISIBLE);
                                        String totalLikess = holder.plikes.getText().toString();
                                        int finalLikess = Integer.parseInt(totalLikess);
                                        int adding = finalLikess + 1;
                                        String sendLikess = String.valueOf(adding);
                                        holder.plikes.setText(sendLikess);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "hkjjk" + e.toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Appointment not successful," + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();

                        params.put("userid", getId);
                        params.put("likess", sendLikes);
                        params.put("postid", idPost);


                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);

//                                }


            }
        });


        holder.dislike_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String totalLikes = holder.plikes.getText().toString();
                idPost = holder.pId.getText().toString();
                int finalLikes = Integer.parseInt(totalLikes);
                if (finalLikes == 0) {
                    Toast.makeText(context, "Likes are 0", Toast.LENGTH_SHORT).show();
                } else {
                    int addings = finalLikes - 1;
                    final String sendDisLikes = String.valueOf(addings);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.UPDATE_DISLIKES_POST,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.i("tagconvertstr", "[" + response + "]");
                                        JSONObject object = new JSONObject(response);
                                        String success = object.getString("success");
                                        if (success.equals("1")) {
                                            Toast.makeText(context, "Confirmation successful", Toast.LENGTH_LONG).show();
                                            String yt = "dislike";
                                            idPost = holder.pId.getText().toString();
//                                            sessionLikes.createSession(yt, idPost);

//                                            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//                                            preferences.edit().putBoolean("button_2_pressed",true).apply();

                                            SharedPreferences.Editor editor = context.getSharedPreferences("Button", MODE_PRIVATE).edit();
                                            editor.putString(String.valueOf(position)+ "pressed", "no");
                                            editor.apply();

                                            holder.dislike_post.setVisibility(View.GONE);
                                            holder.like_post.setVisibility(View.VISIBLE);
                                            String totalLikess = holder.plikes.getText().toString();
                                            int finalLikess = Integer.parseInt(totalLikess);
                                            int adding = finalLikess - 1;
                                            String sendLikess = String.valueOf(adding);
                                            holder.plikes.setText(sendLikess);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(context, "hkjjk" + e.toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Appointment not successful," + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();

                            params.put("userid", getId);
                            params.put("likess", sendDisLikes);
                            params.put("postid", idPost);


                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(stringRequest);

                }


            }
        });


        holder.rlike_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String totalLikes = holder.rplikes.getText().toString();
                idPost = holder.rpId.getText().toString();
                ;
                int finalLikes = Integer.parseInt(totalLikes);
                int addings = 1 + finalLikes;
                sendLikes = String.valueOf(addings);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.UPDATE_LIKES_POST,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.i("tagconvertstr", "[" + response + "]");
                                    JSONObject object = new JSONObject(response);
                                    String success = object.getString("success");
                                    if (success.equals("1")) {
                                        Toast.makeText(context, "Confirmation successful", Toast.LENGTH_LONG).show();
                                        String yt = "like";
                                        idPost = holder.rpId.getText().toString();
//                                        sessionLikes.createSession(yt, idPost);
//                                            Toast.makeText(context, "post id " + postIDD, Toast.LENGTH_LONG).show();
//
//                                            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//                                            preferences.edit().putBoolean("button_1_pressed",true).apply();

                                        //save to preferences
                                        SharedPreferences.Editor editor = context.getSharedPreferences("Button", MODE_PRIVATE).edit();
                                        editor.putString(String.valueOf(position)+ "pressed", "yes");
                                        editor.apply();

                                        holder.rlike_post.setVisibility(View.GONE);
                                        holder.rdislike_post.setVisibility(View.VISIBLE);
                                        String totalLikess = holder.rplikes.getText().toString();
                                        int finalLikess = Integer.parseInt(totalLikess);
                                        int adding = finalLikess + 1;
                                        String sendLikess = String.valueOf(adding);
                                        holder.rplikes.setText(sendLikess);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "hkjjk" + e.toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Appointment not successful," + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();

                        params.put("userid", getId);
                        params.put("likess", sendLikes);
                        params.put("postid", idPost);


                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);

//                                }


            }
        });


        holder.rdislike_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String totalLikes = holder.rplikes.getText().toString();
                idPost = holder.rpId.getText().toString();
                int finalLikes = Integer.parseInt(totalLikes);
                if (finalLikes == 0) {
                    Toast.makeText(context, "Likes are 0", Toast.LENGTH_SHORT).show();
                } else {
                    int addings = finalLikes - 1;
                    final String sendDisLikes = String.valueOf(addings);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.UPDATE_DISLIKES_POST,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.i("tagconvertstr", "[" + response + "]");
                                        JSONObject object = new JSONObject(response);
                                        String success = object.getString("success");
                                        if (success.equals("1")) {
                                            Toast.makeText(context, "Confirmation successful", Toast.LENGTH_LONG).show();
                                            String yt = "dislike";
                                            idPost = holder.rpId.getText().toString();
//                                            sessionLikes.createSession(yt, idPost);

//                                            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//                                            preferences.edit().putBoolean("button_2_pressed",true).apply();


                                            SharedPreferences.Editor editor = context.getSharedPreferences("Button", MODE_PRIVATE).edit();
                                            editor.putString(String.valueOf(position)+ "pressed", "no");
                                            editor.apply();

                                            holder.rdislike_post.setVisibility(View.GONE);
                                            holder.rlike_post.setVisibility(View.VISIBLE);
                                            String totalLikess = holder.rplikes.getText().toString();
                                            int finalLikess = Integer.parseInt(totalLikess);
                                            int adding = finalLikess - 1;
                                            String sendLikess = String.valueOf(adding);
                                            holder.rplikes.setText(sendLikess);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(context, "hkjjk" + e.toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Appointment not successful," + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();

                            params.put("userid", getId);
                            params.put("likess", sendDisLikes);
                            params.put("postid", idPost);


                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(stringRequest);

                }


            }
        });





        /*handle the counting the total no of comments*/
        holder.loadTotalComments();


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView pName, pId, pTime, pmessage, plikes, pcomments, rpTime, rpName, rpmessage,
                rplikes, rpcomments, rpId,rno_comments,no_comments;
        ImageView pimage, post_image, rpimage, rpost_image, rsendButton, sendButton;
        ImageButton like_post, dislike_post, rlike_post, rdislike_post;
        LinearLayout liner_likessss, rlinear_comments, linear_comments;
        View view_owner_post, vei_white, vei_blue;
        RelativeLayout re_owner,re_hide_comments,rrre_hide_comments;
        CardView card_postss, card_postss_receive;
        RecyclerView recycle_post_comments, rrecycle_post_comments;
        EditText rmessageArea, messageArea;
        List<CommentsModel> mDatas;
        CommentsAdapter adapters;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            sessionManager = new SessionManager(context);
//        sessionManager.checkLogin();
            HashMap<String, String> user = sessionManager.getUserDetail();
            getId = user.get(SessionManager.ID);
            contact = user.get(SessionManager.USERNAME);
            imagess = user.get(SessionManager.IMAGE);

            mDatas = new ArrayList<>();

            pName = itemView.findViewById(R.id.pName);
            pId = itemView.findViewById(R.id.pId);
            pTime = itemView.findViewById(R.id.pTime);
            like_post = itemView.findViewById(R.id.like_post);
            pmessage = itemView.findViewById(R.id.pmessage);
            plikes = itemView.findViewById(R.id.plikes);
            pcomments = itemView.findViewById(R.id.pcomments);
            pimage = itemView.findViewById(R.id.pimage);
            post_image = itemView.findViewById(R.id.post_image);
            dislike_post = itemView.findViewById(R.id.dislike_post);
            liner_likessss = itemView.findViewById(R.id.liner_likessss);
            view_owner_post = itemView.findViewById(R.id.view_owner_post);
            re_owner = itemView.findViewById(R.id.re_owner);
            card_postss = itemView.findViewById(R.id.card_postss);
            vei_white = itemView.findViewById(R.id.vei_white);
            vei_blue = itemView.findViewById(R.id.vei_blue);
            re_hide_comments = itemView.findViewById(R.id.re_hide_comments);
            rrre_hide_comments = itemView.findViewById(R.id.rrre_hide_comments);


/*posts part*/
            rmessageArea = itemView.findViewById(R.id.rmessageArea);
            messageArea = itemView.findViewById(R.id.messageArea);
            recycle_post_comments = itemView.findViewById(R.id.recycle_post_comments);
            rrecycle_post_comments = itemView.findViewById(R.id.rrecycle_post_comments);
            rlinear_comments = itemView.findViewById(R.id.rlinear_comments);
            linear_comments = itemView.findViewById(R.id.linear_comments);
            sendButton = itemView.findViewById(R.id.sendButton);
            rsendButton = itemView.findViewById(R.id.rsendButton);
            no_comments = itemView.findViewById(R.id.no_comments);
            rno_comments = itemView.findViewById(R.id.rno_comments);




            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setStackFromEnd(true);
            recycle_post_comments.setLayoutManager(layoutManager);
            recycle_post_comments.setAdapter(adapters);

            adapters = new CommentsAdapter(context, mDatas);
            LinearLayoutManager layoutManagers = new LinearLayoutManager(context);
            layoutManager.setStackFromEnd(true);
            rrecycle_post_comments.setLayoutManager(layoutManagers);
            rrecycle_post_comments.setAdapter(adapters);

            adapters = new CommentsAdapter(context, mDatas);



            rpName = itemView.findViewById(R.id.rpName);
            rpId = itemView.findViewById(R.id.rpId);
            rpTime = itemView.findViewById(R.id.rpTime);
            rlike_post = itemView.findViewById(R.id.rlike_post);
            rpmessage = itemView.findViewById(R.id.rpmessage);
            rplikes = itemView.findViewById(R.id.rplikes);
            rpcomments = itemView.findViewById(R.id.rpcomments);
            rpimage = itemView.findViewById(R.id.rpimage);
            rpost_image = itemView.findViewById(R.id.rpost_image);
            rdislike_post = itemView.findViewById(R.id.rdislike_post);
            card_postss_receive = itemView.findViewById(R.id.card_postss_receive);

//            sessionManager = new SessionManager(context);
//            HashMap<String, String> user = sessionManager.getUserDetail();
//            getId = user.get(SessionManager.ID);
            // when sending the appointments i have to know which side is it for
            urls = new Urls();


            sessionLikes = new SessionLikes(context);


//            HashMap<String, String> users = sessionLikes.getUserDetail();
//            statuss = users.get(SessionLikes.STATUS);
//            postIDD = users.get(SessionLikes.PID);
//
//            idPost = pId.getText().toString();

            /*have to find out away to use the poster's id so that i can check against it and then&& postIDD.equals(idPost)&& postIDD.equals(idPost)
//                Toast.makeText(context, "frt", Toast.LENGTH_SHORT).show();
            * run the bi status codes*/



            /*handle the button status*/

//            if (!statuss.isEmpty()) {

//            }





            //handle the like part


            /*handle comments part*/

            pcomments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    linear_comments.setVisibility(View.VISIBLE);
                    getSMS();
                }
            });
            rpcomments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rlinear_comments.setVisibility(View.VISIBLE);
                    getSMSSender();
                }
            });

            re_hide_comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    linear_comments.setVisibility(View.GONE);

                }
            });
            rrre_hide_comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rlinear_comments.setVisibility(View.GONE);
                }
            });

            sendButton.setOnClickListener(view -> {
                String text_sms = messageArea.getText().toString();
                String text_id = pId.getText().toString();
                if (text_sms.isEmpty()) {
                    messageArea.setError("type something for message");
                } else {
                    sendSMS(text_id, text_sms, getId, contact,imagess);
                }
            });

            rsendButton.setOnClickListener(view -> {
                String text_sms = rmessageArea.getText().toString();
                String text_id = pId.getText().toString();
                if (text_sms.isEmpty()) {
                    rmessageArea.setError("type something for message");
                } else {
                    sendSMS(text_id, text_sms,getId,contact,imagess);
                }
            });

        }

        private void loadTotalComments() {

            String crr = pId.getText().toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.GET_NO_COMMENTS,
                    response -> {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONArray tips = new JSONArray(response);
                            if (tips.length() == 0) {

                                pcomments.setText("0");
                                rpcomments.setText("0");

                            } else {
                                for (int i = 0; i < tips.length(); i++) {
                                    JSONObject inputsObjects = tips.getJSONObject(i);

                                    String total = inputsObjects.getString("total");

                                    pcomments.setText(total);
                                    rpcomments.setText(total);

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
                    params.put("post_id", crr);
                    return params;

                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        }

        /*load posts*/
        private void sendSMS(String text_id, String text_sms, String getId, String contact,String imagess) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.SEND_COMMENT_POST,
                    response -> {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONObject object = new JSONObject(response);
                            String success = object.getString("success");
                            if (success.equals("1")) {
                                rno_comments.setVisibility(View.GONE);
                                rmessageArea.setText("");
//                                Clear();
                                getSMS();

                            } else if (success.equals("0")) {
                                Toast.makeText(context, "Comment Not sent", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Something went wrong, please try again " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }, error -> {
                Toast.makeText(context, "Something went wrong, please check your connection and try again " + error.toString(), Toast.LENGTH_SHORT).show();
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("postid", text_id);
                    params.put("usernames", contact);
                    params.put("comments", text_sms);
                    params.put("userid", PostAdapter.this.getId);
                    params.put("image", imagess);
                    return params;

                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        }

        //gets the comments of the receiver's posts
        private void getSMS() {

            final String se = pId.getText().toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.GET_COMMENT_POST,
                    response -> {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONArray sms = new JSONArray(response);
                            if (sms.length() == 0) {
                                no_comments.setVisibility(View.VISIBLE);

                            } else {
                                for (int i = 0; i < sms.length(); i++) {
                                    JSONObject smsObjects = sms.getJSONObject(i);

                                    String id = smsObjects.getString("id");
                                    String comment = smsObjects.getString("comments");
                                    String date = smsObjects.getString("time");
                                    String from = smsObjects.getString("sender");
                                    String fromimage = smsObjects.getString("senderimage");

                                    no_comments.setVisibility(View.GONE);
                                    CommentsModel commentsModel = new CommentsModel(id,
                                            comment,
                                            date,
                                            from,fromimage);////add comment sender6
                                    mDatas.add(commentsModel);
                                }
                            }

                            adapters = new CommentsAdapter(context, mDatas);
                            recycle_post_comments.setAdapter(adapters);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            no_comments.setVisibility(View.INVISIBLE);
                            Toast.makeText(context, "something went wrong, please try again" + e.toString(), Toast.LENGTH_LONG).show();

                        }

                    }, error -> {
                no_comments.setVisibility(View.INVISIBLE);
                Toast.makeText(context, "could not load messages, please check your internet connection and try again" +error.toString(), Toast.LENGTH_LONG).show();
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("postid", se);
                    params.put("userid", getId);
                    return params;

                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);

        }

        //gets the comments of the sender's posts
        private void getSMSSender() {

            final String se = pId.getText().toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.GET_COMMENT_POST,
                    response -> {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONArray sms = new JSONArray(response);
                            if (sms.length() == 0) {
                                rno_comments.setVisibility(View.VISIBLE);

                            } else {
                                for (int i = 0; i < sms.length(); i++) {
                                    JSONObject smsObjects = sms.getJSONObject(i);

                                    String id = smsObjects.getString("id");
                                    String comment = smsObjects.getString("comments");
                                    String date = smsObjects.getString("time");
                                    String from = smsObjects.getString("sender");
                                    String fromimage = smsObjects.getString("senderimage");

                                    rno_comments.setVisibility(View.GONE);
                                    CommentsModel commentsModel = new CommentsModel(id,
                                            comment,
                                            date,
                                            from,fromimage);////add comment sender6
                                    mDatas.add(commentsModel);
                                }
                            }

                            adapters = new CommentsAdapter(context, mDatas);
                            rrecycle_post_comments.setAdapter(adapters);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            rno_comments.setVisibility(View.GONE);
                            Toast.makeText(context, "something went wrong, please try again" + e.toString(), Toast.LENGTH_LONG).show();

                        }

                    }, error -> {
                no_comments.setVisibility(View.INVISIBLE);
                Toast.makeText(context, "could not load messages, please check your internet connection and try again" +error.toString(), Toast.LENGTH_LONG).show();
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("postid", se);
                    params.put("userid", getId);
                    return params;

                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);

        }

        /*clears the recyclerview once a message is sent*/
        public void Clear() {
            mDatas.clear();
            adapters.notifyDataSetChanged();
        }
    }


}





