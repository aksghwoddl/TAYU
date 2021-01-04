package com.example.tayu;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.TMapView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.ArrayList;
import java.lang.Runnable;
import java.util.List;
import java.util.Locale;

public class Fragment2 extends Fragment implements TMapGpsManager.onLocationChangedCallback, View.OnClickListener {
    ViewGroup viewGroup;

    TMapView mapView;

    LocationManager mLM;
    String mProvider = LocationManager.GPS_PROVIDER;

    EditText keywordView;
    EditText keywordView1;
    ListView listView;
    ListView listView1;
    ArrayAdapter<POI> mAdapter;

    TMapPoint start, end;
    String start1, end1;
    RadioGroup typeView;
    RadioButton rbtn1, rbtn2;


    private Context mContext = null;
    private boolean m_bTrackingMode = true;

    private TMapData tmapdata = null;
    private TMapGpsManager tmapgps = null;
    private TMapView tmapview = null;
    Editable stPoint = null;
    Editable edPoint = null;
    double stX;
    double stY;
    double edX;
    double edY;
    long posi;
    POI poi1;



    private BlankFragment.OnDataSetListener onDataSetListener;


    @Override
    public void onLocationChange(Location location) {
        if (m_bTrackingMode) {
            tmapview.setLocationPoint(location.getLongitude(), location.getLatitude());
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment2, container, false);


        mContext = container.getContext();

        Button btn4 = (Button)viewGroup.findViewById(R.id.btn_now);
        final LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        final LocationListener gpsLocationListener = new LocationListener() {

            public void onLocationChanged(Location location) { //현재위치 주기적으로 갱신

                final double longitude1 = location.getLongitude();
                final double latitude1 = location.getLatitude();

                //searchPOI_2(longitude1,latitude1);

            }
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
            public void onProviderEnabled(String provider) {

            }
            public void onProviderDisabled(String provider) {
            }

        };

        btn4.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 23 &&

                        ContextCompat.checkSelfPermission(mContext.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},

                            0);


                } else {

                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    final double longitude = location.getLongitude();
                    final double latitude = location.getLatitude();
                    searchPOI_2(longitude,latitude);


                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,

                            1000,

                            1,
                            gpsLocationListener
                    );

                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,

                            1000,

                            1,

                            gpsLocationListener);
                }

            }

        });

        typeView = (RadioGroup) viewGroup.findViewById(R.id.radioGroup);
        rbtn1 = (RadioButton) viewGroup.findViewById(R.id.radio_start);
        rbtn2 = (RadioButton) viewGroup.findViewById(R.id.radio_end);
        keywordView = (EditText) viewGroup.findViewById(R.id.edit_keyword);
        keywordView1 = (EditText) viewGroup.findViewById(R.id.edit_keyword1);
        listView = (ListView) viewGroup.findViewById(R.id.listView);
        listView1 = (ListView) viewGroup.findViewById(R.id.listView1);
        mAdapter = new ArrayAdapter<POI>(mContext, android.R.layout.simple_list_item_1);
        listView.setAdapter(mAdapter);
        listView1.setAdapter(mAdapter);

        mLM = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mapView = (TMapView) viewGroup.findViewById(R.id.map_view);
        mapView.setOnApiKeyListener(new TMapView.OnApiKeyListenerCallback() {
            @Override
            public void SKTMapApikeySucceed() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setupMap();
                    }
                });
            }

            @Override
            public void SKTMapApikeyFailed(String s) {

            }
        });

        mapView.setSKTMapApiKey("l7xx71cfd335915b4f6792efa7e150a510b4"); //api키 세팅
        mapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return viewGroup;
        }

        //mapView.setIconVisibility(true);

        Button btn = (Button) viewGroup.findViewById(R.id.btn_search);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView1.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);
                listView.setBackgroundColor(Color.parseColor("#ffffff"));
                searchPOI();
            }
        });

        btn = (Button) viewGroup.findViewById(R.id.btn_search1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setVisibility(View.INVISIBLE);
                listView1.setVisibility(View.VISIBLE);
                listView1.setBackgroundColor(Color.parseColor("#ffffff"));
                searchPOI1();
            }
        });

        btn = (Button) viewGroup.findViewById(R.id.btn_route);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    //시작지점과 종료 지점중 하나라도 널 값이 있다면 토스트 메시지 출력하도록 설정
                //onDataSetListener.onDataSet(""+stPoint,""+stX,""+stY,""+edPoint,""+edX,""+edY);
                if (start != null && end != null) {
                    searchRoute(start, end);
                    start = end = null;
                } else {
                    Toast.makeText(mContext, "start or end is null", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn = (Button) viewGroup.findViewById(R.id.btn_go);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDataSetListener.onDataSet("" + stPoint, stX, stY, "" + edPoint, edX, edY);
            }
        });
        return viewGroup;

    }


    private void searchRoute(TMapPoint start, TMapPoint end) {  //시작지점과 종료지점사이의 거리를 빨간줄로 경로표시 해주는 메소드
        TMapData data = new TMapData();
        data.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, start, end, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(final TMapPolyLine path) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        path.setLineWidth(10);  //선 두께
                        path.setLineColor(Color.RED); //선 색깔
                        mapView.addTMapPath(path); //지도에 선 표시

                    }
                });
            }

        });
        data.findPathDataAllType(TMapData.TMapPathType.PEDESTRIAN_PATH, start, end, new TMapData.FindPathDataAllListenerCallback() {
            @Override
            public void onFindPathDataAll(Document document) {
                Element root = document.getDocumentElement();
                NodeList nodeListPlacemark = root.getElementsByTagName("Document");
                for (int i = 0; i < nodeListPlacemark.getLength(); i++) {
                    NodeList nodeListPlacemarkItem = nodeListPlacemark.item(i).getChildNodes();
                    for (int j = 0; j < nodeListPlacemarkItem.getLength(); j++) {
                        if (nodeListPlacemarkItem.item(j).getNodeName().equals("tmap:totalDistance")) {
                            Log.d("dis", nodeListPlacemarkItem.item(j).getTextContent().trim());
                        }
                        if (nodeListPlacemarkItem.item(j).getNodeName().equals("tmap:totalTime")) {
                            Log.d("time", nodeListPlacemarkItem.item(j).getTextContent().trim());
                        }
                    }
                }
            }
        });
    }

    private void searchPOI() { //키워드를 통해서 티맵으로 주소를 찾는 작업
        TMapData data = new TMapData();
        final String keyword = keywordView.getText().toString();
        if (!TextUtils.isEmpty(keyword)) {
            data.findAllPOI(keyword, new TMapData.FindAllPOIListenerCallback() {
                @Override
                public void onFindAllPOI(final ArrayList<TMapPOIItem> arrayList) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mapView.removeAllMarkerItem();
                            mAdapter.clear();
                            int i = 0;

                            for (TMapPOIItem poi : arrayList) {  //검색한 주소를 list에 더해주는 작업
                                mAdapter.add(new POI(poi));
                            }
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                    poi1 = (POI) listView.getItemAtPosition(position);
                                    keywordView.setText("" + poi1);
                                    posi = listView.getItemIdAtPosition(position);
                                    TMapPOIItem poi = arrayList.get((int) posi);
                                    stX = poi.getPOIPoint().getLatitude();
                                    stY = poi.getPOIPoint().getLongitude();
                                    stPoint = keywordView.getText();
                                    start = poi.getPOIPoint();
                                    moveMap(poi.getPOIPoint().getLatitude(), poi.getPOIPoint().getLongitude());
                                    listView.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    private void searchPOI1() { //키워드를 통해서 티맵으로 주소를 찾는 작업
        TMapData data = new TMapData();
        final String keyword1 = keywordView1.getText().toString();
        if (!TextUtils.isEmpty(keyword1)) {
            data.findAllPOI(keyword1, new TMapData.FindAllPOIListenerCallback() {
                @Override
                public void onFindAllPOI(final ArrayList<TMapPOIItem> arrayList) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mapView.removeAllMarkerItem();
                            mAdapter.clear();
                            int i = 0;
                            for (TMapPOIItem poi : arrayList) {  //검색한 주소를 list에 더해주는 작업
                                mAdapter.add(new POI(poi));
                            }
                            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                    poi1 = (POI) listView1.getItemAtPosition(position);
                                    keywordView1.setText("" + poi1);
                                    posi = listView1.getItemIdAtPosition(position);
                                    TMapPOIItem poi = arrayList.get((int) posi);
                                    edX = poi.getPOIPoint().getLatitude();
                                    edY = poi.getPOIPoint().getLongitude();
                                    edPoint = keywordView1.getText();
                                    end = poi.getPOIPoint();
                                    moveMap(poi.getPOIPoint().getLatitude(), poi.getPOIPoint().getLongitude());
                                    listView1.setVisibility(View.INVISIBLE);
                                }
                            });
                        }

                    });
                }
            });
        }
    }


    public void addMarker(TMapPOIItem poi) { //간단하게 tmap에서 제공하는 마커 지정하는 메소드임. 주소 검색하고 해당 주소들 중에서 하나를 클릭하면 마커지정할수 있도록 도와주는 메소드
        TMapMarkerItem item = new TMapMarkerItem();
        item.setTMapPoint(poi.getPOIPoint());
        Bitmap icon = ((BitmapDrawable) ContextCompat.getDrawable(mContext, android.R.drawable.ic_input_add)).getBitmap();
        item.setIcon(icon);
        item.setPosition(0.5f, 1);
        item.setCalloutTitle(poi.getPOIName());
        item.setCalloutSubTitle(poi.getPOIContent());
        Bitmap left = ((BitmapDrawable) ContextCompat.getDrawable(mContext, android.R.drawable.ic_dialog_alert)).getBitmap();
        item.setCalloutLeftImage(left);
        Bitmap right = ((BitmapDrawable) ContextCompat.getDrawable(mContext, android.R.drawable.ic_input_get)).getBitmap();
        item.setCalloutRightButtonImage(right);
        item.setCanShowCallout(true);
        mapView.addMarkerItem(poi.getPOIID(), item);
    }

    private void addMarker(double lat, double lng, String title) {
        TMapMarkerItem item = new TMapMarkerItem();
        TMapPoint point = new TMapPoint(lat, lng);
        item.setTMapPoint(point);
        Bitmap icon = ((BitmapDrawable) ContextCompat.getDrawable(mContext, android.R.drawable.ic_input_add)).getBitmap();
        item.setIcon(icon);
        item.setPosition(0.5f, 1);
        item.setCalloutTitle(title);
        item.setCalloutSubTitle("sub " + title);
        Bitmap left = ((BitmapDrawable) ContextCompat.getDrawable(mContext, android.R.drawable.ic_dialog_alert)).getBitmap();
        item.setCalloutLeftImage(left);
        Bitmap right = ((BitmapDrawable) ContextCompat.getDrawable(mContext, android.R.drawable.ic_input_get)).getBitmap();
        item.setCalloutRightButtonImage(right);
        item.setCanShowCallout(true);
        mapView.addMarkerItem("m" + id, item);
        id++;
    }


    int id = 0;

    boolean isInitialized = false;

    private void setupMap() {
        isInitialized = true;
        mapView.setMapType(TMapView.MAPTYPE_STANDARD);
        if (cacheLocation != null) {
            moveMap(cacheLocation.getLatitude(), cacheLocation.getLongitude());
            setMyLocation(cacheLocation.getLatitude(), cacheLocation.getLongitude());
        }
        mapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
                String message = null;
                switch (typeView.getCheckedRadioButtonId()) {
                    case R.id.radio_start:
                        start = tMapMarkerItem.getTMapPoint();
                        message = "start";
                        break;
                    case R.id.radio_end:
                        end = tMapMarkerItem.getTMapPoint();
                        message = "end";
                        break;
                }
                Toast.makeText(mContext, message + " setting", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = mLM.getLastKnownLocation(mProvider);
        if (location != null) {
            mListener.onLocationChanged(location);
        }
        mLM.requestSingleUpdate(mProvider, mListener, null);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLM.removeUpdates(mListener);
    }

    Location cacheLocation = null;

    private void moveMap(double lat, double lng) {
        mapView.setCenterPoint(lng, lat);
    }

    private void setMyLocation(double lat, double lng) {
        Bitmap icon = ((BitmapDrawable) ContextCompat.getDrawable(mContext, android.R.drawable.ic_dialog_map)).getBitmap();
        mapView.setIcon(icon);
        mapView.setLocationPoint(lng, lat);
        mapView.setIconVisibility(true);
    }

    LocationListener mListener;

    {
        mListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (isInitialized) {
                    moveMap(location.getLatitude(), location.getLongitude());
                    setMyLocation(location.getLatitude(), location.getLongitude());
                } else {
                    cacheLocation = location;
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BlankFragment.OnDataSetListener) {
            onDataSetListener = (BlankFragment.OnDataSetListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement");
        }
    }

    public void onDetach() {
        super.onDetach();
        onDataSetListener = null;
    }

    @Override
    public void onClick(View v) {

    }

    public static String getAddress(Context mContext, double lat, double lng) {//위 경도 주소 변환 메소드
        String nowAddress = "현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
                //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
                address = geocoder.getFromLocation(lat, lng, 1);

                if (address != null && address.size() > 0) {
                    // 주소 받아오기
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    nowAddress = currentLocationAddress;

                }
            }

        } catch (IOException e) {
            Toast.makeText(mContext, "주소를 가져 올 수 없습니다.", Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
        return nowAddress;
    }

    private void searchPOI_2(double x, double y) { //현재 위치 전달


        double stX = x;
        double stY = y;
        moveMap(stY, stX);
        String address = getAddress(mContext, stY, stX); //위 경도 받아서 address변수에 할당
        keywordView.setText(address);//주소에 매핑
    }



}