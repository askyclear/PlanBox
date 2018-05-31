package com.example.korea.planner.adapter;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.korea.planner.R;
import com.example.korea.planner.View.widget.firstwidget4x2.PlanObjectWidget;
import com.example.korea.planner.data.GoalData;
import com.example.korea.planner.util.BusProvider;
import com.example.korea.planner.util.ProgressEvent;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by korea on 2017-04-26.
 */

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.ViewHolder> {
    private Realm realm;
    private RealmResults<GoalData> items;
    private Context context;
    private int where;
    static final String ACTION_UPDATE_CLICK = "UPDATE_CLICK";

    public GoalAdapter(RealmResults<GoalData> items, Context context, int where) {
        this.items = items;
        this.context = context;
        this.where = where;
    }

    @Override
    public GoalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh = new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goal_recycler_item, parent, false));
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.goalRecyclerContent.setText(items.get(position).getGoal());
        holder.goalRecyclerSuccessed.setChecked(items.get(position).isSuccesed());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView goalRecyclerContent;
        private CheckBox goalRecyclerSuccessed;
        private CardView goalRecyclerCard;

        public ViewHolder(View itemView) {
            super(itemView);
            goalRecyclerContent = (TextView) itemView.findViewById(R.id.goal_recycler_content);
            goalRecyclerSuccessed = (CheckBox) itemView.findViewById(R.id.goal_recycler_successed);
            if (where == 0) {
                goalRecyclerContent.setOnClickListener(this);
                goalRecyclerContent.setOnLongClickListener(this);

            } else {
                goalRecyclerSuccessed.setClickable(false);
            }
            goalRecyclerSuccessed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    realm = Realm.getDefaultInstance();
                    if (isChecked) {
                        realm.beginTransaction();
                        GoalData item = realm.where(GoalData.class).equalTo("id", items.get(getAdapterPosition()).getId()).findFirst();
                        item.setSuccesed(true);
                        realm.commitTransaction();
                    } else {
                        realm.beginTransaction();
                        GoalData item = realm.where(GoalData.class).equalTo("id", items.get(getAdapterPosition()).getId()).findFirst();
                        item.setSuccesed(false);
                        realm.commitTransaction();
                    }
                    realm.close();
                    BusProvider.getInstance().post(new ProgressEvent());
                }
            });

        }

        private EditText goalContent;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.goal_recycler_content:
                    MaterialDialog dialog = new MaterialDialog.Builder(context)
                            .title("목표 수정")
                            .customView(R.layout.goal_add_fragment, false)
                            .positiveText("수정")
                            .negativeText("취소")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    realm = Realm.getDefaultInstance();
                                    realm.beginTransaction();
                                    GoalData item = realm.where(GoalData.class).equalTo("id", items.get(getAdapterPosition()).getId()).findFirst();
                                    item.setGoal(goalContent.getText().toString());
                                    realm.commitTransaction();
                                    notifyDataSetChanged();
                                    AppWidgetManager appWidgetManger = AppWidgetManager.getInstance(context);
                                    int[] appwidgetIds = appWidgetManger.getAppWidgetIds(new ComponentName(context, PlanObjectWidget.class));
                                    for (int i = 0; i < appwidgetIds.length; i++) {
                                        Intent intent2 = new Intent(context, PlanObjectWidget.class);
                                        intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appwidgetIds[i]);
                                        intent2.setAction(ACTION_UPDATE_CLICK);
                                        context.sendBroadcast(intent2);
                                    }
                                    realm = Realm.getDefaultInstance();
                                }
                            }).build();
                    View view = dialog.getCustomView();
                    goalContent = (EditText) view.findViewById(R.id.goal_content);
                    goalContent.setText(items.get(getAdapterPosition()).getGoal());
                    dialog.show();
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.goal_recycler_content:
                    MaterialDialog dialog = new MaterialDialog.Builder(context)
                            .title("목표 삭제")
                            .content("정말 삭제하시겠습니까?")
                            .positiveText("삭제")
                            .negativeText("취소")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    realm = Realm.getDefaultInstance();
                                    realm.beginTransaction();
                                    GoalData item = realm.where(GoalData.class).equalTo("id", items.get(getAdapterPosition()).getId()).findFirst();
                                    item.deleteFromRealm();
                                    realm.commitTransaction();
                                    notifyDataSetChanged();
                                    AppWidgetManager appWidgetManger = AppWidgetManager.getInstance(context);
                                    int[] appwidgetIds = appWidgetManger.getAppWidgetIds(new ComponentName(context, PlanObjectWidget.class));
                                    for (int i = 0; i < appwidgetIds.length; i++) {
                                        Intent intent2 = new Intent(context, PlanObjectWidget.class);
                                        intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appwidgetIds[i]);
                                        intent2.setAction(ACTION_UPDATE_CLICK);
                                        context.sendBroadcast(intent2);
                                    }
                                    realm.close();
                                }
                            }).build();
                    dialog.show();
                    break;
            }
            return false;
        }
    }
}
