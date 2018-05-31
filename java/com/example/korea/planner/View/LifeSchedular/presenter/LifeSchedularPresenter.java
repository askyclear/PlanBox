package com.example.korea.planner.View.LifeSchedular.presenter;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.korea.planner.R;
import com.example.korea.planner.View.LifeSchedular.LifeSchedularActivity;
import com.example.korea.planner.adapter.DialogColorAdapter;
import com.example.korea.planner.data.DialogColor;
import com.example.korea.planner.data.LifeSchedularData;
import com.example.korea.planner.data.LifeSchedularDataList;
import com.example.korea.planner.util.AlarmUtil;
import com.example.korea.planner.util.BusProvider;
import com.example.korea.planner.util.ItemClickEvent;
import com.example.korea.planner.util.PushEvent;
import com.example.korea.planner.util.SaveEvent;
import com.example.korea.planner.util.SaveEvent2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by korea on 2017-03-15.
 *
 */

public class LifeSchedularPresenter implements LifeSchdularContract.Presenter {
    private Button plus1hour, plus1hourA, plus30, plus30A, plus10, plus10A, plus5, plus5A;
    private EditText dialogTitle;
    private EditText dialogContent;
    private TimePicker timeBefore;
    private TimePicker timeAfter;
    private int lastIndex = 0;
    private RecyclerView dialogRecycler;
    private LifeSchedularActivity activity;
    private MaterialDialog dialog;
    private LinearLayoutManager mLayoutManager;
    private List<DialogColor> list;
    private DialogColorAdapter mAdapter;
    private List<LifeSchedularDataList> addlist;
    private boolean cheked23to1;

    private SaveEvent event;
    private SaveEvent2 event2;
    private long id;
    private Realm realm;
    private RealmResults<LifeSchedularData> data;
    private RealmQuery<LifeSchedularData> query;
    private int[] color = {R.color.red, R.color.pink, R.color.purple,
            R.color.deep_purple, R.color.indigo, R.color.blue,
            R.color.light_blue, R.color.cyan, R.color.teal,
            R.color.green, R.color.light_green, R.color.lime, R.color.yellow,
            R.color.amber, R.color.orange, R.color.deep_orange, R.color.brown,
            R.color.grey, R.color.blue_grey};

    @Override
    public void setView(AppCompatActivity view, long id) {
        this.activity = (LifeSchedularActivity) view;
        this.id = id;
        realm = Realm.getDefaultInstance();

        if (id == 0) {
            addlist = new ArrayList<>();
        } else {
            query = realm.where(LifeSchedularData.class);
            query.equalTo("id", id);
            data = query.findAll();
            addlist = new ArrayList<>();
            addlist.addAll(data.get(0).getList());
            //Toast.makeText(activity,"data size : "+data.size()+data.get(0).getCategory_name(),Toast.LENGTH_SHORT).show();
        }
        //cheked23to1을 확인;
        for (int i = 0; i < addlist.size(); i++) {
            setListTime(addlist.get(i));
        }
        realm.close();
    }

