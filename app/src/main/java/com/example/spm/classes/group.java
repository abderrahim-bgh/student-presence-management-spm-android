package com.example.spm.classes;

public class group {
    private String id;
    int idC;
    private String Name,Type;


    private boolean isExpandble ;

    public group() {
        this.isExpandble = false;
    }

    public boolean isExpandble() {
        return isExpandble;
    }

    public void setExpandble(boolean expandble) {
        isExpandble = expandble;
    }


    public group(String id, String name, String type, int idC) {
        this.id = id;
        this.idC = idC;
        Name = name;
        Type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public int getIdC() {
        return idC;
    }

    public void setIdC(int idC) {
        this.idC = idC;
    }
}
