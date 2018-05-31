package com.example.korea.planner.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.korea.planner.View.LifeSchedular.frgment.LifeFirstFragment;
import com.example.korea.planner.View.LifeSchedular.frgment.LifeSecondFragment;

/**
 * Created by korea on 2017-03-21.
 */

public class LifeTabPagerAdapter extends FragmentStatePagerAdapter {
    private int tabCount;
    private long id;

    public LifeTabPagerAdapter(FragmentManager fm, int tabCount, long id) {
        super(fm);
        this.tabCount = tabCount;
        this.id = id;
    }

    @Override
    public Fragment getItem(int position) {
        //Return the current tabs
        Bundle args = new Bundle();
        args.putLong("LifeId", id);
        switch (position) {
            case 0:
                LifeFirstFragment lifeFirstFragment = new LifeFirstFragment();
                lifeFirstFragment.setArguments(args);
                return lifeFirstFragment;
            case 1:
                LifeSecondFragment lifeSecondFragment = new LifeSecondFragment();
                lifeSecondFragment.setArguments(args);
                return lifeSecondFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
