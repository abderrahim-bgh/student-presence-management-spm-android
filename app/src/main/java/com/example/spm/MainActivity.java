package com.example.spm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.spm.adapters.yearsAdapter;
import com.example.spm.classes.year;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageView addYear;

    Dialog dialog;
    MuDataBase1 mdb;
    RecyclerView yearsRecycler;
    private List<year> yearList;
    yearsAdapter yearAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addYear = findViewById(R.id.add_year);

        dialog = new Dialog(this);



        addYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddYearDialog();
            }
        });


        yearList= new ArrayList<>();
        mdb = new MuDataBase1(MainActivity.this);
        Cursor cursor= mdb.readAllYears();
        if (cursor.getCount()==0){
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()){
                String ye = null;
                String idy = null;
                year a = new year();

                ye= cursor.getString(1);
                idy= cursor.getString(0);
                  a.setYear(ye);
                  a.setIdY(idy);
                yearList.add(a);
            }
        }


        yearAdapter = new yearsAdapter(this,yearList);
        yearsRecycler= findViewById(R.id.years);

        yearsRecycler.setAdapter(yearAdapter);
        yearAdapter.setList(yearList);








    }

    private void openAddYearDialog (){
        dialog.setContentView(R.layout.add_year_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button cancel = dialog.findViewById(R.id.cncl_btn);
        Button add = dialog.findViewById(R.id.add_btn);
        NumberPicker startYear = dialog.findViewById(R.id.start_year);
        NumberPicker endYear = dialog.findViewById(R.id.end_year);

        startYear.setMaxValue(2040);
        startYear.setMinValue(2020);
        endYear.setMaxValue(2040);
        endYear.setMinValue(2020);

        Calendar c = Calendar.getInstance();
        startYear.setValue(c.get(Calendar.YEAR));
        endYear.setValue(c.get(Calendar.YEAR));

        startYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // to store the selected year
            }
        });

        endYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // to store the selected year
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MuDataBase1 myDb = new MuDataBase1(MainActivity.this);
                myDb.addYear(String.valueOf(startYear.getValue())+"/"+String.valueOf(endYear.getValue()));
                Intent intent = new Intent(MainActivity.this, loading.class);
                String msg ="1";
                intent.putExtra("MSG",msg);
                startActivity(intent);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();



    }



}