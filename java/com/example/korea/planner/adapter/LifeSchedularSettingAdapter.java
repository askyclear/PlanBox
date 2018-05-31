package com.example.korea.planner.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.korea.planner.R;
import com.example.korea.planner.data.LifeSchedularData;
import com.example.korea.planner.util.BusProvider;
import com.example.korea.planner.util.SettingChange;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by korea on 2017-04-02.
 */

public class LifeSchedularSettingAdapter extends RecyclerView.Adapter<LifeSchedularSettingAdapter.ViewHolder> {
    private String days[] = {"월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일"};
    private List<LifeSchedularData> lists;
    private List<String> item_names, item_list;
    private List<String> names;
    private Context context;

    public LifeSchedularSettingAdapter(List<String> names, List<LifeSchedularData> lists, Context contxt) {
        this.names = names;
        this.lists = lists;
        this.context = contxt;
        item_names = new ArrayList<>();
        item_list = new ArrayList<>();
        item_names.add("없음");
        item_list.add("없음");
        for (int i = 0; i < lists.size(); i++) {
            item_names.add(lists.get(i).getCategory_name());
            item_list.add(i + 1 + " : " + lists.get(i).getCategory_name());
        }

    }

    @Override
    public LifeSchedularSettingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.life_schedular_setting_recycler_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(LifeSchedularSettingAdapter.ViewHolder holder, int position) {
        holder.setting_days.setText(days[position]);
        holder.setting_name.setText(names.get(position));
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView setting_days, setting_name;
        private Button setting_choose;

        ViewHolder(View itemView) {
            super(itemView);
            setting_days = (TextView) itemView.findViewById(R.id.setting_days);
            setting_name = (TextView) itemView.findViewById(R.id.setting_name);
            setting_choose = (Button) itemView.findViewById(R.id.setting_choose);
            setting_choose.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.setting_choose) {
                int index = matching(setting_name.getText().toString());
                new MaterialDialog.Builder(context)
                        .title("알람 아이템 설정")
                        .items(item_list)
                        .itemsCallbackSingleChoice(index, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                names.set(getAdapterPosition(), item_list.get(dialog.getSelectedIndex()));
                                BusProvider.getInstance().post(new SettingChange(getAdapterPosition(), dialog.getSelectedIndex()));
                                //select_position = itemView.getId();
                                return true;
                            }
                        })
                        .positiveText("설정")
                        .negativeText("취소")
                        .build().show();
            }
        }

        int matching(String txt) {
            for (int i = 0; i < item_names.size(); i++) {
                if ((txt).equals(item_names.get(i))) {
                    return i;
                }
            }
            return -1;
        }

    }
}
