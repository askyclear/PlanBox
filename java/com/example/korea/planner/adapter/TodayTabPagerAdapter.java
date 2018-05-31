package com.example.korea.planner.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.korea.planner.View.Today.fragment.TodayFirstFragment;
import com.example.korea.planner.View.Today.fragment.TodaySecondFragment;

/**
 * Created by korea on 2017-04-25.
 */

public class TodayTabPagerAdapter extends FragmentStatePagerAdapter {
    private int tabCount;
    private int year, month, day;

    public TodayTabPagerAdapter(FragmentManager fm, int tabCount, int year, int month, int day) {
        super(fm);
        this.tabCount = tabCount;
        this.year = year;
        this.month = month;
        this.day = day;
    }


    @Override
    public int getCount() {
        return tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putInt("year", year);
        args.putInt("month", month);
        args.putInt("day", day);
        switch (position) {
            case 0:
                TodayFirstFragment fragment1 = new TodayFirstFragment();
                fragment1.setArguments(args);
                return fragment1;
            case 1:
                TodaySecondFragment fragment2 = new TodaySecondFragment();
                fragment2.setArguments(args);
                return fragment2;
            default:
                return null;
        }
    }
}
