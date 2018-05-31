package com.example.korea.planner.util;

import com.example.korea.planner.data.LifeSchedularDataList;

import java.util.List;

import static android.R.id.list;

/**
 * Created by korea on 2017-03-23.
 */

public class PushEvent {
    private LifeSchedularDataList pushItem;

    public PushEvent(LifeSchedularDataList item) {
        this.pushItem = item;
    }

    public LifeSchedularDataList getPushItem() {
        return pushItem;
    }
}
