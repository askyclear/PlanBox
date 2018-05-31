package com.example.korea.planner.View.Memo.Presenter;

import android.support.v7.app.AppCompatActivity;

import com.example.korea.planner.View.Memo.MemoActivity;
import com.example.korea.planner.data.MemoData;

import java.util.Calendar;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by korea on 2017-04-27.
 */

public class MemoPresenter implements MemoContract.Presenter {
    private MemoActivity activity;
    private Realm realm;
    private RealmResults<MemoData> items;

    @Override
    public void setView(AppCompatActivity view) {
        this.activity = (MemoActivity) view;

    }

    @Override
    public RealmResults<MemoData> getAllDb() {
        realm = Realm.getDefaultInstance();
        items = realm.where(MemoData.class).findAll();
        realm.close();
        return items;
    }

    @Override
    public void saveDb(String title, String content, int color) {
        GregorianCalendar calendar = new GregorianCalendar();
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        MemoData item = realm.createObject(MemoData.class);
        item.setId(System.currentTimeMillis());
        item.setTitle(title);
        item.setContent(content);
        item.setColor(color);
        item.setYear(calendar.get(Calendar.YEAR));
        item.setMonth(calendar.get(Calendar.MONTH) + 1);
        item.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public void updateDb(long id, String title, String content, int color) {
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        MemoData item = realm.where(MemoData.class).equalTo("id", id).findFirst();
        item.setTitle(title);
        item.setContent(content);
        item.setColor(color);
        realm.commitTransaction();
        realm.close();
    }
}
