package com.example.tayu.data.model.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Forecast {
    @SerializedName("list")
    @Expose
    private List<List1> list;
    public List<List1> getList() {
        return list;
    }

    public void setList(List<List1> list) {
        this.list = list;
    }
}