package com.example.spm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.spm.adapters.groupsAdapter;
import com.example.spm.classes.classe;
import com.example.spm.classes.group;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class groupsActivity extends AppCompatActivity {

    groupsAdapter tdAdapter, tpAdapter ;
    RecyclerView tdRecycler, tpRecycler;
    List<group> tdGroupList, tpGroupList;
    CardView tdCardView, tpCardView;
    LinearLayout tdLayout, tpLayout;
    boolean tdIsExpanded, tpIsExpanded;
    ImageButton addGroup;
    Dialog addGroupDialogue;
    private  MuDataBase1 db;
    String idc;
    String val ="";
    private static final int READ_REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_STORAGR = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        Toolbar toolbar = findViewById(R.id.groups_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent in = getIntent();
        String txt = in.getStringExtra("NAME");
        idc = in.getStringExtra("IDC");
        toolbar.setTitle(txt);

        tdCardView=findViewById(R.id.td_card);
        tpCardView=findViewById(R.id.tp_card);
        tdRecycler = findViewById(R.id.td_RecyclerView);
        tpRecycler=findViewById(R.id.tp_RecyclerView);
        tdLayout=findViewById(R.id.expnded_td);
        tpLayout=findViewById(R.id.expnded_tp);



        tdAdapter = new groupsAdapter(groupsActivity.this,tdGroupList);
        tdRecycler.setAdapter(tdAdapter);

        tpAdapter = new groupsAdapter(groupsActivity.this,tpGroupList);
        tpRecycler.setAdapter(tpAdapter);

        addGroup = findViewById(R.id.addgroup);
        addGroupDialogue = new Dialog(groupsActivity.this);
        db = new MuDataBase1(groupsActivity.this);

        tdLayout.setVisibility(View.GONE);
        tdIsExpanded=false;
        tpLayout.setVisibility(View.GONE);
        tpIsExpanded=false;



        tdGroupList=new ArrayList<>();
        tpGroupList=new ArrayList<>();
        MuDataBase1 mdb = new MuDataBase1(groupsActivity.this);


        tdCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tdIsExpanded=!tdIsExpanded;
                tdLayout.setVisibility(tdIsExpanded ? View.VISIBLE : View.GONE);


                tdGroupList = mdb.getGroupsByType(Integer.parseInt(idc),"td");

                    tdAdapter.setGroups(tdGroupList);


            }
        });
        Cursor cur= mdb.readAllGroups();
        tpCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tpIsExpanded=!tpIsExpanded;
                tpLayout.setVisibility(tpIsExpanded ? View.VISIBLE : View.GONE);

                tpGroupList = mdb.getGroupsByType(Integer.parseInt(idc),"tp");

                    tpAdapter.setGroups(tpGroupList);

            }
        });


        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddGroup();
            }
        });




    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home ){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    private void openAddGroup(){
        addGroupDialogue.setContentView(R.layout.add_group_dialog);
        addGroupDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button cancel = addGroupDialogue.findViewById(R.id.cncl_btn_addGroup);
        Button add = addGroupDialogue.findViewById(R.id.add_btn_addgroup);
        EditText groupName = addGroupDialogue.findViewById(R.id.edit_text_add_group);
        RadioButton tp = addGroupDialogue.findViewById(R.id.tp_radio_btn_addgroup);
        RadioButton td = addGroupDialogue.findViewById(R.id.td_radio_btn_addgroup);


        RadioGroup GR = addGroupDialogue.findViewById(R.id.radio_group_td_tp_addgroup);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addGroupDialogue.dismiss();

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = groupName.getText().toString();
                db = new MuDataBase1(groupsActivity.this);

                int radioId = 0;
                radioId = GR.getCheckedRadioButtonId();
                String gname = name;
                int classId = Integer.parseInt( idc);
                group g = new group();
                boolean d = false;

                if ( radioId==0 ){
                    Toast.makeText(groupsActivity.this, " please check td or tp", Toast.LENGTH_SHORT).show();
                } else {
                    if (td.isChecked()){
                         g = new group("-1",gname,"td",classId);
                        d = db.insertGroup(g);

                    } else if (tp.isChecked()){
                         g = new group("-1",gname,"tp",classId);
                        d = db.insertGroup(g);
                    }



                    if (d){
                        Toast.makeText(groupsActivity.this,"group added",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(groupsActivity.this,"failed groop",Toast.LENGTH_SHORT).show();
                    }
                   refresh();
                    addGroupDialogue.dismiss();

                }
            }
        });

        addGroupDialogue.show();

    }

    public void refresh(){
        db = new MuDataBase1(groupsActivity.this);

        tdGroupList.clear();
        tpGroupList.clear();
        tdGroupList = db.getGroupsByType(Integer.parseInt(idc),"td");
        tpGroupList  = db.getGroupsByType(Integer.parseInt(idc),"tp");
        tdAdapter.setGroups(tdGroupList);
        tpAdapter.setGroups(tpGroupList);
    }
    private String readgorupe(String input){
        FileInputStream fis =null;
        String rr="";
        try {
            fis = new FileInputStream(new File(Environment.getExternalStorageDirectory(),input)) ;
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet mySheet = workbook.getSheetAt(0);
            Iterator<Row> rows = mySheet.rowIterator();
            XSSFRow row = (XSSFRow) rows.next();
            XSSFRow row1 = (XSSFRow) rows.next();
            XSSFRow row2 = (XSSFRow) rows.next();
            XSSFRow row3 = (XSSFRow) rows.next();

            Iterator cells = row3.cellIterator();
            XSSFCell cell = (XSSFCell) cells.next();
            rr = cell.toString();
        }



        catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
        return  rr;
    }
    private String readlinetd(String input){

        return input;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            String path = uri.getPath();
            path = path.substring(path.indexOf(":") + 1);
            String gv = readgorupe(path) + "\n";
          if (val.indexOf(gv)>=0){
                Toast.makeText(this, "Cette groupe exist déja", Toast.LENGTH_LONG).show();
            }else {
                val = val + gv;
                MuDataBase1 mb = new MuDataBase1(this);

             // mb.updateDatag(tdAdapter.idgg(),val);
              refresh();
              Toast.makeText(this, gv+val, Toast.LENGTH_LONG).show();
              /*  textView.setText(val);
                nbrrr = nbrrr+1;
                String g = String.valueOf(nbrrr);
                nbrtd.setText("Nombre de groupe est " +g);
                linearr.removeAllViews();
                linearr.addView(textView);
                listtd.add(readlinetd(path));
                index++;*/

            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_STORAGR) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "permision garnte", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "permission not garent", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}