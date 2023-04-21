package com.example.tayu.ui.fragment.timer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.tayu.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TimerFragment extends Fragment implements LocationListener {
    ViewGroup viewGroup;
    private Context mContext = null;

    //view objects

    Button tStart , tStop , tPause;

    LocationManager locationManager;
    Location mLastlocation = null;
    TextView timeView , speedView , distancView;
    double speed;
    double distance;
    double all_range;
    private Thread timeThread = null;
    private Boolean isRunning = true;
    int sec;
    int min;
    int hour;
    String uid = FirebaseAuth.getInstance().getUid();
    int user_hour , user_sec, user_min;
    int sum_hour , sum_min , sum_sec;
    float sum_dis , user_dis;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment5, container, false);
        mContext = container.getContext();

//initializing views

        tStart = viewGroup.findViewById(R.id.timer_Start);
        tStop = viewGroup.findViewById(R.id.timer_Stop);
        tPause = viewGroup.findViewById(R.id.timer_Pause);
        timeView = viewGroup.findViewById(R.id.Time);
        speedView = viewGroup.findViewById(R.id.Speed);
        distancView = viewGroup.findViewById(R.id.distanceView);

        /*FirebaseDatabase.getInstance().getReference().child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_hour = dataSnapshot.child("hour").getValue(Integer.class);
                user_min = dataSnapshot.child("min").getValue(Integer.class);
                user_sec = dataSnapshot.child("sec").getValue(Integer.class);
                user_dis = dataSnapshot.child("user_distance").getValue(Float.class);
                Log.d("dsd" , ""+user_sec);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/



        if(ActivityCompat.checkSelfPermission(mContext , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return viewGroup;
        }


        if(ActivityCompat.checkSelfPermission(mContext , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return viewGroup;
        }

        locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);

        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        /*if(lastKnownLocation != null){
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String formatDate = sdf.format(new Date(lastKnownLocation.getTime()));
            timeView.setText(formatDate);
        }*/

        tStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                tStop.setVisibility(View.VISIBLE);
                tPause.setVisibility(View.VISIBLE);
                timeThread = new Thread(new timeThread());
                all_range = 0;
                timeThread.start();
            }
        });

        tStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                tStart.setVisibility(View.VISIBLE);
                tPause.setVisibility(View.GONE);

                sum_hour = user_hour + hour;
                sum_min = user_min + min;
                sum_sec = user_min + sec;
                sum_dis = user_dis + (float)all_range;

        /*        FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("hour").setValue(sum_hour);
                FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("min").setValue(sum_min);
                FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("sec").setValue(sum_sec);
                FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("user_distance").setValue((int)sum_dis);*/
                all_range = 0;
                timeThread.interrupt();
            }
        });

        tPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = !isRunning;
                if (isRunning) {
                    tPause.setText("일시정지");
                } else {
                    tPause.setText("다시시작");
                }
            }
        });

        return viewGroup;
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int mSec = msg.arg1 % 100;
             sec = (msg.arg1 / 100) % 60;
             min = (msg.arg1 / 100) / 60;
             hour = (msg.arg1 / 100) / 360;
            //1000이 1초 1000*60 은 1분 1000*60*10은 10분 1000*60*60은 한시간
            @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d:%02d:%02d", hour, min, sec, mSec);
            timeView.setText(result);
        }
    };

    public class timeThread implements Runnable {
        @Override
        public void run() {
            int i = 0;

            while (true) {
                while (isRunning) { //일시정지를 누르면 멈춤
                    Message msg = new Message();
                    msg.arg1 = i++;
                    handler.sendMessage(msg);

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        getActivity().runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                timeView.setText("");
                                timeView.setText("00:00:00:00");
                            }
                        });
                        return; // 인터럽트 받을 경우 return
                    }
                }
            }
        }
    }



    @Override
    public void onLocationChanged(Location location) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        double deltaTime = 0;
        double getSpeed = Double.parseDouble(String.format("%.3f" , location.getSpeed()));
        speedView.setText(""+(int) getSpeed);
        String formatDate = sdf.format(new Date(location.getTime()));
        //timeView.setText(formatDate);

        if(mLastlocation != null) {
            //시간 간격
            deltaTime = (location.getTime() - mLastlocation.getTime()) / 1000.0;

            speed = mLastlocation.distanceTo(location) / deltaTime;
            distance = mLastlocation.distanceTo(location);
            all_range += distance;
            /*String formatLastDate = sdf.format(new Date(mLastlocation.getTime()));
            timeView.setText(" " + formatLastDate);  //Last Time
            Log.d("timeView",""+formatLastDate);*/
            double calSpeed = Double.parseDouble(String.format("%.3f", speed));
            speedView.setText(" " + (int)calSpeed *3600/1000 +"km/h") ;  //Cal Speed\
            distancView.setText(  (int)all_range + "m");
            /*distancView.setText(  (int)all_range + "Km");*/
            Log.d("speedView",""+calSpeed);
        }
        mLastlocation = location;

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        if(ActivityCompat.checkSelfPermission(mContext , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER , 1000 , 0,  this);
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onResume(){
        super.onResume();

        if(ActivityCompat.checkSelfPermission(mContext , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER , 1000 , 0,  this);
    }

    @Override
    public void onPause(){
        super.onPause();

        locationManager.removeUpdates(this);
    }

    @Override
    public void onStart(){
        super.onStart();

        if(ActivityCompat.checkSelfPermission(mContext , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission( mContext , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.ACCESS_COARSE_LOCATION} , 100);
                return;
            }

            else{
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.ACCESS_COARSE_LOCATION} , 100);
                return;
            }
        }
    }
}




