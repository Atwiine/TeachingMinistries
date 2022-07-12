package com.example.tushome.MessageOptions;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.tushome.Adapters.fChatAdapter;
import com.example.tushome.Authors.AuthorActivity;
import com.example.tushome.Models.ChatModel;
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

public class Chat extends AppCompatActivity {

    RecyclerView recyclerView, frecyclerView;
    List<ChatModel> mData;
    List<fChatModel> fmData;
    ChatAdapter adapter;
    fChatAdapter fadapter;
    TextView messageText;
    SessionManager sessionManager;
    String getId;
    String usernames;
    String userimage;
    TextView error_message;
    ProgressBar progressBar;
    ImageView sendBtn,image_nochat;
    TextView fphone, you, patient, no_chat,sending_chat,notsending_chat;
    Urls urls;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

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
        sending_chat = findViewById(R.id.sending_chat);
        notsending_chat = findViewById(R.id.notsending_chat);



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
        recyclerView = findViewById(R.id.recycle_messages);

        mData = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recycle_messages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter = new ChatAdapter(getApplicationContext(), mData);

        //get intents
        patient.setText(getIntent().getStringExtra("author"));
        fphone.setText(getIntent().getStringExtra("fPhone"));


        ImageView back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*check to see who is chatting author or reader*/
                String cc = patient.getText().toString();
                if (cc.contains("author")) {
                    startActivity(new Intent(Chat.this, AuthorActivity.class));
                } else {
                    startActivity(new Intent(Chat.this, MainActivity.class));
                }
                finish();
            }
        });

        getSMS();
    }

    private void sendSMS() {
        progressBar.setVisibility(View.VISIBLE);
        sending_chat.setVisibility(View.VISIBLE);
        final String scontact = usernames;
        final String message = messageText.getText().toString().trim();
//        final String rcontact = fphone.getText().toString(); /// the guy thats going to receive this message
        final String rcontact = "hh";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.SEND_MESSAGE,
                response -> {
                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONObject object = new JSONObject(response);
                        String success = object.getString("success");
                        if (success.equals("1")) {
                            sending_chat.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "sent", Toast.LENGTH_SHORT).show();
                            messageText.setText("");
                            getSMS();
//                            startActivity(new Intent(Chat.this, Chat.class));
//                            finish();
//                            fgetSMS();
                            Clear();

                        } else if (success.equals("0")) {
                            sending_chat.setVisibility(View.GONE);
                            notsending_chat.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Message Not sent", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        sending_chat.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "error " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            sending_chat.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "error " + error.toString(), Toast.LENGTH_SHORT).show();
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("scontact", scontact);
                params.put("userid", getId);
                params.put("message", message);
                params.put("rcontact", rcontact);
                params.put("image", userimage);

                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getSMS() {
//        progressBar.setVisibility(View.VISIBLE);
        final String phone = usernames;
        final String farmer_phone = fphone.getText().toString();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, urls.MESSAGES_LIST,
                response -> {
                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONArray sms = new JSONArray(response);
                        if (sms.length() == 0) {
                            progressBar.setVisibility(View.GONE);
                            error_message.setVisibility(View.GONE);
//                            no_chat.setVisibility(View.VISIBLE);
                            image_nochat.setVisibility(View.VISIBLE);

                        } else {
                            for (int i = 0; i < sms.length(); i++) {
                                JSONObject smsObjects = sms.getJSONObject(i);

                                String message = smsObjects.getString("message");
                                String time = smsObjects.getString("time");
                                String image = smsObjects.getString("image");
                                String sender = smsObjects.getString("sender");
                                String receiver = smsObjects.getString("reciever");

                                progressBar.setVisibility(View.GONE);
                                error_message.setVisibility(View.GONE);
                                ChatModel chatModel = new ChatModel(message, time, sender, receiver, image,"");
                                mData.add(chatModel);
                            }
                        }

                        adapter = new ChatAdapter(getApplicationContext(), mData);
                        recyclerView.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        error_message.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "something went wrong, please try again" + e.toString(), Toast.LENGTH_LONG).show();

                    }

                }, error -> {
            progressBar.setVisibility(View.GONE);
            error_message.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "could not load messages, please check your internet connection and try again" , Toast.LENGTH_LONG).show();
        });
        Volley.newRequestQueue(Chat.this).add(stringRequest);

    }

    /*clears the recyclerview once a message is sent*/
    public void Clear() {
        mData.clear();
        adapter.notifyDataSetChanged();
    }
}