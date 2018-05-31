package com.example.korea.planner.View.widget.firstwidget4x2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.korea.planner.R;
import com.example.korea.planner.View.LifeSchedular.LifeSchedularMainActivity;
import com.example.korea.planner.View.LifeSchedular.setting.LifeSettingActivity;
import com.example.korea.planner.View.Today.TodayActivity;
import com.example.korea.planner.data.LifeSchedularData;
import com.example.korea.planner.data.LifeSchedularDataList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Implementation of App Widget functionality.
 */
public class PlanObjectWidget extends AppWidgetProvider {
    private SharedPreferences preferences;
    static final String ACTION_UPDATE_CLICK = "UPDATE_CLICK";
    static final String ACTION_DOWN_CLICK = "DOWN_CLICK";
    static final String ACTION_UP_CLICK = "UP_CLICK";
    static final String ACTION_LEFT_CLICK = "LEFT_CLICK";
    static final String ACTION_RIGHT_CLICK = "LIGHT_CLICK";
    static final String ACTION_GOAL_CLICK = "GOAL_CLICK";
    static final String ACTION_SETTING_CLICK = "SETTING_CLICK";
    static final String ACTION_PLAN_CLICK = "PLAN_CLICK";
    private static int WIDGET_UPDATE_INTERVAL = 5 * 60 * 1000;
    private static PendingIntent mSender;
    private static AlarmManager mManager;
    private String[] days = {"mon", "tue", "wen", "tur", "fry", "sat", "sun"};
    private String[] korea_days = {"일", "월", "화", "수", "목", "금", "토"};
    private long id;
    private static int position = 0;
    private GregorianCalendar gregorianCalendar;
    private int nWeek;
    private Realm realm;
    private RealmResults<LifeSchedularData> mitems;
    private List<LifeSchedularDataList> item;
    private static long current_time_millons;

