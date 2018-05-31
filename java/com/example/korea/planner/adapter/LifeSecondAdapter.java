package com.example.korea.planner.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.korea.planner.R;
import com.example.korea.planner.data.DialogColor;
import com.example.korea.planner.data.LifeSchedularDataList;
import com.example.korea.planner.util.BusProvider;
import com.example.korea.planner.util.ItemClickEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by korea on 2017-03-22.
 *
 */

public class LifeSecondAdapter extends RecyclerView.Adapter<LifeSecondAdapter.ViewHolder> {
    private List<LifeSchedularDataList> lifeSchedularDataLists;
    private Context context;
    private boolean cheked23to1 = false;

    public LifeSecondAdapter(List<LifeSchedularDataList> items, Context context, boolean cheked23to1) {
        this.lifeSchedularDataLists = items;
        this.context = context;
        this.cheked23to1 = cheked23to1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.second_fragment_recycler_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LifeSchedularDataList item = lifeSchedularDataLists.get(position);
        holder.lifeColor.setBackgroundResource(item.getColor());
        holder.lifeTitle.setText(item.getTitle());
        holder.lifeTitle.setTextColor(item.getColor());
        holder.lifeContent.setText(item.getContent());
        holder.lifeContent.setTextColor(item.getColor());
        holder.lifeTime.setText(item.getBeforeHour() + " : " +
                item.getBeforeMin() + " ~ " +
                item.getAfterHour() + " : " +
                item.getAfterMin());
        holder.lifeTime.setTextColor(item.getColor());
    }

    @Override
    public int getItemCount() {
        return lifeSchedularDataLists.size();
    }

    //item 이름들
    private Button plus1hour, plus1hourA, plus30, plus30A, plus10, plus10A, plus5, plus5A;
    private EditText dialogTitle;
    private EditText dialogContent;
    private TimePicker timeBefore;
    private TimePicker timeAfter;
    private RecyclerView dialogRecycler;
    private List<DialogColor> colorlist;
    private DialogColorAdapter mDialogColorAdapter;
    private int[] color = {R.color.red, R.color.pink, R.color.purple,
            R.color.deep_purple, R.color.indigo, R.color.blue,
            R.color.light_blue, R.color.cyan, R.color.teal,
            R.color.green, R.color.light_green, R.color.lime, R.color.yellow,
            R.color.amber, R.color.orange, R.color.deep_orange, R.color.brown,
            R.color.grey, R.color.blue_grey};

    private int lastCheked;

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button lifeColor;
        private TextView lifeTitle;
        private TextView lifeContent;
        private TextView lifeTime;
        private ImageButton lifeDelete;
        private int position;

