package com.example.tushome.MessageOptions;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
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
import com.example.tushome.Adapters.PostAdapter;
import com.example.tushome.Authors.AuthorActivity;
import com.example.tushome.Models.PostModel;
import com.example.tushome.R;
import com.example.tushome.Reader.MainActivity;
import com.example.tushome.Urls.SessionManager;
import com.example.tushome.Urls.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupPost extends AppCompatActivity {

    RecyclerView recyclerView, frecyclerView;
    List<PostModel> mData;
    PostAdapter adapter;
    TextView messageText;
    SessionManager sessionManager;
    String getId;
    String usernames;
    String userimage;
    TextView error_message, sName, sID;
    ProgressBar progressBar;
    ImageView sendBtn, image_nochat, sImage, preview_post_image;
    TextView fphone, you, patient, no_chat,sending_chat,notsending_chat;
    Urls urls;
    private Bitmap bitmap;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

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
        preview_post_image = findViewById(R.id.preview_post_image);
        sending_chat = findViewById(R.id.sending_chat);
        notsending_chat = findViewById(R.id.notsending_chat);

//
//        sID.setText(getIntent().getStringExtra("id"));
//        sName.setText(getIntent().getStringExtra("username"));
//
//
//        String imgurl = getIntent().getStringExtra("image_url");
//
//        Glide.with(this)
//                .load(imgurl)
//                .placeholder(R.drawable.ic_launcher_background)
//                .error(R.drawable.ic_launcher_background)
//                .fallback(R.drawable.ic_launcher_background)
//                .into(sImage);


        Glide.with(this)
        .load(R.drawable.nochats)
        .into(image_nochat);


        sendBtn.setOnClickListener(view -> {
            String text_sms = messageText.getText().toString();
            if (text_sms.isEmpty() && preview_post_image.getDrawable() == null) {
//                messageText.setError("type something for message");
                Toast.makeText(this, "Post is required", Toast.LENGTH_SHORT).show();
            } else if (!text_sms.isEmpty() && preview_post_image.getDrawable() == null) {
                sendSMSNoImage();
            } else if (preview_post_image.getDrawable() != null) {
                sendSMS(getStringImage(bitmap));
            }

        });


        //handle recyclerview
        recyclerView = findViewById(R.id.recycle_group_post);
        mData = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recycle_group_post);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter = new PostAdapter(getApplicationContext(), mData);


        ImageView back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*check to see who is chatting author or reader*/
//                String cc = patient.getText().toString();
//                if (cc.contains("author")) {
//                    startActivity(new Intent(GroupPost.this, AuthorActivity.class));
//                    finish();
//                } else {
//
//                }

                startActivity(new Intent(GroupPost.this, PostUsers.class));
                finish();
            }
        });

        /*refresh the get posts after every second*/
//        final Handler handler = new Handler();
//        Runnable refresh = new Runnable() {
//            @Override
//            public void run() {
        getSMS();
//                handler.postDelayed(this,100);
//            }
//        };
//        handler.postDelayed(refresh,100);


    }


    public void openGallery(View view) {
        chooseFile();
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a profile picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                preview_post_image.setVisibility(View.VISIBLE);
                preview_post_image.setImageBitmap(bitmap);
                getStringImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }


    private void sendSMS(String postImage) {
//        progressBar.setVisibility(View.VISIBLE);
        sending_chat.setVisibility(View.VISIBLE);
        final String scontact = usernames;
        final String message = messageText.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.SEND_GROUP_POST,
            response -> {
                try {
                    Log.i("tagconvertstr", "[" + response + "]");
                    JSONObject object = new JSONObject(response);
                    String success = object.getString("success");
                    if (success.equals("1")) {
                        sending_chat.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "sent", Toast.LENGTH_SHORT).show();
                        messageText.setText("");
                        image_nochat.setVisibility(View.GONE);
                        preview_post_image.setImageResource(0);
                        preview_post_image.setVisibility(View.GONE);
                        bitmap.recycle();
                        getSMS();
//                            startActivity(new Intent(Chat.this, Chat.class));
//                            finish();
//                            fgetSMS();
                        Clear();

                    } else if (success.equals("0")) {
                        sending_chat.setVisibility(View.GONE);
                        notsending_chat.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Post not sent", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    sending_chat.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Something went wrong, please try again " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }, error -> {
            sending_chat.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Something went wrong, please check your connection and try again " + error.toString(), Toast.LENGTH_SHORT).show();
            }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("send_name", scontact);
                params.put("userid", getId);
                params.put("message", message);
                params.put("userimage", userimage);
                params.put("image", postImage);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void sendSMSNoImage() {
//        progressBar.setVisibility(View.VISIBLE);
        sending_chat.setVisibility(View.VISIBLE);
        final String scontact = usernames;
        final String message = messageText.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.SEND_GROUP_POST,
            response -> {
                try {
                    Log.i("tagconvertstr", "[" + response + "]");
                    JSONObject object = new JSONObject(response);
                    String success = object.getString("success");
                    if (success.equals("1")) {
                        sending_chat.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "sent", Toast.LENGTH_SHORT).show();
                        messageText.setText("");
                        image_nochat.setVisibility(View.GONE);
                        Clear();
                        getSMS();


                    } else if (success.equals("0")) {
                        sending_chat.setVisibility(View.GONE);
                        notsending_chat.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Post not sent", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    sending_chat.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Something went wrong, please try again " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }, error -> {
            sending_chat.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Something went wrong, please check your connection and try again " + error.toString(), Toast.LENGTH_SHORT).show();
            }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                String em = "null";
                params.put("send_name", scontact);
                params.put("userid", getId);
                params.put("message", message);
                params.put("userimage", userimage);
                params.put("image", em);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getSMS() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urls.GET_GROUP_POST,
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

//                                postID,userimage,username,time,postimage,message;

                            String message = smsObjects.getString("message");
                            String time = smsObjects.getString("time");
                            String userimage = smsObjects.getString("userimage");
                            String sender = smsObjects.getString("sender");
                            String reciever_name = smsObjects.getString("reciever_name");
                            String reciever_id = smsObjects.getString("reciever_id");
                            String posterImage = smsObjects.getString("image");
                            String likess = smsObjects.getString("likess");
                            String postid = smsObjects.getString("postid");

                            image_nochat.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            error_message.setVisibility(View.INVISIBLE);
                            PostModel postModel = new PostModel(postid,
                                userimage,
                                sender,
                                time,
                                posterImage,
                                message,likess,"");
                            mData.add(postModel);
                        }
                    }

                    adapter = new PostAdapter(getApplicationContext(), mData);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    error_message.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "something went wrong, please try again" + e.toString(), Toast.LENGTH_LONG).show();

                }

            }, error -> {
                progressBar.setVisibility(View.GONE);
                error_message.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "could not load posts, please check your internet connection and try again", Toast.LENGTH_LONG).show();
            });
        Volley.newRequestQueue(GroupPost.this).add(stringRequest);

    }

    /*clears the recyclerview once a message is sent*/
    public void Clear() {
        mData.clear();
        adapter.notifyDataSetChanged();
    }

    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }


}