    public RemoteViews updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.plan_object_widget);
        // Construct the RemoteViews object
        if (id != 0) {
            int current_time = gregorianCalendar.get(Calendar.HOUR_OF_DAY) * 60 +
                    gregorianCalendar.get(Calendar.MINUTE);
            for (int i = 0; i < item.size(); i++) {
                int list_before_time = item.get(i).getBeforeHour() * 60 + item.get(i).getBeforeMin();
                int list_after_time = item.get(i).getAfterHour() * 60 + item.get(i).getAfterMin();
                if (list_before_time < list_after_time && current_time >= list_before_time && current_time < list_after_time) {
                    position = i;

                    break;
                } else if (list_before_time > list_after_time && current_time >= list_before_time) {
                    position = i;
                    break;
                } else {
                    position = item.size();
                }
            }
            if (position == item.size()) {
                views.setTextViewText(R.id.widget_time_text,
                        gregorianCalendar.get(Calendar.HOUR_OF_DAY) + "시");
                views.setTextViewText(R.id.life_schedualr_widget_title, "등록된 일정 없음.");
                views.setTextViewText(R.id.life_schedualr_widget_content, "");
            } else if (position < item.size()) {
                views.setTextViewText(R.id.widget_time_text,
                        item.get(position).getBeforeHour() + ":"
                                + item.get(position).getBeforeMin() + " ~ "
                                + item.get(position).getAfterHour() + ":"
                                + item.get(position).getAfterMin());
                views.setTextViewText(R.id.life_schedualr_widget_title, item.get(position).getTitle());
                views.setTextViewText(R.id.life_schedualr_widget_content, item.get(position).getContent());
            }

        } else {
            views.setTextViewText(R.id.widget_time_text,
                    "00:00 ~ 24:00");
            views.setTextViewText(R.id.life_schedualr_widget_title, "오늘 계획 없음");
            views.setTextViewText(R.id.life_schedualr_widget_content, "");
        }
        // Instruct the widget manager to update the widget
        views.setTextViewText(R.id.widget_today_text,
                (gregorianCalendar.get(Calendar.MONTH) + 1) + "월" +
                        gregorianCalendar.get(Calendar.DAY_OF_MONTH) + "일 "
                        + korea_days[nWeek - 1]);
        views.setTextViewText(R.id.widget_week_text, korea_days[nWeek - 1] + "요일");


        current_time_millons = gregorianCalendar.getTimeInMillis();
        Intent intent = new Intent(context, PlanObjectWidget.class);
        intent.setAction(ACTION_UP_CLICK);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        /*intent.putExtra("position", position);
        intent.putExtra("time_million", gregorianCalendar.getTimeInMillis());*/
        //up but intent 등록
        PendingIntent pending = PendingIntent.getBroadcast(context, appWidgetId, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_up, pending);
        //down but intent 등록
        intent.setAction(ACTION_DOWN_CLICK);
        PendingIntent pending1 = PendingIntent.getBroadcast(context, appWidgetId, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_down, pending1);
        //Update but intent 등록
        intent.setAction(ACTION_UPDATE_CLICK);
        PendingIntent pending2 = PendingIntent.getBroadcast(context, appWidgetId, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_new_but, pending2);
        //Left but intent 등록
        intent.setAction(ACTION_LEFT_CLICK);
        PendingIntent pending3 = PendingIntent.getBroadcast(context, appWidgetId, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_week_left, pending3);
        //Right but intent 등록
        intent.setAction(ACTION_RIGHT_CLICK);
        PendingIntent pending4 = PendingIntent.getBroadcast(context, appWidgetId, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_week_right, pending4);
        //list view click
        intent.setAction(ACTION_GOAL_CLICK);
        PendingIntent pending5 = PendingIntent.getBroadcast(context, appWidgetId, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_goal_layout, pending5);
        //setting click
        intent.setAction(ACTION_SETTING_CLICK);
        PendingIntent pending6 = PendingIntent.getBroadcast(context, appWidgetId, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_set_but, pending6);
        //plan click
        intent.setAction(ACTION_PLAN_CLICK);
        PendingIntent pending7 = PendingIntent.getBroadcast(context, appWidgetId, intent, 0);
        views.setOnClickPendingIntent(R.id.life_schedualr_widget_title, pending7);
        //list 뷰
        Intent svcIntent = new Intent(context, WidgetService.class);
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.putExtra("year", gregorianCalendar.get(Calendar.YEAR));
        svcIntent.putExtra("month", gregorianCalendar.get(Calendar.MONTH));
        svcIntent.putExtra("day", gregorianCalendar.get(Calendar.DAY_OF_MONTH));
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

        views.setRemoteAdapter(R.id.widget_list, svcIntent);
        views.setEmptyView(R.id.widget_list, R.id.empty_view);

        appWidgetManager.updateAppWidget(appWidgetId, views);
        AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list);

        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        gregorianCalendar = new GregorianCalendar();
        nWeek = gregorianCalendar.get(Calendar.DAY_OF_WEEK);
        initData(context);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public void initData(Context context) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
        preferences = context.getSharedPreferences("life_setting", Context.MODE_PRIVATE);

        item = new ArrayList<>();
        if (nWeek == 1) {
            id = preferences.getLong(days[6], 0);
        } else {
            id = preferences.getLong(days[nWeek - 2], 0);
        }
        if (id != 0) {
            mitems = realm.where(LifeSchedularData.class).equalTo("id", id).findAll();
            if (!mitems.isEmpty()) {
                for (int i = 0; i < mitems.get(0).getList().size(); i++) {
                    item.add((LifeSchedularDataList) mitems.get(0).getList().get(i));
                }
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        int id_update = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        if (action != null && (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
                || action.equals(Intent.ACTION_BOOT_COMPLETED)
                || action.equals(Intent.ACTION_DATE_CHANGED))) {
            long firstTime = System.currentTimeMillis() + WIDGET_UPDATE_INTERVAL;
            //Toast.makeText(context,"Update",Toast.LENGTH_LONG).show();
            removePreviousAlarm();
            mSender = PendingIntent.getBroadcast(context, 4000, intent, 0);
            mManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            mManager.set(AlarmManager.RTC, firstTime, mSender);
        } else if (action != null && action.equals(ACTION_UP_CLICK)) {
            gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTimeInMillis(current_time_millons);
            nWeek = gregorianCalendar.get(Calendar.DAY_OF_WEEK);
            initData(context);
            if (item != null && item.size() != 0) {
                //제일 처음이라면
                if (position - 1 < 0) {
                    position = item.size() - 1;
                } else
                    position = position - 1;
                onButtonUpDownClick(context, AppWidgetManager.getInstance(context), id_update, position);
            }
        } else if (action != null && action.equals(ACTION_DOWN_CLICK)) {
            gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTimeInMillis(current_time_millons);
            nWeek = gregorianCalendar.get(Calendar.DAY_OF_WEEK);
            initData(context);
            if (item != null && item.size() != 0) {
                //마지막 일정이라면
                if (position + 1 >= item.size())
                    position = 0;
                else
                    position = position + 1;
                onButtonUpDownClick(context, AppWidgetManager.getInstance(context), id_update, position);
            }
        } else if (action != null && action.equals(ACTION_UPDATE_CLICK)) {
            gregorianCalendar = new GregorianCalendar();
            nWeek = gregorianCalendar.get(Calendar.DAY_OF_WEEK);
            initData(context);
            updateAppWidget(context, AppWidgetManager.getInstance(context), id_update);
            AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(id_update, R.id.widget_list);
        } else if (action != null && action.equals(ACTION_LEFT_CLICK)) {
            gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTimeInMillis(current_time_millons - 24 * 60 * 60 * 1000);
            intent.putExtra("time_million", gregorianCalendar.getTimeInMillis());
            nWeek = gregorianCalendar.get(Calendar.DAY_OF_WEEK);
            initData(context);

            updateAppWidget(context, AppWidgetManager.getInstance(context), id_update);
            //AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(id_update, R.id.widget_list);
        } else if (action != null && action.equals(ACTION_RIGHT_CLICK)) {
            gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTimeInMillis(current_time_millons + 24 * 60 * 60 * 1000);
            nWeek = gregorianCalendar.get(Calendar.DAY_OF_WEEK);
            initData(context);

            updateAppWidget(context, AppWidgetManager.getInstance(context), id_update);
            //AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(id_update, R.id.widget_list);

        } else if (action != null && action.equals("android.appwidget.action.APPWIDGET_DISABLED")) {
            removePreviousAlarm();
        } else if (action != null && action.equals(ACTION_SETTING_CLICK)) {
            Intent intent1 = new Intent(context, LifeSettingActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        } else if (action != null && action.equals(ACTION_PLAN_CLICK)) {
            Intent intent1 = new Intent(context, LifeSchedularMainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);

        } else if (action != null && action.equals(ACTION_GOAL_CLICK)) {
            gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTimeInMillis(current_time_millons);
            Intent intent1 = new Intent(context, TodayActivity.class);
            intent1.putExtra("year", gregorianCalendar.get(Calendar.YEAR));
            intent1.putExtra("month", gregorianCalendar.get(Calendar.MONTH));
            intent1.putExtra("day", gregorianCalendar.get(Calendar.DAY_OF_MONTH));
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
        if (realm != null && !realm.isClosed())
            realm.close();
    }

    public void removePreviousAlarm() {
        if (mManager != null && mSender != null) {
            mSender.cancel();
            mManager.cancel(mSender);
        }
    }

    public void onButtonUpDownClick(Context context, AppWidgetManager appWidgetManager,
                                    int appWidgetId, int position) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.plan_object_widget);
        views.setTextViewText(R.id.widget_time_text,
                item.get(position).getBeforeHour() + ":"
                        + item.get(position).getBeforeMin() + " ~ "
                        + item.get(position).getAfterHour() + ":"
                        + item.get(position).getAfterMin());
        views.setTextViewText(R.id.life_schedualr_widget_title, item.get(position).getTitle());
        views.setTextViewText(R.id.life_schedualr_widget_content, item.get(position).getContent());
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}

