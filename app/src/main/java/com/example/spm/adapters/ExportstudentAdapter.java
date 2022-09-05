package com.example.spm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spm.MuDataBase1;
import com.example.spm.R;
import com.example.spm.classes.student;

import java.util.ArrayList;
import java.util.List;

public class ExportstudentAdapter extends RecyclerView.Adapter<ExportstudentAdapter.viewHolder> {

    private Context activity;
    private List<student> studentList;
    private List<Integer> absList,prList,juList;
    private String gId;
    int present, absence, justified;

    public ExportstudentAdapter(Context activity, List<student> studentList) {
        this.activity = activity;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.export_layout,parent,false);
        return new viewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        student s = studentList.get(position);
          MuDataBase1 db = new MuDataBase1(activity);

                  present = db.calculAbsence(gId, s.getId(), "PRESENT").size();
                  absence = db.calculAbsence(gId, s.getId(), "ABSENT").size();
                  justified = db.calculAbsence(gId, s.getId(), "JUSTIFIED").size();
                  absList= new ArrayList<Integer>();
                  prList= new ArrayList<Integer>();
                  juList= new ArrayList<Integer>();
                  int i=0;
                 while (absList.size()<studentList.size()){
                     absList.add(db.calculAbsence(gId, studentList.get(i).getId(), "ABSENT").size());
                     prList.add(db.calculAbsence(gId, studentList.get(i).getId(), "PRESENT").size());
                     juList.add(db.calculAbsence(gId, studentList.get(i).getId(), "JUSTIFIED").size());
                     i++;
                 }
        holder.mat.setText(String.valueOf(s.getMat()));
        holder.famName.setText(s.getFamName());
        holder.name.setText(s.getName());
        holder.absentN.setText(String.valueOf(present));
        holder.presentN.setText(String.valueOf(absence));


        holder.justify.setText(String.valueOf(justified));







    }


    @Override
    public int getItemCount() {
        return studentList.size();
    }
  public List<Integer> getAbsence(){

        return absList;
  }

    public List<Integer> getPresent(){

        return prList;
    }
    public List<Integer> getJustifier(){

        return juList;
    }
    public void setStudentList(List<student> slist) {
        this.studentList = slist;
        notifyDataSetChanged();
    }

    public void getGroup(String Id){
        this.gId=Id;
    }
     public String sendGrp(){
        return gId;
     }



    public class viewHolder extends RecyclerView.ViewHolder {
        TextView  mat, famName, name,justify,absentN, presentN;
        View v;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            mat = itemView.findViewById(R.id.mat_ex);
            famName = itemView.findViewById(R.id.prenomex);
            name = itemView.findViewById(R.id.nomex);
            v = itemView;
            justify = itemView.findViewById(R.id.juex);
            absentN = itemView.findViewById(R.id.abex);
            presentN = itemView.findViewById(R.id.prex);

        }
    }



}
