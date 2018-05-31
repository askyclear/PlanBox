package com.example.korea.planner.View.Memo.Presenter;

import android.support.v7.app.AppCompatActivity;

import com.example.korea.planner.data.MemoData;

import io.realm.RealmResults;

/**
 * Created by korea on 2017-04-27.
 */

public interface MemoContract {
    interface View {
        void init();
    }

    interface Presenter {
        void setView(AppCompatActivity view);

        RealmResults<MemoData> getAllDb();

        void saveDb(String title, String content, int color);

        void updateDb(long id, String title, String content, int color);
    }
}
