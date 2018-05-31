package com.example.korea.planner.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.korea.planner.data.LifeSchedularData;
import com.example.korea.planner.data.LifeSchedularDataList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * Created by korea on 2017-04-04.
 * alarm singleton
 */

public class AlarmUtil {
    private volatile static AlarmUtil ourInstance;
    private static int mCrouteCode = 2000;
    private static int mFirstCode = 3000;
    private AlarmManager alarmManager;
    private GregorianCalendar mCalendar;
    private int nWeek;
    private SharedPreferences preferences;
    private Realm realm;
    private RealmResults<LifeSchedularData> items;
    private List<LifeSchedularDataList> item;
    private long id;
    private String[] days = {"sun", "mon", "tue", "wen", "tur", "fry", "sat"};
    private int[] before_times = {0, 10, 15, 30, 60};
    private int before_time;

    public static AlarmUtil getInstance() {
        if (ourInstance == null) {
            synchronized (AlarmUtil.class) {
                if (ourInstance == null) {
                    ourInstance = new AlarmUtil();
                }
            }
        }
        return ourInstance;
    }

    private AlarmUtil() {
    }

    public void startAlram(Context context) {
        Realm.init(context);
        preferences = context.getSharedPreferences("life_setting", Context.MODE_PRIVATE);
        mCalendar = new GregorianCalendar();
        realm = Realm.getDefaultInstance();
        item = new ArrayList<>();
        //기존 알람을 해제 하고
        nWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
        alramCancel(context);
        if (preferences.getBoolean("clicked", false)) {//알람을 쓰겠다고 설정했는지 확인하는 부분.
            //alram 오늘 요일에 설정된 item id를 불러온다.
            id = preferences.getLong(days[nWeek - 1], 0);
            before_time = before_times[preferences.getInt("timed", 0)];
            if (id != 0) {
                items = realm.where(LifeSchedularData.class).equalTo("id", id).findAll();
                if (items.size() != 0) {
                    item.addAll(items.get(0).getList());
                }
            /*모든 알람을 등록하는 것보다 알람을 사용하고자 할때 그 부분에 가장 가까운 알람을 등록하는것이 가장 좋다고 생각됨;
1            알람이 발생 할시 그 때 그 다음 알람을 등록하는 정도?? 이런식으로 하는것이 알람을 set하고 cancle하는것이 좋다고 보임;
             */
                int current_time = mCalendar.get(Calendar.HOUR_OF_DAY) * 60 + mCalendar.get(Calendar.MINUTE);
                for (int i = 0; i < item.size(); i++) {
                    int alarm_time = item.get(i).getBeforeHour() * 60 + item.get(i).getBeforeMin() - before_time;
                    if (alarm_time > current_time) {
                        mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                                mCalendar.get(Calendar.DATE), item.get(i).getBeforeHour(), item.get(i).getBeforeMin(), 0);
                        Log.d("ALARM", "설정된 id : " + id +
                                " nWeek :" + nWeek +
                                " current second " + (System.currentTimeMillis() / 1000) +
                                " alarm second " + ((mCalendar.getTimeInMillis() / 1000) - before_time * 60));
                        alramSet(context, mCalendar.getTimeInMillis() - before_time * 60 * 1000);
                        break;
                    } else if (i == (item.size() - 1) && alarm_time < current_time) {//오늘 일정중 마지막 일정이고, 이미 알람을 실행했었다면
                        // 다음날 예정 알람목록을 가져와 첫번째 일정을 등록해 준다.
                        //딱히 필요없는 부분으로 보임
                        if (nWeek == 7) {
                            id = preferences.getLong(days[0], 0);
                        } else {
                            id = preferences.getLong(days[nWeek], 0);
                        }
                        if (id != 0) {//다음날 계획표가 존재할 시
                            items = realm.where(LifeSchedularData.class).equalTo("id", id).findAll();
                            item = new ArrayList<>();
                            item.addAll(items.get(0).getList());//리스트를 가져온후
                            mCalendar.setTimeInMillis(mCalendar.getTimeInMillis() + 1000 * 60 * 60 * 24);//mCalendar를 다음날로 바꾼후
                            mCalendar.set(Calendar.HOUR, item.get(0).getBeforeHour());//시간 분 초 등록
                            mCalendar.set(Calendar.MONTH, item.get(0).getBeforeMin());
                            mCalendar.set(Calendar.SECOND, 0);
                            mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                                    mCalendar.get(Calendar.DATE), item.get(0).getBeforeHour(), item.get(0).getBeforeMin(), 0);
                            //alramSet(context, 24 * 60 * 60 * 1000 + mCalendar.getTimeInMillis() - before_time * 60 * 1000);
                        }
                    }
                }
                realm.close();
            }
        }
        firstAlarmCancel(context);
        firstAlarmSet(context);
    }
    private void alramCancel(Context context) {
        Intent intent = new Intent(context, LifeSchedularAlarmReciver.class);
        PendingIntent pendingintent = PendingIntent.getBroadcast(context, mCrouteCode, intent, 0);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingintent);
    }

    private void alramSet(Context context, long triger) {
        Intent intent = new Intent(context, LifeSchedularAlarmReciver.class);
        PendingIntent pendingintent = PendingIntent.getBroadcast(context, mCrouteCode, intent, 0);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Log.d("alarm", "triger : " + triger);
        Log.d("alarm", "current : " + mCalendar.getTimeInMillis());
        if (Build.VERSION.SDK_INT >= 23)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triger, pendingintent);
        else {
            if (Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triger, pendingintent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triger, pendingintent);
            }
        }
    }

    public void firstAlarmSet(Context context) {
        preferences = context.getSharedPreferences("life_setting", Context.MODE_PRIVATE);
        if (preferences.getBoolean("clicked", false)) {
            Intent intent = new Intent(context, BootReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(context, mFirstCode, intent, 0);
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE),
                    0, 0, 0);
            if (Build.VERSION.SDK_INT >= 19) {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + 1000 * 60 * 60 * 24, 1000 * 60 * 60 * 24, pi);
            } else {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + 1000 * 60 * 60 * 24, 1000 * 60 * 60 * 24, pi);
            }
        }
    }

    public void firstAlarmCancel(Context context) {
        Intent intent = new Intent(context, BootReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, mFirstCode, intent, 0);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pi);
    }
}
