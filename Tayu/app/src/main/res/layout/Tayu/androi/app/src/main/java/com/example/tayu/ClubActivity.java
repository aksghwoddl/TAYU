package com.example.tayu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.tayu.domain.model.club.ClubItemData;
import com.example.tayu.ui.activity.club.AddClub;
import com.example.tayu.ui.activity.club.adapter.ClubListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClubActivity extends AppCompatActivity {

    ArrayList<ClubItemData> clubList;
    int count=0;
    static boolean calledAlready=false;
    String strTitle=null;
    String strSize=null;
    String strLoc=null;
    String strCon=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        this.InitializeClubData();

        final ListView listView=(ListView)findViewById(R.id.clublist);
        final ClubListAdapter myAdapter= new ClubListAdapter(this,clubList);

        listView.setAdapter(myAdapter);
        if(!calledAlready){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready=true;
        }

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference databaseRef=database.getReference();
        databaseRef.child("clubs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    strTitle = fileSnapshot.child("strTitle").getValue(String.class);
                    strSize = fileSnapshot.child("strSize").getValue(String.class);
                    strLoc = fileSnapshot.child("strLoc").getValue(String.class);
                    strCon=fileSnapshot.child("strCon").getValue(String.class);
                    clubList.add(new ClubItemData(R.drawable.tayuimg,""+strTitle,""+strSize,""+strLoc,""+strCon));
                    myAdapter.notifyDataSetChanged();
                    count++;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("tag","fail",databaseError.toException());

            }
        });

        Button btn=(Button)findViewById(R.id.testadd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), AddClub.class);
                intent.putExtra("posi",count);
                startActivity(intent);
            }
        });

    }

    Button btn=(Button)findViewById(R.id.Back);
    public void InitializeClubData(){
        clubList=new ArrayList<ClubItemData>();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

}