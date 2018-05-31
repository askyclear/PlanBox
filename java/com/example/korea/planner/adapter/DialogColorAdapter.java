package com.example.korea.planner.adapter;

import android.graphics.Color;
import android.support.design.widget.CheckableImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.korea.planner.R;
import com.example.korea.planner.data.DialogColor;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by korea on 2017-03-22.
 */

public class DialogColorAdapter extends RecyclerView.Adapter<DialogColorAdapter.ViewHolder> {
    private List<DialogColor> dialogColorList;
    private static int lastCheckedPosition = -1;

    //생성자
    public DialogColorAdapter(List<DialogColor> items, int colorPosition) {
        this.dialogColorList = items;
        lastCheckedPosition = colorPosition;
    }

    @Override
    public DialogColorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_color_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DialogColor item = dialogColorList.get(position);
        holder.colorBackground.setBackgroundResource(item.getColor());
        if (item.getChecked()) {
            item.setChecked(false);
            lastCheckedPosition = position;
        }
        if (lastCheckedPosition == position) {
            holder.colorCheked.setChecked(true);
            holder.colorCheked.setAlpha(0.99f);
        } else {
            holder.colorCheked.setChecked(false);
            holder.colorCheked.setAlpha(0.00f);
        }
    }

    @Override
    public int getItemCount() {
        return dialogColorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView colorBackground;
        public CheckableImageButton colorCheked;

        public ViewHolder(View view) {
            super(view);
            colorBackground = (ImageView) view.findViewById(R.id.color_background);
            colorCheked = (CheckableImageButton) view.findViewById(R.id.color_cheked);
            colorCheked.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            DialogColor item = dialogColorList.get(getAdapterPosition());
            item.setChecked(true);
            lastCheckedPosition = getAdapterPosition();
            dialogColorList.set(getAdapterPosition(), item);
            notifyDataSetChanged();
        }
    }

    public int getLastCheked() {
        return lastCheckedPosition;
    }

}
