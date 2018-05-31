package com.example.korea.planner.View.Today.Presenter;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by korea on 2017-04-25.
 */

public interface TodayContract {
    interface View {
        void init();
    }

    interface Presenter {
        void setView(AppCompatActivity view);
    }
}
