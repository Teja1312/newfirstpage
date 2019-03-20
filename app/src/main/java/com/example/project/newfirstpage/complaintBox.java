package com.example.project.newfirstpage;

import android.content.Intent;
import android.support.annotation.NonNull;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class complaintBox extends AppCompatActivity {

    DatabaseReference complaint ;
    long id=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_box);

        final TextView textMsg = findViewById(R.id.textView);
        complaint = FirebaseDatabase.getInstance().getReference().child("Complaint");


        complaint.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    id=(dataSnapshot.getChildrenCount());

                    String[] message = dataSnapshot.getValue().toString().replace("]"," ").split(",");
                    textMsg.setText("");
                    for (int i = 1; i<message.length;i++){
                        textMsg.append(message[i] + "\n\n");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                textMsg.setText("Cancelled");
            }
        });
    }

    public void post(View view){

        EditText editPost = findViewById(R.id.editText1);
        complaint.child(String.valueOf(id+1)).setValue(editPost.getText().toString());
        editPost.setText("");

    }
}
