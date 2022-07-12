package com.example.tushome.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tushome.Authors.RealFeedback;
import com.example.tushome.Models.AuthorModel;
import com.example.tushome.R;
import com.example.tushome.Urls.Urls;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class SeeFeebackAdapter extends RecyclerView.Adapter<SeeFeebackAdapter.FarmerViewHolder> {
    Context context;
    List<AuthorModel> mData;
    Urls urls;
    Dialog details;

    public SeeFeebackAdapter(Context context, List<AuthorModel> mData) {
        this.context = context;
        this.mData = mData;
        urls = new Urls();
        details = new Dialog(context);
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public FarmerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_see_feedback, null, false);
        return new FarmerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FarmerViewHolder holder, final int position) {

        AuthorModel farmerModel = mData.get(position);
        holder.title.setText(mData.get(position).getTitle());
        holder.preview.setText(mData.get(position).getPreview());
        holder.author.setText(mData.get(position).getAuthor());
        holder.id.setText(mData.get(position).getId());
//        holder.total_feedback.setText(mData.get(position).getTotal());
        holder.read_what.setText("View requests");
        holder.balance_card.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        holder.linear_ff.setVisibility(View.GONE);
        holder.preview.setVisibility(View.VISIBLE);
        holder.linear_request_options.setVisibility(View.GONE);
        holder.order.setVisibility(View.VISIBLE);
        holder.card_total.setVisibility(View.GONE);

        String imageUrl = urls.https + "cover_images/" + farmerModel.getImage();
        try {

            Glide.with(context)
                    .load(imageUrl)
                    .into(holder.product_image);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class FarmerViewHolder extends RecyclerView.ViewHolder {

        TextView title, preview, author, id, read_what, total_feedback;
        CardView balance_card, order;
        ImageView product_image;
        LinearLayout linear_ff,linear_request_options;
        MaterialCardView card_total;

        public FarmerViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            preview = itemView.findViewById(R.id.preview);
            author = itemView.findViewById(R.id.author);
            balance_card = itemView.findViewById(R.id.balance_card);
            id = itemView.findViewById(R.id.id);
            product_image = itemView.findViewById(R.id.product_image);
            order = itemView.findViewById(R.id.order);
            read_what = itemView.findViewById(R.id.read_what);
            total_feedback = itemView.findViewById(R.id.total_feedback);
            linear_ff = itemView.findViewById(R.id.linear_ff);
            linear_request_options = itemView.findViewById(R.id.linear_request_options);
            card_total = itemView.findViewById(R.id.card_total);

            order.setOnClickListener(view -> {
                Intent request = new Intent(context, RealFeedback.class);
                request.putExtra("title", mData.get(getAdapterPosition()).getTitle());
//                request.putExtra("preview", mData.get(getAdapterPosition()).getPreview());
//                request.putExtra("author", mData.get(getAdapterPosition()).getAuthor());
                request.putExtra("id", mData.get(getAdapterPosition()).getId());
//                request.putExtra("image_url", urls.https + "cover_images/" + mData.get(getAdapterPosition()).getImage());
                context.startActivity(request);
            });


        }
    }
}
