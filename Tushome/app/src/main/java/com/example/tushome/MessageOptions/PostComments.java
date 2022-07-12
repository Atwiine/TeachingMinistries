package com.example.tushome.MessageOptions;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.bumptech.glide.Glide;
import com.example.tushome.Adapters.ChatAdapter;
import com.example.tushome.Adapters.CommentsAdapter;
import com.example.tushome.Adapters.PostAdapter;
import com.example.tushome.Adapters.fChatAdapter;
import com.example.tushome.Authors.AuthorActivity;
import com.example.tushome.Models.ChatModel;
import com.example.tushome.Models.CommentsModel;
import com.example.tushome.Models.PostModel;
import com.example.tushome.Models.fChatModel;
import com.example.tushome.R;
import com.example.tushome.Reader.MainActivity;
import com.example.tushome.Urls.SessionManager;
import com.example.tushome.Urls.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostComments extends AppCompatActivity {

    RecyclerView recyclerView, frecyclerView;
    List<CommentsModel> mData;
    CommentsAdapter adapters;
    TextView messageText;
    SessionManager sessionManager;
    String getId;
    String usernames;
    String userimage,typepost,name;
    TextView error_message,sName,sID;
    ProgressBar progressBar;
    ImageView sendBtn,image_nochat,sImage;
    TextView fphone, you, patient, no_chat;
    Urls urls;
    EditText messageArea;
    String postid = "";


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comments);

        urls = new Urls();
        //handle session manager
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        usernames = user.get(SessionManager.USERNAME);
        userimage = user.get(SessionManager.IMAGE);


        fphone = findViewById(R.id.farmer_phone);
        you = findViewById(R.id.you);
        patient = findViewById(R.id.farmer);
        no_chat = findViewById(R.id.no_chat);
        progressBar = findViewById(R.id.progressBar);
        error_message = findViewById(R.id.error_message);
        messageText = findViewById(R.id.messageArea);
        sendBtn = findViewById(R.id.sendButton);
        image_nochat = findViewById(R.id.image_nochat);
        sImage = findViewById(R.id.sImage);
        sName = findViewById(R.id.sName);
        sID = findViewById(R.id.sID);
//        messageArea = findViewById(R.id.messageArea);




        sID.setText(getIntent().getStringExtra("id"));
        sName.setText(getIntent().getStringExtra("name"));


        String imgurl = getIntent().getStringExtra("image_url");
         postid = getIntent().getStringExtra("postid");
        typepost = getIntent().getStringExtra("typepost");
        name = getIntent().getStringExtra("name");

        Glide.with(this)
                .load(imgurl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .fallback(R.drawable.ic_launcher_background)
                .into(sImage);



        Glide.with(this)
                .load(R.drawable.nochats)
                .into(image_nochat);


        sendBtn.setOnClickListener(view -> {
            String text_sms = messageText.getText().toString();
            if (text_sms.isEmpty()) {
                messageText.setError("type something for message");
            } else {
                sendSMS();
            }
        });


        //handle recyclerview
        recyclerView = findViewById(R.id.recycle_post_comments);

        mData = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recycle_post_comments);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapters);

        adapters = new CommentsAdapter(getApplicationContext(), mData);

        //get intents
        patient.setText(getIntent().getStringExtra("author"));
        fphone.setText(getIntent().getStringExtra("fPhone"));


        ImageView back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*check to see who is chatting author or reader*/
//                String cc = patient.getText().toString();
                if (typepost.equals("author")) {
                    startActivity(new Intent(PostComments.this, AuthorActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(PostComments.this, MainActivity.class));
                    finish();
                }
            }
        });

        getSMS();
    }

    private void sendSMS() {
//        progressBar.setVisibility(View.VISIBLE);
        final String scontact = usernames;
        final String message = messageText.getText().toString().trim();
        final String postidss = sID.getText().toString(); /// the guy thats going to receive this message
        final String reciever_name = sName.getText().toString(); /// the guy thats going to receive this message

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.SEND_COMMENT_POST,
                response -> {
                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONObject object = new JSONObject(response);
                        String success = object.getString("success");
                        if (success.equals("1")) {
                            image_nochat.setVisibility(View.GONE);
                            messageText.setText("");
//                            if (typepost.equals("group")){
//                                startActivity(new Intent(PostComments.this, GroupPost.class));
//                                finish();
//                            }else if (typepost.equals("single")){
//                                startActivity(new Intent(PostComments.this, SinglesPost.class));
//                                finish();
//                            }
                            Clear();
                            getSMS();

                        } else if (success.equals("0")) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Comment Not sent", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Something went wrong, please try again " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Something went wrong, please check your connection and try again " + error.toString(), Toast.LENGTH_SHORT).show();
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("postid", postidss);
                params.put("usernames", usernames);
                params.put("comments", message);
                params.put("userid", getId);
               // params.put("reciever_name", reciever_name);
                params.put("image", userimage);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getSMS() {

        final String se = sID.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.GET_COMMENT_POST,
                response -> {
                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONArray sms = new JSONArray(response);
                        if (sms.length() == 0) {
                            progressBar.setVisibility(View.GONE);
                            error_message.setVisibility(View.INVISIBLE);
                            image_nochat.setVisibility(View.VISIBLE);

                        } else {
                            for (int i = 0; i < sms.length(); i++) {
                                JSONObject smsObjects = sms.getJSONObject(i);

                                String id = smsObjects.getString("id");
                                String comment = smsObjects.getString("comments");
                                String date = smsObjects.getString("time");
                                String from = smsObjects.getString("sender");
                                String fromimage = smsObjects.getString("senderimage");

                                image_nochat.setVisibility(View.GONE);
                                error_message.setVisibility(View.GONE);
                                CommentsModel commentsModel = new CommentsModel(id,
                                        comment,
                                        date,
                                        from,fromimage);////add comment sender6
                                mData.add(commentsModel);
                            }
                        }

                        adapters = new CommentsAdapter(getApplicationContext(), mData);
                        recyclerView.setAdapter(adapters);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        error_message.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "something went wrong, please try again" + e.toString(), Toast.LENGTH_LONG).show();

                    }

                }, error -> {
            progressBar.setVisibility(View.GONE);
            error_message.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "could not load messages, please check your internet connection and try again" +error.toString(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("postid", se);
                params.put("userid", getId);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    /*clears the recyclerview once a message is sent*/
    public void Clear() {
        mData.clear();
        adapters.notifyDataSetChanged();
    }
}