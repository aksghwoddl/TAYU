package com.example.tayu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.TMapView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Timer;
import java.util.TimerTask;

public class TmapTest1 extends AppCompatActivity
        implements TMapGpsManager.onLocationChangedCallback
{
    private Context mContext = null;
    private boolean m_bTrackingMode = true;

    private TMapGpsManager tmapgps = null;
    private TMapView tmapview = null;
    private static String mApiKey = "l7xx71cfd335915b4f6792efa7e150a510b4";

    int zoom = 18;
    String start;
    Double stX,stY=null;
    String end;
    Double edX,edY=null;
    TMapPoint st,ed,st1,ed1;

    double bb;
    TextView textView1;
    TextView textView2;
    private Button now,plus,minus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmap_test1);
        mContext = this;

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final LocationListener gpsLocationListener = new LocationListener() {

            public void onLocationChanged(Location location) { //현재위치 주기적으로 갱신

                final double longitude = location.getLongitude();
                final double latitude = location.getLatitude();


            }
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
            public void onProviderEnabled(String provider) {

            }
            public void onProviderDisabled(String provider) {
            }

        };

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.mapview);
        tmapview = new TMapView(this);
        linearLayout.addView(tmapview);
        tmapview.setSKTMapApiKey(mApiKey);
        tmapview.setCompassMode(true);
        /* 현위치 아이콘표시 */
        tmapview.setIconVisibility(true);

        /* 줌레벨 */
        tmapview.setZoomLevel(18);
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);

        tmapgps = new TMapGpsManager(TmapTest1.this);
        tmapgps.setMinTime(10);
        tmapgps.setMinDistance(5);
        tmapgps.setProvider(tmapgps.NETWORK_PROVIDER); //연결된 인터넷으로 현 위치를 받습니다.
        //실내일 때 유용합니다.
        // tmapgps.setProvider(tmapgps.GPS_PROVIDER); //gps로 현 위치를 잡습니다.
        tmapgps.OpenGps();

        /*  화면중심을 단말의 현재위치로 이동 */
        tmapview.setTrackingMode(true);
        tmapview.setSightVisible(true);

        textView1=(TextView)findViewById(R.id.start1);


        Intent intent=getIntent();
        start=intent.getStringExtra("stPoint");
        stX=intent.getDoubleExtra("stX",0);
        stY=intent.getDoubleExtra("stY",0);
        end=intent.getStringExtra("edPoint");
        edX=intent.getDoubleExtra("edX",0);
        edY=intent.getDoubleExtra("edY",0);

        //textView1.setText("시작: "+start+"X: "+stX+"y: "+stY);
        //textView2.setText("도착: "+end+"X: "+edX+"y: "+edY);
        st=new TMapPoint(stX,stY);
        ed=new TMapPoint(edX,edY);
        searchRoute(st,ed);
        Timer timer=new Timer();
        TimerTask TT=new TimerTask() {
            @Override
            public void run() {
                RouteInfo(st1,ed);
            }
        };
        timer.schedule(TT,0,5000);


        Button now = (Button) findViewById(R.id.now);
        now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tmapview.setZoomLevel(18);
                tmapview.setTrackingMode(true);
                tmapview.setSightVisible(true);
            }
            });



        Button plus = (Button) findViewById(R.id.plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tmapview.setZoomLevel(++zoom);

            }
        });

        Button minus = (Button) findViewById(R.id.minus);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tmapview.setZoomLevel(--zoom);
            }
        });


    }
    private void searchRoute(TMapPoint start, TMapPoint end){  //시작지점과 종료지점사이의 거리를 빨간줄로 경로표시 해주는 메소드
        TMapData data = new TMapData();
        data.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH,start, end, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(final TMapPolyLine path) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        path.setLineWidth(10);  //선 두께
                        path.setLineColor(Color.RED); //선 색깔
                        tmapview.addTMapPath(path); //지도에 선 표시

                    }
                });
            }

        });
    }
    private void RouteInfo(TMapPoint start,TMapPoint end){
        TMapData data= new TMapData();
        data.findPathDataAllType(TMapData.TMapPathType.PEDESTRIAN_PATH,start,end,new TMapData.FindPathDataAllListenerCallback(){
            @Override
            public void onFindPathDataAll(Document document) {
                Element root=document.getDocumentElement();
                NodeList nodeListPlacemark=root.getElementsByTagName("Document");
                loopOut:
                for(int i=0;i<nodeListPlacemark.getLength();i++){
                    NodeList nodeListPlacemarkItem=nodeListPlacemark.item(i).getChildNodes();
                    for(int j=0;j<nodeListPlacemarkItem.getLength();j++){
                        if(nodeListPlacemarkItem.item(j).getNodeName().equals("Placemark")){
                            Log.d("pl",nodeListPlacemarkItem.item(j).getTextContent().trim());
                            NodeList nodeListPlacemark1=root.getElementsByTagName("Placemark");
                            for(int l=0;l<nodeListPlacemark1.getLength();l++) {
                                NodeList nodeListPlacemarkItem1 = nodeListPlacemark1.item(l).getChildNodes();
                                for (int k = 0; k < nodeListPlacemarkItem1.getLength(); k++) {
                                    if (nodeListPlacemarkItem1.item(k).getNodeName().equals("description")) {
                                        String des = nodeListPlacemarkItem1.item(k).getTextContent().trim();
                                        Log.d("string", des);
                                        textView1.setText(""+des);
                                        break loopOut;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onLocationChange(Location location) {
        TMapData data = new TMapData();
        if (m_bTrackingMode) {
            tmapview.setLocationPoint(location.getLongitude(), location.getLatitude());
        }
        final double loX=location.getLatitude();
        final double loY=location.getLongitude();
        TMapPoint lo=new TMapPoint(loX,loY);
        st1=new TMapPoint(loX,loY);
        //searchRoute(st1,ed);
                /*data.findPathDataAllType(TMapData.TMapPathType.PEDESTRIAN_PATH,lo,ed,new TMapData.FindPathDataAllListenerCallback(){
                    @Override
                    public void onFindPathDataAll(Document document) {
                        Element root=document.getDocumentElement();
                        NodeList nodeListPlacemark=root.getElementsByTagName("Document");
                        for(int i=0;i<nodeListPlacemark.getLength();i++){
                            NodeList nodeListPlacemarkItem=nodeListPlacemark.item(i).getChildNodes();
                            for(int j=0;j<nodeListPlacemarkItem.getLength();j++){
                                if(nodeListPlacemarkItem.item(j).getNodeName().equals("Placemark")){
                                    Log.d("pl",nodeListPlacemarkItem.item(j).getTextContent().trim());
                                    NodeList nodeListPlacemark1=root.getElementsByTagName("Placemark");
                                    for(int l=0;l<nodeListPlacemark1.getLength();l++) {
                                        NodeList nodeListPlacemarkItem1 = nodeListPlacemark1.item(l).getChildNodes();
                                        for (int k = 0; k < nodeListPlacemarkItem1.getLength(); k++) {
                                            if (nodeListPlacemarkItem1.item(k).getNodeName().equals("description")) {
                                                Log.d("des1", nodeListPlacemarkItem1.item(k).getTextContent().trim());
                                                String des = nodeListPlacemarkItem1.item(k).getTextContent().trim();
                                                Log.d("string1", des);
                                                Log.d("kk",""+k);
                                                textView1.setText(des);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                });*/
    }
}