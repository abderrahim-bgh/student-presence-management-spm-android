package com.example.spm.adapters;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spm.MuDataBase1;
import com.example.spm.R;
import com.example.spm.StudentsActivity;
import com.example.spm.classes.group;
import com.example.spm.classes.student;
import com.example.spm.classes.year;
import com.example.spm.groupsActivity;
import com.example.spm.seance;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class groupsAdapter extends RecyclerView.Adapter<groupsAdapter.viewHolder> {
    private Context activity;

    private List<group> groupList;
    Dialog exportGroupDialogue;
    RecyclerView studentRecycler,studentRecycler4;
    List<student> studentList;
    List<String> stateListe;
    ExportstudentAdapter adapter;
    ExportstudentAdapter4 adapter4;
    MuDataBase1 db;
    static String getId,gId2;
    public groupsAdapter(Context a, List<group> gList) {
        this.activity = a;
        this.groupList = gList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grp_seance,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        group g = groupList.get(position);
        Boolean b = g.isExpandble();
        int i = position;;

        getId= g.getId();
        holder.groupName.setText(g.getName());
        holder.linearLayout.setVisibility(b ? View.VISIBLE : View.GONE);
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                g.setExpandble(!g.isExpandble());
                year ye= new year();
                holder.linearLayout.setVisibility(g.isExpandble() ? View.VISIBLE : View.GONE);


            }
        });
        holder.seance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, seance.class);
                i.putExtra("CLASS_ID",g.getIdC());
                i.putExtra("GROUP_ID", g.getId());
                i.putExtra("GROUP_NAME",g.getName());
                i.putExtra("GROUP_TYPE",g.getType());
                activity.startActivity(i);
            }
        });

        holder.lite_grp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, StudentsActivity.class);
                i.putExtra("CLASS_ID",g.getIdC());
                i.putExtra("GROUP_ID", g.getId());
                i.putExtra("GROUP_NAME",g.getName());
                i.putExtra("GROUP_TYPE",g.getType());
                activity.startActivity(i);
            }
        });



        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog edit_d= new Dialog(activity);
                edit_d.setContentView(R.layout.update);
                edit_d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                EditText edit_grp= edit_d.findViewById(R.id.txt_update);

                AppCompatButton cancel= edit_d.findViewById(R.id.cancel_update);
                AppCompatButton update= edit_d.findViewById(R.id.Update);
                edit_grp.setText(g.getName());
                MuDataBase1 myDb= new MuDataBase1(activity);

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alert= new AlertDialog.Builder(activity);
                        alert.setTitle("Edit");
                        alert.setMessage("Are you sure ");
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String grp= edit_grp.getText().toString();
                                myDb.updateDatag(g.getId(),grp);
                                holder.groupName.setText(edit_grp.getText());
                                edit_d.dismiss();
                            }
                        });
                        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        });
                        alert.show();
                    }
                });



                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edit_d.dismiss();
                    }
                });
                edit_d.show();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("Delete group")
                        .setMessage("Do you want to delete this group ?")
                        .setCancelable(true)
                        .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MuDataBase1 db = new MuDataBase1(activity);
                                boolean b = db.deleteGroups(g.getId(),g.getType());
                                if(b){
                                    Toast.makeText(activity, "Group Deleted successfully", Toast.LENGTH_SHORT).show();
                                    groupList.remove(i);
                                    notifyItemRemoved(i);
                                    notifyItemChanged(i);
                                } else {
                                    Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();

                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
            }
        });

        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");


        db = new MuDataBase1(activity);
        studentList= new ArrayList<>();
        studentList = db.getstudent(Integer.parseInt(g.getId()),g.getType());
        adapter = new ExportstudentAdapter(activity,studentList);
        adapter.getGroup(g.getId());





        holder.export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter = new ExportstudentAdapter(activity,studentList);
                adapter.getGroup(g.getId());
                adapter4 = new ExportstudentAdapter4(activity,studentList);
                adapter4.getGroup(g.getId());
                gId2 = g.getId();
                onDismissDialog(Integer.parseInt(g.getId()),g.getType());

                exportGroupDialogue = new Dialog(activity);
                exportGroupDialogue.setContentView(R.layout.dialoge_export);
                exportGroupDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                LinearLayout tabStu =exportGroupDialogue.findViewById(R.id.tabStu);


                RadioButton s_all,s4,im,ex;
                EditText seance1= exportGroupDialogue.findViewById(R.id.seance1);
                EditText seancEend= exportGroupDialogue.findViewById(R.id.seanceend);
                s_all= exportGroupDialogue.findViewById(R.id.s_all);
                s4= exportGroupDialogue.findViewById(R.id.s4);
                im=exportGroupDialogue.findViewById(R.id.impo);
                ex=exportGroupDialogue.findViewById(R.id.expo);

                studentRecycler = exportGroupDialogue.findViewById(R.id.export_recy);
                studentRecycler.setAdapter(adapter);
                studentRecycler.setLayoutManager(new LinearLayoutManager(activity));
                studentRecycler.setVisibility(View.GONE);
                AppCompatButton cancel, export;

                cancel= exportGroupDialogue.findViewById(R.id.canelex);
                export = exportGroupDialogue.findViewById(R.id.explor);
                studentRecycler4 = exportGroupDialogue.findViewById(R.id.export_recy4);
                studentRecycler4.setAdapter(adapter4);
                studentRecycler4.setLayoutManager(new LinearLayoutManager(activity));
                studentRecycler4.setVisibility(View.GONE);
                tabStu.setVisibility(View.GONE);
                seance1.setVisibility(View.GONE);
                seancEend.setVisibility(View.GONE);
                s_all.setVisibility(View.GONE);
                s4.setVisibility(View.GONE);
                im.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tabStu.setVisibility(View.GONE);
                        seance1.setVisibility(View.GONE);
                        seancEend.setVisibility(View.GONE);
                        s_all.setVisibility(View.GONE);
                        s4.setVisibility(View.GONE);
                        studentRecycler4.setVisibility(View.GONE);
                        studentRecycler.setVisibility(View.GONE);
                        export.setText("Import");

                    }
                });
                ex.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        s_all.setVisibility(View.VISIBLE);
                        s4.setVisibility(View.VISIBLE);
                        export.setText("export");
                    }
                });
                s_all.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          studentRecycler4.setVisibility(View.GONE);
                              studentRecycler.setVisibility(View.VISIBLE);
                          tabStu.setVisibility(View.VISIBLE);
                          seance1.setVisibility(View.GONE);
                          seancEend.setVisibility(View.GONE);
                      }
                  });
                s4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        studentRecycler4.setVisibility(View.VISIBLE);
                        studentRecycler.setVisibility(View.GONE);
                        tabStu.setVisibility(View.VISIBLE);
                        seance1.setVisibility(View.VISIBLE);
                        seancEend.setVisibility(View.VISIBLE);
                    }
                });
                exportGroupDialogue.show();

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exportGroupDialogue.dismiss();
                    }
                });
                export.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(export.getText().toString().equals("export")){
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            if (activity.getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                ActivityCompat.requestPermissions((Activity) activity,permissions, 1);
                            } else {
                                if (s_all.isChecked())
                                createXlFile(g.getName());
                                else {
                                    if (seance1.getText().toString().isEmpty() || seancEend.getText().toString().isEmpty())
                                        Toast.makeText(activity, "Seance first or seance end is empty", Toast.LENGTH_SHORT).show();
                                   else
                                    createXlFile4(g.getName(),seance1.getText().toString(),seancEend.getText().toString());
                                }
                            }
                        } else {
                            if (s_all.isChecked())
                            createXlFile(g.getName());
                            else {
                                if (seance1.getText().toString().isEmpty() || seancEend.getText().toString().isEmpty())
                                    Toast.makeText(activity, "Seance first or seance end is empty", Toast.LENGTH_SHORT).show();
                                else
                                    createXlFile4(g.getName(),seance1.getText().toString(),seancEend.getText().toString());
                            }
                        }
                    }
                        else {

                            perfromFileSearch();

                            Toast.makeText(activity, "import", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


    }
      public String idgg(){
        return getId;
    }
    private void perfromFileSearch(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            ((groupsActivity)activity).startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    1);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(activity, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }



    private void createXlFile(String grpname) {

        MuDataBase1 dbb = new MuDataBase1(activity);
        Cursor cursorA= dbb.readAllATTENDANCE();
        Cursor cursorS= dbb.readAllseance();
        stateListe = new ArrayList<>();


        // File filePath = new File(Environment.getExternalStorageDirectory() + "/Demo.xls");
        Workbook wb = new HSSFWorkbook();

        Cell cell1 = null;
        Cell cell2 = null;
        Cell cell3 = null;
        Cell cell4 = null;
        Cell cell = null;

        Sheet sheet = null;
        sheet = wb.createSheet("Demo Excel Sheet");
        //Now column and row
        Row rowU = sheet.createRow(0);
        cell1= rowU.createCell(1);
        cell1.setCellValue("Université Mustapha Stambouli Mascara");
        Row rowF = sheet.createRow(1);
        cell2= rowF.createCell(1);
        cell2.setCellValue("Faculté Sciences Exactes");
        Row rowD = sheet.createRow(2);
        cell3= rowD.createCell(1);
        cell3.setCellValue("Département Informatique");
        Row rowG = sheet.createRow(4);
        cell4= rowG.createCell(1);
        cell4.setCellValue(grpname);
        Row row = sheet.createRow(6);
        cell = row.createCell(0);
        cell.setCellValue("Num");
        cell = row.createCell(1);
        cell.setCellValue("matrucul");

        cell = row.createCell(2);
        cell.setCellValue("nom");


        cell = row.createCell(3);
        cell.setCellValue("prenom");

        // cell = row.createCell(4);
        // cell.setCellValue("absent");

        // cell = row.createCell(5);
        // cell.setCellValue("present");

        // cell = row.createCell(6);
        // cell.setCellValue("justifier");



        //column width
        sheet.setColumnWidth(1, (20 * 200));
        sheet.setColumnWidth(2, (30 * 200));
        sheet.setColumnWidth(3, (30 * 200));
        sheet.setColumnWidth(4, (30 * 100));
        sheet.setColumnWidth(5, (30 * 100));
        sheet.setColumnWidth(6, (30 * 100));




        for (int i = 0; i < studentList.size(); i++) {
            Row row1 = sheet.createRow(i + 7);

            cell = row1.createCell(0);
            cell.setCellValue(i+1);
            cell = row1.createCell(1);
            cell.setCellValue(studentList.get(i).getMat());

            cell = row1.createCell(2);
            cell.setCellValue(studentList.get(i).getName());

            cell = row1.createCell(3);
            cell.setCellValue((studentList.get(i).getFamName()));


            //  cell.setCellStyle(cellStyle);




            sheet.setColumnWidth(1, (20 * 200));
            sheet.setColumnWidth(2, (30 * 200));
            sheet.setColumnWidth(3, (30 * 200));


        }

        int j =0;
        int i=0;
        int x=0;
        while (cursorS.moveToNext()) {
            String se = cursorS.getString(2);
            String id_g = cursorS.getString(1);
            while (i < studentList.size()) {
                if (cursorA.moveToPosition(x)) {

                    String state = cursorA.getString(2);
                    String id2 = cursorA.getString(4);




                    if (adapter.sendGrp().equals(id2) ) {

                        Row rowp = sheet.getRow(i + 7);
                        cell = rowp.createCell(4 + j);
                        cell.setCellValue(state);
                    }
                    i++;
                    x++;

                }

            }
            if (adapter.sendGrp().equals(id_g)) {

                cell = row.createCell(4 + j);
                cell.setCellValue(se);
                sheet.setColumnWidth(j + 4, (30 * 200));
                j++;
            }


            i = 0;
        }


        String folderName = "Import Excel spm";
        String fileName = folderName + System.currentTimeMillis() + ".xls";
        String path = Environment.getExternalStorageDirectory() + File.separator + folderName + File.separator + fileName;

        File file = new File(Environment.getExternalStorageDirectory() + File.separator + folderName);
        if (!file.exists()) {
            file.mkdirs();
        }

        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(path);
            wb.write(outputStream);
            // ShareViaEmail(file.getParentFile().getName(),file.getName());
            Toast.makeText(activity.getApplicationContext(), "Excel Created in " + path, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(activity.getApplicationContext(), "Not OK", Toast.LENGTH_LONG).show();
            try {
                outputStream.close();
            } catch (Exception ex) {
                ex.printStackTrace();

            }
        }


    }
    private void createXlFile4(String grpname, String s1, String sEnd) {


        // File filePath = new File(Environment.getExternalStorageDirectory() + "/Demo.xls");
        Workbook wb = new HSSFWorkbook();

        Cell cell1 = null;
        Cell cell2 = null;Cell
                cell3 = null;
        Cell cell4 = null;
        Cell cell = null;

        Sheet sheet = null;
        sheet = wb.createSheet("Demo Excel Sheet");
        //Now column and row
        Row rowU = sheet.createRow(0);
        cell1= rowU.createCell(1);
        cell1.setCellValue("Université Mustapha Stambouli Mascara");
        Row rowF = sheet.createRow(1);
        cell2= rowF.createCell(1);
        cell2.setCellValue("Faculté Sciences Exactes");
        Row rowD = sheet.createRow(2);
        cell3= rowD.createCell(1);
        cell3.setCellValue("Département Informatique");
        Row rowG = sheet.createRow(4);
        cell4= rowG.createCell(1);
        cell4.setCellValue(grpname);
        Row row = sheet.createRow(6);
        cell = row.createCell(0);
        cell.setCellValue("Num");
        cell = row.createCell(1);
        cell.setCellValue("matrucul");

        cell = row.createCell(2);
        cell.setCellValue("nom");


        cell = row.createCell(3);
        cell.setCellValue("prenom");

        //  cell = row.createCell(4);
        // cell.setCellValue("absent");

        // cell = row.createCell(5);
        // cell.setCellValue("present");

        //  cell = row.createCell(6);
        //  cell.setCellValue("justifier");




        //column width
        sheet.setColumnWidth(1, (20 * 200));
        sheet.setColumnWidth(2, (30 * 200));
        sheet.setColumnWidth(3, (30 * 200));
        sheet.setColumnWidth(4, (30 * 200));
        sheet.setColumnWidth(5, (30 * 200));
        sheet.setColumnWidth(6, (30 * 200));




        for (int i = 0; i < studentList.size(); i++) {
            Row row1 = sheet.createRow(i + 7);

            cell = row1.createCell(0);
            cell.setCellValue(i+1);
            cell = row1.createCell(1);
            cell.setCellValue(studentList.get(i).getMat());

            cell = row1.createCell(2);
            cell.setCellValue(studentList.get(i).getName());

            cell = row1.createCell(3);
            cell.setCellValue((studentList.get(i).getFamName()));

            // cell = row1.createCell(4);
            //if (i<adapter4.getAbsence().size()){
            // cell.setCellValue((adapter4.getAbsence().get(i)));}
            // cell = row1.createCell(5);
            // cell.setCellValue((adapter4.getPresent().get(i)));
            // cell = row1.createCell(6);
            // cell.setCellValue((adapter4.getJustifier().get(i)));

            //  cell.setCellStyle(cellStyle);




            sheet.setColumnWidth(1, (20 * 200));
            sheet.setColumnWidth(2, (30 * 200));
            sheet.setColumnWidth(3, (30 * 200));
            // sheet.setColumnWidth(4, (30 * 50));
            //  sheet.setColumnWidth(5, (30 * 50));
            //   sheet.setColumnWidth(6, (30 * 50));

        }
        MuDataBase1 dbb = new MuDataBase1(activity);
        Cursor cursorA= dbb.readAllATTENDANCE();
        Cursor cursorS= dbb.readAllseance();
        stateListe = new ArrayList<>();
        int j =0;
        int i=0;

        int a= Integer.parseInt(s1)-1,z=Integer.parseInt(sEnd)-1;
        int x= studentList.size()*a;

        while (cursorS.moveToPosition(a) &&j<=z){
            String se = cursorS.getString(2);
            String id_g =cursorS.getString(1);

            while (i<studentList.size())  {
                if (cursorA.moveToPosition(x)) {
                    String state = cursorA.getString(2);
                    String id2 =cursorA.getString(4);

                    Row rowp = sheet.getRow(i+7);
                    cell = rowp.createCell(4+j);
                    if (adapter.sendGrp().equals(id2)) {

                        cell.setCellValue(state);
                    }
                    i++;
                    x++;
                }

            }
            if (adapter.sendGrp().equals(id_g)) {

                cell = row.createCell(4 + j);
                cell.setCellValue(se);

                j++;
                sheet.setColumnWidth(j + 4, (30 * 200));
            }
            i=0;
            a++;
        }



        String folderName = "Import Excel spm";
        String fileName = folderName + System.currentTimeMillis() + ".xls";
        String path = Environment.getExternalStorageDirectory() + File.separator + folderName + File.separator + fileName;

        File file = new File(Environment.getExternalStorageDirectory() + File.separator + folderName);
        if (!file.exists()) {
            file.mkdirs();
        }

        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(path);
            wb.write(outputStream);
            // ShareViaEmail(file.getParentFile().getName(),file.getName());
            Toast.makeText(activity.getApplicationContext(), "Excel Created in " + path, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(activity.getApplicationContext(), "Not OK", Toast.LENGTH_LONG).show();
            try {
                outputStream.close();
            } catch (Exception ex) {
                ex.printStackTrace();

            }
        }


    }

    public void setGroups(List<group> g){
        this.groupList=g;
        notifyDataSetChanged();
    }



    public void onDismissDialog(int y,String gType){
        studentList.clear();
        studentList = db.getstudent(y,gType);
        adapter.setStudentList(studentList);
        adapter4.setStudentList(studentList);

    }
    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView groupName;
        View v;
        ImageView edit,delete,export;
        LinearLayout linearLayout;
        CardView lite_grp, seance;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            seance= itemView.findViewById(R.id.seance);
            lite_grp= itemView.findViewById(R.id.lite_grp);
            linearLayout= itemView.findViewById(R.id.expnded_grp);
            groupName=itemView.findViewById(R.id.group_name_text);
            edit = itemView.findViewById(R.id.edit_c);
            delete = itemView.findViewById(R.id.del_g);
            export=itemView.findViewById(R.id.export);
            v=itemView;


        }
    }
}