        void setFindViewId(View view) {
            dialogTitle = (EditText) view.findViewById(R.id.dialog_title);
            dialogContent = (EditText) view.findViewById(R.id.dialog_content);
            timeBefore = (TimePicker) view.findViewById(R.id.time_before);
            timeAfter = (TimePicker) view.findViewById(R.id.time_after);
            dialogRecycler = (RecyclerView) view.findViewById(R.id.dialog_recycler);

            //기존 정보 등록
            dialogTitle.setText(lifeSchedularDataLists.get(position).getTitle());
            dialogContent.setText(lifeSchedularDataLists.get(position).getContent());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                timeBefore.setHour(lifeSchedularDataLists.get(position).getBeforeHour());
                timeBefore.setMinute(lifeSchedularDataLists.get(position).getBeforeMin());
                timeAfter.setHour(lifeSchedularDataLists.get(position).getAfterHour());
                timeAfter.setMinute(lifeSchedularDataLists.get(position).getAfterMin());
            } else {
                timeBefore.setCurrentHour(lifeSchedularDataLists.get(position).getBeforeHour());
                timeBefore.setCurrentMinute(lifeSchedularDataLists.get(position).getBeforeMin());
                timeAfter.setCurrentHour(lifeSchedularDataLists.get(position).getAfterHour());
                timeAfter.setCurrentMinute(lifeSchedularDataLists.get(position).getAfterMin());
            }
            plus1hour = (Button) view.findViewById(R.id.plus1hour);
            plus1hourA = (Button) view.findViewById(R.id.plus1hourA);
            plus30 = (Button) view.findViewById(R.id.plus30);
            plus30A = (Button) view.findViewById(R.id.plus30A);
            plus10 = (Button) view.findViewById(R.id.plus10);
            plus10A = (Button) view.findViewById(R.id.plus10A);
            plus5 = (Button) view.findViewById(R.id.plus5);
            plus5A = (Button) view.findViewById(R.id.plus5A);
        }

        ViewHolder(View itemView) {
            super(itemView);
            lifeColor = (Button) itemView.findViewById(R.id.life_color);
            lifeTitle = (TextView) itemView.findViewById(R.id.life_title);
            lifeContent = (TextView) itemView.findViewById(R.id.life_content);
            lifeTime = (TextView) itemView.findViewById(R.id.life_time);
            lifeDelete = (ImageButton) itemView.findViewById(R.id.life_delete);
            lifeDelete.setOnClickListener(this);
            lifeColor.setOnClickListener(this);
            lifeTitle.setOnClickListener(this);
            lifeContent.setOnClickListener(this);
            lifeTime.setOnClickListener(this);
        }

        private void setColor(int position) {
            colorlist = new ArrayList<>();
            for (int i = 0; i < color.length; i++) {
                DialogColor dialogColor = new DialogColor();
                dialogColor.setColor(color[i]);
                if (color[i] == lifeSchedularDataLists.get(position).getColor()) {
                    dialogColor.setChecked(true);
                    lastCheked = i;
                } else {
                    dialogColor.setChecked(false);
                }
                colorlist.add(dialogColor);
            }
        }

        private boolean chekedTime(LifeSchedularDataList item, int position) {
            boolean timeCheker = true;
            //현재 수정하려는것이 23to1일 경우
            if (lifeSchedularDataLists.get(position).getBeforeHour() > lifeSchedularDataLists.get(position).getAfterHour()) {
                cheked23to1 = false;
            }
            //전시간이 24시 이전 후시간이 24시 이후 일때.
            if (item.getBeforeHour() > item.getAfterHour()) {
                //먼저 이러한것이 있는지 확인하고,
                if (cheked23to1) {
                    return false;
                } else {//없으면 아래것을 실행.
                    for (int i = 0; i < lifeSchedularDataLists.size(); i++) {
                        if (i != position) {
                            if (item.getBeforeHour() > lifeSchedularDataLists.get(i).getAfterHour()
                                    && item.getAfterHour() < lifeSchedularDataLists.get(i).getBeforeHour()) {
                                timeCheker = true;
                            } else if ((item.getBeforeHour() == lifeSchedularDataLists.get(i).getAfterHour() && item.getBeforeMin() >= lifeSchedularDataLists.get(i).getAfterMin() &&
                                    item.getAfterHour() < lifeSchedularDataLists.get(i).getBeforeHour()) ||
                                    item.getBeforeHour() > lifeSchedularDataLists.get(i).getAfterHour() && item.getAfterHour() == lifeSchedularDataLists.get(i).getBeforeHour() &&
                                            item.getAfterMin() <= lifeSchedularDataLists.get(i).getBeforeMin()) {
                                timeCheker = true;

                            } else if (item.getBeforeHour() == lifeSchedularDataLists.get(i).getAfterHour() && item.getAfterHour() == lifeSchedularDataLists.get(i).getBeforeHour()
                                    && item.getBeforeMin() >= lifeSchedularDataLists.get(i).getAfterMin() && item.getAfterMin() <= lifeSchedularDataLists.get(i).getBeforeMin()) {
                                timeCheker = true;
                            } else {
                                return false;
                            }
                        }
                    }
                    cheked23to1 = true;
                }
            } else {//전시간이 후 시간보다 작을때에는 아래것 실행.
                for (int i = 0; i < lifeSchedularDataLists.size(); i++) {
                    if (i != position) {
                        if (item.getAfterHour() < lifeSchedularDataLists.get(i).getBeforeHour() ||
                                item.getBeforeHour() > lifeSchedularDataLists.get(i).getAfterHour()) {
                            timeCheker = true;
                        } else if ((item.getAfterHour() == lifeSchedularDataLists.get(i).getBeforeHour()
                                && item.getAfterMin() <= lifeSchedularDataLists.get(i).getBeforeMin()) ||
                                (item.getBeforeHour() == lifeSchedularDataLists.get(i).getAfterHour() &&
                                        item.getBeforeMin() >= lifeSchedularDataLists.get(i).getAfterMin())) {

                        } else {
                            return false;
                        }
                    }
                }
            }

            return timeCheker;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.life_delete:
                    new MaterialDialog.Builder(context)
                            .title("삭제")
                            .content("정말 삭제하시겠습니까?")
                            .positiveText("삭제")
                            .negativeText("취소")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                //event 발생 알리기
                                                BusProvider.getInstance().post(new ItemClickEvent(lifeSchedularDataLists, getAdapterPosition(), 1));
                                            }
                                        }

                            )
                            .build().show();
                    break;
                default:
                    position = getAdapterPosition();
                    MaterialDialog dialog = new MaterialDialog.Builder(context)
                            .title("목록수정")
                            .customView(R.layout.dialog_custom_addlist, true)
                            .positiveText("수정")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    //taget 기기 정보에 따라
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                        //item 생성후
                                        LifeSchedularDataList item = new LifeSchedularDataList(dialogTitle.getText().toString(),
                                                dialogContent.getText().toString(), lifeSchedularDataLists.get(position).getId(),
                                                colorlist.get(mDialogColorAdapter.getLastCheked()).getColor(),
                                                timeBefore.getHour(), timeBefore.getMinute(),
                                                timeAfter.getHour(), timeAfter.getMinute());
                                        if (chekedTime(item, position)) {
                                            lifeSchedularDataLists.set(position, item);
                                            BusProvider.getInstance().post(new ItemClickEvent(lifeSchedularDataLists, position, 2));
                                        } else {
                                            Toast.makeText(context, "중복된 시간이 있습니다. 확인해주세요.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        LifeSchedularDataList item = new LifeSchedularDataList(dialogTitle.getText().toString(),
                                                dialogTitle.getText().toString(), lifeSchedularDataLists.get(position).getId(),
                                                colorlist.get(mDialogColorAdapter.getLastCheked()).getColor(),
                                                timeBefore.getCurrentHour(), timeBefore.getCurrentMinute(),
                                                timeAfter.getCurrentHour(), timeAfter.getCurrentMinute());
                                        if (chekedTime(item, position)) {
                                            lifeSchedularDataLists.set(position, item);
                                            BusProvider.getInstance().post(new ItemClickEvent(lifeSchedularDataLists, position, 2));
                                        } else {
                                            Toast.makeText(context, "중복된 시간이 있습니다. 확인해주세요.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            })
                            .negativeText("취소")
                            .build();
                    View view = dialog.getCustomView();
                    setFindViewId(view);
                    setColor(position);
                    setClickListener();
                    //match color 찾기
                    //Layoutmanger 설정 및 연결
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
                    mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    mLayoutManager.scrollToPosition(lastCheked);
                    dialogRecycler.setLayoutManager(mLayoutManager);
                    //color recycler 어댑터 연결
                    mDialogColorAdapter = new DialogColorAdapter(colorlist, lastCheked);
                    dialogRecycler.setAdapter(mDialogColorAdapter);

                    dialogRecycler.setItemAnimator(new DefaultItemAnimator());
                    dialog.show();

                    break;
            }
        }

        private void setClickListener() {
            plus1hour.setOnClickListener(new PlusButton());
            plus1hourA.setOnClickListener(new PlusButton());
            plus30.setOnClickListener(new PlusButton());
            plus30A.setOnClickListener(new PlusButton());
            plus10.setOnClickListener(new PlusButton());
            plus10A.setOnClickListener(new PlusButton());
            plus5.setOnClickListener(new PlusButton());
            plus5A.setOnClickListener(new PlusButton());
        }

        class PlusButton implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                int beforeMin;
                int afterMin;
                int beforeHour;
                int afterHour;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    beforeMin = timeBefore.getMinute();
                    beforeHour = timeBefore.getHour();
                    afterMin = timeAfter.getMinute();
                    afterHour = timeAfter.getHour();
                } else {
                    beforeMin = timeBefore.getCurrentMinute();
                    beforeHour = timeBefore.getCurrentHour();
                    afterHour = timeAfter.getCurrentHour();
                    afterMin = timeAfter.getCurrentMinute();
                }
                switch (v.getId()) {
                    case R.id.plus1hour:
                        beforeHour = beforeHour + 1;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            if (beforeHour == 24) {
                                timeBefore.setHour(beforeHour - 24);
                            } else {
                                timeBefore.setHour(beforeHour);
                            }
                        } else {
                            if (beforeHour >= 24) {
                                timeBefore.setCurrentHour(beforeHour - 24);
                            } else {
                                timeBefore.setCurrentHour(beforeHour);
                            }
                        }
                        break;
                    case R.id.plus1hourA:
                        afterHour = afterHour + 1;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            if (afterHour == 24) {
                                timeAfter.setHour(afterHour - 24);
                            } else {
                                timeAfter.setHour(afterHour);
                            }
                        } else {
                            if (afterHour == 24) {
                                timeAfter.setCurrentHour(afterHour - 24);
                            } else {
                                timeAfter.setCurrentHour(afterHour);
                            }
                        }
                        break;
                    case R.id.plus30:
                        beforeMin = beforeMin + 30;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            if (beforeMin >= 60) {
                                if ((beforeHour + 1) == 24)
                                    timeBefore.setHour(0);
                                else
                                    timeBefore.setHour(beforeHour + 1);
                                timeBefore.setMinute(beforeMin - 60);
                            } else {
                                timeBefore.setMinute(beforeMin);
                            }
                        } else {
                            if (beforeMin >= 60) {
                                if ((beforeHour + 1) == 24)
                                    timeBefore.setCurrentHour(0);
                                else
                                    timeBefore.setCurrentHour(beforeHour + 1);
                                timeBefore.setCurrentMinute(beforeMin - 60);
                            } else {
                                timeBefore.setCurrentMinute(beforeMin);
                            }
                        }
                        break;
                    case R.id.plus30A:
                        afterMin = afterMin + 30;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            if (afterMin >= 60) {
                                if ((afterHour + 1) == 24)
                                    timeAfter.setHour(0);
                                else
                                    timeAfter.setHour(afterHour + 1);
                                timeAfter.setMinute(afterMin - 60);
                            } else {
                                timeAfter.setMinute(afterMin);
                            }
                        } else {
                            if (afterMin >= 60) {
                                if ((afterHour + 1) == 24)
                                    timeAfter.setCurrentHour(0);
                                else
                                    timeAfter.setCurrentHour(afterHour + 1);
                                timeAfter.setCurrentMinute(afterMin - 60);
                            } else {
                                timeAfter.setCurrentMinute(afterMin);
                            }
                        }
                        break;
                    case R.id.plus10:
                        beforeMin = beforeMin + 10;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            if (beforeMin >= 60) {
                                if ((beforeHour + 1) == 24)
                                    timeBefore.setHour(0);
                                else
                                    timeBefore.setHour(beforeHour + 1);
                                timeBefore.setMinute(beforeMin - 60);
                            } else {
                                timeBefore.setMinute(beforeMin);
                            }
                        } else {
                            if (beforeMin >= 60) {
                                if ((beforeHour + 1) == 24)
                                    timeBefore.setCurrentHour(0);
                                else
                                    timeBefore.setCurrentHour(beforeHour + 1);
                                timeBefore.setCurrentMinute(beforeMin - 60);
                            } else {
                                timeBefore.setCurrentMinute(beforeMin);
                            }
                        }
                        break;
                    case R.id.plus10A:
                        afterMin = afterMin + 10;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            if (afterMin >= 60) {
                                if ((afterHour + 1) == 24)
                                    timeAfter.setHour(0);
                                else
                                    timeAfter.setHour(afterHour + 1);
                                timeAfter.setMinute(afterMin - 60);
                            } else {
                                timeAfter.setMinute(afterMin);
                            }
                        } else {
                            if (afterMin >= 60) {
                                if ((afterHour + 1) == 24)
                                    timeAfter.setCurrentHour(0);
                                else
                                    timeAfter.setCurrentHour(afterHour + 1);
                                timeAfter.setCurrentMinute(afterMin - 60);
                            } else {
                                timeAfter.setCurrentMinute(afterMin);
                            }
                        }
                        break;
                    case R.id.plus5:
                        beforeMin = beforeMin + 5;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            if (beforeMin >= 60) {
                                if ((beforeHour + 1) == 24)
                                    timeBefore.setHour(0);
                                else
                                    timeBefore.setHour(beforeHour + 1);
                                timeBefore.setMinute(beforeMin - 60);
                            } else {
                                timeBefore.setMinute(beforeMin);
                            }
                        } else {
                            if (beforeMin >= 60) {
                                if ((beforeHour + 1) == 24)
                                    timeBefore.setCurrentHour(0);
                                else
                                    timeBefore.setCurrentHour(beforeHour + 1);
                                timeBefore.setCurrentMinute(beforeMin - 60);
                            } else {
                                timeBefore.setCurrentMinute(beforeMin);
                            }
                        }
                        break;
                    case R.id.plus5A:
                        afterMin = afterMin + 5;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            if (afterMin >= 60) {
                                if ((afterHour + 1) == 24)
                                    timeAfter.setHour(0);
                                else
                                    timeAfter.setHour(afterHour + 1);
                                timeAfter.setMinute(afterMin - 60);
                            } else {
                                timeAfter.setMinute(afterMin);
                            }
                        } else {
                            if (afterMin >= 60) {
                                if ((afterHour + 1) == 24)
                                    timeAfter.setCurrentHour(0);
                                else
                                    timeAfter.setCurrentHour(afterHour + 1);
                                timeAfter.setCurrentMinute(afterMin - 60);
                            } else {
                                timeAfter.setCurrentMinute(afterMin);
                            }
                        }
                        break;
                }
            }
        }
    }
}
