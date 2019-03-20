package com.example.project.newfirstpage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateProfile extends AppCompatActivity {

    EditText username,password;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
    }
    public void submit(View view)
    {
        Toast.makeText(this, "updated Successfully", Toast.LENGTH_LONG).show();
    }
}
