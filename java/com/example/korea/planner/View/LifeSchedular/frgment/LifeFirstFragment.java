package com.example.korea.planner.View.LifeSchedular.frgment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.korea.planner.R;
import com.example.korea.planner.data.LifeSchedularData;
import com.example.korea.planner.data.LifeSchedularDataList;
import com.example.korea.planner.util.BusProvider;
import com.example.korea.planner.util.ChangeChekedEvent;
import com.example.korea.planner.util.CustomLifeSchedularView;
import com.example.korea.planner.util.ItemClickEvent;
import com.example.korea.planner.util.PushEvent;
import com.example.korea.planner.util.SaveEvent2;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by korea on 2017-03-21.
 *
 */

public class LifeFirstFragment extends Fragment {
    @BindView(R.id.category_title)
    EditText categoryTitle;
    @BindView(R.id.category_content)
    EditText categoryContent;
    Unbinder unbinder;
    Realm realm;
    RealmResults<LifeSchedularData> data;
    List<LifeSchedularDataList> lists;
    @BindView(R.id.custom_widget)
    CustomLifeSchedularView customWidget;
    private boolean eventCheked = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_lifefirstfragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        init();
        return view;
    }

    public void init() {
        Long id = getArguments().getLong("LifeId");
        lists = new ArrayList<>();
        realm = Realm.getDefaultInstance();
        if (id != 0) {

            data = realm.where(LifeSchedularData.class).equalTo("id", id).findAll();
            categoryTitle.setText(data.get(0).getCategory_name());
            categoryContent.setText(data.get(0).getCategory_content());
            lists.addAll(data.get(0).getList());
        }
        customWidget.setTing(lists, 1);
        customWidget.invalidate();
    }

    private CustomOnClickListener customListener;

    interface CustomOnClickListener {
        void onClicked(int id);
    }

    @Override
    public void onDestroyView() {
        BusProvider.getInstance().unregister(this);
        unbinder.unbind();
        realm.close();
        super.onDestroyView();
    }

    @Subscribe
    public void FinishLoad(ChangeChekedEvent changeChekedEvent) {
        if (!(changeChekedEvent.getTitle().equals(categoryTitle.getText().toString()))) {
            eventCheked = true;
        } else if (!(changeChekedEvent.getContent().equals(categoryContent.getText().toString()))) {
            eventCheked = true;
        }
        changeChekedEvent.setIsChangeCheked(eventCheked);
    }

    @Subscribe
    public void FinishLoad(SaveEvent2 saveEvent2) {
        eventCheked = true;
// 이벤트가 발생한뒤 수행할 작업
        String name, content;
        name = categoryTitle.getText().toString();
        content = categoryContent.getText().toString();
        saveEvent2.setCategory_name(name);
        saveEvent2.setCategory_content(content);
    }

    @Subscribe
    public void FinishLoad(ItemClickEvent itemClickEvent) {
        //item 삭제 이벤트 시
        eventCheked = true;
        if (itemClickEvent.getEvent() == 1) {
            lists.remove(itemClickEvent.getPosition());
            customWidget.invalidate();
        }
        //item 수정 이벤트 시
        else if (itemClickEvent.getEvent() == 2) {
            lists.set(itemClickEvent.getPosition(), itemClickEvent.getList().get(itemClickEvent.getPosition()));
            Collections.sort(lists, new timeBeforeCompare());
            customWidget.invalidate();
        }
    }

    @Subscribe
    public void FinishLoad(PushEvent pushEvent) {//item 추가시
        eventCheked = true;
        lists.add(pushEvent.getPushItem());
        Collections.sort(lists, new timeBeforeCompare());
        customWidget.invalidate();
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
}
