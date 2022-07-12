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
import com.example.teachingministries.Models.Track1Model;
import com.example.teachingministries.R;
import com.example.teachingministries.Urls.SessionManager;
import com.example.teachingministries.Urls.Urls;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Track1Adapter extends RecyclerView.Adapter<Track1Adapter.AuthorViewHolder> {
    Context context;
    public static ArrayList<Track1Model> mData;
    Urls urls;
    SessionManager sessionManager;
    String getId, idPost, teacher;
     ArrayList<Track1Model> track1ModelArrayList;

    public Track1Adapter(Context context, ArrayList<Track1Model> mData) {
        this.context = context;
        this.mData = mData;
        urls = new Urls();
        sessionManager = new SessionManager(context);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        teacher = user.get(SessionManager.TYPE);
    }

//    public void setparticipants(ArrayList<Track1Model> track1ModelArrayList) {
//        this.track1ModelArrayList = new ArrayList<>();
//        this.track1ModelArrayList = track1ModelArrayList;
//        notifyDataSetChanged();
//    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_track, null, false);
        return new AuthorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, final int position) {

        Track1Model track1Model = mData.get(position);
        holder.title.setText(mData.get(position).getNames());
        holder.phone.setText(mData.get(position).getPhone());
        holder.id.setText(mData.get(position).getId());
        holder.current_teacher.setText(mData.get(position).getTeacher());
        holder.track_attendance.setText(mData.get(position).getTrack());
        holder.track_attendance.setVisibility(View.VISIBLE);
        holder.balance_card.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.student_remarks.setText(mData.get(position).getRemarks());

       if (holder.current_teacher.getText().toString().equals(teacher)) {
           holder.ennttrr.setVisibility(View.VISIBLE);
       } else {
           holder.ennttrr.setVisibility(View.GONE);
           holder.tx_notattended.setVisibility(View.GONE);
           holder.tx_attended.setVisibility(View.GONE);
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


                       if (isChecked) {
                           holder.missed.setChecked(false);
                           Toast.makeText(context, mData.get(holder.getAdapterPosition()).getNames() + " attended class", Toast.LENGTH_SHORT).show();
                       }


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


           holder.missed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                       if (isChecked) {
                           holder.attended.setChecked(false);
                           Toast.makeText(context, mData.get(holder.getAdapterPosition()).getNames() + "missed class", Toast.LENGTH_SHORT).show();
                       }

                       mData.get(holder.getAdapterPosition()).setSelected(isChecked);
                       Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                       final String studentName = holder.title.getText().toString();
                       idPost = holder.id.getText().toString();

                       String iidd = mData.get(holder.getAdapterPosition()).getId();
                       String nnaa = mData.get(holder.getAdapterPosition()).getNames();

                       StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.SEND_MISSED,
                               new Response.Listener<String>() {
                                   @Override
                                   public void onResponse(String response) {
                                       try {
                                           Log.i("tagconvertstr", "[" + response + "]");
                                           JSONObject object = new JSONObject(response);
                                           String success = object.getString("success");
                                           if (success.equals("1")) {
                                               Toast.makeText(context, "successful", Toast.LENGTH_LONG).show();
                                               holder.track_attendance.setText("missed");
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
                               String mm = "missed";
                               params.put("userid", getId);//teachers id
                               params.put("name", nnaa);//students name
                               params.put("id", iidd);//students id
                               params.put("missed", mm);//students attendance
                               params.put("teacher", teacher);//students teacher


                               return params;
                           }
                       };

                       RequestQueue requestQueue = Volley.newRequestQueue(context);
                       requestQueue.add(stringRequest);
                   }

           });


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
                                String phoneNumber = holder.phone.getText().toString();
                                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                                smsIntent.setType("vnd.android-dir/mms-sms");
                                smsIntent.setData(Uri.parse("sms:" + phoneNumber));
                                smsIntent.putExtra("address",064567);
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
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "remarks sent , check your connection and try again" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

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


        /*show and hide options*/
        holder.remarks_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.remarks_opts.setVisibility(View.VISIBLE);
                holder.remarks_options.setVisibility(View.GONE);
            }
        });
        holder.ph_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.phone_opts.setVisibility(View.VISIBLE);
                holder.ph_options.setVisibility(View.GONE);
            }
        });
        holder.close_remarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.remarks_opts.setVisibility(View.GONE);
                holder.remarks_options.setVisibility(View.VISIBLE);
            }
        });
        holder.close_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.phone_opts.setVisibility(View.GONE);
                holder.ph_options.setVisibility(View.VISIBLE);
            }
        });



    }

    @Override
    public int getItemCount() {
//        return mData.size();
        return mData == null ? 0 : mData.size();
    }

    public void clear() {
        int size = mData.size();
        if (size > 0) {
            mData.subList(0, size).clear();

         notifyItemRangeRemoved(0, size);
        }
    }

    public static class AuthorViewHolder extends RecyclerView.ViewHolder {

        TextView title, phone, id, tx_notattended, tx_attended, track_attendance, current_teacher;
        CardView balance_card;
        CheckBox attended, missed;
        LinearLayout ennttrr,remarks_opts;
        EditText student_remarks;
        MaterialCardView card_send_remarks,phones,sms,ph_options,remarks_options,phone_opts;
        CheckBox check_message;
        private View view;
        ImageView close_phone,close_remarks;

        public AuthorViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tx_notattended = itemView.findViewById(R.id.tx_notattended);
            tx_attended = itemView.findViewById(R.id.tx_attended);
            title = itemView.findViewById(R.id.title);
            phone = itemView.findViewById(R.id.phone);
            balance_card = itemView.findViewById(R.id.balance_card);
            id = itemView.findViewById(R.id.id);
            attended = itemView.findViewById(R.id.attended);
            missed = itemView.findViewById(R.id.missed);
            track_attendance = itemView.findViewById(R.id.track_attendance);
            current_teacher = itemView.findViewById(R.id.current_teacher);
            ennttrr = itemView.findViewById(R.id.ennttrr);
            student_remarks = itemView.findViewById(R.id.student_remarks);
            card_send_remarks = itemView.findViewById(R.id.card_send_remarks);
            phones = itemView.findViewById(R.id.phones);
            check_message = itemView.findViewById(R.id.check_message);
            sms = itemView.findViewById(R.id.sms);
            remarks_options = itemView.findViewById(R.id.remarks_options);
            ph_options = itemView.findViewById(R.id.ph_options);
            remarks_opts = itemView.findViewById(R.id.remarks_opts);
            phone_opts = itemView.findViewById(R.id.phone_opts);
            close_remarks = itemView.findViewById(R.id.close_remarks);
            close_phone = itemView.findViewById(R.id.close_phone);

            /*check to see which student was present or missed and then send the present on*/
//
//            attended.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//                    if (b) {
//                        missed.setChecked(false);
//
//                        Toast.makeText(context, title.getText().toString() + " attended class", Toast.LENGTH_SHORT).show();
//                    }
//
//                    mData.get(getAdapterPosition()).setSelected(b);
//                    String iidd = mData.get(getAdapterPosition()).getId();
//                    String nnaa = mData.get(getAdapterPosition()).getNames();
//
//                    final String studentName = title.getText().toString();
//                    idPost = id.getText().toString();
//
//                    StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.SEND_ATTENDED,
//                            new Response.Listener<String>() {
//                                @Override
//                                public void onResponse(String response) {
//                                    try {
//                                        Log.i("tagconvertstr", "[" + response + "]");
//                                        JSONObject object = new JSONObject(response);
//                                        String success = object.getString("success");
//                                        if (success.equals("1")) {
//                                            Toast.makeText(context, "successful", Toast.LENGTH_LONG).show();
//                                            tx_notattended.setVisibility(View.GONE);
//                                            tx_attended.setVisibility(View.VISIBLE);
//                                        }
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                        Toast.makeText(context, "Attendance not registered, please try again", Toast.LENGTH_LONG).show();
//                                    }
//                                }
//                            }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(context, "Attendance not successful, check your connection and try again" + error.toString(), Toast.LENGTH_LONG).show();
//                        }
//                    }) {
//
//                        @Override
//                        protected Map<String, String> getParams() {
//                            Map<String, String> params = new HashMap<>();
//                            String mm = "attended";
//
//                            params.put("userid", getId);//teachers id
//                            params.put("name", nnaa);//students name
//                            params.put("id", iidd);//students id
//                            params.put("attended", mm);//students attendance
//
//
//                            return params;
//                        }
//                    };
//
//                    RequestQueue requestQueue = Volley.newRequestQueue(context);
//                    requestQueue.add(stringRequest);
//                }
//            });
//            missed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    if (b) {
//                        attended.setChecked(false);
//                        Toast.makeText(context, mData.get(getAdapterPosition()).getNames() + "missed class", Toast.LENGTH_SHORT).show();
//                    }
//
//                    mData.get(getAdapterPosition()).setSelected(b);
//                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
//                    final String studentName = title.getText().toString();
//                    idPost = id.getText().toString();
//
//                    String iidd = mData.get(getAdapterPosition()).getId();
//                    String nnaa = mData.get(getAdapterPosition()).getNames();
//
//                    StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.SEND_MISSED,
//                            new Response.Listener<String>() {
//                                @Override
//                                public void onResponse(String response) {
//                                    try {
//                                        Log.i("tagconvertstr", "[" + response + "]");
//                                        JSONObject object = new JSONObject(response);
//                                        String success = object.getString("success");
//                                        if (success.equals("1")) {
//                                            Toast.makeText(context, "successful", Toast.LENGTH_LONG).show();
//                                            tx_attended.setVisibility(View.GONE);
//                                            tx_notattended.setVisibility(View.VISIBLE);
//                                        }
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                        Toast.makeText(context, "Attendance not registered, please try again", Toast.LENGTH_LONG).show();
//                                    }
//                                }
//                            }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(context, "Attendance not successful, check your connection and try again" + error.toString(), Toast.LENGTH_LONG).show();
//                        }
//                    }) {
//
//                        @Override
//                        protected Map<String, String> getParams() {
//                            Map<String, String> params = new HashMap<>();
//                            String mm = "missed";
//                            params.put("userid", getId);//teachers id
//                            params.put("name", nnaa);//students name
//                            params.put("id", iidd);//students id
//                            params.put("missed", mm);//students attendance
//
//
//                            return params;
//                        }
//                    };
//
//                    RequestQueue requestQueue = Volley.newRequestQueue(context);
//                    requestQueue.add(stringRequest);
//                }
//            });



        }
    }
}
