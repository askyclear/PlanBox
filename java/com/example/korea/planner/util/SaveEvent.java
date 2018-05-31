package com.example.korea.planner.util;

import com.example.korea.planner.data.LifeSchedularDataList;

import java.util.List;

/**
 * Created by korea on 2017-03-23.
 */

public class SaveEvent {
    private List<LifeSchedularDataList> mLifeSchedularDataLists;

    public SaveEvent(List<LifeSchedularDataList> mLifeSchedularDataLists) {
        this.mLifeSchedularDataLists = mLifeSchedularDataLists;
    }

    public SaveEvent() {
    }

    public void setmLifeSchedularDataLists(List<LifeSchedularDataList> list) {
        this.mLifeSchedularDataLists = list;
    }

    public List<LifeSchedularDataList> getList() {
        return mLifeSchedularDataLists;
    }

}
