package com.example.tayu.data.model.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class List1{
    @SerializedName("main")
    @Expose
    private Main1 main;

    @SerializedName("weather")
    @Expose
    private List<Weather1> weather;

    @SerializedName("dt_txt")
    @Expose
    private String dt_txt;

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }

    public Main1 getMain() {
        return main;
    }

    public void setMain(Main1 main) {
        this.main = main;
    }

    public List<Weather1> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather1> weather) {
        this.weather = weather;
    }

}
