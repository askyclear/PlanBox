package com.example.korea.planner.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.korea.planner.R;
import com.example.korea.planner.data.AnniversaryData;
import com.example.korea.planner.util.BusProvider;
import com.example.korea.planner.util.CalendarDeleteEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by korea on 2017-04-24.
 */

public class CalendarAnniverAdapter extends RecyclerView.Adapter<CalendarAnniverAdapter.ViewHolder> {
    private List<AnniversaryData> items = new ArrayList<>();
    private List<AnniversaryData> sub1 = new ArrayList<>();
    private List<AnniversaryData> sub2 = new ArrayList<>();
    private GregorianCalendar calendar;

    public CalendarAnniverAdapter(RealmResults<AnniversaryData> items) {
        this.items.addAll(items);
        subSetList();

    }

    private void subSetList() {
        GregorianCalendar mCalendar = new GregorianCalendar();
        int current = mCalendar.get(Calendar.DAY_OF_YEAR);
        for (int i = 0; i < items.size(); i++) {
            mCalendar.set(mCalendar.get(Calendar.YEAR), items.get(i).getMonth() - 1, items.get(i).getDay());
            int val1 = mCalendar.get(Calendar.DAY_OF_YEAR) - current;
            if (val1 >= 0) {
                sub1.add(items.get(i));
            } else {
                sub2.add(items.get(i));
            }
        }
        Collections.sort(sub1, new MonthCompare());
        Collections.sort(sub2, new MonthCompare());
        items.clear();
        items.addAll(sub1);
        items.addAll(sub2);
    }

    private class MonthCompare implements Comparator<AnniversaryData> {

        @Override
        public int compare(AnniversaryData o1, AnniversaryData o2) {
            GregorianCalendar mCalendar = new GregorianCalendar();
            int current = mCalendar.get(Calendar.DAY_OF_YEAR);
            mCalendar.set(mCalendar.get(Calendar.YEAR), o1.getMonth() - 1, o1.getDay());
            int val1 = mCalendar.get(Calendar.DAY_OF_YEAR) - current;
            mCalendar.set(mCalendar.get(Calendar.YEAR), o2.getMonth() - 1, o2.getDay());
            int val2 = mCalendar.get(Calendar.DAY_OF_YEAR) - current;
            if (val1 > val2) {
                return 1;
            } else if (val1 < val2) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh = new ViewHolder(LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.calendar_anniver_item, parent, false));
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        calendar = new GregorianCalendar();
        holder.title.setText(items.get(position).getTitle());
        holder.content.setText(items.get(position).getContent());
        holder.date.setText(items.get(position).getYear() + "년 " + "\n" + items.get(position).getMonth() + "월 " +
                items.get(position).getDay() + "일");
        int first = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.set(calendar.get(Calendar.YEAR), items.get(position).getMonth() - 1, items.get(position).getDay());
        int last = calendar.get(Calendar.DAY_OF_YEAR);
        if (last - first < 0) {
            holder.remainingday.setText((-1) * (last - first) + "일 지남");
        } else {
            holder.remainingday.setText((last - first) + "일 남음");
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title, content, date, remainingday;
        private ImageButton delete;

        ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_anniver_item);
            content = (TextView) itemView.findViewById(R.id.content_anniver_item);
            date = (TextView) itemView.findViewById(R.id.date_anniver_item);
            remainingday = (TextView) itemView.findViewById(R.id.remaining_anniver_item);
            delete = (ImageButton) itemView.findViewById(R.id.delete_anniver_item);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.delete_anniver_item) {
                Log.d("ADAPTER", "" + items.get(getAdapterPosition()).getId());
                new MaterialDialog.Builder(v.getContext())
                        .title("삭제")
                        .content("정말삭제하시겠습니까?")
                        .positiveText("삭제")
                        .negativeText("취소")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                BusProvider.getInstance().post(new CalendarDeleteEvent(items.get(getAdapterPosition()).getId()));
                            }
                        }).build().show();

            }
        }
    }
}
