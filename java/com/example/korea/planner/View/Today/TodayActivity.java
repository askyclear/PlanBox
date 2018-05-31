package com.example.korea.planner.View.Today;

import android.annotation.TargetApi;
import android.content.Intent;
import android.icu.util.ChineseCalendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.korea.planner.R;
import com.example.korea.planner.View.MemoAdd.MemoAddActivity;
import com.example.korea.planner.View.Today.Presenter.TodayContract;
import com.example.korea.planner.View.Today.Presenter.TodayPresenter;
import com.example.korea.planner.adapter.TodayTabPagerAdapter;
import com.example.korea.planner.util.BusProvider;
import com.example.korea.planner.util.GoalAddEvent;
import com.example.korea.planner.util.SolarLunarUtil;
import com.github.clans.fab.FloatingActionButton;

import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by korea on 2017-04-25.
 */

public class TodayActivity extends AppCompatActivity implements TodayContract.View, TabLayout.OnTabSelectedListener, View.OnClickListener {
    @BindView(R.id.today_korea)
    TextView todayKorea;
    @BindView(R.id.today_annivious)
    TextView todayAnnivious;
    @BindView(R.id.today_day_of_year)
    TextView todayDayOfYear;
    @BindView(R.id.today_chinese_day)
    TextView todayChineseDay;
    @BindView(R.id.today_toolbar)
    Toolbar todayToolbar;
    @BindView(R.id.today_toolbar_back)
    ImageButton todayToolbarBack;
    @BindView(R.id.today_toolbar_title)
    TextView todayToolbarTitle;
    @BindView(R.id.today_viewpager)
    ViewPager todayViewpager;
    @BindView(R.id.today_fab_add)
    FloatingActionButton todayFabAdd;
    @BindView(R.id.today_tablayout)
    TabLayout todayTablayout;
    private int year, month, day;
    private TodayContract.Presenter presenter;
    private GregorianCalendar kCalendar;
    private ChineseCalendar cCalendar;
    private String[] day_of_week_text = {"일", "월", "화", "수", "목", "금", "토"};
    private TodayTabPagerAdapter todayTabPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);
        ButterKnife.bind(this);
        Realm.init(this);
        init();
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void init() {
        Intent intent = getIntent();
        presenter = new TodayPresenter();

        presenter.setView(this);
        setSupportActionBar(todayToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        kCalendar = new GregorianCalendar();

        year = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 0);
        day = intent.getIntExtra("day", 0);

        String key;
        String text = "";
        key = SolarLunarUtil.getInstance().changeKey(month, day);
        if (SolarLunarUtil.getInstance().getSolar(key) != null) {
            text = SolarLunarUtil.getInstance().getSolar(key);
        }
        //양력 음력 입력
        kCalendar.set(year, month, day);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            cCalendar = new ChineseCalendar();
            cCalendar.setTimeInMillis(kCalendar.getTimeInMillis());
            todayChineseDay.setText("음력 " + (cCalendar.get(ChineseCalendar.EXTENDED_YEAR) - 2637) + "년 "
                    + (cCalendar.get(ChineseCalendar.MONTH) + 1) + "월 "
                    + cCalendar.get(ChineseCalendar.DAY_OF_MONTH) + "일");
            key = SolarLunarUtil.getInstance().changeKey(cCalendar.get(ChineseCalendar.MONTH)
                    , cCalendar.get(ChineseCalendar.DAY_OF_MONTH));
        }
        if (SolarLunarUtil.getInstance().getLundar(key) != null) {
            text = text + SolarLunarUtil.getInstance().getLundar(key);
        }
        //기념일 set
        todayAnnivious.setText(text);
        //선택한 날짜 set Text
        todayKorea.setText(year + "." + (month + 1) + "." + day + "(" +
                day_of_week_text[kCalendar.get(Calendar.DAY_OF_WEEK) - 1] + ")");
        if (year % 4 == 0 && year % 100 > 0 || year % 400 == 0) {
            todayDayOfYear.setText("윤년 366일 중 " + kCalendar.get(Calendar.DAY_OF_YEAR) + "일");
        } else {
            todayDayOfYear.setText("365일 중 " + kCalendar.get(Calendar.DAY_OF_YEAR) + "일");
        }
        todayTablayout.addTab(todayTablayout.newTab().setText("오늘목표"));
        todayTablayout.addTab(todayTablayout.newTab().setText("메모"));
        todayTablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        todayTablayout.setSelectedTabIndicatorHeight(0);
        todayTabPagerAdapter = new TodayTabPagerAdapter(getSupportFragmentManager(), todayTablayout.getTabCount(), year, month, day);
        todayViewpager.setAdapter(todayTabPagerAdapter);

        todayViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(todayTablayout));
        todayTablayout.addOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                todayToolbar.setBackgroundResource(R.color.red);
                todayToolbarTitle.setText("오늘목표");
                break;
            case 1:
                todayToolbar.setBackgroundResource(R.color.light_blue);
                todayToolbarTitle.setText("메모");
                break;
            default:
                break;
        }
        todayViewpager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private EditText goalContent;

    @OnClick({R.id.today_fab_add, R.id.today_toolbar_back})
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.today_fab_add) {
            if (todayTablayout.getSelectedTabPosition() == 0) {
                //goal 일때

                MaterialDialog dialog = new MaterialDialog.Builder(this)
                        .title("목표 추가")
                        .positiveText("추가")
                        .negativeText("취소")
                        .customView(R.layout.goal_add_fragment, true)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                BusProvider.getInstance().post(new GoalAddEvent(goalContent.getText().toString()));
                            }
                        })
                        .build();
                View view = dialog.getCustomView();
                goalContent = (EditText) view.findViewById(R.id.goal_content);
                goalContent.setHint(year + "." + (month + 1) + "." + day + " 목표를 입력하세요");
                dialog.show();
            } else if (todayTablayout.getSelectedTabPosition() == 1) {
                //memo 일때
                Intent intent = new Intent(this, MemoAddActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }

        } else if (v.getId() == R.id.today_toolbar_back) {
            finish();
        }
    }
}
