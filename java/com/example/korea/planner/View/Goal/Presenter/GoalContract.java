package com.example.korea.planner.View.Goal.Presenter;

import android.support.v7.app.AppCompatActivity;

import com.example.korea.planner.data.GoalData;

import io.realm.RealmResults;

/**
 * Created by korea on 2017-04-27.
 */

public interface GoalContract {
    interface View {
        void init();
    }

    interface Presenter {
        void setView(AppCompatActivity view);

        RealmResults<GoalData> getAddDb();

        int getGoalProgress();
    }
}
