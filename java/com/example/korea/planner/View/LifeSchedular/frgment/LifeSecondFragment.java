package com.example.korea.planner.View.LifeSchedular.frgment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.korea.planner.R;
import com.example.korea.planner.adapter.LifeSecondAdapter;
import com.example.korea.planner.data.LifeSchedularData;
import com.example.korea.planner.data.LifeSchedularDataList;
import com.example.korea.planner.util.BusProvider;
import com.example.korea.planner.util.FabShowing;
import com.example.korea.planner.util.ItemClickEvent;
import com.example.korea.planner.util.PushEvent;
import com.example.korea.planner.util.SaveEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * Created by korea on 2017-03-21.
 * 따로 프레젠터는 만들지 않았음.
 * 계획 목록을 보여주는 프레그먼트
 */

public class LifeSecondFragment extends Fragment {
    private final static String TAG = "CHEKED";
    RecyclerView lifeRecycle;
    private LifeSecondAdapter mAdapter;
    private List<LifeSchedularDataList> list;
    private LinearLayoutManager linearLayoutManager;

    private View view;
    private Realm realm;
    private RealmResults<LifeSchedularData> data;
    private boolean cheked23to1 = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_lifesecondfragment, container, false);
        initView();
        return view;
    }

    public void initView() {
        lifeRecycle = (RecyclerView) view.findViewById(R.id.lifeRecycle);
        list = new ArrayList<>();
        Long id = getArguments().getLong("LifeId");
        realm = Realm.getDefaultInstance();
        if (id != 0) {
            data = realm.where(LifeSchedularData.class).equalTo("id", id).findAll();
            list.addAll(data.get(0).getList());
            for (int i = 0; i < list.size(); i++) {
                setListTime(list.get(i));
            }
        }
        //Toast.makeText(this.getContext(), "" + list.size(), Toast.LENGTH_LONG).show();
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lifeRecycle.setLayoutManager(linearLayoutManager);
        mAdapter = new LifeSecondAdapter(list, this.getContext(), cheked23to1);
        lifeRecycle.setAdapter(mAdapter);
        lifeRecycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    BusProvider.getInstance().post(new FabShowing(1));
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0) {
                    BusProvider.getInstance().post(new FabShowing(0));
                }
            }
        });
    }

    @Subscribe
    public void FinishLoad(PushEvent mPushEvent) {
// 이벤트가 발생한뒤 수행할 작업
        list.add(mPushEvent.getPushItem());
        Collections.sort(list, new timeBeforeCompare());
        mAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void FinishLoad(ItemClickEvent itemClickEvent) {
        //item 삭제 이벤트 시
        if (itemClickEvent.getEvent() == 1) {
            list.remove(itemClickEvent.getPosition());
            mAdapter.notifyItemRemoved(itemClickEvent.getPosition());
        }
        //item 수정 이벤트 시
        else if (itemClickEvent.getEvent() == 2) {
            list.set(itemClickEvent.getPosition(), itemClickEvent.getList().get(itemClickEvent.getPosition()));
            Collections.sort(list, new timeBeforeCompare());
            mAdapter.notifyDataSetChanged();
        }
        //23to1 체크
        for (int i = 0; i < list.size(); i++) {
            setListTime(list.get(i));
        }
    }

    @Subscribe
    public void FinishLoad(SaveEvent saveEvent) {
// 이벤트가 발생한뒤 수행할 작업
        saveEvent.setmLifeSchedularDataLists(list);
    }

    @Override
    public void onDestroyView() {
        BusProvider.getInstance().unregister(this);
        realm.close();
        super.onDestroyView();
    }

    private class timeBeforeCompare implements Comparator<LifeSchedularDataList> {

        @Override
        public int compare(LifeSchedularDataList o1, LifeSchedularDataList o2) {
            if (o1.getBeforeHour() * 60 + o1.getBeforeMin() > o2.getBeforeHour() * 60 + o2.getBeforeMin())
                return 1;
            else if (o1.getBeforeHour() * 60 + o1.getBeforeMin() < o2.getBeforeHour() * 60 + o2.getBeforeMin())
                return -1;
            else
                return 0;
        }
    }

    private void setListTime(LifeSchedularDataList item) {
        if (item.getBeforeHour() > item.getAfterHour()) {
            cheked23to1 = true;
        }
    }
}
