package com.example.spm.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spm.MuDataBase1;
import com.example.spm.R;
import com.example.spm.classes.classe;
import com.example.spm.classes.group;
import com.example.spm.groupsActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class classesAdapter extends RecyclerView.Adapter<classesAdapter.viewHolder> {

    private Activity activity;
    private List<classe> classeList;

    public classesAdapter(Activity a, List<classe> list) {
        this.activity = a;
        notifyDataSetChanged();

        Collections.reverse(list);
        this.classeList = list;
    }



    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.classes_layout,parent,false);
        return new classesAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        int i = position;
        classe c = classeList.get(position);
        holder.className.setText(c.getName());
        holder.v.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                   String s= c.getName().toString();
                   String idd= c.getId_c();
                Intent i = new Intent(activity, groupsActivity.class);
                i.putExtra("NAME",s);
                i.putExtra("IDC",idd);
                activity.startActivity(i);

            }
        });
         holder.edit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
            Dialog edit_d= new Dialog(activity);
            edit_d.setContentView(R.layout.update);
            edit_d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                 EditText edit_class= edit_d.findViewById(R.id.txt_update);

                 AppCompatButton cancel= edit_d.findViewById(R.id.cancel_update);
                 AppCompatButton update= edit_d.findViewById(R.id.Update);
                 edit_class.setText(c.getName());
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
                                      String clas= edit_class.getText().toString();
                                      myDb.updateData(c.getId_c(),clas);
                                      holder.className.setText(edit_class.getText());
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
        MuDataBase1 db = new MuDataBase1(activity);
        String classId = c.getId_c();

        holder.delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                builder.setTitle("Delete class")
                        .setMessage("Do you want to delete this class ?")
                        .setCancelable(true)
                        .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean b = db.deleteClass( c.getId_c());
                                if(b){
                                    Toast.makeText(activity, "Class Deleted successfully", Toast.LENGTH_SHORT).show();
                                    deleteGroupsOfClass(classId);
                                    classeList.remove(i);
                                    notifyItemRemoved(i);
                                    notifyItemChanged(i);
                                } else {

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






    }


    public void setclass(List<classe> list){
        this.classeList=list;
        notifyDataSetChanged();
    }





    @Override
    public int getItemCount() {
        return classeList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView className;
        View v;
        ImageView edit,delete;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.classes_text);
            edit = itemView.findViewById(R.id.edit_c);
            delete =itemView.findViewById(R.id.del_c);
            v= itemView;
        }
    }




    public void deleteGroupsOfClass(String classId){
        MuDataBase1 db = new MuDataBase1(activity);
        List<group> groupsList = new ArrayList<>();

        groupsList = db.getgroups(classId);

        for (int i=0;i<groupsList.size();i++){
            group g = groupsList.get(i);

            boolean d =  db.deleteGroups(g.getId(),g.getType());
            if(d){
                Toast.makeText(activity, "groups deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "Failed groups", Toast.LENGTH_SHORT).show();
            }

        }



}

}
