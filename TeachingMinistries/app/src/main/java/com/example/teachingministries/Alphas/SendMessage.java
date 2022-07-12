package com.example.teachingministries.Alphas;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingministries.Adapters.GraduationAdapter;
import com.example.teachingministries.Adapters.StudentsAdapter;
import com.example.teachingministries.Adapters.ToBeBaptisedAdapter;
import com.example.teachingministries.Adapters.Track1Adapter;
import com.example.teachingministries.MainActivity;
import com.example.teachingministries.R;
import com.google.android.material.card.MaterialCardView;

public class SendMessage extends AppCompatActivity {
    private TextView tv,tv2,emty_list;
String whose_message,tt;
MaterialCardView card_send_remarks;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        tv = (TextView) findViewById(R.id.tv);
        tv2 = (TextView) findViewById(R.id.tv2);
        emty_list = (TextView) findViewById(R.id.emty_list);
        card_send_remarks = (MaterialCardView) findViewById(R.id.card_send_remarks);

        whose_message = getIntent().getStringExtra("whose_message");
        tt = getIntent().getStringExtra("go");



        switch (whose_message) {
            case "Track1Adapter":
//                if (tv2.getText().toString().trim().isEmpty()){
//                    emty_list.setVisibility(View.VISIBLE);
//                    card_send_remarks.setVisibility(View.GONE);
//
//                }else {
//                    emty_list.setVisibility(View.GONE);
//                    card_send_remarks.setVisibility(View.VISIBLE);
//                }
                for (int i = 0; i < Track1Adapter.mData.size(); i++) {
                    if (Track1Adapter.mData.get(i).getSelected()) {
                        tv.setText(tv.getText() + " " + Track1Adapter.mData.get(i).getPhone() + " ");
                        tv2.setText(tv2.getText() + " " + Track1Adapter.mData.get(i).getNames() + " " + "( " +
                                Track1Adapter.mData.get(i).getPhone() + " )");
                    }
                }
                break;
            case "StudentsAdapter":
                for (int i = 0; i < StudentsAdapter.mData.size(); i++) {
                    if (StudentsAdapter.mData.get(i).getSelected()) {
                        tv.setText(tv.getText() + " " + StudentsAdapter.mData.get(i).getPhone() + " ");
                        tv2.setText(tv2.getText() + " " + StudentsAdapter.mData.get(i).getNames() + " " + "( " +
                                StudentsAdapter.mData.get(i).getPhone() + " )");
                    }
                }
                break;
            case "GraduationAdapter":
                for (int i = 0; i < GraduationAdapter.mData.size(); i++) {
                    if (GraduationAdapter.mData.get(i).getSelected()) {
                        tv.setText(tv.getText() + " " + GraduationAdapter.mData.get(i).getPhone() + " ");
                        tv2.setText(tv2.getText() + " " + GraduationAdapter.mData.get(i).getNames() + " " + "( " +
                                GraduationAdapter.mData.get(i).getPhone() + " )");
                    }
                }
                break;
            case "ToBeBaptisedAdapter":
                for (int i = 0; i < ToBeBaptisedAdapter.mData.size(); i++) {
                    if (ToBeBaptisedAdapter.mData.get(i).getSelected()) {
                        tv.setText(tv.getText() + " " + ToBeBaptisedAdapter.mData.get(i).getPhone() + " ");
                        tv2.setText(tv2.getText() + " " + ToBeBaptisedAdapter.mData.get(i).getNames() + " " + "( " +
                                ToBeBaptisedAdapter.mData.get(i).getPhone() + " )");
                    }
                }
                break;
        }

    }


    public void back(View view) {
        tv.setText("");
        tv2.setText("");
        switch (tt) {
            case "Trackatt":
            case "Miss":
            case "Pass":
            case "Stud":
                startActivity(new Intent(SendMessage.this, MainActivity.class));
                finish();
                break;
            case "Grad":
                startActivity(new Intent(SendMessage.this, Graduation.class));
                finish();
                break;
            case "Tobe":
                startActivity(new Intent(SendMessage.this, ToBeBaptised.class));
                finish();
                break;
            case "Bast":
                startActivity(new Intent(SendMessage.this, BaptisedStudents.class));
                finish();
                break;
        }

    }

    public void sendgroupsms(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, you want to send a message to the selected participants");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        sendSMS();                            }
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



    @SuppressLint("IntentReset")
    private void sendSMS() {
//        FIGURE OUT HOW TO ADD SPACES FROM THE MESSAGING APP SIDE
        String phoneNumber =  " "+tv.getText().toString();
        Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.setData(Uri.parse("sms:" + " "+ phoneNumber));
//        smsIntent.putExtra("address",064567);
//        smsIntent.putExtra("address"  , new String (phoneNumber));
        smsIntent.putExtra("sms_body","Hi how are you");
        startActivity(smsIntent);
    }


}