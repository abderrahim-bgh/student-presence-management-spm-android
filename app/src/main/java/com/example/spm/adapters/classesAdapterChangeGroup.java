package com.example.spm.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spm.R;
import com.example.spm.classes.classe;
import com.example.spm.classes.group;
import com.example.spm.classes.year;

import java.util.List;

public class classesAdapterChangeGroup extends RecyclerView.Adapter<classesAdapterChangeGroup.viewHolder> {

    private Activity activity;
    List<classe> classeList ;

    private classRecyclerViewClickInterface listener  = null;


    public classesAdapterChangeGroup(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.changegroup_items_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        classe y = classeList.get(position);

        holder.text.setText(y.getName());

        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onClassItemClicked(y);
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return classeList.size();
    }


    public void setList (List<classe> a) {
        this.classeList=a;
        notifyDataSetChanged();

    }

    public void setOnItemClickListener(classRecyclerViewClickInterface listener){
        this.listener = listener;
    }



    public class viewHolder extends RecyclerView.ViewHolder {
        TextView text;
        View v;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            text =  itemView.findViewById(R.id.year_text1);
            v=itemView;


        }
    }

    public interface classRecyclerViewClickInterface {

        void onClassItemClicked(classe c);
    }


}
