package com.example.korea.planner.View.Today.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.korea.planner.R;
import com.example.korea.planner.adapter.MemoAdapter;
import com.example.korea.planner.data.MemoData;
import com.example.korea.planner.util.BusProvider;
import com.example.korea.planner.util.MemoAddEvent;
import com.example.korea.planner.util.MemoUpdateEvent;
import com.squareup.otto.Subscribe;

import java.util.Calendar;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by korea on 2017-04-25.
 */

public class TodaySecondFragment extends Fragment {
    RecyclerView todaySecondRecycler;
    private int year, month, day;
    private Realm realm;
    private View view;
    private RealmResults<MemoData> items;
    private MemoAdapter mAdapter;
    private LinearLayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.today_second_fragment, container, false);
        init();
        return view;
    }

    public void init() {
        Realm.init(getContext());
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        todaySecondRecycler = (RecyclerView) view.findViewById(R.id.today_second_recycler);
        realm = Realm.getDefaultInstance();
        year = getArguments().getInt("year", gregorianCalendar.get(Calendar.YEAR));
        month = getArguments().getInt("month", gregorianCalendar.get(Calendar.MONTH)) + 1;
        day = getArguments().getInt("day", gregorianCalendar.get(Calendar.DAY_OF_MONTH));

        items = realm.where(MemoData.class).equalTo("year", year)
                .equalTo("month", month)
                .equalTo("day", day).findAll();

        layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter = new MemoAdapter(items, view.getContext());

        todaySecondRecycler.setLayoutManager(layoutManager);
        todaySecondRecycler.setAdapter(mAdapter);

    }

    @Override
    public void onDestroy() {
        BusProvider.getInstance().unregister(this);
        realm.close();
        super.onDestroy();
    }

    @Subscribe
    public void AddLoad(MemoAddEvent event) {
        realm.beginTransaction();
        MemoData item = realm.createObject(MemoData.class);
        item.setId(System.currentTimeMillis());
        item.setYear(year);
        item.setMonth(month);
        item.setDay(day);
        item.setTitle(event.getTitle());
        item.setContent(event.getContent());
        item.setColor(event.getColor());
        realm.commitTransaction();
        mAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void UpdateLoad(final MemoUpdateEvent event) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                MemoData item = realm.where(MemoData.class).equalTo("id", event.getId()).findFirst();
                item.setTitle(event.getTitle());
                item.setContent(event.getContent());
                item.setColor(event.getColor());
            }
        });
        mAdapter.notifyDataSetChanged();
    }
}
