package com.example.spm.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spm.MuDataBase1;
import com.example.spm.R;
import com.example.spm.StudentInfoActivity;
import com.example.spm.StudentsActivity;
import com.example.spm.classes.classe;
import com.example.spm.classes.group;
import com.example.spm.classes.onDialogDissmissed;
import com.example.spm.classes.student;
import com.example.spm.classes.studentOfSeance;
import com.example.spm.classes.year;

import java.util.ArrayList;
import java.util.List;

public class studentAdapter extends RecyclerView.Adapter<studentAdapter.viewHolder> {

    private Context activity;
    private List<student> studentList;
    private String gId;

    private String sClass="";
    private String sGroup="";
    private String syear="";

    List<year> yearList ;
    List<classe> classeList = new ArrayList<>();
    List<group> groupList = new ArrayList<>()   ;


    classesAdapterChangeGroup classAdapter ;
    changeGroupItemsAdapter gtdAdapter;
    changeGroupItemsAdapter gtpAdapter;


    boolean yearSelected = false;
    boolean classSelected = false;
    boolean groupSelected=  false;


    public studentAdapter(Context activity, List<student> studentList) {
        this.activity = activity;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_layout,parent,false);
        return new studentAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        student s = studentList.get(position);
        int nmbr = position+1;
        holder.n.setText("N° : "+ nmbr);
        holder.mat.setText("Mat :"+s.getMat());
        holder.famName.setText(s.getFamName());
        holder.name.setText(s.getName());
        holder.absentN.setVisibility(View.GONE);
        holder.justify.setVisibility(View.GONE);
/*
        MuDataBase1 databo = new MuDataBase1(activity);
        int absenceNmbr = databo.calculAbsence(gId,s.getId(),"ABSENT").size();
        if( absenceNmbr > 0 && absenceNmbr <= 2){
            holder.absentN.setText("Absence : "+absenceNmbr);
            holder.absentN.setVisibility(View.VISIBLE);

        }else if (absenceNmbr > 2 ){
            holder.absentN.setText("Exclue : "+absenceNmbr +" absence");
            holder.absentN.setVisibility(View.VISIBLE);
            holder.absentN.setTextColor(activity.getResources().getColor(R.color.absent));
        }
        int justifiedNmbr = databo.calculAbsence(gId,s.getId(),"JUSTIFIED").size();

        if( justifiedNmbr > 0 && justifiedNmbr <= 4){
            holder.justify.setText("justified : "+justifiedNmbr);
            holder.justify.setVisibility(View.VISIBLE);

        }else if (justifiedNmbr > 4 ){
            holder.justify.setText("Exclue : "+ justifiedNmbr +" justified ");
            holder.justify.setVisibility(View.VISIBLE);
            holder.justify.setTextColor(activity.getResources().getColor(R.color.absent));
        }
        
 */


        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, StudentInfoActivity.class);
                i.putExtra("STUDENT_ID",s.getId());
                i.putExtra("STUDENT_NAME",s.getName());
                i.putExtra("STUDENT_FAMILY_NAME",s.getFamName());
                i.putExtra("STUDENT_MAT",s.getMat());
                i.putExtra("STUDENT_GROUP_TD_ID",s.getId_G_TD());
                i.putExtra("STUDENT_GROUP_TP_ID",s.getId_G_TP());
                activity.startActivity(i);

            }
        });

        holder.v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openchangeStudentGroupDialog(s);

                return false;
            }
        });



    }


    public void updateStudentInfo(int pos){
        student s = studentList.get(pos);
        openUpdateStudentDialog(s,pos);
        notifyItemChanged(pos);

    }

    private void openUpdateStudentDialog (student s, int pos){
        Dialog updateDialog = new Dialog(activity);
        MuDataBase1 db = new MuDataBase1(activity);

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

                String name = studentName.getText().toString();
                String famname = studentFamName.getText().toString();
                String m = studentMat.getText().toString();

                int mat = Integer.parseInt(m);
                student a = db.studentByMatExist(mat);

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
                } else if (a.getId()!=s.getId() && a.getId()!=-1) {
                    studentMat.setError("there is an other registered student with this matrecule");
                    Toast.makeText(activity, "there is an other registered student with this matrecule", Toast.LENGTH_SHORT).show();
                }
                else {

                    boolean b = false;
                    b = db.updateStudentInfo(s.getId(),mat,name,famname);


                    if (b) {
                        Toast.makeText(activity, "student added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "failed student", Toast.LENGTH_SHORT).show();
                    }

                    updateDialog.dismiss();

                }
            }
        });

        updateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (activity instanceof onDialogDissmissed){
                    ((onDialogDissmissed)activity).onStudentUpdated(dialog);
                }
            }
        });

        updateDialog.show();

    }



    public Context getContext(){
       return activity;

    }

    public void deleteStudent(int pos){

        MuDataBase1 db = new MuDataBase1(activity);
        student s = studentList.get(pos);
        boolean b = db.deleteStudent(String.valueOf(s.getId()));
        if(b){
            Toast.makeText(activity, "student Deleted successfully", Toast.LENGTH_SHORT).show();
            studentList.remove(pos);
            notifyItemRemoved(pos);
        } else {
            Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
        }

    }






    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public void setStudentList(List<student> slist) {
        this.studentList = slist;
    }

    public void getGroup(String Id){
        this.gId=Id;
    }


    @UiThread
    protected void dataSetChanged(int pos) {
        notifyDataSetChanged();
    }



    private void openchangeStudentGroupDialog (student s){
        Dialog changeGroupDialogue = new Dialog(activity);
        MuDataBase1 db = new MuDataBase1(activity);



        yearsAdapterChangeGroup yearAdapter;


        yearAdapter = new yearsAdapterChangeGroup((Activity) activity);
        classAdapter = new classesAdapterChangeGroup((Activity) activity);
        gtpAdapter = new changeGroupItemsAdapter((Activity) activity);
        gtdAdapter =  new changeGroupItemsAdapter((Activity) activity);


        changeGroupDialogue.setContentView(R.layout.change_group_layout);
        changeGroupDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView close = changeGroupDialogue.findViewById(R.id.close_dialog_c);
        ImageView previous = changeGroupDialogue.findViewById(R.id.previouse_);

        LinearLayout yearLayout = changeGroupDialogue.findViewById(R.id.yearsLayout_changeGroup);
        LinearLayout classLayout = changeGroupDialogue.findViewById(R.id.classes_layout_changeGroup);
        LinearLayout groupsLayout = changeGroupDialogue.findViewById(R.id.groups_layout_changeGroup);

        CardView yearCard = changeGroupDialogue.findViewById(R.id.year_card_changeGroup);
        CardView classCard = changeGroupDialogue.findViewById(R.id.classes_card_changeGroup);

        RecyclerView yearsRecycler = changeGroupDialogue.findViewById(R.id.years_recyclerView_change);
        yearsRecycler.setLayoutManager(new GridLayoutManager(activity,3));

        RecyclerView classesRecycler = changeGroupDialogue.findViewById(R.id.classes_recyclerView_change);
        classesRecycler.setLayoutManager(new GridLayoutManager(activity,3));


        RecyclerView tdRecycler = changeGroupDialogue.findViewById(R.id.groups_td_recyclerView_change);
        tdRecycler.setLayoutManager(new GridLayoutManager(activity,3));



        TextView yearText = changeGroupDialogue.findViewById(R.id.year_text_changeGroup);
        TextView classText =changeGroupDialogue.findViewById(R.id.class_text_changeGroup);

        TextView classChooseText = changeGroupDialogue.findViewById(R.id.class_choose);
        TextView tdChooseText =changeGroupDialogue.findViewById(R.id.group_TD_Choose);




        groupsLayout.setVisibility(View.GONE);
        classLayout.setVisibility(View.GONE);
        classCard.setVisibility(View.GONE);
        yearCard.setVisibility(View.GONE);

        previous.setVisibility(View.INVISIBLE);
        yearList = getAllYears();

        yearsRecycler.setAdapter(yearAdapter);
        yearAdapter.setList(yearList);

        group v = db.getGroupById(gId);


        yearAdapter.setOnItemClickListener(new yearsAdapterChangeGroup.recyclerViewClickInterface() {
            @Override
            public void onYearItemClicked(year y) {

                classLayout.setVisibility(View.VISIBLE);
                yearCard.setVisibility(View.VISIBLE);
                yearText.setText("Year :"+y.getYear());
                classeList.clear();
                List<classe> tmp = getclassesbyYear(y.getYear());


                for (int i =0;i<tmp.size();i++) {
                    classe c = tmp.get(i);
                    List<group> groupTmp = db.getGroupsByType(Integer.parseInt(c.getId_c()), v.getType());
                    if (groupTmp.size()!=0) {
                        classeList.add(c);
                    }
                }

                classAdapter.setList(classeList);
                yearLayout.setVisibility(View.GONE);
                yearSelected = true;
                syear = y.getYear();
                previous.setVisibility(View.VISIBLE);

                classesRecycler.setAdapter(classAdapter);

                if(classeList.size()==0){
                    classChooseText.setText("No Classes");
                }


            }
        });



        classAdapter.setOnItemClickListener(new classesAdapterChangeGroup.classRecyclerViewClickInterface() {
            @Override
            public void onClassItemClicked(classe c) {

                classLayout.setVisibility(View.GONE);
                classCard.setVisibility(View.VISIBLE);
                classText.setText("Class :"+c.getName());
                groupsLayout.setVisibility(View.VISIBLE);

                if (groupList.size()!=0){
                    groupList.clear();

                }



                List<group> tmp = db.getGroupsByType(Integer.parseInt(c.getId_c()),v.getType());

                for (int i =0;i<tmp.size();i++) {
                    group g = tmp.get(i);
                    if (!g.getId().equals(gId)) {
                        groupList.add(g);
                    }
                }

                gtdAdapter.setList(groupList);

                classSelected = true;
                sClass=c.getName();

                tdRecycler.setAdapter(gtdAdapter);

                if (groupList.size()==0){
                    tdChooseText.setText("No group "+v.getType());
                } else {
                    String d = v.getType().toUpperCase();
                    tdChooseText.setText(d+" groups :");
                }





            }
        });

        gtdAdapter.setOnItemClickListener(new changeGroupItemsAdapter.groupRecyclerViewClickInterface() {
            @Override
            public void onGroupItemClicked(group y) {

                groupSelected = true;
                sGroup = y.getName();
                AlertDialog.Builder builder=new AlertDialog.Builder(activity);
                builder.setMessage("do you want to change this student's group to : \n group :"+
                        sGroup+"\n class :"+sClass +"\n year :"+syear
                )
                        .setTitle("Change Student's Group")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean b;
                                if (v.getType().equals("td")) {
                                     b = db.updateStudent_group(s.getId(), Integer.parseInt(y.getId()), s.getId_G_TP());
                                } else {
                                    b = db.updateStudent_group(s.getId(),s.getId_G_TD(),Integer.parseInt(y.getId()));
                                }
                                if (b){
                                    Toast.makeText(activity, "group changed to group "+y.getType()+" : "+sGroup , Toast.LENGTH_SHORT).show();
                                        int i = 0;
                                        boolean found = false;
                                        while (i < studentList.size() && !found) {
                                            student m = studentList.get(i);
                                            if (m.getId() == s.getId()) {
                                                found = true;
                                                studentList.remove(i);
                                                notifyItemRemoved(i);
                                                boolean c = db.deleteStudentAttendence(String.valueOf(s.getId()),v.getId());
                                            } else {
                                                i++;
                                            }
                                        }



                                } else {
                                    Toast.makeText(activity, "changed failed", Toast.LENGTH_SHORT).show();
                                }

                                changeGroupDialogue.dismiss();
                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        changeGroupDialogue.dismiss();
                    }
                });
                builder.show();
                changeGroupDialogue.dismiss();
            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  onDismissDialog(year);
                changeGroupDialogue.dismiss();
            }
        });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            if (classSelected){
                    groupsLayout.setVisibility(View.GONE);
                    classLayout.setVisibility(View.VISIBLE);
                    yearLayout.setVisibility(View.GONE);
                    classCard.setVisibility(View.GONE);
                    yearCard.setVisibility(View.VISIBLE);
                    classSelected=false;
                } else if (yearSelected){
                    classCard.setVisibility(View.GONE);
                    yearCard.setVisibility(View.GONE);
                    classLayout.setVisibility(View.GONE);
                    yearLayout.setVisibility(View.VISIBLE);
                    previous.setVisibility(View.INVISIBLE);
                }


            }
        });




        changeGroupDialogue.show();

    }




    public List<year> getAllYears(){

        List<year> yearList= new ArrayList<>();
        MuDataBase1 mdb = new MuDataBase1(activity);
        Cursor cursor= mdb.readAllYears();
        if (cursor.getCount()==0){
            Toast.makeText(activity, "No data", Toast.LENGTH_SHORT).show();
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


        return yearList;
    }


    public List<classe> getclassesbyYear(String yer){

        List<classe> classList= new ArrayList<>();
        MuDataBase1 mdb = new MuDataBase1(activity);
        Cursor cursor= mdb.readAllClasses();
        if (cursor.getCount()==0){
            Toast.makeText(activity, "No data", Toast.LENGTH_SHORT).show();
        }else {

            while (cursor.moveToNext()) {
                String cl = null;
                String idy = null;
                String idc= null;

                classe cla = new classe();
                cl= cursor.getString(1);
                idy= cursor.getString(0);
                idc= cursor.getString(2);
                cla.setName(cl);
                cla.setId_c(idc);
                cla.setId_y(idy);
                if (yer.equals(idy)){classList.add(cla);}


            }
        }
        return classList;
    }







    public class viewHolder extends RecyclerView.ViewHolder {
        TextView n, mat, famName, name,justify,absentN;
        View v;
        CardView card;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            n = itemView.findViewById(R.id.student_nmbr_text);
            mat = itemView.findViewById(R.id.student_matrecule_text);
            famName = itemView.findViewById(R.id.student_familyName_text);
            name = itemView.findViewById(R.id.student_name_text);
            v = itemView;
            justify = itemView.findViewById(R.id.student_absence_justified);
            absentN = itemView.findViewById(R.id.student_absence);
            card = itemView.findViewById(R.id.student_layout);
        }
    }
}
