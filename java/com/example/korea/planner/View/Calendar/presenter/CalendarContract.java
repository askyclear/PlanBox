package com.example.korea.planner.View.Calendar.presenter;

import android.support.v7.app.AppCompatActivity;

import com.example.korea.planner.View.LifeSchedular.presenter.LifeSchdularContract;
import com.example.korea.planner.data.AnniversaryData;

import io.realm.RealmResults;

/**
 * Created by korea on 2017-04-24.
 */

public interface CalendarContract {
    interface View {
        void init();
    }

    interface Presenter {
        void setView(AppCompatActivity view);

        RealmResults<AnniversaryData> setDb(int year, int month, int day, String title, String content);

        RealmResults<AnniversaryData> getDb();

        RealmResults<AnniversaryData> delDb(long id);
    }
}
