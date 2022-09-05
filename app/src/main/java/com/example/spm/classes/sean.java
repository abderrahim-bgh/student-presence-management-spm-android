package com.example.spm.classes;

public class sean {


    String id;
    String id_G;

    String gType;
    String date, nameSeance, h1,h2;

    String called;

    public String getgType() {
        return gType;
    }

    public void setgType(String gType) {
        this.gType = gType;
    }


    public sean() {
    }




    public sean(String id, String id_G, String date, String nameSeance , String h_bg , String h_end) {
        this.id = id;
        this.id_G= id_G;
        this.nameSeance= nameSeance;
        this.date= date;
        this.h1= h_bg;
        this.h2=h_end;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_G() {
        return id_G;
    }

    public void setId_G(String id_G) {
        this.id_G = id_G;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNameSeance() {
        return nameSeance;
    }

    public void setNameSeance(String nameSeance) {
        this.nameSeance = nameSeance;
    }

    public String getH1() {
        return h1;
    }

    public void setH1(String h1) {
        this.h1 = h1;
    }

    public String getH2() {
        return h2;
    }

    public void setH2(String h2) {
        this.h2 = h2;
    }

    public String getCalled() {
        return called;
    }

    public void setCalled(String called) {
        this.called = called;
    }
}
