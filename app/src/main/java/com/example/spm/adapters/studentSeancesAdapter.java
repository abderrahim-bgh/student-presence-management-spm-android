package com.example.spm.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spm.MuDataBase1;
import com.example.spm.R;
import com.example.spm.classes.attendance;
import com.example.spm.classes.sean;

import java.util.List;

public class studentSeancesAdapter extends RecyclerView.Adapter<studentSeancesAdapter.viewHolder> {
    Activity activity;
    private List<sean> seanceList;
    private int studentId;
    private MuDataBase1 db;

    public studentSeancesAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.students_seances,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        sean s = seanceList.get(position);
        db = new MuDataBase1(activity);
        holder.hs.setText(s.getH1());
        holder.he.setText(s.getH2());
        holder.date.setText(s.getDate());
        holder.seanceName.setText(s.getNameSeance());

        attendance a =  db.getStudentAttendance(s.getId(),studentId);
        holder.state.setText(a.getState());

        if (a.getState()==null){
            holder.state.setVisibility(View.INVISIBLE);
        } else {

            if (a.getState().equals("ABSENT")) {
                holder.state.setVisibility(View.VISIBLE);
                holder.state.setTextColor(activity.getResources().getColor(R.color.absent));
            }  else if (a.getState().equals("PRESENT")) {
                holder.state.setVisibility(View.VISIBLE);
                holder.state.setTextColor(activity.getResources().getColor(R.color.present));
            } else if(a.getState().equals("JUSTIFIED")){
                holder.state.setVisibility(View.VISIBLE);
                holder.state.setText("JUSTIFIED");
                holder.state.setTextColor(activity.getResources().getColor(R.color.blue));
            }
        }

        holder.modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog updatestate = new Dialog(activity);
                updatestate.setContentView(R.layout.modifey_state_dialog);
                updatestate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                AppCompatButton cancel = updatestate.findViewById(R.id.attendance_dialogu_absent);
                AppCompatButton modify = updatestate.findViewById(R.id.attendance_dialog_present);
                TextView studentName = updatestate.findViewById(R.id.attendance_student_name);
                MuDataBase1 mb =new MuDataBase1(activity);

                String id_A;
                id_A=String.valueOf(studentId);
                if (holder.state.getText().toString().equals("justified")) {
                    studentName.setText("the student is justified");
                    modify.setVisibility(View.GONE);
                }
                if (holder.state.getText().toString().equals("PRESENT")) modify.setText("ABSENT");
                else if (holder.state.getText().toString().equals("ABSENT")) modify.setText("PRESENT");

                modify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.state.getText().toString().equals("PRESENT")){
                            modify.setText("ABSENT");
                            holder.state.setText("ABSENT");
                            holder.state.setTextColor(activity.getResources().getColor(R.color.absent));
                            mb.updatestate(id_A,"ABSENT",s.getId());
                            updatestate.dismiss();
                        }
                        else if (holder.state.getText().toString().equals("ABSENT")){
                            modify.setText("PRESENT");
                            holder.state.setText("PRESENT");
                            holder.state.setTextColor(activity.getResources().getColor(R.color.present));
                            mb.updatestate(id_A,"PRESENT",s.getId());
                            updatestate.dismiss();
                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updatestate.dismiss();
                    }
                });

                updatestate.show();

            }
        });
        //imene

        holder.justify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog updatestate = new Dialog(activity);
                updatestate.setContentView(R.layout.modifey_state_dialog);
                updatestate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                AppCompatButton cancel = updatestate.findViewById(R.id.attendance_dialogu_absent);
                AppCompatButton modify = updatestate.findViewById(R.id.attendance_dialog_present);
                TextView studentName = updatestate.findViewById(R.id.attendance_student_name);

                MuDataBase1 mb =new MuDataBase1(activity);
                String id_A;
                id_A=String.valueOf(studentId);
                if (holder.state.getText().toString().equals("PRESENT")) {
                    studentName.setText("the student is present");
                    modify.setVisibility(View.GONE);
                }
                if (holder.state.getText().toString().equals("ABSENT")) modify.setText("justify");
                else if (holder.state.getText().toString().equals("JUSTIFIED")) modify.setText("ABSENT");
                modify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.state.getText().toString().equals("ABSENT")){
                            modify.setText("JUSTIFIED");
                            holder.state.setText("JUSTIFIED");
                            holder.state.setTextColor(activity.getResources().getColor(R.color.blue));

                            mb.updatestate(id_A,"JUSTIFIED",s.getId());
                            updatestate.dismiss();
                        }
                        else if (holder.state.getText().toString().equals("JUSTIFIED")){
                            modify.setText("ABSENT");
                            holder.state.setText("ABSENT");
                            holder.state.setTextColor(activity.getResources().getColor(R.color.absent));
                            mb.updatestate(id_A,"ABSENT",s.getId());
                            updatestate.dismiss();
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updatestate.dismiss();
                    }
                });

                updatestate.show();
            }
        });






    }

    @Override
    public int getItemCount() {
        return seanceList.size();
    }

    public  void setStudentId(int id) {
        this.studentId = id;
    }

    public  void setSeanceList(List<sean> s) {
        this.seanceList = s;
    }





    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView hs,he,date,seanceName,state;
        private ImageView justify,modify,comment;
        private View v;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            hs = itemView.findViewById(R.id.Start_Hour_Seance);
            he = itemView.findViewById(R.id.End_Hour_Seance);
            date = itemView.findViewById(R.id.date_of_seance);
            seanceName = itemView.findViewById(R.id.Name_of_seance);
            state = itemView.findViewById(R.id.State_of_Presence);
            modify= itemView.findViewById(R.id.update_state_of_seance);
            justify = itemView.findViewById(R.id.Justify_seance);
            comment = itemView.findViewById(R.id.Comment_on_student);
            v = itemView;

        }


    }
}
