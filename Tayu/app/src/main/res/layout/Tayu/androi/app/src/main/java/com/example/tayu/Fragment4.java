package com.example.tayu;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;
import static android.content.Context.LOCATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;


public class Fragment4 extends Fragment {

    FrameLayout frame;
    ViewGroup viewGroup;
    EditText keywordView;
    ListView listView;
    ArrayAdapter<POI> mAdapter;
    private Context mContext = null;
    Editable stPoint = null;
    double stX;
    double stY;
    long posi;
    POI poi1;
    private TMapView tmapview = null;

    LinearLayout li1, li2, li3;
    TextView wtemp, whumi, wtemax, wtemin, now, search;
    TextView wtemp_1, whumi_1, wtemax_1, wtemin_1;
    TextView wtemp_2, whumi_2, wtemax_2, wtemin_2;
    TextView wtemp_3, whumi_3, wtemax_3, wtemin_3;
    TextView wtemp_4, whumi_4, wtemax_4, wtemin_4;
    TextView wtemp_5, whumi_5, wtemax_5, wtemin_5;
    TextView wtemp_6, whumi_6, wtemax_6, wtemin_6;
    ImageView wtIcon, wtIcon_1, wtIcon_2, wtIcon_3, wtIcon_4, wtIcon_5, wtIcon_6;
    String icon;
    private BlankFragment.OnDataSetListener onDataSetListener;
    Handler handler;

    double longitude, latitude;

