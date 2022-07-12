package com.example.tushome.Authors;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.tushome.R;
import com.example.tushome.Urls.SessionManager;
import com.example.tushome.Urls.Urls;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AuthorBookDetails extends AppCompatActivity {

    MaterialCardView add_product, upload_image,read_card_options;
    ImageView image_preview;
    EditText book_title, book_summary, book_author_name;
    Urls urls;
    SessionManager sessionManager;
    String getID, getName;
    TextView book_id,tx_pending,tx_suspended,tx_denied,status_dets,suspend_dets,reason_action_book;
    String imgurl;
    Dialog ss_card, ee_card;
    private Bitmap bitmap;
    LinearLayout reason_linear,linear_cancel_options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_book_details);


        ss_card = new Dialog(this);
        ee_card = new Dialog(this);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(SessionManager.ID);
        getName = user.get(SessionManager.FULLNAME);
        urls = new Urls();

        book_title = findViewById(R.id.book_title);
        book_summary = findViewById(R.id.book_summary);
        image_preview = findViewById(R.id.image_preview);
        add_product = findViewById(R.id.add_product);
        upload_image = findViewById(R.id.upload_image);
        book_id = findViewById(R.id.book_id);
        book_author_name = findViewById(R.id.book_author_name);
        tx_pending = findViewById(R.id.tx_pending);
        tx_suspended = findViewById(R.id.tx_suspended);
        tx_denied = findViewById(R.id.tx_denied);
        status_dets = findViewById(R.id.status_dets);
        suspend_dets = findViewById(R.id.suspend_dets);
        read_card_options = findViewById(R.id.read_card_options);
        linear_cancel_options = findViewById(R.id.linear_cancel_options);
        reason_linear = findViewById(R.id.reason_linear);
        reason_action_book = findViewById(R.id.reason_action_book);


        book_summary.setText(getIntent().getStringExtra("preview"));
        book_title.setText(getIntent().getStringExtra("title"));
        book_author_name.setText(getIntent().getStringExtra("author"));
        book_id.setText(getIntent().getStringExtra("id"));
        suspend_dets.setText(getIntent().getStringExtra("suspend"));
        status_dets.setText(getIntent().getStringExtra("status"));
        reason_action_book.setText(getIntent().getStringExtra("reason"));


        imgurl = getIntent().getStringExtra("image_url");

        Glide.with(this)
                .load(imgurl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .fallback(R.drawable.ic_launcher_background)
                .into(image_preview);


        upload_image.setOnClickListener(v -> chooseFile());

        String ss = status_dets.getText().toString();
        String sp = suspend_dets.getText().toString();

        if (ss.equals("pending")) {
            linear_cancel_options.setVisibility(View.VISIBLE);
            tx_pending.setVisibility(View.VISIBLE);

        }  else if (ss.equals("denied")) {
            linear_cancel_options.setVisibility(View.VISIBLE);
            tx_denied.setVisibility(View.VISIBLE);

        } else if (ss.equals("confirmed") && sp.equals("deactivated")) {
            linear_cancel_options.setVisibility(View.VISIBLE);
            tx_suspended.setVisibility(View.VISIBLE);
        }

        read_card_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reason_linear.setVisibility(View.VISIBLE);
            }
        });
//

    }


    /*
     * HANDLE UPLOAD IMAGES FROM GALLERY
     * */
    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select picture"), 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image_preview.setVisibility(View.VISIBLE);
                image_preview.setImageBitmap(bitmap);
                getStringImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }


    public void updateOrder(View view) {

        /*submitting part*/

        String bt = book_title.getText().toString().trim();
        String bs = book_summary.getText().toString().trim();
        String ba = book_author_name.getText().toString().trim();
        String ii = bitmap.toString();

        if (getStringImage(bitmap).equals(imgurl)) {
            Toast.makeText(this, "Please choice a cover book image", Toast.LENGTH_SHORT).show();
        } else {

            if (!bt.isEmpty() && !bs.isEmpty() && !ba.isEmpty()) {

                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        AuthorBookDetails.this);
                alertDialog2.setTitle("Confirm before submitting");
                alertDialog2.setMessage("Make sure you double check before uploading your book's cover and summary");
                alertDialog2.setIcon(R.drawable.ic_warning);
                alertDialog2.setPositiveButton("YES",
                        (dialog, which) ->
                                sendProduct(getStringImage(bitmap)));
                alertDialog2.setNegativeButton("NO",
                        (dialog, which) -> dialog.cancel());
                alertDialog2.show();

            } else {
                Toast.makeText(AuthorBookDetails.this, "All fields are required", Toast.LENGTH_SHORT).show();

            }
        }


    }


    private void sendProduct(final String photo) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait ...");
        dialog.show();

        String bt = book_title.getText().toString().trim();
        String bs = book_summary.getText().toString().trim();
        String ba = book_author_name.getText().toString().trim();
        String bookid = book_id.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.UPDATE_BOOK_COVER,

                response -> {
                    Log.i("tagconvertstr", "[" + response + "]");
                    try {

                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        if (success.equals("1")) {
                            dialog.dismiss();
                            ss_card = new Dialog(this);
                            ss_card.setContentView(R.layout.right);
                            CardView dismiss = ss_card.findViewById(R.id.ss_card);
                            TextView success_type = ss_card.findViewById(R.id.tx_success);

                            success_type.setText("Upload was successful");
                            dismiss.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent aa = new Intent(AuthorBookDetails.this, AuthorsBooks.class);
                                    startActivity(aa);
                                    finish();
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
                        dialog.dismiss();
//                        Toast.makeText(AuthorBookDetails.this, "failed to upload your book'cover and summary, please try again " + e.toString(), Toast.LENGTH_SHORT).show();

                        ee_card = new Dialog(this);
                        ee_card.setContentView(R.layout.error);
                        CardView dismiss = ee_card.findViewById(R.id.ee_card);
                        TextView success_type = ee_card.findViewById(R.id.tx_success);

                        success_type.setText("failed to upload your book, please try again ");
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
                },

                error -> {
                    dialog.dismiss();
//                    Toast.makeText(AuthorBookDetails.this, "failed to  upload your book'cover and summary, please check your connection and try again" +
//                            error.toString(), Toast.LENGTH_SHORT).show();

                    ee_card = new Dialog(this);
                    ee_card.setContentView(R.layout.error);
                    CardView dismiss = ee_card.findViewById(R.id.ee_card);
                    TextView success_type = ee_card.findViewById(R.id.tx_success);

                    success_type.setText("failed to upload your book, please check your connection and try again");
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("authorid", getID);
                params.put("author", ba);
                params.put("btitle", bt);
                params.put("bsummary", bs);
                params.put("image", photo);
                params.put("bookid", bookid);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap = ((BitmapDrawable) image_preview.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }

    public void back(View view) {
        startActivity(new Intent(AuthorBookDetails.this, AuthorActivity.class));
        finish();
    }

}