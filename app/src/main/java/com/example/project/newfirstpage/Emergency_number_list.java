package com.example.project.newfirstpage;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Emergency_number_list extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_number_list);
        findViewById(R.id.call1).setOnClickListener(this);
        findViewById(R.id.call2).setOnClickListener(this);
        findViewById(R.id.call3).setOnClickListener(this);
        findViewById(R.id.call4).setOnClickListener(this);
        findViewById(R.id.call5).setOnClickListener(this);
        findViewById(R.id.call6).setOnClickListener(this);
        findViewById(R.id.call7).setOnClickListener(this);
        findViewById(R.id.call8).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent callIntent;
     switch(v.getId())
     {
         case R.id.call1 :  callIntent = new Intent(Intent.ACTION_CALL);
                            startActivity(callIntent.setData(Uri.parse("tel:100")));
             break;
         case R.id.call2 :  callIntent = new Intent(Intent.ACTION_CALL);
                            startActivity(callIntent.setData(Uri.parse("tel:181")));
             break;
         case R.id.call3 :  callIntent = new Intent(Intent.ACTION_CALL);
                            startActivity(callIntent.setData(Uri.parse("tel:102")));
             break;
         case R.id.call4 : callIntent = new Intent(Intent.ACTION_CALL);
                            startActivity(callIntent.setData(Uri.parse("tel:101")));
             break;
         case R.id.call5 :  callIntent = new Intent(Intent.ACTION_CALL);
                               startActivity(callIntent.setData(Uri.parse("tel:108")));
             break;
         case R.id.call6 :  callIntent = new Intent(Intent.ACTION_CALL);
                             startActivity(callIntent.setData(Uri.parse("tel:1512")));
             break;
         case R.id.call7 :  callIntent = new Intent(Intent.ACTION_CALL);
                               startActivity(callIntent.setData(Uri.parse("tel:103")));
             break;
         case R.id.call8 :  callIntent = new Intent(Intent.ACTION_CALL);
                              startActivity(callIntent.setData(Uri.parse("tel:1091")));
             break;
     }
    }
}
