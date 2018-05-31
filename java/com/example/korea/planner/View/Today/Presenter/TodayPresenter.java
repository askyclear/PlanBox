package com.example.korea.planner.View.Today.Presenter;

import android.support.v7.app.AppCompatActivity;

import com.example.korea.planner.View.Today.TodayActivity;

/**
 * Created by korea on 2017-04-25.
 */

public class TodayPresenter implements TodayContract.Presenter {
    private TodayActivity activity;

    @Override
    public void setView(AppCompatActivity view) {
        this.activity = (TodayActivity) view;
    }
}
