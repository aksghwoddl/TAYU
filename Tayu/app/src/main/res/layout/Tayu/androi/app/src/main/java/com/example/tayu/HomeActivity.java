package com.example.tayu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity
    implements BlankFragment.OnDataSetListener{

    BottomNavigationView bottomNavigationView;

    Fragment2 fragment2;
    Fragment3 fragment3;
    Fragment4 fragment4;
    Fragment5 fragment5;
    String start2,startX,startY=null;
    String end2,endX,endY=null;

    @Override
    public void onDataSet(String stName, String stX, String stY, String edName, String edX, String edY) {
        start2=stName;
        startX=stX;
        startY=stY;
        end2=edName;
        endX=edX;
        endY=edY;

        if(start2!=null&&end2!=null) {
            Intent intent = new Intent(this, TmapTest1.class);
            intent.putExtra("stPoint", "" + start2);
            intent.putExtra("stX",""+startX);
            intent.putExtra("stY",""+startY);
            intent.putExtra("edPoint", "" + end2);
            intent.putExtra("edX", "" + endX);
            intent.putExtra("edY", "" + endY);
            startActivity(intent);
        }
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);


        //프래그먼트 생성

        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        fragment4 = new Fragment4();
        fragment5 = new Fragment5();
        //제일 처음 띄워줄 뷰를 세팅해줍니다.
        // commit();까지 해줘야 합니다.
        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout,fragment4).commitAllowingStateLoss();
        //bottomnavigationview의 아이콘을 선택 했을때 원하는 프래그먼트가 띄워질 수 있도록 리스너를 추가합니다.
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    //menu_bottom.xml에서 지정해줬던 아이디 값을 받아와서 각 아이디값마다 다른 이벤트를 발생시킵니다.
                    case R.id.tab1:{ getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,fragment4).commitAllowingStateLoss(); return true; }
                    case R.id.tab2:{ getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,fragment2).commitAllowingStateLoss(); return true; }
                    case R.id.tab3:{ getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,fragment3).commitAllowingStateLoss(); return true; }
                    case R.id.tab4:{ getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,fragment5).commitAllowingStateLoss(); return true; }
                    default: return false;
                }
            }
        });

    }


}

