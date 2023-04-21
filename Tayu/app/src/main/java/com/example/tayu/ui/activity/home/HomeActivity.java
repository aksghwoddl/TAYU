package com.example.tayu.ui.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tayu.R;
import com.example.tayu.ui.activity.TmapNaviActivity;
import com.example.tayu.ui.fragment.bluetooth.BluetoothFragment;
import com.example.tayu.ui.fragment.navi.NaviFragment;
import com.example.tayu.ui.fragment.timer.TimerFragment;
import com.example.tayu.ui.fragment.weather.DataSetListener;
import com.example.tayu.ui.fragment.weather.WeatherFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity{

    BottomNavigationView bottomNavigationView;
    WeatherFragment weatherFragment;
    NaviFragment naviFragment;
    BluetoothFragment bluetoothFragment;
    TimerFragment timerFragment;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        //프래그먼트 생성
        naviFragment = new NaviFragment();
        bluetoothFragment = new BluetoothFragment();
        weatherFragment = new WeatherFragment();
        timerFragment = new TimerFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, weatherFragment).commitAllowingStateLoss();
        //bottomnavigationview의 아이콘을 선택 했을때 원하는 프래그먼트가 띄워질 수 있도록 리스너를 추가합니다.
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    //menu_bottom.xml에서 지정해줬던 아이디 값을 받아와서 각 아이디값마다 다른 이벤트를 발생시킵니다.
                    case R.id.tab1:{ getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, weatherFragment).commitAllowingStateLoss(); return true; }
                    case R.id.tab2:{ getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, naviFragment).commitAllowingStateLoss(); return true; }
                    case R.id.tab3:{ getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, bluetoothFragment).commitAllowingStateLoss(); return true; }
                    case R.id.tab4:{ getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, timerFragment).commitAllowingStateLoss(); return true; }
                    default: return false;
                }
            }
        });

    }



}

