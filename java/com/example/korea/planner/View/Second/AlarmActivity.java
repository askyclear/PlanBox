package com.example.korea.planner.View.Second;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.korea.planner.R;
import com.example.korea.planner.data.LifeSchedularData;
import com.example.korea.planner.data.LifeSchedularDataList;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by korea on 2017-04-05.
 * Alarm Activity
 */

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.alarmView_title)
    TextView alarmViewTitle;
    @BindView(R.id.alarmView_content)
    TextView alarmViewContent;
    @BindView(R.id.alarmView_time)
    TextView alarmViewTime;

    @BindView(R.id.alarmView_nexttitle)
    TextView alarmViewNexttitle;
    @BindView(R.id.alarmView_nextcontent)
    TextView alarmViewNextcontent;
    @BindView(R.id.alarmnextView_time)
    TextView alarmnextViewTime;
    @BindView(R.id.alarmView_end)
    Button alarmViewEnd;
    @BindView(R.id.alarm_adView)
    AdView alarmAdView;
    private SharedPreferences preferences;
    private Ringtone ringtone;
    private RingtoneManager rm;
    private Vibrator vide;
    private long[] vibePattern = {0, 1000, 500, 1000, 500, 1000};
    private AudioManager audioManager;
    private Realm realm;
    private RealmResults<LifeSchedularData> results;
    private List<LifeSchedularDataList> items;
    private long id;
    private static final String[] days = { "sun","mon", "tue", "wen", "tur", "fry", "sat"};
    private int[] before_times = {0, 10, 15, 30, 60};
    private int ringerMode = 0;
    private Handler mHandler;
    private Runnable mRunnable;
    private Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);
        context = this;
        init();
        setView();
        if (preferences.getBoolean("vibed", false)) {
            //audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            vide.vibrate(vibePattern, 0);
        }
        if (preferences.getBoolean("sounded", false)) {
            ringtone = RingtoneManager.getRingtone(context, Uri.parse(preferences.getString("sound_uri", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString())));

            if (ringtone != null) {
                ringtone.setStreamType(AudioManager.STREAM_ALARM);
                ringtone.play();
            }
        }
        //30초후 vibe or sound 실행을 종료하기 위한 runable postDelayed로 30초 뒤 종료.
        mRunnable = new Runnable() {
            @Override
            public void run() {
                audioManager.setRingerMode(ringerMode);
                if (ringtone != null) {
                    ringtone.stop();
                }
                if (vide != null) {
                    vide.cancel();
                }
            }
        };

        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 30 * 1000);
        /*
        * audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
         * audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
         * audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        */
    }

    private void setView() {
        GregorianCalendar mCalendar = new GregorianCalendar();
        int weeked = mCalendar.get(Calendar.DAY_OF_WEEK);
        id = preferences.getLong(days[weeked - 1], 0);
        int before_time = before_times[preferences.getInt("timed", 0)];
        results = realm.where(LifeSchedularData.class).equalTo("id", id).findAll();
        for (int i = 0; i < results.get(0).getList().size(); i++) {
            items.add((LifeSchedularDataList) results.get(0).getList().get(i));
        }

        int current_time = mCalendar.get(Calendar.HOUR_OF_DAY) * 60 + mCalendar.get(Calendar.MINUTE);

        findCurIndex(current_time,before_time,weeked);
    }
    private void findCurIndex(int cur_time,int before_time,int weeked){
        int position = 0;
        for(LifeSchedularDataList item : items){
            int alarm_Before = item.getBeforeHour() *60 + item.getBeforeMin() - before_time;
            int alarm_After = item.getAfterHour() * 60 + item.getAfterMin() - before_time;

            if(cur_time-alarm_Before>=0 &&
                    cur_time-alarm_After<0){
                setAlarmText(item);
                //예외 상황
                if(position+1>=items.size()) {
                    exceptionState(weeked);
                }else{
                    setAlarmNextText(items.get(position+1));
                }
            }else if(cur_time-alarm_Before>=0&&
                    alarm_Before-alarm_After>=0){
                setAlarmText(item);
                exceptionState(weeked);
            }
            position++;
        }
    }
    private void exceptionState(int weeked){
        long local_id = 0;
        if (weeked == 1) {
            local_id = preferences.getLong(days[0], 0);
        } else {
            local_id = preferences.getLong(days[weeked - 1], 0);
        }
        if(local_id!=0){
            RealmResults<LifeSchedularData> result =realm.where(LifeSchedularData.class).equalTo("id", id).findAll();
            if(result.get(0).getList().get(0) instanceof LifeSchedularDataList)
                setAlarmNextText((LifeSchedularDataList)result.get(0).getList().get(0));
        }else{
            alarmViewNexttitle.setText("다음에 등록된 일정이 없습니다.");
            alarmViewNextcontent.setText("내용 - ");
            alarmnextViewTime.setText("일정 - ");
        }
    }
    private void setAlarmText(LifeSchedularDataList item){
        alarmViewTitle.setText("제목 - " + item.getTitle());
        alarmViewContent.setText("내용 - " + item.getContent());
        alarmViewTime.setText("일정 - " + item.getBeforeHour() + " : " + item.getBeforeMin()
                + " ~ " + item.getAfterHour() + " : " + item.getAfterMin());

    }
    private void setAlarmNextText(LifeSchedularDataList item){
        alarmViewNexttitle.setText("제목 - " + item.getTitle());
        alarmViewNextcontent.setText("내용 - " + item.getContent());
        alarmnextViewTime.setText("일정 - " + item.getBeforeHour() + " : " + item.getBeforeMin()
                + " ~ " + item.getAfterHour() + " : " + item.getAfterMin());
    }
    private void init() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        AdRequest adRequest = new AdRequest.Builder().build();
        alarmAdView.loadAd(adRequest);
        Realm.init(this);
        items = new ArrayList<>();
        realm = Realm.getDefaultInstance();

        preferences = getSharedPreferences("life_setting", Context.MODE_PRIVATE);
        rm = new RingtoneManager(this);
        rm.setType(RingtoneManager.TYPE_ALARM);

        vide = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        ringerMode = audioManager.getRingerMode();

    }
    @Override
    protected void onDestroy() {
        //mRunnable이 실행되기전에 종료할 경우 취소시켜야 함.
        mHandler.removeCallbacks(mRunnable);
        if (ringtone != null) {
            ringtone.stop();
        }
        if (vide != null) {
            vide.cancel();
        }
        Toast.makeText(this, "알람 확인 완료", Toast.LENGTH_SHORT).show();
        realm.close();
        super.onDestroy();
    }

    @OnClick({R.id.alarmView_end})
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.alarmView_end) {
            finish();
        }
    }
}
