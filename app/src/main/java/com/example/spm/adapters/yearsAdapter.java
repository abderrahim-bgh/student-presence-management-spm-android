package com.example.spm.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spm.MainActivity;
import com.example.spm.MuDataBase1;
import com.example.spm.R;
import com.example.spm.classes.classe;
import com.example.spm.classes.group;
import com.example.spm.classes.year;
import com.example.spm.classesActivity;
import com.example.spm.loading;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class yearsAdapter extends RecyclerView.Adapter<yearsAdapter.viewHolder> {

    private Activity activity;
    private List<year> yearList;
    List<classe> classList;
    classesAdapter adapter;
    Dialog addClassDialog;
    String tp1="",td1="";

    public yearsAdapter(Activity a, List<year> y) {
        this.activity = a;
        Collections.reverse(y);
        this.yearList = y;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.year_layout,parent,false);
        return new viewHolder(view);
    }

    @Override

    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {
        year y = yearList.get(position);
        boolean b = y.isExpandble();
        holder.expandedLayout.setVisibility(b ? View.VISIBLE : View.GONE);
        holder.text.setText(y.getYear());

        if (b){
            holder.arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
        } else {
            holder.arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
        }

        classList= new ArrayList<>();
        MuDataBase1 mdb = new MuDataBase1(activity);





        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                y.setExpandble(!y.isExpandble());
                year ye= new year();
                ye.setIdY(holder.text.getText().toString());
                if (y.isExpandble()){
                    holder.arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                } else {
                    holder.arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                }
                holder.expandedLayout.setVisibility(y.isExpandble() ? View.VISIBLE : View.GONE);


            }
        });
        String yer = yearList.get(position).getYear();
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



        adapter = new classesAdapter(activity,classList);
        holder.classesRecycler.setAdapter(adapter);
        adapter.setclass(classList);

        addClassDialog = new Dialog(getC());

        holder.addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isEnabled()){
                    String idY= y.getYear();
                    openAddClassDialog(idY);



                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return yearList.size();
    }

    public void setList(List<year> list){
        this.yearList=list;
        notifyDataSetChanged();
    }

    public Context getC(){
        return activity;
    }


    private void openAddClassDialog (String idY){
        addClassDialog.setContentView(R.layout.add_class_dialog);
        addClassDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        AppCompatButton cancel = addClassDialog.findViewById(R.id.cncl_btn_addClass);
        AppCompatButton add = addClassDialog.findViewById(R.id.add_btn_addClass);
        AppCompatButton td =addClassDialog.findViewById(R.id.td);
        AppCompatButton tp =addClassDialog.findViewById(R.id.tp);
        EditText className = addClassDialog.findViewById(R.id.edit_text_class);
        CheckBox onlyOneGroup, tdCheck, tpCheck;
        LinearLayout tdTPCheck = addClassDialog.findViewById(R.id.nmbr_td_TP_pick);

        NumberPicker tdNmbr = addClassDialog.findViewById(R.id.nmbr_TD_pick);
        NumberPicker tpNmbr = addClassDialog.findViewById(R.id.nmbr_TP_pick);

        onlyOneGroup = addClassDialog.findViewById(R.id.onlyOneGroup_check);
        tdCheck = addClassDialog.findViewById(R.id.td_Check);
        tpCheck = addClassDialog.findViewById(R.id.tp_Check);

        tdNmbr.setVisibility(View.GONE);
        tpNmbr.setVisibility(View.GONE);
        td.setVisibility(View.GONE);
        tp.setVisibility(View.GONE);

        tdNmbr.setMaxValue(20);
        tdNmbr.setMinValue(1);
        tpNmbr.setMaxValue(20);
        tpNmbr.setMinValue(1);

        tdCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // to hide the nmbr picker
                tdNmbr.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });


        tpCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // to hide the nmbr picker

                tpNmbr.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });



        tdNmbr.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // to store the selected year
            }
        });

        tdNmbr.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // to store the selected year
            }
        });




        onlyOneGroup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // to hid tp and td

                tdTPCheck.setVisibility(!isChecked ? View.VISIBLE : View.GONE);
                td.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                tp.setVisibility(isChecked ? View.VISIBLE : View.GONE);

            }
        });

        td.setOnClickListener(new View.OnClickListener() {
            Boolean click =false;
            @Override
            public void onClick(View v) {
                if (click.equals(false)){
                    td.setBackgroundResource(R.drawable.background_btn1);
                    click=true;
                    td1="1";
                }
                else if (click.equals(true)) {
                    td.setBackgroundResource(R.drawable.background_btn);
                    click= false;
                    td1="";
                }
            }
        });
        tp.setOnClickListener(new View.OnClickListener() {
            Boolean click =false;
            @Override
            public void onClick(View v) {
                if (click.equals(false)){
                    tp.setBackgroundResource(R.drawable.background_btn1);
                    click=true;
                    tp1="1";
                }
                else if (click.equals(true)) {
                    tp.setBackgroundResource(R.drawable.background_btn);
                    click= false;
                    tp1="";
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidClass(className).equals(false)){ ValidClass(className);}
                else {
                    MuDataBase1 myDb2 = new MuDataBase1(activity);
                    String tdd = String.valueOf(tdNmbr.getValue());
                    String tpp = String.valueOf(tpNmbr.getValue());
                    // myDb2.addgr(idY,tdd);
                    myDb2.addClass(idY, className.getText().toString());
                    MuDataBase1 mdb = new MuDataBase1(activity);
                    Cursor cursor= mdb.readAllClasses();
                    String id = null;
                    if (cursor.moveToLast()){
                        id= cursor.getString(2);
                    }
                    if (onlyOneGroup.isChecked() && tp1.equals("1")&& td1.equals("1")){
                        addGrops(tp1 ,td1,id);
                        MuDataBase1 myDb3 = new MuDataBase1(activity);

                        myDb3.addGrptp(tpp, id);
                        myDb3.addGrp(tdd, id);
                    }
                    else if (onlyOneGroup.isChecked() &&tp1.equals("1") && td1.equals("")){
                        addGrops(tp1 ,td1,id);
                        MuDataBase1 myDb3 = new MuDataBase1(activity);

                        myDb3.addGrptp(tpp, id);

                    }
                    else if (onlyOneGroup.isChecked() &&td1.equals("1") && tp1.equals("")){
                        addGrops(tp1 ,td1,id);
                        MuDataBase1 myDb3 = new MuDataBase1(activity);

                        myDb3.addGrp(tdd, id);

                    }
                    else if (!onlyOneGroup.isChecked() &&tdCheck.isChecked() && tpCheck.isChecked()){
                        MuDataBase1 myDb3 = new MuDataBase1(activity);

                        myDb3.addGrptp(tpp, id);
                        myDb3.addGrp(tdd, id);
                    }
                    else if (!onlyOneGroup.isChecked() && !tdCheck.isChecked() && tpCheck.isChecked()){
                        MuDataBase1 myDb3 = new MuDataBase1(activity);

                        myDb3.addGrptp(tpp, id);
                    }
                    else if (!onlyOneGroup.isChecked() && tdCheck.isChecked() && !tpCheck.isChecked()){
                        MuDataBase1 myDb3 = new MuDataBase1(activity);

                        myDb3.addGrp(tdd, id);
                    }
                    else Toast.makeText(activity, "check td or tp", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(activity, loading.class);
                    activity.startActivity(intent);
                }

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClassDialog.dismiss();
            }
        });
        addClassDialog.show();



    }

    void addGrops(String tp , String td , String id){



    }

    private Boolean ValidClass(EditText pass){
        String passIN = pass.getText().toString();

        if(passIN.length()>2){

            return true;
        }
        else {
            pass.setError("Error in the name of class");

            return false;
        }
    }





    public class viewHolder extends RecyclerView.ViewHolder {
        TextView text;
        LinearLayout expandedLayout;
        RecyclerView classesRecycler;
        CardView addClass;
        ImageView arrow;
        View v;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.year_text);
            expandedLayout = itemView.findViewById(R.id.expnded_classes);
            classesRecycler =  itemView.findViewById(R.id.classesR);
            arrow=itemView.findViewById(R.id.arrow_years);
            addClass=itemView.findViewById(R.id.addClass);

            v=itemView;

        }
    }
}
