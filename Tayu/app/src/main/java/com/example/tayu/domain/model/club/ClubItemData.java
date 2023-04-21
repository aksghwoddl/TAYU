package com.example.tayu.domain.model.club;

public class ClubItemData {
    private int img;
    private String strTitle;
    private String strSize;
    private String strLoc;
    private String strCon;


    public ClubItemData(int img, String strTitle, String strSize, String strLoc, String strCon){
        this.img=img;
        this.strTitle=strTitle;
        this.strSize=strSize;
        this.strLoc=strLoc;
        this.strCon=strCon;
    }

    public String getStrCon() {
        return strCon;
    }

    public int getImg() {
        return this.img;
    }

    public String getStrTitle() {
        return this.strTitle;
    }

    public String getStrSize() {
        return this.strSize;
    }

    public String getStrLoc() {
        return strLoc;
    }

}