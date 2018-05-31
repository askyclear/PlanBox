package com.example.korea.planner.util;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.korea.planner.View.widget.firstwidget4x2.PlanObjectWidget;
import com.example.korea.planner.View.widget.secondwidget4x4.SecondWidget4x4;

/**
 * Created by korea on 2017-04-04.
 * bootreceiver
 */

public class BootReceiver extends BroadcastReceiver {
    static final String ACTION_UPDATE_CLICK = "UPDATE_CLICK";
    static final String APP_WIDGET_ID = "WIDGET_ID";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            //set the alarm here;
            //alarm receive에 관련된 AlarmReciver Broadcast를 한다.
            /*Intent intent1 = new Intent(context,LifeSchedularAlarmReciver.class);
            context.sendBroadcast(intent1);*/
            AlarmUtil.getInstance().firstAlarmCancel(context);
            AlarmUtil.getInstance().firstAlarmSet(context);
            AlarmUtil.getInstance().startAlram(context);

        } else if (intent.getAction() != null && intent.getAction().equals("android.intent.action.DATE_CHANGED")) {
            AlarmUtil.getInstance().firstAlarmCancel(context);
            AlarmUtil.getInstance().firstAlarmSet(context);
            AlarmUtil.getInstance().startAlram(context);
            AppWidgetManager appWidgetManger = AppWidgetManager.getInstance(context);
            int[] appwidgetIds = appWidgetManger.getAppWidgetIds(new ComponentName(context, PlanObjectWidget.class));
            for (int i = 0; i < appwidgetIds.length; i++) {
                Intent intent2 = new Intent(context, PlanObjectWidget.class);
                intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appwidgetIds[i]);
                intent2.setAction(ACTION_UPDATE_CLICK);
                context.sendBroadcast(intent2);
            }
            int[] appwidgetIds2 = appWidgetManger.getAppWidgetIds(new ComponentName(context, SecondWidget4x4.class));
            for (int i = 0; i < appwidgetIds2.length; i++) {
                Intent intent2 = new Intent(context, SecondWidget4x4.class);
                intent2.putExtra(APP_WIDGET_ID, appwidgetIds2[i]);
                intent2.setAction(ACTION_UPDATE_CLICK);
                context.sendBroadcast(intent2);
            }
        }
    }
}
