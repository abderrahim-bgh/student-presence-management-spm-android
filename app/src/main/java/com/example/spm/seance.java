package com.example.spm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spm.adapters.seanceAdapter;
import com.example.spm.adapters.yearsAdapter;
import com.example.spm.classes.sean;
import com.example.spm.classes.year;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class seance extends AppCompatActivity {

    ImageButton addSeance;
    Dialog addDialogue;
    String idg,cId,gType;
    RecyclerView recyclerView;
    seanceAdapter adapterSe;
     List<sean> seanceListe;
    MuDataBase1 mdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seance);
        Toolbar toolbar = findViewById(R.id.seance_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Intent intent =getIntent();
            String s= intent.getStringExtra("GROUP_NAME");
            idg= intent.getStringExtra("GROUP_ID");
             cId= intent.getStringExtra("CLASS_ID");
             gType = intent.getStringExtra("GROUP_TYPE");


            toolbar.setTitle(s);
        addSeance = findViewById(R.id.addSeance);
        addDialogue = new Dialog(seance.this);
        addSeance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddSeance();
            }
        });

        seanceListe= new ArrayList<>();
        mdb = new MuDataBase1(seance.this);
        Cursor cursor= mdb.readAllseance();
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

                sean a = new sean();

                ids = cursor.getString(0);
                id_g = cursor.getString(1);
                datte = cursor.getString(2);
                h1 = cursor.getString(3);
                h2 = cursor.getString(4);

                called = cursor.getString(5);

                a.setId(ids);
                a.setH1(h1);
                a.setH2(h2);
                a.setId_G(id_g);
                a.setDate(datte);

                a.setCalled(called);

                a.setgType(gType);
                if (idg.equals(id_g)) {
                    seanceListe.add(a);
                }
            }
        }

        recyclerView= findViewById(R.id.recSeance);
        adapterSe = new seanceAdapter(seance.this,seanceListe);

        recyclerView.setAdapter(adapterSe);
        recyclerView.setLayoutManager(new LinearLayoutManager(seance.this));



    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home ){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    void AddSeance(){
        addDialogue.setContentView(R.layout.add_seance_dialoge);
        addDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        AppCompatButton add = addDialogue.findViewById(R.id.add_se);
        TextView editText =addDialogue.findViewById(R.id.dat_piker);
        AppCompatButton cancel = addDialogue.findViewById(R.id.cncl_se);
        NumberPicker start_h = addDialogue.findViewById(R.id.start_h);
        NumberPicker start_m = addDialogue.findViewById(R.id.start_m);
        NumberPicker end_m = addDialogue.findViewById(R.id.end_m);
        NumberPicker end_h = addDialogue.findViewById(R.id.end_h);
        start_m.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                picker.setValue((newVal < oldVal)?oldVal-15:oldVal+15);
            }
        });
        end_m.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                picker.setValue((newVal < oldVal)?oldVal-15:oldVal+15);
            }
        });
        start_h.setMaxValue(17);
        start_m.setMaxValue(59);
        start_h.setMinValue(8);
        start_m.setMinValue(00);
        end_h.setMaxValue(17);
        end_m.setMaxValue(59);
        end_h.setMinValue(8);
        end_m.setMinValue(00);
        Calendar calendar = Calendar.getInstance();
        final String[] date = new String[1];

        editText.setText(DateFormat.getDateInstance().format(calendar.getTime()));
        final Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog  StartTime = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editText.setText(DateFormat.getDateInstance().format(newDate.getTime()));

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
      editText.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              StartTime.show();


          }
      });


        MuDataBase1 mdb= new MuDataBase1(seance.this);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date[0] = editText.getText().toString();
                String t1 =String.valueOf(start_h.getValue()+" : "+start_m.getValue());
                String t2 =String.valueOf(end_h.getValue()+" : "+end_m.getValue());
                int seanceNmbr = getSeancesLength()+1;
                String name = "SEANCE "+seanceNmbr;

                mdb.addSeance(idg,t1,t2, date[0],name);

                    seanceListe.clear();
                    Cursor cursor = mdb.readAllseance();
                    adapterSe.setStudentList(seanceListe);
                if (cursor.getCount()==0){
                    Toast.makeText(seance.this, "No data", Toast.LENGTH_SHORT).show();
                }else {
                    while (cursor.moveToNext()) {
                        String h1 = null;
                        String h2 = null;
                        String ids = null;
                        String id_g = null;
                        String datte = null;

                        sean a = new sean();

                        ids = cursor.getString(0);
                        id_g = cursor.getString(1);
                        datte = cursor.getString(2);
                        h1 = cursor.getString(3);
                        h2 = cursor.getString(4);

                        // so the attendance call start thmthm
                        String called = cursor.getString(5);

                        a.setId(ids);
                        a.setH1(h1);
                        a.setH2(h2);
                        a.setId_G(id_g);
                        a.setDate(datte);
                        a.setCalled(called);
                        a.setgType(gType);
                        if (idg.equals(id_g)) {
                            seanceListe.add(a);

                        }
                    }
                }
                int lastPos = seanceListe.size()-1;
                sean s = seanceListe.get(lastPos);

                Intent i = new Intent(seance.this, StudentsOfSeanceActivity.class);
                i.putExtra("GROUP_ID",s.getId_G());
                i.putExtra("GROUP_type",s.getgType());
                // for testing ur idea of dialog
                i.putExtra("SEANCE_ID",s.getId());
                i.putExtra("SEANCE_NAME",s.getNameSeance());
                String t = s.getCalled();

/*
                if (t.equals("CALLED")) {
                    i.putExtra("CALLED", true);

                } else {
                    i.putExtra("CALLED", false);
                }

 */
                i.putExtra("CALLED", false);


                startActivity(i);

                addDialogue.dismiss();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addDialogue.dismiss();

            }
        });

        addDialogue.show();

    }



    public int getSeancesLength(){
        int l = 0;
        List<sean> ll = new ArrayList<>();
        Cursor cursor = mdb.readAllseance();
        if (cursor.getCount()==0){
            Toast.makeText(seance.this, "No data", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()) {
                String h1 = null;
                String h2 = null;
                String ids = null;
                String id_g = null;
                String datte = null;

                sean a = new sean();

                ids = cursor.getString(0);
                id_g = cursor.getString(1);
                datte = cursor.getString(2);
                h1 = cursor.getString(3);
                h2 = cursor.getString(4);

                // so the attendance call start thmthm
                String called = cursor.getString(5);


                a.setId(ids);
                a.setH1(h1);
                a.setH2(h2);
                a.setId_G(id_g);
                a.setDate(datte);
                a.setCalled(called);
                a.setgType(gType);
                if (idg.equals(id_g)) {
                    ll.add(a);

                }
            }
        }

        l = ll.size();

        return l;
    }

           int annee=0,month=0,day=0;


}