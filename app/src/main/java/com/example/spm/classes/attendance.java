package com.example.spm.classes;

public class attendance {
    int id, studentId, seanceId,idGroup;
    String state;



    public attendance() {

    }

    public attendance(int id, int studentId, int seanceId, String state, int idG) {
        this.id = id;
        this.studentId = studentId;
        this.seanceId = seanceId;
        this.state = state;
        this.idGroup = idG;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getSeanceId() {
        return seanceId;
    }

    public void setSeanceId(int seanceId) {
        this.seanceId = seanceId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }
}
