package com.example.spm;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spm.adapters.studentOfSeanceAdapter;
import com.example.spm.adapters.yearsAdapterChangeGroup;
import com.example.spm.classes.attendance;
import com.example.spm.classes.onDialogDissmissed;
import com.example.spm.classes.student;
import com.example.spm.classes.studentOfSeance;
import com.example.spm.classes.year;
import com.example.spm.toucheHelpers.studentOfSeanceTouchHeleper;
import com.example.spm.toucheHelpers.studentTouchHelper;

import java.util.ArrayList;
import java.util.List;

public class StudentsOfSeanceActivity extends AppCompatActivity implements onDialogDissmissed {

    private List<studentOfSeance> studentList;
    studentOfSeanceAdapter adapter;
    RecyclerView studentRecycler;
    ImageButton addStudent;
    ImageView startCall;
    Dialog addStudentDialog, attendanceDialog , sameGrpDialog;
    private Intent data;
    MuDataBase1 db;
    String cId;
    String gId, gType, seanceId;
    boolean called = true;
    int i = 0;
    List<studentOfSeance> restList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_of_seance);



        studentRecycler = findViewById(R.id.students_RecyclerView);
        addStudent= findViewById(R.id.addStudent);
        addStudentDialog = new Dialog(StudentsOfSeanceActivity.this);
        startCall = findViewById(R.id.startCall);


        attendanceDialog = new Dialog(StudentsOfSeanceActivity.this);
        sameGrpDialog = new Dialog(StudentsOfSeanceActivity.this);


        data = getIntent();

        gId = data.getStringExtra("GROUP_ID");
        gType= data.getStringExtra("GROUP_type");
        db = new MuDataBase1(StudentsOfSeanceActivity.this);
        called = data.getBooleanExtra("CALLED",true);
        seanceId = data.getStringExtra("SEANCE_ID");

        studentList= new ArrayList<>();
        studentList = db.getstudentOfseance(Integer.parseInt(gId),gType);
        adapter = new studentOfSeanceAdapter(this,studentList);
        studentRecycler.setAdapter(adapter);
        adapter.setSeanceId(seanceId);
        adapter.getGroup(gId);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new studentOfSeanceTouchHeleper(adapter));
        itemTouchHelper.attachToRecyclerView(studentRecycler);



        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddStudentDialog();
            }
        });



        startCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAttendanceCall();
            }
        });





        for (int c = 0 ; c<studentList.size(); c++){
            studentOfSeance s = studentList.get(c);
            attendance a = db.getStudentAttendance(seanceId,s.getId());
            if (a.getState()==null){
                restList.add(s);
            }
        }
        if (restList.size()==0){
            startCall.setVisibility(View.INVISIBLE);
        } else {
            db.updateSeanceCall(Integer.parseInt(seanceId), false);
        }

        if (!called){
            startAttendanceCall();
        }






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
                        Toast.makeText(StudentsOfSeanceActivity.this, "student added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(StudentsOfSeanceActivity.this, "failed student", Toast.LENGTH_SHORT).show();
                    }


                 //   onDismissDialog(Integer.parseInt(gId));
                    addStudentDialog.dismiss();

                }


            }


        });


        addStudentDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Activity activity = StudentsOfSeanceActivity.this;
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
        studentList = db.getstudentOfseance(y,gType);
        adapter.setStudentList(studentList);

    }

 */


    private void startAttendanceCall (){
        int absenceN , justifiedN;



        attendanceDialog.setContentView(R.layout.attendance_dialog);
        attendanceDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button absent = attendanceDialog.findViewById(R.id.attendance_dialogu_absent);
        Button present = attendanceDialog.findViewById(R.id.attendance_dialog_present);
        TextView studentName = attendanceDialog.findViewById(R.id.attendance_student_name);
        TextView absenceNmbr = attendanceDialog.findViewById(R.id.exclue_txt);
        TextView justifiedNmbr = attendanceDialog.findViewById(R.id.justified_absence_txt);
        absenceNmbr.setVisibility(View.INVISIBLE);

        justifiedNmbr.setVisibility(View.INVISIBLE);


        ImageView close = attendanceDialog.findViewById(R.id.close_dialog);
        MuDataBase1 db = new MuDataBase1(StudentsOfSeanceActivity.this);



        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendanceDialog.dismiss();
                onAttendanceCallDismissed();
                i = 0;
            }
        });


        if (restList.size()>0) {
            studentOfSeance s = restList.get(0);

            studentName.setText(s.getFamName() +"  "+s.getName());
            absenceN = db.calculAbsence(gId,s.getId(),"ABSENT").size();
            justifiedN = db.calculAbsence(gId,s.getId(),"JUSTIFIED").size();




         if (absenceN > 2 ){
             absenceNmbr.setText("Exclu absence :"+absenceN);
             absenceNmbr.setVisibility(View.VISIBLE);
             absenceNmbr.setTextColor(getResources().getColor(R.color.absent));

            }
      if (justifiedN > 4 ){

                justifiedNmbr.setText("Exclue justified :"+justifiedN);
                justifiedNmbr.setVisibility(View.VISIBLE);
                justifiedNmbr.setTextColor(getResources().getColor(R.color.absent));
            }


            absent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    studentOfSeance k = restList.get(i);

                        attendance a = new attendance(-1, k.getId(), Integer.parseInt(seanceId), "ABSENT", Integer.parseInt(gId));
                        db.startAppele(a);
                        restList.remove(0);



                    if ( restList.size()<=0) {
                        i = 0;
                        db.updateSeanceCall(Integer.parseInt(seanceId), true);
                        onAttendanceCallDismissed();
                        startCall.setVisibility(View.INVISIBLE);
                        attendanceDialog.dismiss();


                    } else {
                        int absenceN , justifiedN;
                        studentOfSeance l =restList.get(0);
                        studentName.setText(l.getFamName() +"  " +l.getName());
                        absenceN = db.calculAbsence(gId,l.getId(),"ABSENT").size();
                        justifiedN = db.calculAbsence(gId,l.getId(),"JUSTIFIED").size();




                        if (absenceN > 2 ){
                            absenceNmbr.setText("Exclu absence :"+absenceN);
                            absenceNmbr.setVisibility(View.VISIBLE);
                            absenceNmbr.setTextColor(getResources().getColor(R.color.absent));

                        } else {
                            absenceNmbr.setVisibility(View.INVISIBLE);
                        }


                        if (justifiedN > 4 ){

                            justifiedNmbr.setText("Exclue justified :"+justifiedN);
                            justifiedNmbr.setVisibility(View.VISIBLE);
                            justifiedNmbr.setTextColor(getResources().getColor(R.color.absent));
                        } else {
                            justifiedNmbr.setVisibility(View.INVISIBLE);
                        }


                    }


                    //  onDismissDialog(year);

                }
            });


            present.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    studentOfSeance k = restList.get(0);

                        attendance a = new attendance(-1, k.getId(), Integer.parseInt(seanceId), "PRESENT",Integer.parseInt(gId));
                        db.startAppele(a);
                        restList.remove(0);



                    if (restList.size()<=0) {
                        i = 0;
                        db.updateSeanceCall(Integer.parseInt(seanceId), true);
                        onAttendanceCallDismissed();
                        startCall.setVisibility(View.INVISIBLE);
                        attendanceDialog.dismiss();

                    } else {
                        int absenceN , justifiedN;
                        studentOfSeance l = restList.get(0);
                        studentName.setText(l.getFamName() +" "+l.getName());

                        absenceN = db.calculAbsence(gId,l.getId(),"ABSENT").size();
                        justifiedN = db.calculAbsence(gId,l.getId(),"JUSTIFIED").size();




                        if (absenceN > 2 ){
                            absenceNmbr.setText("Exclu absence :"+absenceN);
                            absenceNmbr.setVisibility(View.VISIBLE);

                            absenceNmbr.setTextColor(getResources().getColor(R.color.absent));

                        } else {
                            absenceNmbr.setVisibility(View.INVISIBLE);
                        }
                        if (justifiedN > 4 ){

                            justifiedNmbr.setText("Exclue justified :"+justifiedN);
                            justifiedNmbr.setVisibility(View.VISIBLE);
                            justifiedNmbr.setTextColor(getResources().getColor(R.color.absent));
                        } else {
                            justifiedNmbr.setVisibility(View.INVISIBLE);
                        }

                    }
                }

            });
        }
        else if (studentList.size()==0){
            Toast.makeText(StudentsOfSeanceActivity.this, "no student to call", Toast.LENGTH_SHORT).show();
            studentName.setText("No student to call");
            present.setClickable(false);
            absent.setClickable(false);
        } else if (restList.size()==0)
        {
            Toast.makeText(StudentsOfSeanceActivity.this, "already called for attendance", Toast.LENGTH_SHORT).show();
            studentName.setText("already called for attendance");
            present.setClickable(false);
            absent.setClickable(false);
        }


        attendanceDialog.show();



    }

    public void onAttendanceCallDismissed(){
        MuDataBase1 m = new MuDataBase1(StudentsOfSeanceActivity.this);
        studentList.clear();
        studentList=m.getstudentOfseance(Integer.parseInt(gId),gType);
        adapter.setStudentList(studentList);
        adapter.getGroup(gId);

        for (int c = 0 ; c<studentList.size(); c++){
            studentOfSeance s = studentList.get(c);
            attendance a = db.getStudentAttendance(seanceId,s.getId());
            if (a.getState()==null){
                restList.add(s);
            }
        }
        if (restList.size()==0){
            startCall.setVisibility(View.INVISIBLE);
        }  else {
            db.updateSeanceCall(Integer.parseInt(seanceId), false);
        }

    }






    @Override
    public void onStudentUpdated(DialogInterface dialogInterface) {
            studentList.clear();
            studentList = db.getstudentOfseance(Integer.parseInt(gId),gType);
            adapter.setStudentList(studentList);
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onAddStudentDialogueDismised(DialogInterface dialogInterface) {
        studentList.clear();
        studentList = db.getstudentOfseance(Integer.parseInt(gId),gType);
        adapter.setStudentList(studentList);
        adapter.notifyDataSetChanged();
    }
}