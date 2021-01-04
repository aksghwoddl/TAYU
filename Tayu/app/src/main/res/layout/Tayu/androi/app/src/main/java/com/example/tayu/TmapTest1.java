package com.example.tayu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.TMapView;

public class TmapTest1 extends AppCompatActivity
        {

         String start,stX,stY=null;
         String end,edX,edY=null;
         double bb;
         TextView textView1;
         TextView textView2;
         TMapView mapView;


            @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_tmap_test1);

        textView1=(TextView)findViewById(R.id.start1);
        textView2=(TextView)findViewById(R.id.end1);

        Intent intent=getIntent();
        start=intent.getStringExtra("stPoint");
        stX=intent.getStringExtra("stX");
        stY=intent.getStringExtra("stY");
        end=intent.getStringExtra("edPoint");
        edX=intent.getStringExtra("edX");
        edY=intent.getStringExtra("edY");



       textView1.setText("시작: "+start+"X: "+stX+"Y: "+stY);
       textView2.setText("도착: "+end+"X: "+edX+"Y: "+edY);

    }



        }