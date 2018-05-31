package com.example.korea.planner.View.widget.secondwidget4x4;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.korea.planner.R;
import com.example.korea.planner.View.LifeSchedular.LifeSchedularMainActivity;
import com.example.korea.planner.View.LifeSchedular.setting.LifeSettingActivity;
import com.example.korea.planner.data.LifeSchedularData;
import com.example.korea.planner.data.LifeSchedularDataList;
import com.example.korea.planner.util.CustomLifeSchedularView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by korea on 2017-05-03.
 */

public class SecondWidget4x4 extends AppWidgetProvider {
    private RemoteViews views;
    private Realm realm;
    private RealmResults<LifeSchedularData> items;
    private List<LifeSchedularDataList> imagesitems;
    private SharedPreferences preferences;
    private String[] days = {"mon", "tue", "wen", "tur", "fry", "sat", "sun"};
    private String[] korea_days = {"일", "월", "화", "수", "목", "금", "토"};
    private int nWeek;
    private long id;
    private GregorianCalendar mCalendar;
    private CustomLifeSchedularView customLifeSchedularView;
    private PendingIntent mSender;
    private AlarmManager mAlarmManager;
    private static int WIDGET_UPDATE_INTERVAL = 5 * 60 * 1000;
    static final String ACTION_UPDATE_CLICK = "UPDATE_CLICK";
    static final String ACTION_LEFT_CLICK = "LEFT_CLICK";
    static final String ACTION_RIGHT_CLICK = "LIGHT_CLICK";
    static final String ACTION_SETTING_CLICK = "SETTING_CLICK";
    static final String ACTION_PLAN_CLICK = "PLAN_CLICK";
    static final String APP_WIDGET_ID = "WIDGET_ID";
    private static long current_time_millions;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        int id_update = intent.getIntExtra(APP_WIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        mCalendar = new GregorianCalendar();
        switch (action) {
            case AppWidgetManager.ACTION_APPWIDGET_UPDATE:
                removePreviousAlarm();
                setPreviousAlarm(context, intent);
                break;
            case Intent.ACTION_DATE_CHANGED:
                removePreviousAlarm();
                setPreviousAlarm(context, intent);
                break;
            case Intent.ACTION_BOOT_COMPLETED:
                removePreviousAlarm();
                setPreviousAlarm(context, intent);
                break;

            case ACTION_UPDATE_CLICK:
                mCalendar = new GregorianCalendar();
                nWeek = mCalendar.get(Calendar.DAY_OF_WEEK);

                updateAppWidget(context,
                        AppWidgetManager.getInstance(context),
                        id_update);
                break;

            case ACTION_SETTING_CLICK:
                Intent intent1 = new Intent(context, LifeSettingActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
                break;

            case ACTION_LEFT_CLICK:
                mCalendar.setTimeInMillis(current_time_millions - 24 * 60 * 60 * 1000);
                nWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
                updateAppWidget(context, AppWidgetManager.getInstance(context), id_update);
                break;
            case ACTION_RIGHT_CLICK:
                mCalendar.setTimeInMillis(current_time_millions + 24 * 60 * 60 * 1000);
                nWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
                updateAppWidget(context, AppWidgetManager.getInstance(context), id_update);
                break;
            case ACTION_PLAN_CLICK:
                Intent intent2 = new Intent(context, LifeSchedularMainActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent2);
                break;
        }
        if (realm != null && !realm.isClosed())
            realm.close();
    }

    private void removePreviousAlarm() {
        if (mAlarmManager != null && mSender != null) {
            mSender.cancel();
            mAlarmManager.cancel(mSender);
        }
    }

    private void setPreviousAlarm(Context context, Intent intent) {
        long firsttime = System.currentTimeMillis() + WIDGET_UPDATE_INTERVAL;
        mSender = PendingIntent.getBroadcast(context, 4000, intent, 0);
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.set(AlarmManager.RTC, firsttime, mSender);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        mCalendar = new GregorianCalendar();
        nWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
    public RemoteViews updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId) {
        views = new RemoteViews(context.getPackageName(), R.layout.second_widget);
        initData(context);
        //customview setting
        customLifeSchedularView = new CustomLifeSchedularView(context);
        customLifeSchedularView.setTing(imagesitems, 2);
        customLifeSchedularView.measure(512, 512);
        customLifeSchedularView.layout(0, 0, 512, 512);
        customLifeSchedularView.setDrawingCacheEnabled(true);
        Bitmap bitmap = customLifeSchedularView.getDrawingCache();

        views.setImageViewBitmap(R.id.big_widget_image, bitmap);

        //
        views.setTextViewText(R.id.big_widget_today_text,
                (mCalendar.get(Calendar.MONTH) + 1) + "월" +
                        mCalendar.get(Calendar.DAY_OF_MONTH) + "일 " +
                        korea_days[nWeek - 1]);
        views.setTextViewText(R.id.big_widget_week_text,
                korea_days[nWeek - 1] + "요일");
        views.setTextViewText(R.id.big_widget_cur_title, setCurTitle());
        //event 등록을 위한 intent 생성
        Intent intent = new Intent(context, SecondWidget4x4.class);
        intent.putExtra(APP_WIDGET_ID, appWidgetId);

        current_time_millions = mCalendar.getTimeInMillis();
        //update but event 등록
        intent.setAction(ACTION_UPDATE_CLICK);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 5000, intent, 0);
        views.setOnClickPendingIntent(R.id.big_widget_new_but, pendingIntent1);
        //setting but event 등록
        intent.setAction(ACTION_SETTING_CLICK);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 5000, intent, 0);
        views.setOnClickPendingIntent(R.id.big_widget_set_but, pendingIntent2);
        //left but event 등록
        intent.setAction(ACTION_LEFT_CLICK);

        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(context, 5000, intent, 0);
        views.setOnClickPendingIntent(R.id.big_widget_week_left, pendingIntent3);

