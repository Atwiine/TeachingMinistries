package com.example.tushome.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tushome.Models.ReaderModel;
import com.example.tushome.R;
import com.example.tushome.Reader.BookDetails;
import com.example.tushome.Urls.Urls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ReaderAdapter extends RecyclerView.Adapter<ReaderAdapter.FarmerViewHolder> implements Filterable {
    Context context;
    List<ReaderModel> mData;
    Urls urls;
    List<ReaderModel> markets_filter;

    private final Filter examplefilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ReaderModel> filterexample = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filterexample.addAll(markets_filter);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ReaderModel marketsModel : markets_filter) {
                    if (marketsModel.getTitle().toLowerCase().contains(filterPattern)) {
                        filterexample.add(marketsModel);
                    }

                }
            }

            FilterResults results = new FilterResults();
            results.values = filterexample;
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mData.clear();
            mData.addAll((Collection<? extends ReaderModel>) results.values);
            notifyDataSetChanged();
        }
    };


    public ReaderAdapter(Context context, List<ReaderModel> mData) {
        this.context = context;
        this.mData = mData;
        urls = new Urls();
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public FarmerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_reader, null, false);
        return new FarmerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FarmerViewHolder holder, final int position) {

        ReaderModel farmerModel = mData.get(position);
        holder.title.setText(mData.get(position).getTitle());
        holder.preview.setText(mData.get(position).getPreview());
        holder.author.setText(mData.get(position).getAuthor());
        holder.id.setText(mData.get(position).getId());

        holder.balance_card.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

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

    @Override
    public Filter getFilter() {
        return examplefilter;
    }

    public void filterList(ArrayList<ReaderModel> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }

    public class FarmerViewHolder extends RecyclerView.ViewHolder {

        TextView title, preview, author, id;
        CardView balance_card, order;
        ImageView product_image;

        public FarmerViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            preview = itemView.findViewById(R.id.preview);
            author = itemView.findViewById(R.id.author);
            balance_card = itemView.findViewById(R.id.balance_card);
            id = itemView.findViewById(R.id.id);
            product_image = itemView.findViewById(R.id.product_image);
            order = itemView.findViewById(R.id.order);


            balance_card.setOnClickListener(view -> {
                Intent request = new Intent(context, BookDetails.class);
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
