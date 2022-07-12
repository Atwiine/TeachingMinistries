package com.example.teachingministries.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.teachingministries.Models.StudentsModel;
import com.example.teachingministries.R;
import com.example.teachingministries.Urls.SessionManager;
import com.example.teachingministries.Urls.Urls;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.AuthorViewHolder> {
    Context context;
    public static  List<StudentsModel> mData;
    Urls urls;
    SessionManager sessionManager;
    String  getTYPE;
    String getId, idPost, teacher;

    public StudentsAdapter(Context context, List<StudentsModel> mData) {
        this.context = context;
        this.mData = mData;
        urls = new Urls();
        sessionManager = new SessionManager(context);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        teacher = user.get(SessionManager.TYPE);
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_students, null, false);
        return new AuthorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, final int position) {

        StudentsModel authorModel = mData.get(position);
        holder.title.setText(mData.get(position).getNames());
        holder.phone.setText(mData.get(position).getPhone());
        holder.id.setText(mData.get(position).getId());
        holder.student_remarks.setText(mData.get(position).getRemarks());
        holder.track_attendance.setText(mData.get(position).getTrack());
        holder.track_attendance.setVisibility(View.VISIBLE);
        holder.balance_card.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        String tt =  holder.track_attendance.getText().toString();
     if (tt.equals("Attendance not taken yet") || tt.equals("missed")){
            holder.mark_attendance_missed.setVisibility(View.VISIBLE);
        }else {
         holder.mark_attendance_missed.setVisibility(View.GONE);
     }
//
        holder.phones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ad = holder.phone.getText().toString();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + ad));
                try {
                    context.startActivity(callIntent);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        });


        if (holder.student_remarks.getText().toString().equals("1")){
            holder.ennttrr.setVisibility(View.GONE);
            holder.update_student.setVisibility(View.GONE);
            holder.mark_attendance_missed.setVisibility(View.GONE);
            holder.show_track.setVisibility(View.GONE);
        }

        if (holder.track_attendance.getText().toString().equals("1")){
            holder.ennttrr.setVisibility(View.GONE);
            holder.update_student.setVisibility(View.GONE);
            holder.mark_attendance_missed.setVisibility(View.GONE);
            holder.show_track.setVisibility(View.GONE);
        }

        if (holder.track_attendance.getText().toString().equals("0")){
            holder.update_student.setVisibility(View.GONE);
            holder.track_attendance.setText("missed");
            holder.mark_attendance_missed.setVisibility(View.VISIBLE);
        }

        if (holder.track_attendance.getText().toString().equals("attended")){
            holder.update_student.setVisibility(View.GONE);
        }

        if (holder.track_attendance.getText().toString().equals("Fully baptised")){
            holder.update_student.setVisibility(View.GONE);
        }

        //in some cases, it will prevent unwanted situations
        holder.attended.setOnCheckedChangeListener(null);
        holder.missed.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
        holder.attended.setChecked(mData.get(position).getSelected());
        holder.missed.setChecked(mData.get(position).getSelected());

        holder.attended.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mData.get(holder.getAdapterPosition()).setSelected(isChecked);
                String iidd = mData.get(holder.getAdapterPosition()).getId();
                String nnaa = mData.get(holder.getAdapterPosition()).getNames();

                final String studentName = holder.title.getText().toString();
                idPost = holder.id.getText().toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.SEND_ATTENDED,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.i("tagconvertstr", "[" + response + "]");
                                    JSONObject object = new JSONObject(response);
                                    String success = object.getString("success");
                                    if (success.equals("1")) {
                                        Toast.makeText(context, "successful", Toast.LENGTH_LONG).show();
                                        holder.track_attendance.setText("attended");

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "Attendance not registered, please try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Attendance not successful, check your connection and try again" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        String mm = "attended";
                        params.put("userid", getId);//teachers id
                        params.put("name", nnaa);//students name
                        params.put("id", iidd);//students id
                        params.put("attended", mm);//students attendance
                        params.put("teacher", teacher);//students teacher

                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);

            }
        });

        holder.card_send_remarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String iidd = holder.id.getText().toString();
                String ree =  holder.student_remarks.getText().toString().trim();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.SEND_REMARKS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.i("tagconvertstr", "[" + response + "]");
                                    JSONObject object = new JSONObject(response);
                                    String success = object.getString("success");
                                    if (success.equals("1")) {
                                        Toast.makeText(context, "remarks sent successfully", Toast.LENGTH_LONG).show();
//                                        Intent ss = new Intent(context, Students.class);
//                                        context.startActivity(ss);
//                                        ((Activity) context).finish();

                                        holder.student_remarks.setText(ree);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "remarks sent , please try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        }, error -> Toast.makeText(context, "remarks sent , check your connection and try again" + error.toString(), Toast.LENGTH_LONG).show()) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("id", iidd);//students id
                        params.put("remark", ree);//students id
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);
            }
        });

        /*show and hide options*/

        holder.ph_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.phone_opts.setVisibility(View.VISIBLE);
                holder.ph_options.setVisibility(View.GONE);
            }
        });

        holder.close_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.phone_opts.setVisibility(View.GONE);
                holder.ph_options.setVisibility(View.VISIBLE);
            }
        });
        holder.remarks_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.ennttrr.setVisibility(View.VISIBLE);
                holder.remarks_options.setVisibility(View.GONE);
            }
        });

        holder.close_remarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.ennttrr.setVisibility(View.GONE);
                holder.remarks_options.setVisibility(View.VISIBLE);
            }
        });

        holder.sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure,You wanted to send a message to the selected participant");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                String nssmr =  holder.title.getText().toString();
                                String phoneNumber = "0"+holder.phone.getText().toString();
                                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                                smsIntent.setType("vnd.android-dir/mms-sms");
                                smsIntent.setData(Uri.parse("sms:" + phoneNumber));
