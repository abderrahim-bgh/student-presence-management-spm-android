package com.example.spm.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spm.R;
import com.example.spm.classes.classe;
import com.example.spm.classes.recyclerViewClickInterface;
import com.example.spm.classes.year;

import java.util.Collections;
import java.util.List;

public class yearsAdapterChangeGroup extends RecyclerView.Adapter<yearsAdapterChangeGroup.viewHolder> {


    private Activity activity;
    private List<year> yearList;
    private recyclerViewClickInterface listener  = null;



    public yearsAdapterChangeGroup(Activity a) {
        this.activity = a;

    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.changegroup_items_layout,parent,false);
        return new yearsAdapterChangeGroup.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        year y = yearList.get(position);

        holder.text.setText(y.getYear());

        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onYearItemClicked(y);
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return yearList.size();
    }


    public void setList (List<year> a) {
        Collections.reverse(a);
        this.yearList=a;
        notifyDataSetChanged();

    }

   public void setOnItemClickListener(recyclerViewClickInterface listener){
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

    public interface recyclerViewClickInterface {

        void onYearItemClicked(year y);
    }


}
