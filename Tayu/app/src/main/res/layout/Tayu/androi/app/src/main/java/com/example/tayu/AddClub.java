package com.example.tayu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddClub extends AppCompatActivity {
    private DatabaseReference mDatabase;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_club);

        mDatabase= FirebaseDatabase.getInstance().getReference();


        final EditText et1=(EditText)findViewById(R.id.addCT);
        final EditText et2=(EditText)findViewById(R.id.addCS);
        final EditText et3=(EditText)findViewById(R.id.addCL);
        final EditText et4=(EditText)findViewById(R.id.addCC);


        i=getIntent().getIntExtra("posi",1);


        Button btn=(Button)findViewById(R.id.addC);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ClubActivity.class);

                String test=""+et1.getText();
                String ts=""+et2.getText();
                String tl=""+et3.getText();
                String tc=""+et4.getText();

                HashMap result=new HashMap<>();
                result.put("title",test);
                result.put("size",ts);
                result.put("loc",tl);
                result.put("content",tc);
                writeNewClub(""+i,test,ts,tl,tc);


                startActivity(intent);
            }
        });





    }
    private void writeNewClub(String clubId,String title,String size,String loc,String con){
        Club club=new Club(title,size,loc,con);

        mDatabase.child("clubs").child(clubId).setValue(club)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddClub.this,"저장완료",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddClub.this,"저장실패",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}