package com.example.korea.planner.View.Main.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.korea.planner.View.Main.MainActivity;
import com.example.korea.planner.util.SolarLunarUtil;

/**
 * Created by korea on 2017-03-14.
 */

public class MainPresenter implements MainContract.Presenter {
    private MainActivity activity;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    public void setView(MainContract.View view) {
        this.activity = (MainActivity) view;
        SolarLunarUtil.getInstance();
        SolarLunarUtil.getInstance().setSolarLunar();
        preferences = activity.getSharedPreferences("life_setting", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public boolean isCheckedMethod() {
        return preferences.getBoolean("is_link_view", false);
    }

    public void setCheckedMethod(boolean checkedMethod) {
        editor.putBoolean("is_link_view", checkedMethod);
        editor.commit();
    }
}
