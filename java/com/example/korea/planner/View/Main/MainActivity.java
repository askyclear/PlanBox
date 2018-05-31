package com.example.korea.planner.View.Main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.korea.planner.R;
import com.example.korea.planner.View.Calendar.CalendarActivity;
import com.example.korea.planner.View.Goal.GoalActivity;
import com.example.korea.planner.View.Info.InfoActivity;
import com.example.korea.planner.View.LifeSchedular.LifeSchedularMainActivity;
import com.example.korea.planner.View.Main.presenter.MainContract;
import com.example.korea.planner.View.Main.presenter.MainPresenter;
import com.example.korea.planner.View.Memo.MemoActivity;
import com.example.korea.planner.View.Today.TodayActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements MainContract.View, View.OnClickListener {
    @BindView(R.id.main_year)
    TextView mainYear;
    @BindView(R.id.main_title)
    TextView mainTitle;
    @BindView(R.id.main_but1)
    Button mainBut1;
    @BindView(R.id.main_but2)
    Button mainBut2;
    @BindView(R.id.main_but3)
    Button mainBut3;
    @BindView(R.id.main_but4)
    Button mainBut4;
    @BindView(R.id.main_but5)
    Button mainBut5;
    @BindView(R.id.main_but6)
    Button mainBut6;
    @BindView(R.id.main_adview)
    AdView mainAdview;

    private MainContract.Presenter presenter;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "bye bye", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();

    }

    @Override
    public void init() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mainAdview.loadAd(adRequest);
        //presenter setting
        presenter = new MainPresenter();
        presenter.setView(this);
        Realm.init(getApplicationContext());
        GregorianCalendar calendar = new GregorianCalendar();
        //현재 연도 셋팅
        mainYear.setText(calendar.get(Calendar.YEAR) + "");
        mainBut1.setText("계획");
        mainBut2.setText("달력");
        mainBut3.setText("오늘");
        mainBut4.setText("메모");
        mainBut5.setText("목표");
        mainBut6.setText("정보");
        final MainPresenter ee = (MainPresenter) presenter;
        if (!ee.isCheckedMethod()) {
            new MaterialDialog.Builder(this)
                    .title("사용법")
                    .content("지금 플랜Box 사용법을 보시겠습니까?\n" +
                            "이동 을 누르시면 사용법이 적혀 있는 블로그로 이동 됩니다.")
                    .positiveText("이동")
                    .negativeText("아니오")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://blog.naver.com/askyclear/221225506484"));
                            startActivity(intent);
                        }
                    })
                    .checkBoxPrompt("다시 이창을 보지 않음", false, new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                ee.setCheckedMethod(true);
                            } else {
                                ee.setCheckedMethod(false);
                            }
                        }
                    }).build().show();
        }
    }

    @OnClick({R.id.main_but1, R.id.main_but2, R.id.main_but3
            , R.id.main_but4, R.id.main_but5, R.id.main_but6})
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.main_but1:
                //생활계획표로 이동
                intent = new Intent(v.getContext(), LifeSchedularMainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
                //Toast.makeText(this, "but1clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_but2:
                //Calender
                intent = new Intent(v.getContext(), CalendarActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
                break;
            case R.id.main_but3:
                //오늘 일정 등록
                GregorianCalendar calendar = new GregorianCalendar();
                intent = new Intent(v.getContext(), TodayActivity.class);
                intent.putExtra("year", calendar.get(Calendar.YEAR));
                intent.putExtra("month", calendar.get(Calendar.MONTH));
                intent.putExtra("day", calendar.get(Calendar.DAY_OF_MONTH));
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
                break;
            case R.id.main_but4:
                //전체 메모
                intent = new Intent(v.getContext(), MemoActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
                break;
            case R.id.main_but5:
                //목표설정
                intent = new Intent(v.getContext(), GoalActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
                break;
            case R.id.main_but6:
                //설정? 버전 이름 백업 정도?
                intent = new Intent(v.getContext(), InfoActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
                break;
        }
    }
}
