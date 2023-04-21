package com.example.tayu.data.model.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Main1 {
    @SerializedName("humidity")    @Expose
    private Integer humidity;
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
