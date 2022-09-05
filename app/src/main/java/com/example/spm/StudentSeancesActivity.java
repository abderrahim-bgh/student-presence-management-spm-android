package com.example.spm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.spm.adapters.studentSeancesAdapter;
import com.example.spm.classes.sean;

import java.util.ArrayList;
import java.util.List;

public class StudentSeancesActivity extends AppCompatActivity {
  private Toolbar toolbar;
  private RecyclerView recyclerView;
  private studentSeancesAdapter adapter;
  private  MuDataBase1 db ;
  private List<sean> seanceList;
  private int studentId, groupId;
  private String type= "", studentName, studetFamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_seances);

        toolbar = findViewById(R.id.s_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.students_seance_RecyclerView);
        db = new MuDataBase1(StudentSeancesActivity.this);
        seanceList = new ArrayList<>();
        Intent data = getIntent();
        studentId = data.getIntExtra("STUDENT_ID",0);
        type = data.getStringExtra("TYPE");
        groupId = data.getIntExtra("GROUP",0);
        studentName = data.getStringExtra("STUDENT_NAME");
        studetFamName = data.getStringExtra("STUDENT_FAMILY_NAME");
        toolbar.setTitle(studetFamName+" "+studentName);


        Cursor cursor= db.readAllseance();
        if (cursor.getCount()==0){
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()) {
                String h1 = null;
                String h2 = null;
                String ids = null;
                String id_g = null;
                String datte = null;
                String called = null;
                String name = null;

                sean a = new sean();

                ids = cursor.getString(0);
                id_g = cursor.getString(1);
                datte = cursor.getString(2);
                h1 = cursor.getString(3);
                h2 = cursor.getString(4);

                called = cursor.getString(5);
                name = cursor.getString(6);

                a.setId(ids);
                a.setH1(h1);
                a.setH2(h2);
                a.setId_G(id_g);
                a.setDate(datte);

                a.setCalled(called);
                a.setNameSeance(name);
                a.setgType(type);
                if (groupId == Integer.parseInt(a.getId_G()) && a.getCalled().equals("CALLED")) {
                    seanceList.add(a);
                }
            }
        }

        adapter = new studentSeancesAdapter(StudentSeancesActivity.this);
        recyclerView.setAdapter(adapter);
        adapter.setStudentId(studentId);
        adapter.setSeanceList(seanceList);




    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home ){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}