    public Fragment4() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_4, container, false);
        mContext = container.getContext();
        Button btn1 = (Button) viewGroup.findViewById(R.id.btn_nowweather);
        final LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        final LocationListener gpsLocationListener = new LocationListener() {

            public void onLocationChanged(Location location) { //현재위치 주기적으로 갱신

                final double longitude = location.getLongitude();
                final double latitude = location.getLatitude();

                weatherRun_1(latitude, longitude);

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            public void onProviderEnabled(String provider) {

            }

            public void onProviderDisabled(String provider) {
            }

        };

        btn1.setOnClickListener(new View.OnClickListener() {

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

                    weatherRun_1(latitude, longitude); //현재위치 위도 , 경도 weatherRun_1 로 전달


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

        li1 = (LinearLayout) viewGroup.findViewById(R.id.li1);
        li2 = (LinearLayout) viewGroup.findViewById(R.id.li2);
        li3 = (LinearLayout) viewGroup.findViewById(R.id.li3);
        frame = (FrameLayout) viewGroup.findViewById(R.id.frame);
        keywordView = (EditText) viewGroup.findViewById(R.id.edit_keyword);
        listView = (ListView) viewGroup.findViewById(R.id.listView);
        mAdapter = new ArrayAdapter<POI>(mContext, android.R.layout.simple_list_item_1);
        listView.setAdapter(mAdapter);
        tmapview = new TMapView(mContext);
        tmapview.setSKTMapApiKey("l7xx71cfd335915b4f6792efa7e150a510b4"); //api키 세팅
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);

        //현재위치 날씨값
        now = (TextView) viewGroup.findViewById(R.id.now);
        wtemp = (TextView) viewGroup.findViewById(R.id.wttemp);
        whumi = (TextView) viewGroup.findViewById(R.id.wthu);
        wtemax = (TextView) viewGroup.findViewById(R.id.wttmax);
        wtemin = (TextView) viewGroup.findViewById(R.id.wttmin);
        wtIcon = (ImageView) viewGroup.findViewById(R.id.wticon);

        //검색위치 날씨값
        search = (TextView) viewGroup.findViewById(R.id.search);
        wtemp_1 = (TextView) viewGroup.findViewById(R.id.wttemp_1);
        whumi_1 = (TextView) viewGroup.findViewById(R.id.wthu_1);
        wtIcon_1 = (ImageView) viewGroup.findViewById(R.id.wticon_1);

        //검색위치 날씨값
        wtemp_2 = (TextView) viewGroup.findViewById(R.id.wttemp_2);
        whumi_2 = (TextView) viewGroup.findViewById(R.id.wthu_2);
        wtIcon_2 = (ImageView) viewGroup.findViewById(R.id.wticon_2);

        //검색위치 날씨값
        wtemp_3 = (TextView) viewGroup.findViewById(R.id.wttemp_3);
        whumi_3 = (TextView) viewGroup.findViewById(R.id.wthu_3);
        wtIcon_3 = (ImageView) viewGroup.findViewById(R.id.wticon_3);

//검색위치 날씨값
        wtemp_4 = (TextView) viewGroup.findViewById(R.id.wttemp_4);
        whumi_4 = (TextView) viewGroup.findViewById(R.id.wthu_4);
        wtIcon_4 = (ImageView) viewGroup.findViewById(R.id.wticon_4);

        //검색위치 날씨값
        wtemp_5 = (TextView) viewGroup.findViewById(R.id.wttemp_5);
        whumi_5 = (TextView) viewGroup.findViewById(R.id.wthu_5);
        wtIcon_5 = (ImageView) viewGroup.findViewById(R.id.wticon_5);

        //검색위치 날씨값
        wtemp_6 = (TextView) viewGroup.findViewById(R.id.wttemp_6);
        whumi_6 = (TextView) viewGroup.findViewById(R.id.wthu_6);
        wtIcon_6 = (ImageView) viewGroup.findViewById(R.id.wticon_6);

        //Button btn1 = (Button) viewGroup.findViewById(R.id.btn_nowweather);
        /*btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPOI_1();
                weatherRun_1(stX,stY);
                listView.setVisibility(View.INVISIBLE);
            }
        });*/

        Button btn2 = (Button) viewGroup.findViewById(R.id.btn_search);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setVisibility(View.VISIBLE);
                //listView.setBackgroundColor(Color.parseColor("6699FF"));
                listView.setBackgroundColor(Color.rgb(102, 153, 255));
                li1.setVisibility(View.INVISIBLE);
                li2.setVisibility(View.INVISIBLE);
                li3.setVisibility(View.INVISIBLE);
                now.setVisibility(View.INVISIBLE);
                search.setVisibility(View.INVISIBLE);
                searchPOI();

            }
        });

        Button btn3 = (Button) viewGroup.findViewById(R.id.btn_start);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    //시작지점과 종료 지점중 하나라도 널 값이 있다면 토스트 메시지 출력하도록 설정
                //onDataSetListener.onDataSet(""+stPoint,""+stX,""+stY,""+edPoint,""+edX,""+edY);
                weatherRun(stX, stY);
                listView.setVisibility(View.INVISIBLE);
                li1.setVisibility(View.VISIBLE);
                li2.setVisibility(View.VISIBLE);
                li3.setVisibility(View.VISIBLE);
                now.setVisibility(View.VISIBLE);
                search.setVisibility(View.VISIBLE);
            }
        });


        return viewGroup;
    }

    @Override
    public void onStart() { //프레그먼트 시작하자마자 현재 위치 날씨를 화면에 뿌려주는 역할
        super.onStart();
        final LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        final LocationListener gpsLocationListener = new LocationListener() {

            public void onLocationChanged(Location location) { //현재위치 주기적으로 갱신

                final double longitude = location.getLongitude();
                final double latitude = location.getLatitude();
                weatherRun_1(latitude, longitude);

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            public void onProviderEnabled(String provider) {

            }

            public void onProviderDisabled(String provider) {
            }

        };
        if (Build.VERSION.SDK_INT >= 23 &&

                ContextCompat.checkSelfPermission(mContext.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},

                    0);

        } else {

            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            final double longitude = location.getLongitude();

            final double latitude = location.getLatitude();

            weatherRun_1(latitude, longitude); //현재위치 위도 , 경도 weatherRun_1 로 전달


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

    weatherRun_1(latitude,longitude);
    }

    private void searchPOI() {
        TMapData data = new TMapData();
        final String keyword = keywordView.getText().toString();
        listView.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(keyword)) {
            data.findAllPOI(keyword, new TMapData.FindAllPOIListenerCallback() {
                @Override
                public void onFindAllPOI(final ArrayList<TMapPOIItem> arrayList) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.clear();

                            for (TMapPOIItem poi : arrayList) {
                                mAdapter.add(new POI(poi));
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                        poi1 = (POI) listView.getItemAtPosition(position);
                                        keywordView.setText(""+poi1);
                                        posi=listView.getItemIdAtPosition(position);
                                        Toast.makeText(mContext,""+poi1,Toast.LENGTH_SHORT).show();
                                        TMapPOIItem poi = arrayList.get((int)posi);
                                        stX=poi.getPOIPoint().getLatitude();
                                        stY=poi.getPOIPoint().getLongitude();
                                        //Toast.makeText(mContext, "x:" +stX+"y:"+stY, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }
    }


    /*public void weatherRun(double x,double y){
        Retrofit client=new Retrofit.Builder().baseUrl("http://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiInterface service=client.create(ApiInterface.class);
        Call<WeatherTest> call=service.getWeather("b0ea95fa326c0d43e5c822aff3c99947",Double.valueOf(stX),Double.valueOf(stY));
        call.enqueue(new Callback<WeatherTest>() {
            @Override
            public void onResponse(Call<WeatherTest> call, Response<WeatherTest> response) {
                if(response.isSuccessful()){
                    WeatherTest repo=response.body();
                    List<Weather>weathers=repo.getWeather();
                    icon=weathers.get(0).getIcon();
                    wtemp_1.setText("현재 온도 : "+Math.round(repo.getMain().getTemp()-273.15)*10/10.0);
                    whumi_1.setText("  습 도  : "+repo.getMain().getHumidity());
                    wtemax_1.setText("최고 온도 : "+Math.round(repo.getMain().getTempMax()-273.15)*10/10.0);
                    wtemin_1.setText("최저 온도 : "+Math.round(repo.getMain().getTempMin()-273.15)*10/10.0);
                    handler=new Handler();
                    Thread t=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL url=new URL("http://openweathermap.org/img/w/"+icon+".png");
                                InputStream is=url.openStream();
                                final Bitmap bitmap=BitmapFactory.decodeStream(is);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        wtIcon_1.setImageBitmap(bitmap);
                                    }
                                });
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    t.start();
                }else{
                }
            }
            @Override
            public void onFailure(Call<WeatherTest> call, Throwable t) {
            }
        });

    }*/
    public void weatherRun(double x,double y){// 검색 위치 날씨
        Retrofit client=new Retrofit.Builder().baseUrl("http://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiInterface service=client.create(ApiInterface.class);
        Call<WeatherTest> call=service.getWeather("b0ea95fa326c0d43e5c822aff3c99947",Double.valueOf(stX),Double.valueOf(stY));
        call.enqueue(new Callback<WeatherTest>() {
            @Override
            public void onResponse(Call<WeatherTest> call, Response<WeatherTest> response) {
                if(response.isSuccessful()){
                    WeatherTest repo=response.body();
                    Log.d("tag","wweeee");
                    List<Weather>weathers=repo.getWeather();
                    Log.d("ttt",""+weathers.get(0).getIcon());
                    icon=weathers.get(0).getIcon();
                    now.setText("검색 위치 현재 날씨");
                    search.setText("시간대별 검색 위치 날씨");
                    wtemp.setText("현재 온도 : "+Math.round(repo.getMain().getTemp()-273.15)*10/10.0);
                    whumi.setText("  습 도  : "+repo.getMain().getHumidity());
                    wtemax.setText("최고 온도 : "+Math.round(repo.getMain().getTempMax()-273.15)*10/10.0);
                    wtemin.setText("최저 온도 : "+Math.round(repo.getMain().getTempMin()-273.15)*10/10.0);
                    Toast.makeText(mContext,"검색 위치 날씨는 먼저 찾고 검색하세요",Toast.LENGTH_LONG).show();
                    handler=new Handler();
                    Thread t=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL url=new URL("http://openweathermap.org/img/w/"+icon+".png");
                                InputStream is=url.openStream();
                                final Bitmap bitmap=BitmapFactory.decodeStream(is);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        wtIcon.setImageBitmap(bitmap);
                                        Log.d("test","suss");
                                    }
                                });
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    t.start();
                }else{
                }
            }
            @Override
            public void onFailure(Call<WeatherTest> call, Throwable t) {
                Log.d("tag","wwwwwwww");
            }
        });
        Call<Forecast> call1=service.getForecast("b0ea95fa326c0d43e5c822aff3c99947",Double.valueOf(stX),Double.valueOf(stY));
        call1.enqueue(new Callback<Forecast>() {
            @Override
            public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                if(response.isSuccessful()){
                    Forecast repo1=response.body();
                    final List<List1>list1s=repo1.getList();
                    wtemp_1.setText(list1s.get(0).getDt_txt().substring(11,13)+"시");
                    whumi_1.setText(String.valueOf(Math.round(list1s.get(0).getMain().getTemp()-273.15)*10/10.0)+"도");
                    wtemp_2.setText(list1s.get(1).getDt_txt().substring(11,13)+"시");
                    whumi_2.setText(String.valueOf(Math.round(list1s.get(1).getMain().getTemp()-273.15)*10/10.0)+"도");
                    wtemp_3.setText(list1s.get(2).getDt_txt().substring(11,13)+"시");
                    whumi_3.setText(String.valueOf(Math.round(list1s.get(2).getMain().getTemp()-273.15)*10/10.0)+"도");
                    wtemp_4.setText(list1s.get(3).getDt_txt().substring(11,13)+"시");
                    whumi_4.setText(String.valueOf(Math.round(list1s.get(3).getMain().getTemp()-273.15)*10/10.0)+"도");
                    wtemp_5.setText(list1s.get(4).getDt_txt().substring(11,13)+"시");
                    whumi_5.setText(String.valueOf(Math.round(list1s.get(4).getMain().getTemp()-273.15)*10/10.0)+"도");
                    wtemp_6.setText(list1s.get(5).getDt_txt().substring(11,13)+"시");
                    whumi_6.setText(String.valueOf(Math.round(list1s.get(5).getMain().getTemp()-273.15)*10/10.0)+"도");
                    handler=new Handler();
                    Thread t=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL url=new URL("http://openweathermap.org/img/w/"+list1s.get(0).getWeather().get(0).getIcon()+".png");
                                InputStream is=url.openStream();
                                final Bitmap bitmap=BitmapFactory.decodeStream(is);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        wtIcon_1.setImageBitmap(bitmap);
                                    }
                                });
                                URL url1=new URL("http://openweathermap.org/img/w/"+list1s.get(1).getWeather().get(0).getIcon()+".png");
                                InputStream is1=url1.openStream();
                                final Bitmap bitmap1=BitmapFactory.decodeStream(is1);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        wtIcon_2.setImageBitmap(bitmap1);
                                    }
                                });
                                URL url2=new URL("http://openweathermap.org/img/w/"+list1s.get(2).getWeather().get(0).getIcon()+".png");
                                InputStream is2=url2.openStream();
                                final Bitmap bitmap2=BitmapFactory.decodeStream(is2);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        wtIcon_3.setImageBitmap(bitmap2);
                                    }
                                });
                                URL url3=new URL("http://openweathermap.org/img/w/"+list1s.get(3).getWeather().get(0).getIcon()+".png");
                                InputStream is3=url3.openStream();
                                final Bitmap bitmap3=BitmapFactory.decodeStream(is3);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        wtIcon_4.setImageBitmap(bitmap3);
                                    }
                                });
                                URL url4=new URL("http://openweathermap.org/img/w/"+list1s.get(4).getWeather().get(0).getIcon()+".png");
                                InputStream is4=url4.openStream();
                                final Bitmap bitmap4=BitmapFactory.decodeStream(is4);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        wtIcon_5.setImageBitmap(bitmap4);
                                    }
                                });
                                URL url5=new URL("http://openweathermap.org/img/w/"+list1s.get(5).getWeather().get(0).getIcon()+".png");
                                InputStream is5=url5.openStream();
                                final Bitmap bitmap5=BitmapFactory.decodeStream(is5);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        wtIcon_6.setImageBitmap(bitmap5);
                                    }
                                });
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    t.start();
                }
            }

            @Override
            public void onFailure(Call<Forecast> call, Throwable t) {

            }
        });

    }
    public void weatherRun_1(double x,double y){ //현재날씨 검색
        Retrofit client=new Retrofit.Builder().baseUrl("http://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiInterface service=client.create(ApiInterface.class);
        Call<WeatherTest> call=service.getWeather("b0ea95fa326c0d43e5c822aff3c99947",Double.valueOf(x),Double.valueOf(y));
        call.enqueue(new Callback<WeatherTest>() {
            @Override
            public void onResponse(Call<WeatherTest> call, Response<WeatherTest> response) {
                if(response.isSuccessful()){
                    WeatherTest repo=response.body();
                    List<Weather>weathers=repo.getWeather();
                    icon=weathers.get(0).getIcon();
                    now.setText("현재 위치 날씨");
                    search.setText("시간대별 현재 위치 날씨");
                    wtemp.setText("현재 온도 : "+Math.round(repo.getMain().getTemp()-273.15)*10/10.0);
                    whumi.setText("  습 도  : "+repo.getMain().getHumidity());
                    wtemax.setText("최고 온도 : "+Math.round(repo.getMain().getTempMax()-273.15)*10/10.0);
                    wtemin.setText("최저 온도 : "+Math.round(repo.getMain().getTempMin()-273.15)*10/10.0);
                    handler=new Handler();
                    Thread t=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL url=new URL("http://openweathermap.org/img/w/"+icon+".png");
                                InputStream is=url.openStream();
                                final Bitmap bitmap=BitmapFactory.decodeStream(is);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        wtIcon.setImageBitmap(bitmap);
                                    }
                                });
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    t.start();
                }else{
                }
            }
            @Override
            public void onFailure(Call<WeatherTest> call, Throwable t) {
                Log.d("tag","wwwwwwww");
            }
        });
        Call<Forecast> call1=service.getForecast("b0ea95fa326c0d43e5c822aff3c99947",Double.valueOf(x),Double.valueOf(y));
        call1.enqueue(new Callback<Forecast>() {
            @Override
            public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                if(response.isSuccessful()){
                    Forecast repo1=response.body();
                    final List<List1>list1s=repo1.getList();
                    wtemp_1.setText(list1s.get(0).getDt_txt().substring(11,13)+"시");
                    whumi_1.setText(String.valueOf(Math.round(list1s.get(0).getMain().getTemp()-273.15)*10/10.0)+"도");
                    wtemp_2.setText(list1s.get(1).getDt_txt().substring(11,13)+"시");
                    whumi_2.setText(String.valueOf(Math.round(list1s.get(1).getMain().getTemp()-273.15)*10/10.0)+"도");
                    wtemp_3.setText(list1s.get(2).getDt_txt().substring(11,13)+"시");
                    whumi_3.setText(String.valueOf(Math.round(list1s.get(2).getMain().getTemp()-273.15)*10/10.0)+"도");
                    wtemp_4.setText(list1s.get(3).getDt_txt().substring(11,13)+"시");
                    whumi_4.setText(String.valueOf(Math.round(list1s.get(3).getMain().getTemp()-273.15)*10/10.0)+"도");
                    wtemp_5.setText(list1s.get(4).getDt_txt().substring(11,13)+"시");
                    whumi_5.setText(String.valueOf(Math.round(list1s.get(4).getMain().getTemp()-273.15)*10/10.0)+"도");
                    wtemp_6.setText(list1s.get(5).getDt_txt().substring(11,13)+"시");
                    whumi_6.setText(String.valueOf(Math.round(list1s.get(5).getMain().getTemp()-273.15)*10/10.0)+"도");
                    handler=new Handler();
                    Thread t=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL url=new URL("http://openweathermap.org/img/w/"+list1s.get(0).getWeather().get(0).getIcon()+".png");
                                InputStream is=url.openStream();
                                final Bitmap bitmap=BitmapFactory.decodeStream(is);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        wtIcon_1.setImageBitmap(bitmap);
                                    }
                                });
                                URL url1=new URL("http://openweathermap.org/img/w/"+list1s.get(1).getWeather().get(0).getIcon()+".png");
                                InputStream is1=url1.openStream();
                                final Bitmap bitmap1=BitmapFactory.decodeStream(is1);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        wtIcon_2.setImageBitmap(bitmap1);
                                    }
                                });
                                URL url2=new URL("http://openweathermap.org/img/w/"+list1s.get(2).getWeather().get(0).getIcon()+".png");
                                InputStream is2=url2.openStream();
                                final Bitmap bitmap2=BitmapFactory.decodeStream(is2);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        wtIcon_3.setImageBitmap(bitmap2);
                                    }
                                });
                                URL url3=new URL("http://openweathermap.org/img/w/"+list1s.get(3).getWeather().get(0).getIcon()+".png");
                                InputStream is3=url3.openStream();
                                final Bitmap bitmap3=BitmapFactory.decodeStream(is3);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        wtIcon_4.setImageBitmap(bitmap3);
                                    }
                                });
                                URL url4=new URL("http://openweathermap.org/img/w/"+list1s.get(4).getWeather().get(0).getIcon()+".png");
                                InputStream is4=url4.openStream();
                                final Bitmap bitmap4=BitmapFactory.decodeStream(is4);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        wtIcon_5.setImageBitmap(bitmap4);
                                    }
                                });
                                URL url5=new URL("http://openweathermap.org/img/w/"+list1s.get(5).getWeather().get(0).getIcon()+".png");
                                InputStream is5=url5.openStream();
                                final Bitmap bitmap5=BitmapFactory.decodeStream(is5);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        wtIcon_6.setImageBitmap(bitmap5);
                                    }
                                });
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    t.start();
                }
            }

            @Override
            public void onFailure(Call<Forecast> call, Throwable t) {

            }
        });

    }
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof BlankFragment.OnDataSetListener){
            onDataSetListener=(BlankFragment.OnDataSetListener) context;
        }else{
            throw new RuntimeException(context.toString()
                    + "must implement");
        }
    }
    public void onDetach(){
        super.onDetach();
        onDataSetListener=null;
    }
    
}