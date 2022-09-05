package com.example.spm.toucheHelpers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spm.R;
import com.example.spm.adapters.studentOfSeanceAdapter;

public class studentOfSeanceTouchHeleper extends ItemTouchHelper.SimpleCallback{

    studentOfSeanceAdapter adapter;


    public studentOfSeanceTouchHeleper(studentOfSeanceAdapter a) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter=a;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        final int position = viewHolder.getAdapterPosition();
        if (direction==ItemTouchHelper.LEFT){

            AlertDialog.Builder builder=new AlertDialog.Builder(adapter.getContext());
            builder.setMessage("do you want to delete this student")
                    .setTitle("delete Student")
                    .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.deleteStudent(position);
                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.notifyItemChanged(position);

                }
            });
            builder.show();
        } else  if (direction==ItemTouchHelper.RIGHT){

            adapter.updateStudentInfo(position);
        }

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull  RecyclerView recyclerView,
                            @NonNull  RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        Drawable icon;
        ColorDrawable background;

        View itemview = viewHolder.itemView;
        int backgroundCornerOffSet = 20;
        if(dX>0)
        {
            icon= ContextCompat.getDrawable(adapter.getContext(), R.drawable.edit);
            background=new ColorDrawable(ContextCompat.getColor(adapter.getContext(), R.color.grey));
        } else {
            icon=ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_delete_24);
            background=new ColorDrawable(ContextCompat.getColor(adapter.getContext(), R.color.black));
        }

        int iconMargin = (itemview.getHeight() - icon.getIntrinsicHeight())/2;
        int iconTop = itemview.getTop()+(itemview.getHeight() - icon.getIntrinsicHeight())/2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX>0){ // sipe to the right
            int iconLeft = itemview.getLeft() + iconMargin;
            int iconRight = itemview.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft,iconTop,iconRight,iconBottom);

            background.setBounds(itemview.getLeft(), itemview.getTop(),
                    itemview.getLeft() + ((int)dX) + backgroundCornerOffSet,itemview.getBottom()

            );

        } else if(dX<0){ // swipe t the left
            int iconLeft = itemview.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemview.getRight() - iconMargin ;
            icon.setBounds(iconLeft,iconTop,iconRight,iconBottom);

            background.setBounds(itemview.getRight() + ((int)dX) - backgroundCornerOffSet, itemview.getTop(),
                    itemview.getRight() ,itemview.getBottom());
        } else {
            background.setBounds(0,0,0,0);

        }
        background.draw(c);
        icon.draw(c);
    }
}
