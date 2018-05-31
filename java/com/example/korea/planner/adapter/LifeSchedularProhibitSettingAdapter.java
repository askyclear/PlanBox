package com.example.korea.planner.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.korea.planner.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by korea on 2017-04-04.
 * jaj
 */

class LifeSchedularProhibitSettingAdapter extends RecyclerView.Adapter<LifeSchedularProhibitSettingAdapter.ViewHolder> {
    private List<String> prohibit_item;

    public LifeSchedularProhibitSettingAdapter(List<String> prohibit_item) {
        this.prohibit_item = new ArrayList<>();
        this.prohibit_item.addAll(prohibit_item);
    }

    @Override
    public LifeSchedularProhibitSettingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.life_schedular_setting_prohibit_recycler_item, parent, false);
        ViewHolder vh = new LifeSchedularProhibitSettingAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(LifeSchedularProhibitSettingAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.prohibit_before);
            itemView.findViewById(R.id.prohibit_after);
            itemView.findViewById(R.id.prohibit_delete);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
