package com.example.tayu;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tayu.ui.activity.TmapNaviActivity;
import com.example.tayu.ui.fragment.bluetooth.BluetoothFragment;
import com.example.tayu.ui.fragment.navi.NaviFragment;
import com.example.tayu.ui.fragment.timer.TimerFragment;
import com.example.tayu.ui.fragment.weather.WeatherFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity
    implements BlankFragment.OnDataSetListener{

    BottomNavigationView bottomNavigationView;

    NaviFragment naviFragment;
    BluetoothFragment bluetoothFragment;
    WeatherFragment weatherFragment;
    TimerFragment timerFragment;
    String start2,startX,startY=null;
    String end2,endX,endY=null;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);


        //프래그먼트 생성

        fragment2 = new NaviFragment();
        fragment3 = new BluetoothFragment();
        fragment4 = new WeatherFragment();
        fragment5 = new TimerFragment();
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