    @Override
    public void addRealm() {
        event = new SaveEvent();
        event2 = new SaveEvent2();
        BusProvider.getInstance().post(event);
        BusProvider.getInstance().post(event2);
        if (event2.getCategory_name().length() == 0) {
            Toast.makeText(activity, "카테고리 제목을 입력하세요", Toast.LENGTH_SHORT).show();
        } else if (event.getList().size() == 0) {
            Toast.makeText(activity, "목록을 한개이상 추가해 주세요.", Toast.LENGTH_SHORT).show();
        } else {
            realm = Realm.getDefaultInstance();
            if (id == 0) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        LifeSchedularData item = realm.createObject(LifeSchedularData.class);
                        item.setId(System.currentTimeMillis());
                        item.setCategory_name(event2.getCategory_name());
                        item.setCategory_content(event2.getCategory_content());
                        //정렬된 list를 realm객체에 등록.
                        item.getList().addAll(event.getList());
                    }
                });
            } else {
                realm.beginTransaction();
                data.get(0).setCategory_name(event2.getCategory_name());
                data.get(0).setCategory_content(event2.getCategory_content());
                data.get(0).setList(null);
                data.get(0).getList().addAll(event.getList());
                realm.commitTransaction();
            }
            realm.close();
            AlarmUtil.getInstance().startAlram(activity);
            activity.finish();
        }

    }

    @Override
    public void matedrialDialog(int index) {
        lastIndex = index;
        dialog = new MaterialDialog.Builder(activity)
                .title("목록추가")
                .customView(R.layout.dialog_custom_addlist, true)
                .positiveText("추가")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    //target 별 timepicker 정보 얻기 다름.
                                    if (dialogTitle.getText().toString().length() != 0) {
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                            LifeSchedularDataList item = new LifeSchedularDataList(dialogTitle.getText().toString(),
                                                    dialogContent.getText().toString(), System.currentTimeMillis(),
                                                    list.get(mAdapter.getLastCheked()).getColor(), timeBefore.getHour(),
                                                    timeBefore.getMinute(), timeAfter.getHour(), timeAfter.getMinute());
                                            //if((item.getBeforeHour()<item.getAfterHour())//after time이 24보다 작을경우에만 chekedItem Time 적용가능.
                                            //       ||(item.getBeforeHour()==item.getAfterHour()&&item.getBeforeMin()< item.getAfterMin())) {
                                            if (item.getBeforeHour() == item.getAfterHour() && item.getBeforeMin() >= item.getAfterMin()) {
                                                Toast.makeText(activity, "전시간을 후시간보다 작게 만드세요.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                if (checkedItemTime(item)) {
                                                    addlist.add(item);
                                                    Collections.sort(addlist, new timeBeforeCompare());
                                                    BusProvider.getInstance().post(new PushEvent(item));
                                                } else {
                                                    Toast.makeText(activity, "포함되는 시간이 있습니다.", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                }
                                            }

                                        } else {
                                            LifeSchedularDataList item = new LifeSchedularDataList(dialogTitle.getText().toString(),
                                                    dialogContent.getText().toString(), System.currentTimeMillis(),
                                                    list.get(mAdapter.getLastCheked()).getColor(), timeBefore.getCurrentHour(),
                                                    timeBefore.getCurrentMinute(), timeAfter.getCurrentHour(), timeAfter.getCurrentMinute());
                                            if (item.getBeforeHour() == item.getAfterHour() && item.getBeforeMin() >= item.getAfterMin()) {
                                                Toast.makeText(activity, "전시간을 후시간보다 작게 만드세요.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                if (checkedItemTime(item)) {
                                                    addlist.add(item);
                                                    Collections.sort(addlist, new timeBeforeCompare());
                                                    BusProvider.getInstance().post(new PushEvent(item));
                                                } else {
                                                    Toast.makeText(activity, "포함되는 시간이 있습니다.", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                }
                                            }
                                        }
                                    } else {
                                        Toast.makeText(activity, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                )
                .negativeText("취소")
                .build();
        //dialog를 키보드 에 맞춰 리ㅏ
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View view = dialog.getCustomView();
        //butterknife로 되지않아서 각종 버튼 이벤트등을 등록
        setFindViewId(view);
        setClickListener();
        setColor();
        //시간을 00에 맞춰서 or 리스트 다음 시간에 맞춰서 바로 입력 가능하게.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (addlist.size() != 0) {
                timeBefore.setHour(addlist.get(addlist.size() - 1).getAfterHour());
                timeBefore.setMinute(addlist.get(addlist.size() - 1).getAfterMin());

            } else {
                timeBefore.setHour(0);
                timeBefore.setMinute(0);
            }
            timeAfter.setHour(timeBefore.getHour() + 1);
            timeAfter.setMinute(timeBefore.getMinute());
        } else {
            if (addlist.size() != 0) {
                timeBefore.setCurrentHour(addlist.get(addlist.size() - 1).getAfterHour());
                timeBefore.setCurrentMinute(addlist.get(addlist.size() - 1).getAfterMin());
            } else {
                timeBefore.setCurrentHour(0);
                timeBefore.setCurrentMinute(0);
            }
            timeAfter.setCurrentHour(timeBefore.getCurrentHour() + 1);
            timeAfter.setCurrentMinute(0);
        }
        //Layoutmanger 설정 및 연결
        mLayoutManager = new LinearLayoutManager(activity);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mLayoutManager.scrollToPosition(lastIndex);
        dialogRecycler.setLayoutManager(mLayoutManager);
        //color recycler 어댑터 연결
        mAdapter = new DialogColorAdapter(list, lastIndex);
        dialogRecycler.setAdapter(mAdapter);

        dialogRecycler.setItemAnimator(new DefaultItemAnimator());
        dialog.show();
    }

    //Dialog 관련 함수
    private void setFindViewId(View view) {
        dialogTitle = (EditText) view.findViewById(R.id.dialog_title);
        dialogContent = (EditText) view.findViewById(R.id.dialog_content);
        timeBefore = (TimePicker) view.findViewById(R.id.time_before);
        timeAfter = (TimePicker) view.findViewById(R.id.time_after);
        dialogRecycler = (RecyclerView) view.findViewById(R.id.dialog_recycler);
        plus1hour = (Button) view.findViewById(R.id.plus1hour);
        plus1hourA = (Button) view.findViewById(R.id.plus1hourA);
        plus30 = (Button) view.findViewById(R.id.plus30);
        plus30A = (Button) view.findViewById(R.id.plus30A);
        plus10 = (Button) view.findViewById(R.id.plus10);
        plus10A = (Button) view.findViewById(R.id.plus10A);
        plus5 = (Button) view.findViewById(R.id.plus5);
        plus5A = (Button) view.findViewById(R.id.plus5A);
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

    private class PlusButton implements View.OnClickListener {

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


    @Override
    public void setItemClickEvent(ItemClickEvent itemClickEvent) {
        if (itemClickEvent.getEvent() == 1) {
            addlist.remove(itemClickEvent.getPosition());
        } else if (itemClickEvent.getEvent() == 2) {
            addlist.set(itemClickEvent.getPosition(), itemClickEvent.getList().get(itemClickEvent.getPosition()));
            Collections.sort(addlist, new timeBeforeCompare());
        }
        for (int i = 0; i < addlist.size(); i++) {
            setListTime(addlist.get(i));
        }
    }

    private boolean checkedItemTime(LifeSchedularDataList item) {
        boolean chekedtime = true;
        if (item.getBeforeHour() > item.getAfterHour()) {
            if (cheked23to1) {
                return false;
            } else {
                for (int i = 0; i < addlist.size(); i++) {
                    if (item.getBeforeHour() > addlist.get(i).getAfterHour()
                            && item.getAfterHour() < addlist.get(i).getBeforeHour()) {
                        chekedtime = true;
                    } else if ((item.getBeforeHour() == addlist.get(i).getAfterHour() && item.getBeforeMin() >= addlist.get(i).getAfterMin() &&
                            item.getAfterHour() < addlist.get(i).getBeforeHour()) ||
                            item.getBeforeHour() > addlist.get(i).getAfterHour() && item.getAfterHour() == addlist.get(i).getBeforeHour() &&
                                    item.getAfterMin() <= addlist.get(i).getBeforeMin()) {
                        chekedtime = true;

                    } else if (item.getBeforeHour() == addlist.get(i).getAfterHour() && item.getAfterHour() == addlist.get(i).getBeforeHour()
                            && item.getBeforeMin() >= addlist.get(i).getAfterMin() && item.getAfterMin() <= addlist.get(i).getBeforeMin()) {
                        chekedtime = true;
                    } else {
                        return false;
                    }
                }
                cheked23to1 = true;
            }
        } else {
            for (int i = 0; i < addlist.size(); i++) {
                if (item.getAfterHour() < addlist.get(i).getBeforeHour() ||
                        item.getBeforeHour() > addlist.get(i).getAfterHour()) {
                    chekedtime = true;
                } else if ((item.getAfterHour() == addlist.get(i).getBeforeHour() && item.getAfterMin() <= addlist.get(i).getBeforeMin()) ||
                        (item.getBeforeHour() == addlist.get(i).getAfterHour() && item.getBeforeMin() >= addlist.get(i).getAfterMin())) {

                } else {
                    return false;
                }
            }
        }

        return chekedtime;
    }

    private void setColor() {
        list = new ArrayList<DialogColor>();
        for (int i = 0; i < color.length; i++) {
            DialogColor dialogColor = new DialogColor();
            dialogColor.setColor(color[i]);
            if (i == lastIndex) {
                dialogColor.setChecked(true);
            } else {
                dialogColor.setChecked(false);
            }
            list.add(dialogColor);
        }
    }

    private void setListTime(LifeSchedularDataList item) {
        if (item.getBeforeHour() > item.getAfterHour()) {
            cheked23to1 = true;
        }
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
