package com.example.korea.planner.View.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.korea.planner.R;
import com.example.korea.planner.View.Calendar.presenter.CalendarContract;
import com.example.korea.planner.View.Calendar.presenter.CalendarPresenter;
import com.example.korea.planner.View.Today.TodayActivity;
import com.example.korea.planner.adapter.CalendarAnniverAdapter;
import com.example.korea.planner.data.AnniversaryData;
import com.example.korea.planner.util.BusProvider;
import com.example.korea.planner.util.CalendarDeleteEvent;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by korea on 2017-04-24.
 * 캘린더// 기념일 까지 남은 d-day를 보여줌;
 */

public class CalendarActivity extends AppCompatActivity implements CalendarContract.View, View.OnClickListener {
    @BindView(R.id.toolbar_calendar_back)
    ImageButton toolbarCalendarBack;
    @BindView(R.id.toolbar_calendar_title)
    TextView toolbarCalendarTitle;
    @BindView(R.id.calendar_toolbar)
    Toolbar calendarToolbar;
    @BindView(R.id.calendar_view)
    CalendarView calendarView;
    @BindView(R.id.calendar_recycler)
    RecyclerView calendarRecycler;
    @BindView(R.id.calendar_add_but)
    ImageButton calendarAddBut;

    private CalendarContract.Presenter presenter;
    private CalendarAnniverAdapter mAdapter;
    private Realm realm;
    private RealmResults<AnniversaryData> items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        BusProvider.getInstance().register(this);
        ButterKnife.bind(this);
        init();
    }

    public void init() {
        realm = Realm.getDefaultInstance();
        presenter = new CalendarPresenter();
        presenter.setView(this);
        setSupportActionBar(calendarToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        calendarRecycler.setLayoutManager(layoutManager);

        items = presenter.getDb();
        mAdapter = new CalendarAnniverAdapter(items);
        calendarRecycler.setAdapter(mAdapter);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Intent intent = new Intent(getApplicationContext(), TodayActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", dayOfMonth);
                startActivity(intent);
            }
        });

    }

    @OnClick({R.id.calendar_add_but, R.id.toolbar_calendar_back})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.calendar_add_but:
                showMaterial();
                break;
            case R.id.toolbar_calendar_back:
                finish();
                break;
        }
    }

    private EditText dialog_title, dialog_content, dialog_year, dialog_month, dialog_day;

    public void showMaterial() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("기념일 등록")
                .customView(R.layout.custom_annivious_dialog, true)
                .positiveText("등록")
                .negativeText("취소")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String title, content, year, month, day;
                        title = dialog_title.getText().toString();
                        content = dialog_content.getText().toString();
                        year = dialog_year.getText().toString();
                        month = dialog_month.getText().toString();
                        day = dialog_day.getText().toString();
                        if (title.length() > 0) {
                            if (year.length() == 0) {
                                year = "0000";
                            }
                            if (month.length() > 0 && day.length() > 0) {
                                items = presenter.setDb(Integer.parseInt(year),
                                        Integer.parseInt(month),
                                        Integer.parseInt(day), title, content);
                                mAdapter = new CalendarAnniverAdapter(items);
                                calendarRecycler.setAdapter(mAdapter);
                            } else {
                                Toast.makeText(getApplicationContext(), "월/일 을 입력하세요.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "제목을 입력하세요", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .build();
        View view = dialog.getCustomView();
        setViewId(view);
        dialog.show();
    }

    public void setViewId(View view) {
        dialog_title = (EditText) view.findViewById(R.id.annivious_title);
        dialog_content = (EditText) view.findViewById(R.id.annivious_content);
        dialog_year = (EditText) view.findViewById(R.id.annivious_year);
        dialog_month = (EditText) view.findViewById(R.id.annivious_month);
        dialog_day = (EditText) view.findViewById(R.id.annivious_day);
    }

    @Override
    protected void onDestroy() {
        BusProvider.getInstance().unregister(this);
        realm.close();
        super.onDestroy();
    }

    @Subscribe
    public void DeleteLoad(CalendarDeleteEvent event1) {
        items = presenter.delDb(event1.getId());
        mAdapter = new CalendarAnniverAdapter(items);
        calendarRecycler.setAdapter(mAdapter);
    }
}
