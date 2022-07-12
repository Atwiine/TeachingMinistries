package com.example.tushome.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tushome.Authors.OtherBookDetails;
import com.example.tushome.Models.AuthorModel;
import com.example.tushome.R;
import com.example.tushome.Urls.SessionManager;
import com.example.tushome.Urls.Urls;

import java.util.HashMap;
import java.util.List;

public class OthersAdapter extends RecyclerView.Adapter<OthersAdapter.AuthorViewHolder> {
    Context context;
    List<AuthorModel> mData;
    Urls urls;
    SessionManager sessionManager;
    String getID;

    public OthersAdapter(Context context, List<AuthorModel> mData) {
        this.context = context;
        this.mData = mData;
        urls = new Urls();
        sessionManager = new SessionManager(context);
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_other_book, null, false);
        return new AuthorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, final int position) {

        AuthorModel authorModel = mData.get(position);
        holder.title.setText(mData.get(position).getTitle());
        holder.preview.setText(mData.get(position).getPreview());
        holder.author.setText(mData.get(position).getAuthor());
        holder.id.setText(mData.get(position).getId());

        holder.balance_card.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        String imageUrl = urls.https + "cover_images/" + authorModel.getImage();
        try {

            Glide.with(context)
                    .load(imageUrl)
                    .into(holder.product_image);
        } catch (Exception e) {
            e.printStackTrace();
        }


        sessionManager = new SessionManager(context);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(SessionManager.ID);

        String getCardID = holder.id.getText().toString();

        /*check to see if the id on the card is the same as the same id in the session manager then
         * hide the damn card if not show the card*/

        if (getID.matches(getCardID)) {
            holder.balance_card.setVisibility(View.GONE);
        } else {
            holder.balance_card.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class AuthorViewHolder extends RecyclerView.ViewHolder {

        TextView title, preview, author, id;
        CardView balance_card, order;
        ImageView product_image;

        public AuthorViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            preview = itemView.findViewById(R.id.preview);
            author = itemView.findViewById(R.id.author);
            balance_card = itemView.findViewById(R.id.balance_card);
            id = itemView.findViewById(R.id.id);
            product_image = itemView.findViewById(R.id.product_image);
            order = itemView.findViewById(R.id.order);


            balance_card.setOnClickListener(view -> {
                Intent request = new Intent(context, OtherBookDetails.class);
                request.putExtra("title", mData.get(getAdapterPosition()).getTitle());
                request.putExtra("preview", mData.get(getAdapterPosition()).getPreview());
                request.putExtra("author", mData.get(getAdapterPosition()).getAuthor());
                request.putExtra("id", mData.get(getAdapterPosition()).getId());
                request.putExtra("image_url", urls.https + "cover_images/" + mData.get(getAdapterPosition()).getImage());
                context.startActivity(request);
            });


        }
    }
}
