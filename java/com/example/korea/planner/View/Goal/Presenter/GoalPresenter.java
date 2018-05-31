package com.example.korea.planner.View.Goal.Presenter;

import android.support.v7.app.AppCompatActivity;

import com.example.korea.planner.View.Goal.GoalActivity;
import com.example.korea.planner.data.GoalData;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by korea on 2017-04-27.
 * Goal Presenter realm DB & data
 */

public class GoalPresenter implements GoalContract.Presenter {
    private Realm realm;
    private RealmResults<GoalData> items;
    private GoalActivity activity;

    @Override
    public void setView(AppCompatActivity view) {
        this.activity = (GoalActivity) view;

    }

    @Override
    public RealmResults<GoalData> getAddDb() {
        realm = Realm.getDefaultInstance();
        items = realm.where(GoalData.class).findAll();
        realm.close();
        return items;
    }

    @Override
    public int getGoalProgress() {
        realm = Realm.getDefaultInstance();
        items = realm.where(GoalData.class).findAll();
        double max = items.size();
        RealmResults<GoalData> successItem;
        successItem = realm.where(GoalData.class).equalTo("succesed", true).findAll();
        double success = successItem.size();
        realm.close();
        return (int) (success / max * 100);
    }
}
