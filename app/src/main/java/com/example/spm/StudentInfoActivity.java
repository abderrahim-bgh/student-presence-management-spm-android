package com.example.spm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spm.classes.student;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

public class StudentInfoActivity extends AppCompatActivity{
     TextView comment;
    private PieChart tdPieChar, tpPieChart;
    private TextView nameTextView, familyNameTextView, matTextView, classTextView, groupTdTextView, groupTpTextView;
    private int studentId,mat ,classId, absence=0, present=0 , justified=0 , groupTdId,groupTpId;
    private String name,famName, className, groupTdName, groupTpName;
    private CardView tpCard, tdCard, commentsCard;
    private MuDataBase1 db;
    private ImageView edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);

        Toolbar toolbar = findViewById(R.id.student_info_toolbar);
        comment= findViewById(R.id.student_com);


        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db= new MuDataBase1(StudentInfoActivity.this);

        nameTextView = findViewById(R.id.student_name);
        familyNameTextView = findViewById(R.id.student_family_name);
        matTextView = findViewById(R.id.student_matrecule);
        classTextView = findViewById(R.id.student_class);
        groupTdTextView = findViewById(R.id.student_groupTD);
        groupTpTextView = findViewById(R.id.student_groupTp);

        edit = findViewById(R.id.student_info_edit);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                student f  = new student(studentId,mat,famName,name,groupTdId,groupTpId);
                openUpdateStudentDialog(f);
            }
        });


        tdCard = findViewById(R.id.td_card_view);
        tpCard = findViewById(R.id.tp_card_view);

        groupTdTextView.setVisibility(View.GONE);
        groupTpTextView.setVisibility(View.GONE);

        Intent studentInfo = getIntent();
        name = studentInfo.getStringExtra("STUDENT_NAME");
        famName = studentInfo.getStringExtra("STUDENT_FAMILY_NAME");
        mat = studentInfo.getIntExtra("STUDENT_MAT",0);
        studentId = studentInfo.getIntExtra("STUDENT_ID",0);
        groupTdId = studentInfo.getIntExtra("STUDENT_GROUP_TD_ID",0);
        groupTpId = studentInfo.getIntExtra("STUDENT_GROUP_TP_ID",0);
        Cursor cursor= db.readAllstudent();
        while (cursor.moveToNext()) {

            String cm = cursor.getString(3);
            String id =cursor.getString(0);
            if (String.valueOf(studentId).equals(id)) comment.setText(cm);
            else comment.setText("");
        }
        if (groupTdId!=0 && groupTpId==0 ){
            className = db.getClassByGroupId(String.valueOf(groupTdId));
            groupTdTextView.setVisibility(View.VISIBLE);
            groupTdName = db.getGroupById(String.valueOf(groupTdId)).getName();
            tpCard.setVisibility(View.GONE);


        } else if (groupTdId==0 && groupTpId!=0 ){
            className = db.getClassByGroupId(String.valueOf(groupTpId));
            groupTpTextView.setVisibility(View.VISIBLE);
            groupTpName = db.getGroupById(String.valueOf(groupTpId)).getName();
            tdCard.setVisibility(View.GONE);
        } else{

            className = db.getClassByGroupId(String.valueOf(groupTdId));
            groupTdTextView.setVisibility(View.VISIBLE);
            groupTdName = db.getGroupById(String.valueOf(groupTdId)).getName();
            groupTpTextView.setVisibility(View.VISIBLE);
            groupTpName = db.getGroupById(String.valueOf(groupTpId)).getName();
        }

        nameTextView.setText("Name : "+name);
        familyNameTextView.setText("Family Name : "+famName);
        matTextView.setText("ID: "+String.valueOf(mat));
        classTextView.setText("Class : "+className);
        groupTdTextView.setText("group Td : "+groupTdName);
        groupTpTextView.setText("group TP : "+ groupTpName);

        TextView gTD = findViewById(R.id.student_groupTd_incard);
        TextView gTP = findViewById(R.id.student_groupTp_incard);
        gTD.setText(groupTdName);
        gTP.setText(groupTpName);



        tdPieChar = findViewById(R.id.piechart_td);
        tpPieChart = findViewById(R.id.piechart_tp);
        setUpPietd();
        loadDataTD();

        setUpPieTP();
        loadDatatp();

        tdCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(StudentInfoActivity.this,StudentSeancesActivity.class);
                a.putExtra("STUDENT_ID",studentId);
                a.putExtra("TYPE", "td");
                a.putExtra("GROUP",groupTdId);
                a.putExtra("STUDENT_NAME",name);
                a.putExtra("STUDENT_FAMILY_NAME",famName);
                startActivity(a);
            }
        });

        tpCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(StudentInfoActivity.this,StudentSeancesActivity.class);
                a.putExtra("STUDENT_ID",studentId);
                a.putExtra("TYPE", "tp");
                a.putExtra("GROUP",groupTpId);
                a.putExtra("STUDENT_NAME",name);
                a.putExtra("STUDENT_FAMILY_NAME",famName);
                startActivity(a);
            }
        });








    }



    private void setUpPietd(){
        tdPieChar.setDrawHoleEnabled(true);
        tdPieChar.setUsePercentValues(true);
        tdPieChar.setEntryLabelTextSize(22f);
        tdPieChar.setEntryLabelColor(Color.BLACK);

        tdPieChar.getDescription().setEnabled(false);

        Legend l = tdPieChar.getLegend();

        List<LegendEntry> e = new ArrayList<>();
      int  present = db.calculAbsence(String.valueOf(groupTdId),studentId,"PRESENT").size();
        int  absence  = db.calculAbsence(String.valueOf(groupTdId),studentId,"ABSENT").size();
       int  justified = db.calculAbsence(String.valueOf(groupTdId),studentId,"JUSTIFIED").size();

        int sum = present+absence+justified;

        tdPieChar.setCenterText(sum+" \n seances");
        tdPieChar.setCenterTextSize(17);

        e.add(new LegendEntry("Present :"+ present,Legend.LegendForm.SQUARE,18f,2f,null,getResources().getColor(R.color.present)));
        e.add(new LegendEntry( "absent :"+absence,Legend.LegendForm.SQUARE,18f,2f,null,getResources().getColor(R.color.absent)));
        e.add(new LegendEntry("justified :"+justified,Legend.LegendForm.SQUARE,18f,2f,null,getResources().getColor(R.color.blue)));



        l.setCustom(e);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);

        l.setFormSize(16f);
        l.setFormToTextSpace(12f);
        l.setTextSize(14f);


        l.setDrawInside(false);
        l.setEnabled(true);

    }


    private void loadDataTD(){
        List<PieEntry> entries = new ArrayList<>();
        float i = 0.7f;

      int  present = db.calculAbsence(String.valueOf(groupTdId),studentId,"PRESENT").size();
        int absence  = db.calculAbsence(String.valueOf(groupTdId),studentId,"ABSENT").size();
        int justified = db.calculAbsence(String.valueOf(groupTdId),studentId,"JUSTIFIED").size();

        int sum = present+absence+justified;

        float p = ((float)present * 100 )/sum;
        float a = ((float)absence * 100 )/sum;
        float j = ((float)justified * 100 )/sum;

        entries.add(new PieEntry(p,"present"));
        entries.add(new PieEntry(j,"justified"));
        entries.add(new PieEntry(a,"absent"));


        ArrayList<Integer> colors = new ArrayList<>();



        colors.add(getResources().getColor(R.color.present));
        colors.add(getResources().getColor(R.color.blue));
        colors.add(getResources().getColor(R.color.absent));

        PieDataSet dataSet = new PieDataSet(entries,"  ");
        dataSet.setColors(colors);
        dataSet.setSliceSpace(4f);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(tdPieChar));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        tdPieChar.setData(data);
        tdPieChar.setDrawEntryLabels(false);
        tdPieChar.invalidate();

    }

    private void setUpPieTP(){
        tpPieChart.setDrawHoleEnabled(true);
        tpPieChart.setUsePercentValues(true);
        tpPieChart.setEntryLabelTextSize(14);
        tpPieChart.setEntryLabelColor(Color.BLACK);

        tpPieChart.getDescription().setEnabled(false);

        Legend l = tpPieChart.getLegend();


        List<LegendEntry> e = new ArrayList<>();
        int present = db.calculAbsence(String.valueOf(groupTpId),studentId,"PRESENT").size();
        int absence  = db.calculAbsence(String.valueOf(groupTpId),studentId,"ABSENT").size();
        int justified = db.calculAbsence(String.valueOf(groupTpId),studentId,"JUSTIFIED").size();

        int sum = present+absence+justified;

        tpPieChart.setCenterText(sum+" \n seances");
        tpPieChart.setCenterTextSize(17);

        e.add(new LegendEntry("Present :"+ present,Legend.LegendForm.SQUARE,18f,2f,null,getResources().getColor(R.color.present)));
        e.add(new LegendEntry( "absent :"+absence,Legend.LegendForm.SQUARE,18f,2f,null,getResources().getColor(R.color.absent)));
        e.add(new LegendEntry("justified :"+justified,Legend.LegendForm.SQUARE,18f,2f,null,getResources().getColor(R.color.blue)));



        l.setCustom(e);



        l.setFormSize(16f);
        l.setFormToTextSpace(12f);
        l.setTextSize(14f);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);


        l.setDrawInside(false);
        l.setEnabled(true);

    }


    private void loadDatatp(){
        List<PieEntry> entries = new ArrayList<>();
       int  present = db.calculAbsence(String.valueOf(groupTpId),studentId,"PRESENT").size();
        int absence  = db.calculAbsence(String.valueOf(groupTpId),studentId,"ABSENT").size();
        int justified = db.calculAbsence(String.valueOf(groupTpId),studentId,"JUSTIFIED").size();

        int sum = present+absence+justified;

        float p = ((float)present * 100 )/sum;
        float a = ((float)absence * 100 )/sum;
        float j = ((float)justified * 100 )/sum;

        entries.add(new PieEntry(p,"present"));
        entries.add(new PieEntry(j,"justified"));
        entries.add(new PieEntry(a,"absent"));


        ArrayList<Integer> colors = new ArrayList<>();



        colors.add(getResources().getColor(R.color.present));
        colors.add(getResources().getColor(R.color.blue));
        colors.add(getResources().getColor(R.color.absent));

        PieDataSet dataSet = new PieDataSet(entries,"  ");
        dataSet.setColors(colors);
        dataSet.setSliceSpace(6f);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(tpPieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        tpPieChart.setData(data);
        tpPieChart.setDrawEntryLabels(false);
        tpPieChart.invalidate();

    }






    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home ){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    private void openUpdateStudentDialog (student s){
        Dialog updateDialog = new Dialog(StudentInfoActivity.this);
        MuDataBase1 db = new MuDataBase1(StudentInfoActivity.this);

        updateDialog.setContentView(R.layout.add_student_dialog);
        updateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button cancel = updateDialog.findViewById(R.id.cncl_btn_addstudent);
        Button save = updateDialog.findViewById(R.id.add_btn_addstudent_dialog);
        EditText studentMat = updateDialog.findViewById(R.id.student_mat_e_text);
        EditText studentFamName = updateDialog.findViewById(R.id.student_familyNAme_e_text);
        EditText studentName = updateDialog.findViewById(R.id.student_name_e_text);

        studentMat.setText(String.valueOf(s.getMat()));
        studentFamName.setText(s.getFamName());
        studentName.setText(s.getName());



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  onDismissDialog(year);
                updateDialog.dismiss();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String n = studentName.getText().toString();
                String f = studentFamName.getText().toString();
                String m = studentMat.getText().toString();

                int mat = Integer.parseInt(m);
                student a = db.studentByMatExist(mat);

                if (n.isEmpty() && f.isEmpty() && m.isEmpty() ){
                    studentName.setError(" please enter a student name");
                    studentFamName.setError(" please enter a Family name ");
                    studentMat.setError(" please enter a matrecule nmbr");
                }
                else if (n.isEmpty()) {
                    studentName.setError(" name must be at least 3 letters");
                } else if (n.length() < 2) {
                    studentName.setError(" name must be at least 3 letters");
                } else if (f.isEmpty()) {
                    studentFamName.setError("Family name name must be at least 3 letters");
                } else if (studentFamName.length() < 2) {
                    studentFamName.setError(" name must be at least 3 letters");
                } else if (m.isEmpty()) {
                    studentMat.setError(" please enter a matrecule nmbr");
                } else if (a.getId()!=s.getId() && a.getId()!=-1) {
                    studentMat.setError("there is an other registered student with this matrecule");
                    Toast.makeText(StudentInfoActivity.this, "there is an other registered student with this matrecule", Toast.LENGTH_SHORT).show();
                }
                else {

                    boolean b = false;
                    b = db.updateStudentInfo(s.getId(),mat,n,f);

                    if (b) {
                        Toast.makeText(StudentInfoActivity.this, "student updated", Toast.LENGTH_SHORT).show();
                        nameTextView.setText("Name : "+n);
                        familyNameTextView.setText("Family Name : "+f);
                        matTextView.setText("ID: "+m);
                        
                    } else {
                        Toast.makeText(StudentInfoActivity.this, "failed student", Toast.LENGTH_SHORT).show();
                    }

                    updateDialog.dismiss();

                }
            }
        });
        updateDialog.show();

    }




}