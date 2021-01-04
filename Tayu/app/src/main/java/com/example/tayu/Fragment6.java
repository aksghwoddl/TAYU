package com.example.tayu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Fragment6 extends Fragment {
    ViewGroup viewGroup;
    private Context mContext = null;

    //view objects

    ArrayList<ItemData> clubList;
    int count=0;
    static boolean calledAlready=false;
    String strTitle=null;
    String strSize=null;
    String strLoc=null;
    String strCon=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment6, container, false);
        mContext = container.getContext();
        this.InitializeClubData();

        final ListView listView=(ListView)viewGroup.findViewById(R.id.clublist);
        final ListAdapter myAdapter= new ListAdapter(mContext,clubList);

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
                    clubList.add(new ItemData(R.drawable.tayuimg,""+strTitle,""+strSize,""+strLoc,""+strCon));
                    myAdapter.notifyDataSetChanged();
                    count++;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("tag","fail",databaseError.toException());

            }
        });

        Button btn=(Button)viewGroup.findViewById(R.id.testadd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), AddClub.class);
                intent.putExtra("posi",count);
                startActivity(intent);
            }
        });


        return viewGroup;
    }

    Button btn=(Button)viewGroup.findViewById(R.id.Back);
    public void InitializeClubData(){
        clubList=new ArrayList<ItemData>();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}




