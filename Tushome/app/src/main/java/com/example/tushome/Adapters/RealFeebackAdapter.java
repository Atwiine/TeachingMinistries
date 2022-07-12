package com.example.tushome.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tushome.Models.RealFeedbackModel;
import com.example.tushome.R;
import com.example.tushome.Urls.Urls;

import java.util.List;

public class RealFeebackAdapter extends RecyclerView.Adapter<RealFeebackAdapter.FarmerViewHolder> {
    Context context;
    List<RealFeedbackModel> mData;
    Urls urls;

    public RealFeebackAdapter(Context context, List<RealFeedbackModel> mData) {
        this.context = context;
        this.mData = mData;
        urls = new Urls();
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public FarmerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_real_feedback, null, false);
        return new FarmerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FarmerViewHolder holder, final int position) {

        RealFeedbackModel farmerModel = mData.get(position);
        holder.from_who.setText(mData.get(position).getFrom_who());
        holder.feedback.setText(mData.get(position).getFeedback());
        holder.id.setText(mData.get(position).getId());

        holder.balance_card.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class FarmerViewHolder extends RecyclerView.ViewHolder {

        TextView from_who, feedback, id;
        CardView balance_card;

        public FarmerViewHolder(@NonNull View itemView) {
            super(itemView);

            from_who = itemView.findViewById(R.id.from_who);
            feedback = itemView.findViewById(R.id.feedback);
            balance_card = itemView.findViewById(R.id.balance_card);
            id = itemView.findViewById(R.id.id);
        }
    }
}
