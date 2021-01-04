package com.example.tayu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Forecast {
    @SerializedName("list") @Expose private List<List1> list;
    public List<List1> getList() {
        return list;
    }
    public void setList(List<List1> list) {
        this.list = list;
    }


}
class List1{
    @SerializedName("main") @Expose private Main1 main;
    @SerializedName("weather") @Expose private List<Weather1> weather;
    @SerializedName("dt_txt") @Expose private String dt_txt;

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
class Main1{
    @SerializedName("humidity")    @Expose    private Integer humidity;
    @SerializedName("temp_min")    @Expose    private Double tempMin;    @SerializedName("temp_max")
    @Expose    private Double tempMax; @SerializedName("temp")    @Expose    private Double temp;

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Double getTempMin() {
        return tempMin;
    }

    public void setTempMin(Double tempMin) {
        this.tempMin = tempMin;
    }

    public Double getTempMax() {
        return tempMax;
    }

    public void setTempMax(Double tempMax) {
        this.tempMax = tempMax;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }
}
class Weather1{
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @SerializedName("icon")    @Expose    private String icon;

}