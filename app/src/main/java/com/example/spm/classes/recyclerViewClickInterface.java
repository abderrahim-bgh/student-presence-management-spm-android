package com.example.spm.classes;

public interface recyclerViewClickInterface {

    void onYearItemClicked(year y);
    void onClassItemClicked(classe c);
    void onGroupItemClicked(group g);
    void onStudentClicked(student s);
    void onStudentClicked(studentOfSeance s);
    void onStudentUpdated();
    void onStudentLongClick(student s);
    void onStudentOfSeanceLongClick(studentOfSeance s);

}
