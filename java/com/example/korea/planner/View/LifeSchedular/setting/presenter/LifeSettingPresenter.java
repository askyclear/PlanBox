package com.example.korea.planner.View.LifeSchedular.setting.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.example.korea.planner.View.LifeSchedular.setting.LifeSettingActivity;
import com.example.korea.planner.data.LifeSchedularData;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by korea on 2017-03-30.
 * Presenter
 */

public class LifeSettingPresenter implements LifeSettingContract.Presenter {
    private LifeSettingActivity activity;
    private Realm realm;
    private RealmResults<LifeSchedularData> items;
    private RealmQuery<LifeSchedularData> query;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String[] days = {"mon", "tue", "wen", "tur", "fry", "sat", "sun"};
    private RingtoneManager rm;


    @Override
    public void setView(LifeSettingContract.View view) {
        this.activity = (LifeSettingActivity) view;
        realm = Realm.getDefaultInstance();
        items = realm.where(LifeSchedularData.class).findAll();
        preferences = activity.getSharedPreferences("life_setting", Context.MODE_PRIVATE);
        editor = preferences.edit();
        rm = new RingtoneManager(activity);
        rm.setType(RingtoneManager.TYPE_ALARM);
        realm.close();
    }

    @Override
    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        realm = Realm.getDefaultInstance();
        for (int i = 0; i < 7; i++) {
            if (preferences.getLong(days[i], 0) != 0) {
                RealmResults<LifeSchedularData> result;
                query = realm.where(LifeSchedularData.class).equalTo("id", preferences.getLong(days[i], 0));
                result = query.findAll();
                if (result.size() != 0) {
                    names.add(result.get(0).getCategory_name());
                } else {
                    names.add("없음");
                }
            } else {
                names.add("없음");
            }
        }
        realm.close();
        return names;
    }

    @Override
    public List<LifeSchedularData> getItems() {
        return items;
    }

    @Override
    public void changeDays(int position, int select_item_position) {
        if (select_item_position == 0) {
            editor.remove(days[position]);
        } else {
            editor.putLong(days[position], items.get(select_item_position - 1).getId());
        }
        editor.commit();

    }

    @Override
    public void setClicked(boolean b) {
        editor.putBoolean("clicked", b);
        editor.commit();
    }

    @Override
    public boolean getClicked() {
        return preferences.getBoolean("clicked", false);
    }

    @Override
    public boolean getVibed() {
        return preferences.getBoolean("vibed", false);
    }

    @Override
    public boolean getSounded() {
        return preferences.getBoolean("sounded", false);
    }

    @Override
    public void setVibed(boolean b) {
        editor.putBoolean("vibed", b);
        editor.commit();
    }

    @Override
    public void setSounded(boolean b) {
        editor.putBoolean("sounded", b);
        editor.commit();
    }

    @Override
    public void setTimeed(int selectedIndex) {
        editor.putInt("timed", selectedIndex);
        editor.commit();
    }

    @Override
    public int getTimeIndex() {
        return preferences.getInt("timed", 0);
    }

    @Override
    public void setRingtonUri(Uri uri) {
        editor.putString("sound_uri", uri.toString());
        editor.commit();
    }

    @Override
    public Uri getRingtonUri() {
        return Uri.parse(preferences.getString("sound_uri", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString()));
    }

    @Override
    public void initialize() {
        editor.clear();
        editor.commit();
    }
}
