package com.example.tayu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ClubContent extends AppCompatActivity {
    Button Btn_regist;
    String user_club_name = null;
    String club_name = null;
    String uid;

    String club_user_size = null;
    int plus_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_content);


        Btn_regist = findViewById(R.id.btn_join);



        TextView t1=(TextView)findViewById(R.id.ct);
        TextView t2=(TextView)findViewById(R.id.cs);
        TextView t3=(TextView)findViewById(R.id.cl);
        TextView t4=(TextView)findViewById(R.id.cc);

        Intent intent=getIntent();
        t1.setText(intent.getStringExtra("title1"));
        t2.setText(intent.getStringExtra("size1"));
        t3.setText(intent.getStringExtra("loc1"));
        t4.setText(intent.getStringExtra("con1"));

        club_name = intent.getStringExtra("title1");
        club_user_size = intent.getStringExtra("size1");


        uid = FirebaseAuth.getInstance().getUid();

        /*FirebaseDatabase.getInstance().getReference().child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                user_club_name =  dataSnapshot2.child("user_club").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        FirebaseDatabase.getInstance().getReference().child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                user_club_name =  dataSnapshot2.child("user_club").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



      Btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(user_club_name.equals(club_name))
                    Toast.makeText(ClubContent.this, "이미 가입된 클럽입니다!", Toast.LENGTH_SHORT).show();
                else{
                    FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("user_club").setValue(club_name);
                    plus_user = Integer.parseInt(club_user_size);
                    plus_user++;
                    club_user_size = Integer.toString(plus_user);
                    FirebaseDatabase.getInstance().getReference().child("clubs").child(club_name).child("strSize").setValue(club_user_size);
                    Toast.makeText(ClubContent.this, "가입되었습니다!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}