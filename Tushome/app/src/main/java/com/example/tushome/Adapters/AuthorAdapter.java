package com.example.tushome.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.tushome.Authors.AuthorActivity;
import com.example.tushome.Authors.AuthorBookDetails;
import com.example.tushome.Authors.AuthorsBooks;
import com.example.tushome.Models.AuthorModel;
import com.example.tushome.R;
import com.example.tushome.Registrations.LoginSecond;
import com.example.tushome.Urls.SessionManager;
import com.example.tushome.Urls.Urls;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.AuthorViewHolder> {
    Context context;
    List<AuthorModel> mData;
    Urls urls;
    SessionManager sessionManager;
    String getID;
    Dialog ss_card, ee_card;

    public AuthorAdapter(Context context, List<AuthorModel> mData) {
        this.context = context;
        this.mData = mData;
        urls = new Urls();
        ss_card = new Dialog(context);
        ee_card = new Dialog(context);
        sessionManager = new SessionManager(context);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(SessionManager.ID);
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_author_book, null, false);
        return new AuthorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, final int position) {

        AuthorModel authorModel = mData.get(position);
        holder.title.setText(mData.get(position).getTitle());
        holder.preview.setText(mData.get(position).getPreview());
        holder.author.setText(mData.get(position).getAuthor());
        holder.id.setText(mData.get(position).getId());
        holder.status.setText(mData.get(position).getStatus());
        holder.suspended.setText(mData.get(position).getSuspend());
//        suspended
        holder.balance_card.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        String imageUrl = urls.https + "cover_images/" + authorModel.getImage();
        try {

            Glide.with(context)
                    .load(imageUrl)
                    .into(holder.product_image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String ss = holder.status.getText().toString();
        String sp = holder.suspended.getText().toString();


        if (ss.equals("pending")) {
            holder.confirm_card.setVisibility(View.VISIBLE);
            holder.confirm_tx.setText("Pending");
            holder.status_relative.setBackgroundColor(Color.parseColor("#D30000"));
            holder.confirm_card.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        } else if (ss.equals("confirmed") && sp.equals("activated")) {
            holder.confirm_card.setVisibility(View.VISIBLE);
            holder.status_relative.setBackgroundColor(Color.parseColor("#657BF2"));
            holder.confirm_card.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        } else if (ss.equals("denied")) {
            holder.denied_card.setVisibility(View.VISIBLE);
            holder.status_relative.setBackgroundColor(Color.parseColor("#D30000"));
            holder.denied_card.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        } else if (ss.equals("confirmed") && sp.equals("deactivated")) {
            holder.suspend_card.setVisibility(View.VISIBLE);
            holder.status_relative.setBackgroundColor(Color.parseColor("#04960B"));
            holder.suspend_card.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        }


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class AuthorViewHolder extends RecyclerView.ViewHolder {

        TextView title, preview, author, id,denied_tx,status,suspended;
        CardView balance_card,denied_card,confirm_card,suspend_card;
        ImageView product_image;
        TextView edit_book, delete_book,suspend_tx,confirm_tx;
        Dialog successs;
RelativeLayout status_relative;

        public AuthorViewHolder(@NonNull View itemView) {
            super(itemView);

            successs = new Dialog(context);

            title = itemView.findViewById(R.id.title);
            preview = itemView.findViewById(R.id.preview);
            author = itemView.findViewById(R.id.author);
            balance_card = itemView.findViewById(R.id.balance_card);
            id = itemView.findViewById(R.id.id);
            product_image = itemView.findViewById(R.id.product_image);
            edit_book = itemView.findViewById(R.id.edit_book);
            delete_book = itemView.findViewById(R.id.delete_book);
            denied_card = itemView.findViewById(R.id.denied_card);
            denied_tx = itemView.findViewById(R.id.denied_tx);
            status_relative = itemView.findViewById(R.id.status_relative);
            confirm_card = itemView.findViewById(R.id.confirm_card);
            suspend_card = itemView.findViewById(R.id.suspend_card);
            suspend_tx = itemView.findViewById(R.id.suspend_tx);
            confirm_tx = itemView.findViewById(R.id.confirm_tx);
            status = itemView.findViewById(R.id.status);
            suspended = itemView.findViewById(R.id.suspended);

            edit_book.setOnClickListener(view -> {
                Intent request = new Intent(context, AuthorBookDetails.class);
                request.putExtra("title", mData.get(getAdapterPosition()).getTitle());
                request.putExtra("preview", mData.get(getAdapterPosition()).getPreview());
                request.putExtra("author", mData.get(getAdapterPosition()).getAuthor());
                request.putExtra("id", mData.get(getAdapterPosition()).getId());
                request.putExtra("status", mData.get(getAdapterPosition()).getStatus());
                request.putExtra("suspend", mData.get(getAdapterPosition()).getSuspend());
                request.putExtra("reason", mData.get(getAdapterPosition()).getReason());
                request.putExtra("image_url", urls.https + "cover_images/" + mData.get(getAdapterPosition()).getImage());
                context.startActivity(request);
            });
            /*Use this once ....*/


            //            deleting options
            delete_book.setOnClickListener(v -> {
                final String bid = id.getText().toString();
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setTitle("Delete Book")
                        .setMessage("Are you sure you want to Delete this book from your collection permanently?")
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_warning)
                        .setPositiveButton("YES", (dialog, which) -> {
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.DELETE_BOOK_URL,
                                    response -> {
                                        try {
                                            Log.i("tagconvertstr", "[" + response + "]");
                                            JSONObject object = new JSONObject(response);
                                            String success = object.getString("success");
                                            if (success.equals("1")) {
                                                Log.i("tagconvertstr", "[" + response + "]");

                                       /*         successs = new Dialog(context);
                                                successs.setContentView(R.layout.success_delete);
                                                CardView ok_success = successs.findViewById(R.id.ok);

                                                ok_success.setOnClickListener(v1 -> {
                                                    Intent intent = new Intent(context, AuthorsBooks.class);
                                                    context.startActivity(intent);
                                                    ((Activity)context).finish();
                                                });
                                                successs.setCancelable(false);
                                                successs.setCanceledOnTouchOutside(false);
                                                Objects.requireNonNull(successs.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                successs.show();*/


                                                ss_card = new Dialog(context);
                                                ss_card.setContentView(R.layout.right);
                                                CardView dismiss = ss_card.findViewById(R.id.ss_card);
                                                TextView success_type = ss_card.findViewById(R.id.tx_success);

                                                success_type.setText("Book was deleted successfully");
                                                dismiss.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(context, AuthorsBooks.class);
                                                        context.startActivity(intent);
                                                        ((Activity) context).finish();
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
                                            Toast.makeText(context, "Product not deleted, please try again " + e.getMessage(), Toast.LENGTH_LONG).show();
                                            ee_card = new Dialog(context);
                                            ee_card.setContentView(R.layout.error);
                                            CardView dismiss = ee_card.findViewById(R.id.ee_card);
                                            TextView success_type = ee_card.findViewById(R.id.tx_error);

                                            success_type.setText("Book not deleted, please try again");
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
                                    }, error -> {
//            Toast.makeText(BookDetails.this, "Order not sent successful, please check your network and try again", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                ee_card = new Dialog(context);
                                ee_card.setContentView(R.layout.error);
                                CardView dismiss = ee_card.findViewById(R.id.ee_card);
                                TextView success_type = ee_card.findViewById(R.id.tx_error);

                                success_type.setText("Book not deleted, please check your network and try again");
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

                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("authorid", getID);
                                    params.put("bookid", bid);
                                    return params;

                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(context);
                            requestQueue.add(stringRequest);
                        })
                        .setNegativeButton("NO", (dialog, which) -> dialog.dismiss());
                //Creating dialog box
                android.app.AlertDialog dialog = builder.create();
                dialog.show();
            });


        }
    }
}
