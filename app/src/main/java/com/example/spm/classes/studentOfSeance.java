package com.example.spm.classes;

public class studentOfSeance {


    int id, classID,id_G_TD, id_G_TP, nmbr,mat;
    String famName, name,state, seance ;

    public studentOfSeance() {
    }

    /*
    public student(int id , int mat, String famName, String name , int group1 , int group2) {
        this.id = id;
        this.id_G_TD = group1;
        this.id_G_TP = group2;
       /// this.nmbr = n;
        this.famName = famName;
        this.name = name;
        this.mat = mat;
    }

     */


    public studentOfSeance(int id, int mat, String famName, String name , int group1 , int group2) {
        this.id = id;
        this.id_G_TD = group1;
        this.id_G_TP = group2;
        this.famName = famName;
        this.name = name;
        this.mat = mat;
        this.state="NOT YET";
    }


    public studentOfSeance(int id, int mat, String famName, String name , int group1 , int group2, String st) {
        this.id = id;
        this.id_G_TD = group1;
        this.id_G_TP = group2;
        this.famName = famName;
        this.name = name;
        this.mat = mat;
        this.state = st;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public int getId_G_TD() {
        return id_G_TD;
    }

    public void setId_G_TD(int id_G_TD) {
        this.id_G_TD = id_G_TD;
    }

    public int getId_G_TP() {
        return id_G_TP;
    }

    public void setId_G_TP(int id_G_TP) {
        this.id_G_TP = id_G_TP;
    }

    public int getNmbr() {
        return nmbr;
    }

    public void setNmbr(int nmbr) {
        this.nmbr = nmbr;
    }

    public String getFamName() {
        return famName;
    }

    public void setFamName(String famName) {
        this.famName = famName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMat() {
        return mat;
    }

    public void setMat(int mat) {
        this.mat = mat;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSeance() {
        return seance;
    }

    public void setSeance(String seance) {
        this.seance = seance;
    }
}
