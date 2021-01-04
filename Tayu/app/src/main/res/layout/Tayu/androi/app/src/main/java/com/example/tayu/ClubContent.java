package com.example.tayu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ClubContent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_content);

        TextView t1=(TextView)findViewById(R.id.ct);
        TextView t2=(TextView)findViewById(R.id.cs);
        TextView t3=(TextView)findViewById(R.id.cl);
        TextView t4=(TextView)findViewById(R.id.cc);

        Intent intent=getIntent();
        t1.setText(intent.getStringExtra("title1"));
        t2.setText(intent.getStringExtra("size1"));
        t3.setText(intent.getStringExtra("loc1"));
        t4.setText(intent.getStringExtra("con1"));




    }
}