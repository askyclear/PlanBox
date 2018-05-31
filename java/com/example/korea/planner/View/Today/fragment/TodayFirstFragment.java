package com.example.korea.planner.View.Today.fragment;


import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.korea.planner.R;
import com.example.korea.planner.View.widget.firstwidget4x2.PlanObjectWidget;
import com.example.korea.planner.adapter.GoalAdapter;
import com.example.korea.planner.data.GoalData;
import com.example.korea.planner.util.BusProvider;
import com.example.korea.planner.util.GoalAddEvent;
import com.example.korea.planner.util.ProgressEvent;
import com.squareup.otto.Subscribe;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by korea on 2017-04-25.
 */

public class TodayFirstFragment extends Fragment {
    RecyclerView todayFirstRecyclerView;
    private int year, month, day;
    static final String ACTION_UPDATE_CLICK = "UPDATE_CLICK";
    private Realm realm;
    private View view;
    private RealmResults<GoalData> items;
    private GoalAdapter mAdapter;
    private LinearLayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.today_first_fragment, container, false);
        init();
        return view;
    }

    public void init() {
        todayFirstRecyclerView = (RecyclerView) view.findViewById(R.id.today_first_recyclerView);
        year = getArguments().getInt("year");
        month = getArguments().getInt("month") + 1;
        day = getArguments().getInt("day");
        Realm.init(getContext());
        realm = Realm.getDefaultInstance();
        items = realm.where(GoalData.class).
                equalTo("year", year).
                equalTo("month", month).
                equalTo("day", day).findAll();
        layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        todayFirstRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new GoalAdapter(items, view.getContext(), 0);
        todayFirstRecyclerView.setAdapter(mAdapter);
    }

    @Subscribe
    public void AddLoad(GoalAddEvent event) {
        realm.beginTransaction();
        GoalData item = realm.createObject(GoalData.class);
        item.setId(System.currentTimeMillis());
        item.setYear(year);
        item.setMonth(month);
        item.setDay(day);
        item.setSuccesed(false);
        item.setGoal(event.getContent());
        realm.commitTransaction();
        Log.d("WIDGETLIST", "LIST SIZE" + realm.where(GoalData.class).equalTo("year", year)
                .equalTo("month", month).equalTo("day", day).findAll().size());
        mAdapter.notifyDataSetChanged();
        sendAppWidget();
    }

    @Subscribe
    public void ProgressEvent(ProgressEvent event) {
        sendAppWidget();
    }

    private void sendAppWidget() {
        AppWidgetManager appWidgetManger = AppWidgetManager.getInstance(view.getContext());
        int[] appwidgetIds = appWidgetManger.getAppWidgetIds(new ComponentName(view.getContext(), PlanObjectWidget.class));
        for (int i = 0; i < appwidgetIds.length; i++) {
            Intent intent2 = new Intent(view.getContext(), PlanObjectWidget.class);
            intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appwidgetIds[i]);
            intent2.setAction(ACTION_UPDATE_CLICK);
            view.getContext().sendBroadcast(intent2);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        BusProvider.getInstance().unregister(this);
        realm.close();
        super.onDestroy();
    }
}
