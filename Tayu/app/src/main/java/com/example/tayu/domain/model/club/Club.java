package com.example.tayu.domain.model.club;

public class Club {

    public String strTitle;
    public String strSize;
    public String strLoc;
    public String strCon;


    public Club(){

    }

    public Club(String strTitle, String strSize, String strLoc, String strCon ){
        this.strTitle=strTitle;
        this.strSize=strSize;
        this.strLoc=strLoc;
        this.strCon=strCon;
    }


    public String getStrCon() {
        return strCon;
    }

    public void setStrCon(String strCon) {
        this.strCon = strCon;
    }

    public String getStrTitle() {
        return strTitle;
    }

    public void setStrTitle(String strTitle) {
        this.strTitle = strTitle;
    }

    public String getStrSize() {
        return strSize;
    }

    public void setStrSize(String strSize) {
        this.strSize = strSize;
    }

    public String getStrLoc() {
        return strLoc;
    }

    public void setStrLoc(String strLoc) {
        this.strLoc = strLoc;
    }
    public String toString(){
        return "Club{" +
                "ClubTitle= '" + strTitle + '\'' +
                ", ClubSize= '"+ strSize + '\'' +
                ", ClubLoc= '"+ strLoc + '\'' +
                ", ClubContent= '"+ strCon + '\'' +
                '}';
    }
}