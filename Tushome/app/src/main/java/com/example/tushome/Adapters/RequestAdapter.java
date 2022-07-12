package com.example.tushome.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tushome.Models.RequestModel;
import com.example.tushome.R;
import com.example.tushome.Urls.Urls;

import java.util.List;
import java.util.Objects;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.AuthorViewHolder> {
    Context context;
    List<RequestModel> mData;
    Urls urls;
    Dialog details;

    public RequestAdapter(Context context, List<RequestModel> mData) {
        this.context = context;
        this.mData = mData;
        urls = new Urls();
        details = new Dialog(context);
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_requests, null, false);
        return new AuthorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, final int position) {

        RequestModel authorModel = mData.get(position);
        holder.title.setText(mData.get(position).getTitle());
        holder.soft_copy_orders.setText(mData.get(position).getOnlinerequests());
        holder.hard_copy_orders.setText(mData.get(position).getHardrequests());
        holder.total_copies.setText(mData.get(position).getTotalrequests());
        holder.id.setText(mData.get(position).getId());

        holder.balance_card.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));


        String too = holder.total_copies.getText().toString();
        String haaa = holder.hard_copy_orders.getText().toString();
        String soo = holder.soft_copy_orders.getText().toString();

        if (too.equals("null")) {
            too = "0";
        }

        double tt = Double.parseDouble(too);
        double hh = Double.parseDouble(haaa);
        double ss = Double.parseDouble(soo);




        /*should only launch if any of the numbers is in 1000s + also have to shrink the sumb****c*/


        holder.balance_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String too = holder.total_copies.getText().toString();
                String haaa = holder.hard_copy_orders.getText().toString();
                String soo = holder.soft_copy_orders.getText().toString();


                double tt = Double.parseDouble(too);
                double hh = Double.parseDouble(haaa);
                double ss = Double.parseDouble(soo);

//                    double tt = 2489000;
//                    double hh = 145777;
//                    double ss = 5780000;

                if (too.equals("null")) {
                    too = "0";
                }


                if (tt > 1000 && hh > 1000 && ss > 1000) {


//                    double tt = Double.parseDouble(too);
//                    double hh = Double.parseDouble(haaa);
//                    double ss = Double.parseDouble(soo);


                    String numberStringTotal = "";
                    String numberStringSoft = "";
                    String numberStringHard = "";


                    /*total copies part*/
                    if (Math.abs(tt / 1000000) > 1) {
                        numberStringTotal = (tt / 1000000) + "m";
//                        Toast.makeText(context, "1 " + numberStringTotal, Toast.LENGTH_SHORT).show();
                        holder.total_copies.setText(numberStringTotal);

                    } else if (Math.abs(tt / 1000) > 1) {
                        numberStringTotal = (tt / 1000) + "k";
//                        Toast.makeText(context, "2 " + numberStringTotal, Toast.LENGTH_SHORT).show();
                        holder.total_copies.setText(numberStringTotal);
                    } else {
                        numberStringTotal = String.valueOf(tt);
//                        Toast.makeText(context, "3 " + numberStringTotal, Toast.LENGTH_SHORT).show();
                        holder.total_copies.setText(numberStringTotal);
                    }

                    /*hard copy part*/
                    if (Math.abs(hh / 1000000) > 1) {
                        numberStringHard = (hh / 1000000) + "m";
//                        Toast.makeText(context, "1 " + numberStringHard, Toast.LENGTH_SHORT).show();
                        holder.hard_copy_orders.setText(numberStringHard);

                    } else if (Math.abs(hh / 1000) > 1) {
                        numberStringHard = (hh / 1000) + "k";
//                        Toast.makeText(context, "2 " + numberStringHard, Toast.LENGTH_SHORT).show();
                        holder.hard_copy_orders.setText(numberStringHard);
                    } else {
                        numberStringHard = String.valueOf(hh);
//                        Toast.makeText(context, "3 " + numberStringHard, Toast.LENGTH_SHORT).show();
                        holder.hard_copy_orders.setText(numberStringHard);
                    }

                    /*soft copy part*/
                    if (Math.abs(ss / 1000000) > 1) {
                        numberStringSoft = (ss / 1000000) + "m";
//                        Toast.makeText(context, "1 " + numberStringSoft, Toast.LENGTH_SHORT).show();
                        holder.soft_copy_orders.setText(numberStringSoft);

                    } else if (Math.abs(ss / 1000) > 1) {
                        numberStringSoft = (ss / 1000) + "k";
//                        Toast.makeText(context, "2 " + numberStringSoft, Toast.LENGTH_SHORT).show();
                        holder.soft_copy_orders.setText(numberStringSoft);
                    } else {
                        numberStringSoft = String.valueOf(ss);
//                        Toast.makeText(context, "3 " + numberStringSoft, Toast.LENGTH_SHORT).show();
                        holder.soft_copy_orders.setText(numberStringSoft);
                    }

                    details = new Dialog(context);
                    details.setContentView(R.layout.request_dets);
                    CardView go_home = details.findViewById(R.id.go_home);
                    TextView soft_copy_dets = details.findViewById(R.id.soft_copy_dets);
                    TextView hard_copy_dets = details.findViewById(R.id.hard_copy_dets);
                    TextView total_dets = details.findViewById(R.id.total_dets);

                    total_dets.setText(numberStringTotal);
                    hard_copy_dets.setText(numberStringHard);
                    soft_copy_dets.setText(numberStringSoft);

                    go_home.setOnClickListener(v1 -> {
                        details.dismiss();
                    });
                    details.setCancelable(false);
                    details.setCanceledOnTouchOutside(false);
                    Objects.requireNonNull(details.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    details.show();

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class AuthorViewHolder extends RecyclerView.ViewHolder {

        TextView title, preview, author, id, soft_copy_orders, hard_copy_orders, total_copies;
        CardView balance_card;
        ImageView product_image;


        public AuthorViewHolder(@NonNull View itemView) {
            super(itemView);


            title = itemView.findViewById(R.id.title);
            preview = itemView.findViewById(R.id.preview);
            author = itemView.findViewById(R.id.author);
            balance_card = itemView.findViewById(R.id.balance_card);
            id = itemView.findViewById(R.id.id);
            product_image = itemView.findViewById(R.id.product_image);
            soft_copy_orders = itemView.findViewById(R.id.soft_copy_orders);
            hard_copy_orders = itemView.findViewById(R.id.hard_copy_orders);
            total_copies = itemView.findViewById(R.id.total_copies);


        }
    }
}
