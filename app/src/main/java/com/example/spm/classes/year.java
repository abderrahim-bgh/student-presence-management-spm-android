package com.example.spm.classes;

public class year {

    private String Year, idY;
    private boolean isExpandble ;

    public year() {
        this.isExpandble = false;
    }

    public boolean isExpandble() {
        return isExpandble;
    }

    public void setExpandble(boolean expandble) {
        isExpandble = expandble;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getIdY() {
        return idY;
    }

    public void setIdY(String idY) {
        this.idY = idY;
    }
}
