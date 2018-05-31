package com.example.korea.planner.View.Calendar.presenter;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.korea.planner.View.Calendar.CalendarActivity;
import com.example.korea.planner.data.AnniversaryData;

import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by korea on 2017-04-24.
 * 캘린더 프레젠터
 */

public class CalendarPresenter implements CalendarContract.Presenter {
    private CalendarActivity activity;
    private Realm realm;
    private RealmResults<AnniversaryData> items;
    private GregorianCalendar mCalendar;
    private int[] days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    @Override
    public void setView(AppCompatActivity view) {
        this.activity = (CalendarActivity) view;
        realm = Realm.getDefaultInstance();
        items = realm.where(AnniversaryData.class).findAllSorted("month", Sort.ASCENDING, "day", Sort.ASCENDING);
        realm.close();
        mCalendar = new GregorianCalendar();

    }

    @Override
    public RealmResults<AnniversaryData> setDb(int year, int month, int day, String title, String content) {
        if (month >= 1 && month <= 12) {
            if (year % 4 == 0 && year % 100 > 0 || year % 400 == 0) {
                days[1] = 29;
            } else {
                days[1] = 28;
            }
            if (day >= 1 && day <= days[month - 1]) {
                realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                AnniversaryData item = realm.createObject(AnniversaryData.class);
                item.setId(System.currentTimeMillis());
                item.setTitle(title);
                item.setContent(content);
                item.setYear(year);
                item.setMonth(month);
                item.setDay(day);
                realm.commitTransaction();
                realm.close();
            } else {
                Toast.makeText(activity, year + "년 " + month + "월은 1 ~ " + days[month - 1] + "사이 입니다.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, "월은 1~12사이 입니다!", Toast.LENGTH_SHORT).show();
        }
        return items;
    }

    @Override
    public RealmResults<AnniversaryData> getDb() {
        return items;
    }

    @Override
    public RealmResults<AnniversaryData> delDb(long id) {
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == id) {
                items.deleteFromRealm(i);
                break;
            }
        }
        realm.commitTransaction();
        realm.close();
        return items;
    }

}
