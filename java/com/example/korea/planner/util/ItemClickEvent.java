package com.example.korea.planner.util;

import com.example.korea.planner.data.LifeSchedularDataList;

import java.util.List;

/**
 * Created by korea on 2017-03-28.
 */

public class ItemClickEvent {
    private List<LifeSchedularDataList> list;
    private int position;
    private int event;

    //event 가 1 이면 remove 2면 업데이트
    public ItemClickEvent(List<LifeSchedularDataList> list, int position, int event) {
        this.list = list;
        this.position = position;
        this.event = event;
    }

    public List<LifeSchedularDataList> getList() {
        return list;
    }

    public int getPosition() {
        return position;
    }

    public int getEvent() {
        return event;
    }
}
