package com.example.korea.planner.View.LifeSchedular.setting;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.korea.planner.R;
import com.example.korea.planner.View.LifeSchedular.presenter.LifeSchdularContract;
import com.example.korea.planner.View.LifeSchedular.setting.presenter.LifeSettingContract;
import com.example.korea.planner.View.LifeSchedular.setting.presenter.LifeSettingPresenter;
import com.example.korea.planner.adapter.LifeSchedularSettingAdapter;
import com.example.korea.planner.data.LifeSchedularData;
import com.example.korea.planner.util.AlarmUtil;
import com.example.korea.planner.util.BusProvider;
import com.example.korea.planner.util.SettingChange;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by korea on 2017-03-30.
 * Alram에 관한 생활계획표 셋팅 화면이다.
 */

public class LifeSettingActivity extends AppCompatActivity implements LifeSettingContract.View, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.toolbar_set_back)
    ImageButton toolbarSetBack;
    @BindView(R.id.toolbar_set_title)
    TextView toolbarSetTitle;
    @BindView(R.id.life_set_toolbar)
    Toolbar lifeSetToolbar;
    @BindView(R.id.alram_switch)
    SwitchCompat alramSwitch;
    @BindView(R.id.frame_scroll)
    FrameLayout frameScroll;
    @BindView(R.id.alram_method)
    TextView alramMethod;
    @BindView(R.id.alram_vib)
    CheckBox alramVib;
    @BindView(R.id.alram_sound)
    CheckBox alramSound;
    @BindView(R.id.sound_select)
    Button soundSelect;
    @BindView(R.id.time_select)
    Button timeSelect;
    @BindView(R.id.life_set_recycler)
    RecyclerView lifeSetRecycler;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.active_set)
    FrameLayout activeSet;
    private LifeSettingContract.Presenter presenter;
    private List<LifeSchedularData> items;
    private LinearLayoutManager manager;
    private LifeSchedularSettingAdapter mAdapter;
    private RingtoneManager rm;
    private List<String> names;
    private Context context;
    private String[] time_item = {"정시", "10분전", "15분전", "30분전", "1시간전"};
    private Uri uriAlram;
    private Ringtone ringTone;
    private Realm realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_setting);
        this.context = this;
        BusProvider.getInstance().register(this);
        ButterKnife.bind(this);
        init();
    }

    //ringtone 선택 결과를 아래에서 함.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 999) {
            if (resultCode != 0) {
                //선택한 링톤의 uri를 가져옴;
                Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                //ringtone에 셋
                ringTone = RingtoneManager.getRingtone(context, uri);
                ringTone.setStreamType(AudioManager.STREAM_ALARM);
                //presenter에 가져온 uri 정보를 넘김.
                presenter.setRingtonUri(uri);
            }
        }
    }

    @Override
    public void init() {
        setSupportActionBar(lifeSetToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        presenter = new LifeSettingPresenter();
        presenter.setView(this);
        names = new ArrayList<>();
        names.addAll(presenter.getNames());
        items = presenter.getItems();

        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        lifeSetRecycler.setLayoutManager(manager);

        mAdapter = new LifeSchedularSettingAdapter(names, items, this);
        lifeSetRecycler.setAdapter(mAdapter);
        timeSelect.setText(time_item[presenter.getTimeIndex()]);

        rm = new RingtoneManager(this);
        uriAlram = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);



        if (presenter.getVibed()) {
            alramVib.setChecked(true);
        } else
            alramVib.setChecked(false);

        if (presenter.getSounded()) {
            alramSound.setChecked(true);
            soundSelect.setClickable(true);
            soundSelect.setVisibility(View.VISIBLE);
        } else {
            alramSound.setChecked(false);
            soundSelect.setClickable(false);
            soundSelect.setVisibility(View.GONE);
        }

        if (presenter.getClicked()) {
            alramSwitch.setChecked(true);
            activeSet.setVisibility(View.GONE);
            activeSet.setClickable(false);
            presenter.setClicked(true);
        } else {
            alramSwitch.setChecked(false);
            activeSet.setVisibility(View.VISIBLE);
            activeSet.setClickable(true);
            activeSet.setAlpha(0.5f);
            presenter.setClicked(false);
        }

    }
    @OnClick({R.id.toolbar_set_back, R.id.sound_select, R.id.time_select, R.id.toolbar_set_new})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_set_back:
                finish();
                break;
            case R.id.sound_select:
                ringTone = RingtoneManager.getRingtone(context, uriAlram);
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, presenter.getRingtonUri());
                startActivityForResult(intent, 999);
                break;
            case R.id.time_select:
                int index = matching(timeSelect.getText().toString());
                new MaterialDialog.Builder(this)
                        .title("시간 선택")
                        .items((CharSequence[]) time_item)
                        .itemsCallbackSingleChoice(index, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                presenter.setTimeed(dialog.getSelectedIndex());
                                timeSelect.setText(time_item[dialog.getSelectedIndex()]);
                                return true;
                            }
                        })
                        .positiveText("설정")
                        .negativeText("취소")
                        .build().show();
                break;
            case R.id.toolbar_set_new:
                new MaterialDialog.Builder(this)
                        .title("초기화")
                        .positiveText("초기화")
                        .negativeText("취소")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                presenter.initialize();
                                alramSwitch.setChecked(false);
                                timeSelect.setText(time_item[0]);
                                alramSound.setChecked(true);
                                alramVib.setChecked(true);
                                presenter.setVibed(true);
                                presenter.setSounded(true);
                                names.clear();
                                names.addAll(presenter.getNames());
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .build().show();
                break;
        }
        AlarmUtil.getInstance().startAlram(this);
    }

    private int matching(String text) {
        for (int i = 0; i < time_item.length; i++) {
            if (text.equals(time_item[i])) {
                return i;
            }
        }
        return 0;
    }

    @Subscribe
    public void FinishLoad(SettingChange settingChange) {
        presenter.changeDays(settingChange.getPosition(), settingChange.getSelect_item_position());
        if (settingChange.getSelect_item_position() != 0) {
            names.set(settingChange.getPosition(), items.get(settingChange.getSelect_item_position() - 1).getCategory_name());
        } else {
            names.set(settingChange.getPosition(), "없음");
        }
        mAdapter.notifyDataSetChanged();
        AlarmUtil.getInstance().startAlram(this);
    }

    @OnCheckedChanged({R.id.alram_vib, R.id.alram_sound, R.id.alram_switch})
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.alram_vib:
                if (isChecked) {
                    presenter.setVibed(true);
                } else {
                    presenter.setVibed(false);
                }
                break;
            case R.id.alram_sound:
                if (isChecked) {
                    presenter.setSounded(true);
                    soundSelect.setClickable(true);
                    soundSelect.setVisibility(View.VISIBLE);
                } else {
                    presenter.setSounded(false);
                    soundSelect.setClickable(false);
                    soundSelect.setVisibility(View.GONE);
                }
                break;
            case R.id.alram_switch:
                if (isChecked) {
                    //셋팅 건들수 있게
                    activeSet.setVisibility(View.GONE);
                    activeSet.setClickable(false);
                    //shared preference 값 변경
                    presenter.setClicked(true);
                    //AlarmUtil.getInstance().firstAlarmSet(this);
                    //알람 등록?

                } else {
                    activeSet.setVisibility(View.VISIBLE);
                    activeSet.setClickable(true);
                    activeSet.setAlpha(0.5f);
                    presenter.setClicked(false);
                    AlarmUtil.getInstance().firstAlarmCancel(this);

                }
                break;
            default:
                break;
        }
        AlarmUtil.getInstance().startAlram(this);

    }

    @Override
    protected void onDestroy() {
        if (realm != null && !realm.isClosed())
            realm.close();
        super.onDestroy();
    }
}
