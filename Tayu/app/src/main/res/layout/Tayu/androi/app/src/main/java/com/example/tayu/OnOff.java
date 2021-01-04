package com.example.tayu;

public class OnOff { //onoff 파이어베이스 데이터베이스에 저장하기 위한 클래스 변수
    public int onoff;

    public OnOff() {
    }

    public OnOff(int onoff){
        this.onoff=onoff;

    }



    public int getOnoff() {
        return onoff;
    }

    public void setOnoff(int onoff) {
        this.onoff = onoff;
    }
}

