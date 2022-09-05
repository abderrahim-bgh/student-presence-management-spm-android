package com.example.spm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.spm.adapters.classesAdapter;
import com.example.spm.adapters.yearsAdapter;
import com.example.spm.classes.classe;
import com.example.spm.classes.year;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;

public class classesActivity extends AppCompatActivity {

    // testing
    List<classe> classList;
    RecyclerView classRecycler;
    classesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);







    }



}