        //right but event 등록
        intent.setAction(ACTION_RIGHT_CLICK);
        PendingIntent pendingIntent4 = PendingIntent.getBroadcast(context, 5000, intent, 0);
        views.setOnClickPendingIntent(R.id.big_widget_week_right, pendingIntent4);

        //imageview 클릭 event 등록
        intent.setAction(ACTION_PLAN_CLICK);
        PendingIntent pendingIntent5 = PendingIntent.getBroadcast(context, 5000, intent, 0);
        views.setOnClickPendingIntent(R.id.big_widget_image, pendingIntent5);

        appWidgetManager.updateAppWidget(appWidgetId, views);
        return views;
    }

    private String setCurTitle() {
        int cur_time = mCalendar.get(Calendar.HOUR_OF_DAY) * 60 + mCalendar.get(Calendar.MINUTE);
        for (int i = 0; i < imagesitems.size(); i++) {
            int list_before_time = imagesitems.get(i).getBeforeHour() * 60 + imagesitems.get(i).getBeforeMin();
            int list_after_time = imagesitems.get(i).getAfterHour() * 60 + imagesitems.get(i).getAfterMin();
            if (list_before_time < list_after_time && list_before_time <= cur_time && cur_time < list_after_time) {
                return imagesitems.get(i).getTitle();
            } else if (list_before_time > list_after_time && (cur_time >= list_before_time || cur_time < list_after_time)) {
                return imagesitems.get(i).getTitle();
            }
        }
        return "현재 일정 없음";
    }

    private void initData(Context context) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
        preferences = context.getSharedPreferences("life_setting", Context.MODE_PRIVATE);

        imagesitems = new ArrayList<>();
        if (nWeek == 1) {
            id = preferences.getLong(days[6], 0);
        } else {
            id = preferences.getLong(days[nWeek - 2], 0);
        }
        if (id != 0) {
            items = realm.where(LifeSchedularData.class).equalTo("id", id).findAll();
            if (!items.isEmpty()) {
                for (int i = 0; i < items.get(0).getList().size(); i++) {
                    imagesitems.add((LifeSchedularDataList) items.get(0).getList().get(i));
                }
            }
        }

    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        removePreviousAlarm();
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }
}
