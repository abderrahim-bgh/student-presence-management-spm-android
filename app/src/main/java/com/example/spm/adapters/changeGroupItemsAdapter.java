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
import java.util.zip.Inflater;

public class changeGroupItemsAdapter extends RecyclerView.Adapter<changeGroupItemsAdapter.viewHolder> {
    private Activity activity;
    List<group> groupList  ;
    private groupRecyclerViewClickInterface listener  = null;


    public changeGroupItemsAdapter(Activity activity) {
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

        group y = groupList.get(position);

        holder.text.setText(y.getName());

        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onGroupItemClicked(y);
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return groupList.size() ;
    }


    public void setList (List<group> a) {
        this.groupList=a;
        notifyDataSetChanged();

    }

    public void setOnItemClickListener(groupRecyclerViewClickInterface listener){
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

    public interface groupRecyclerViewClickInterface {

        void onGroupItemClicked(group y);
    }
}