//                                smsIntent.putExtra("address",064567);
                                smsIntent.putExtra("sms_body","Hi"+" "+nssmr+" "+"how are you");
                                context.startActivity(smsIntent);                            }
                        });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });


        holder.check_message.setChecked(mData.get(position).getSelected());
        holder.check_message.setTag(position);
        holder.check_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer pos = (Integer) holder.check_message.getTag();
                Toast.makeText(context, mData.get(pos).getPhone() + " clicked!", Toast.LENGTH_SHORT).show();

                if (mData.get(pos).getSelected()) {
                    mData.get(pos).setSelected(false);
                } else {
                    mData.get(pos).setSelected(true);
                    holder.check_message.setText("Selected");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void clear() {
        int size = mData.size();
        if (size > 0) {
            mData.subList(0, size).clear();

            notifyItemRangeRemoved(0, size);
        }
    }
    public class AuthorViewHolder extends RecyclerView.ViewHolder {

        TextView title, phone, id, track_attendance;
        CardView balance_card;
        MaterialCardView update_student;
        EditText student_remarks;
        MaterialCardView card_send_remarks;
        LinearLayout ennttrr,remarks_opts,mark_attendance_missed,show_track;
        CheckBox attended, missed,send_class_1;

        CheckBox check_message;
        ImageView close_phone,close_remarks;
        MaterialCardView sms,ph_options,remarks_options,phone_opts;
        MaterialCardView phones;

        public AuthorViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            phone = itemView.findViewById(R.id.phone);
            balance_card = itemView.findViewById(R.id.balance_card);
            id = itemView.findViewById(R.id.id);
            update_student = itemView.findViewById(R.id.update_student);
            student_remarks = itemView.findViewById(R.id.student_remarks);
            card_send_remarks = itemView.findViewById(R.id.card_send_remarks);
            ennttrr = itemView.findViewById(R.id.ennttrr);
            attended = itemView.findViewById(R.id.attended);
            missed = itemView.findViewById(R.id.missed);
            track_attendance = itemView.findViewById(R.id.track_attendance);
            mark_attendance_missed = itemView.findViewById(R.id.mark_attendance_missed);
            show_track = itemView.findViewById(R.id.show_track);
            show_track = itemView.findViewById(R.id.show_track);

            check_message = itemView.findViewById(R.id.check_message);
            sms = itemView.findViewById(R.id.sms);

            ph_options = itemView.findViewById(R.id.ph_options);
            phone_opts = itemView.findViewById(R.id.phone_opts);
            close_phone = itemView.findViewById(R.id.close_phone);
            phones = itemView.findViewById(R.id.phones);

            remarks_opts = itemView.findViewById(R.id.remarks_opts);
            student_remarks = itemView.findViewById(R.id.student_remarks);
            remarks_options = itemView.findViewById(R.id.remarks_options);
            close_remarks = itemView.findViewById(R.id.close_remarks);
            card_send_remarks = itemView.findViewById(R.id.card_send_remarks);

            update_student.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mm = new Intent(context, com.example.teachingministries.Registrations.update_student.class);
                    mm.putExtra("id", id.getText().toString());
                    context.startActivity(mm);

//                    Toast.makeText(context, "id is " + id.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });


        }
    }
}
