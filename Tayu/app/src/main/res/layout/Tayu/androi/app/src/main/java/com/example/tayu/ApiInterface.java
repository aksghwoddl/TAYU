package com.example.tayu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("/data/2.5/weather")
    Call<WeatherTest> getWeather(@Query("appid")String appid,@Query("lat") double lat,
                                 @Query("lon") double lon);
    @GET("/data/2.5/forecast")
    Call<Forecast> getForecast(@Query("appid")String appid,@Query("lat") double lat,
                               @Query("lon") double lon);

}