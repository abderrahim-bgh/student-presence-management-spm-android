package com.example.spm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.spm.adapters.studentAdapter;
import com.example.spm.classes.onDialogDissmissed;
import com.example.spm.classes.student;
import com.example.spm.toucheHelpers.studentTouchHelper;

import java.util.ArrayList;
import java.util.List;

public class StudentsActivity extends AppCompatActivity implements onDialogDissmissed {

    private List<student> studentList;
    studentAdapter adapter;
    RecyclerView studentRecycler;
    ImageButton addStudent;
    Dialog addStudentDialog;
    private Intent data;
    MuDataBase1 db;
    String cId;
    String gId, gType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        studentRecycler = findViewById(R.id.students_RecyclerView);
        addStudent= findViewById(R.id.addStudent);
        addStudentDialog = new Dialog(StudentsActivity.this);

        data = getIntent();

        cId= data.getStringExtra("CLASS_ID");
        gId = data.getStringExtra("GROUP_ID");
        gType = data.getStringExtra("GROUP_TYPE");

        db = new MuDataBase1(StudentsActivity.this);

        studentList= new ArrayList<>();
        studentList = db.getstudent(Integer.parseInt(gId),gType);
        adapter = new studentAdapter(this,studentList);
        adapter.getGroup(gId);
        studentRecycler.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new studentTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(studentRecycler);

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddStudentDialog();
            }
        });




    }

    private void openAddStudentDialog (){
        addStudentDialog.setContentView(R.layout.add_student_dialog);
        addStudentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button cancel = addStudentDialog.findViewById(R.id.cncl_btn_addstudent);
        Button add = addStudentDialog.findViewById(R.id.add_btn_addstudent_dialog);
        EditText studentMat = addStudentDialog.findViewById(R.id.student_mat_e_text);
        EditText studentFamName = addStudentDialog.findViewById(R.id.student_familyNAme_e_text);
        EditText studentName = addStudentDialog.findViewById(R.id.student_name_e_text);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  onDismissDialog(year);
                addStudentDialog.dismiss();
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = studentName.getText().toString();
                String famname = studentFamName.getText().toString();
                String m = studentMat.getText().toString();

                if (name.isEmpty() && famname.isEmpty() && m.isEmpty() ){
                    studentName.setError(" please enter a student name");
                    studentFamName.setError(" please enter a Family name ");
                    studentMat.setError(" please enter a matrecule nmbr");
                }
                else if (name.isEmpty()) {
                    studentName.setError(" name must be at least 3 letters");
                } else if (name.length() < 2) {
                    studentName.setError(" name must be at least 3 letters");
                } else if (famname.isEmpty()) {
                    studentFamName.setError("Family name name must be at least 3 letters");
                } else if (studentFamName.length() < 2) {
                    studentFamName.setError(" name must be at least 3 letters");
                } else if (m.isEmpty()) {
                    studentMat.setError(" please enter a matrecule nmbr");
                } else {

                    int mat = Integer.parseInt(m);
                    int n = db.getstudent(Integer.parseInt(gId), gType).size() + 1;
                    student a = db.studentByMatExist(mat);
                    boolean b = false;
                    student s = new student(-1, mat, famname, name, 0, 0);

                    b = db.insertStudent(s, gType, gId);

                    if (b) {
                        Toast.makeText(StudentsActivity.this, "student added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(StudentsActivity.this, "failed student", Toast.LENGTH_SHORT).show();
                    }


                 //   onDismissDialog(Integer.parseInt(gId));
                    addStudentDialog.dismiss();

                }


            }


        });

        addStudentDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Activity activity = StudentsActivity.this;
                if (activity instanceof onDialogDissmissed){
                    ((onDialogDissmissed)activity).onAddStudentDialogueDismised(dialog);
                }
            }
        });

        addStudentDialog.show();

    }


    /*
    public void onDismissDialog(int y){
        studentList.clear();
        studentList = db.getstudent(y,gType);
        adapter.setStudentList(studentList);

    }


     */







    @Override
    public void onStudentUpdated(DialogInterface dialogInterface) {
        studentList.clear();
        studentList = db.getstudent(Integer.parseInt(gId),gType);
        adapter.setStudentList(studentList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAddStudentDialogueDismised(DialogInterface dialogInterface) {
        studentList.clear();
        studentList = db.getstudent(Integer.parseInt(gId),gType);
        adapter.setStudentList(studentList);
        adapter.notifyDataSetChanged();
    }
}