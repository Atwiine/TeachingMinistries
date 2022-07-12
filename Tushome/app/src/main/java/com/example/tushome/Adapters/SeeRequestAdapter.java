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
import com.example.tushome.Authors.AllRequests;
import com.example.tushome.Authors.Requests;
import com.example.tushome.Models.AuthorModel;
import com.example.tushome.R;
import com.example.tushome.Urls.Urls;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class SeeRequestAdapter extends RecyclerView.Adapter<SeeRequestAdapter.FarmerViewHolder> {
    Context context;
    List<AuthorModel> mData;
    Urls urls;
    Dialog details;

    public SeeRequestAdapter(Context context, List<AuthorModel> mData) {
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
//        holder.total_requests_per_book.setText(mData.get(position).getTotal());


        holder.balance_card.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.card_total.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        String imageUrl = urls.https + "cover_images/" + farmerModel.getImage();
        try {

            Glide.with(context)
                    .load(imageUrl)
                    .into(holder.product_image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.linear_request_options.setVisibility(View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class FarmerViewHolder extends RecyclerView.ViewHolder {

        TextView title, preview, author, id,total_requests_per_book;
        CardView balance_card, order;
        ImageView product_image;
        MaterialCardView all_requests,total_requests,card_total;
        LinearLayout linear_request_options;

        public FarmerViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            preview = itemView.findViewById(R.id.preview);
            author = itemView.findViewById(R.id.author);
            balance_card = itemView.findViewById(R.id.balance_card);
            id = itemView.findViewById(R.id.id);
            product_image = itemView.findViewById(R.id.product_image);
            order = itemView.findViewById(R.id.order);
//            all_requests = itemView.findViewById(R.id.all_requests);
            total_requests = itemView.findViewById(R.id.total_requests);
            total_requests_per_book = itemView.findViewById(R.id.total_requests_per_book);
            card_total = itemView.findViewById(R.id.card_total);
            linear_request_options = itemView.findViewById(R.id.linear_request_options);


            total_requests.setOnClickListener(view -> {
                Intent request = new Intent(context, Requests.class);
                request.putExtra("title", mData.get(getAdapterPosition()).getTitle());
//                request.putExtra("preview", mData.get(getAdapterPosition()).getPreview());
//                request.putExtra("author", mData.get(getAdapterPosition()).getAuthor());
                request.putExtra("id", mData.get(getAdapterPosition()).getId());
//                request.putExtra("image_url", urls.https + "cover_images/" + mData.get(getAdapterPosition()).getImage());
                context.startActivity(request);
            });



//            all_requests.setOnClickListener(view -> {
//                Intent request = new Intent(context, AllRequests.class);
//                request.putExtra("title", mData.get(getAdapterPosition()).getTitle());
////                request.putExtra("preview", mData.get(getAdapterPosition()).getPreview());
////                request.putExtra("author", mData.get(getAdapterPosition()).getAuthor());
//                request.putExtra("id", mData.get(getAdapterPosition()).getId());
////                request.putExtra("image_url", urls.https + "cover_images/" + mData.get(getAdapterPosition()).getImage());
//                context.startActivity(request);
//            });


        }
    }
}
