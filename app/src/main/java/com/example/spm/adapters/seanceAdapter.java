package com.example.spm.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ipsec.ike.TunnelModeChildSessionParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spm.MuDataBase1;
import com.example.spm.R;
import com.example.spm.StudentsOfSeanceActivity;
import com.example.spm.classes.attendance;
import com.example.spm.classes.sean;
import com.example.spm.classes.student;
import com.example.spm.classes.studentOfSeance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class seanceAdapter extends RecyclerView.Adapter<seanceAdapter.viewHolder> {

    private Context activity;

    private List<sean> seanceList;
    private MuDataBase1 db;
    private List<studentOfSeance> studentList;
    private int pos;



    public seanceAdapter(Context activity, List<sean> seanceList) {
        this.activity = activity;

        this.seanceList = seanceList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.seance_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        sean s = seanceList.get(position);
        int nmbr = position+1;
        int p = position;

        db = new MuDataBase1(activity);
        Cursor cursorA = db.readAllATTENDANCE();

        holder.h1.setText(s.getH1().toString());
        holder.h2.setText(s.getH2().toString());
        holder.datt.setText(s.getDate().toString());
        holder.seanceName.setText("Seance "+nmbr);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        holder.v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                builder.setTitle("Delete Seance")
                        .setMessage("Do you want to delete this Seance ?")
                        .setCancelable(true)
                        .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                int del=0;
                                while (cursorA.moveToNext()) {

                                    String id2 = cursorA.getString(3);
                                    if (s.getId().equals(id2)){
                                        del=1;
                                    }

                                }
                                if (del==1){
                                    Toast.makeText(activity, "you can't delete this seance", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    db.deleteSeance(s.getId());
                                    seanceList.remove(p);
                                    notifyItemRemoved(p);
                                    notifyItemChanged(p);
                                }


                                dialog.dismiss();

                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();

                return false;
            }
        });




        holder.seanceName.setText("Seance "+nmbr);
        studentList = getstudentList(position);

        int called = getCalledNmbr(getstudentList(position),s.getId());

        if (called==studentList.size()) {
            holder.presenceLayout.setVisibility(View.VISIBLE);

            int prsentStudents = getSeancePresentNmbr(studentList,s.getId());
            int studentsTotal = studentList.size();

            if (studentsTotal!=0) {
                holder.presenceProgress.setProgress((100*prsentStudents)/studentsTotal);
                holder.presence.setText(prsentStudents + " students of " + studentsTotal);
            } else {
                holder.presenceLayout.setVisibility(View.GONE);
            }

        } else {
            db.updateSeanceCall(Integer.parseInt(s.getId()), false);
            s.setCalled("NO");
            holder.presenceLayout.setVisibility(View.GONE);

        }




        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, StudentsOfSeanceActivity.class);
                i.putExtra("GROUP_ID",s.getId_G());
                i.putExtra("GROUP_type",s.getgType());
                // for testing ur idea of dialog
                i.putExtra("SEANCE_ID",s.getId());
                String t = s.getCalled();


                if (t.equals("CALLED")) {
                    i.putExtra("CALLED", true);

                } else {
                    i.putExtra("CALLED", false);
                }







                activity.startActivity(i);
            }
        });
            MuDataBase1 md = new MuDataBase1(activity);
        holder.coment_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog commentDialog = new Dialog(activity);
                commentDialog.setContentView(R.layout.add_comment_student);
                commentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                EditText editText= commentDialog.findViewById(R.id.ET_comment);
                Cursor cursor= db.readAllseance();
                while (cursor.moveToNext()) {

                    String cm = cursor.getString(7);
                    String id =cursor.getString(0);
                    if (String.valueOf(s.getId()).equals(id)) editText.setText(cm);
                }
                AppCompatButton cancel, confirm;
                cancel= commentDialog.findViewById(R.id.cncl_btn_comment);
                confirm=commentDialog.findViewById(R.id.confirm_btn_comment);

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String ss = editText.getText().toString();
                        md.comment_seance(Integer.parseInt(s.getId()),ss);
                        commentDialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        commentDialog.dismiss();
                    }
                });
                commentDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return seanceList.size();
    }


    public void setStudentList(List<sean> slist) {
        this.seanceList = slist;
        notifyDataSetChanged();
    }



    public List<studentOfSeance> getstudentList(int p) {
        sean f = seanceList.get(p);
        db=new MuDataBase1(activity);
        List<studentOfSeance> b=db.getstudentOfseance(Integer.parseInt(f.getId_G()),f.getgType());

        return b;
    }


    public int getCalledNmbr(List<studentOfSeance> k,String seanceId) {
        int b=0;
        db = new MuDataBase1(activity);
        for (int i =0;i<k.size();i++) {
            studentOfSeance s =k.get(i);
            attendance a = db.getStudentAttendance(seanceId,s.getId());
            if (a.getState()!=null) {
                b++;
            }
        }

        return b;
    }




    public int getSeancePresentNmbr(List<studentOfSeance> k,String seanceId) {
        int b=0;
        db = new MuDataBase1(activity);
        for (int i =0;i<k.size();i++) {
            studentOfSeance s =k.get(i);
            attendance a = db.getStudentAttendance(seanceId,s.getId());
            if (a.getState()!=null && a.getState().equals("PRESENT")) {
                    b++;
            }
        }

        return b;
    }




    public class viewHolder extends RecyclerView.ViewHolder {
        TextView h1, h2, seanceName, datt, presence;
        View v;
        ProgressBar presenceProgress;
        LinearLayout presenceLayout;
        ImageView coment_s;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            h1 = itemView.findViewById(R.id.h_start);
            h2 = itemView.findViewById(R.id.h_end);
            seanceName = itemView.findViewById(R.id.name_ss);
            datt = itemView.findViewById(R.id.date_ss);
            presenceProgress=itemView.findViewById(R.id.seance_progress_bar);
            presence = itemView.findViewById(R.id.seance_presence);
            presenceLayout = itemView.findViewById(R.id.presence_layout);
            coment_s=itemView.findViewById(R.id.coment_s);
            v = itemView;
        }
    }




}
