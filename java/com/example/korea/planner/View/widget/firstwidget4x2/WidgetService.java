package com.example.korea.planner.View.widget.firstwidget4x2;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.korea.planner.R;
import com.example.korea.planner.data.GoalData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * Created by korea on 2017-04-27.
 * ListView의 어뎁터?? 역할이라고 한다.
 */

public class WidgetService extends RemoteViewsService {
    //Realm realm;
    //RealmResults<GoalData> items;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
        );
        GregorianCalendar mCalendar = new GregorianCalendar();
        int year = intent.getIntExtra("year", mCalendar.get(Calendar.YEAR));
        int month = intent.getIntExtra("month", mCalendar.get(Calendar.MONTH)) + 1;
        int day = intent.getIntExtra("day", mCalendar.get(Calendar.DAY_OF_MONTH));
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent, year, month, day);
    }

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private Realm realm;
        private RealmResults<GoalData> items;
        private List<WidgetItem> widgetItem;
        private Context context;
        private int mAppWidgetId;
        private int year, month, day;

        public ListRemoteViewsFactory(Context context, Intent intent, int year, int month, int day) {
            this.context = context;
            this.mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            this.year = year;
            this.month = month;
            this.day = day;
        }

        public void dataSetList() {
            Realm.init(context);
            realm = Realm.getDefaultInstance();
            items = realm.where(GoalData.class).equalTo("year", year)
                    .equalTo("month", month)
                    .equalTo("day", day)
                    .findAll();

            widgetItem = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                WidgetItem item = new WidgetItem(items.get(i).getGoal(), items.get(i).isSuccesed());
                widgetItem.add(item);
            }
            realm.close();
        }

        @Override
        public void onCreate() {

            dataSetList();
        }

        @Override
        public void onDataSetChanged() {

            dataSetList();
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            return widgetItem.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_item);
            //위젯에 취소선을 넣기 위한 SpannableStringBuilder임
            SpannableStringBuilder content = new SpannableStringBuilder();
            content.append(widgetItem.get(position).getContent())
                    .setSpan(new StrikethroughSpan(), 0, widgetItem.get(position).getContent().length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            if (widgetItem.get(position).isChecked()) {
                rv.setTextViewText(R.id.widget_list_item, content);
            } else {
                rv.setTextViewText(R.id.widget_list_item, widgetItem.get(position).getContent());
            }


            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
