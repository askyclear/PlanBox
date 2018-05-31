package com.example.korea.planner.data;

import io.realm.RealmObject;

/**
 * Created by korea on 2017-03-21.
 */

public class LifeSchedularDataList extends RealmObject {
    private String title;
    private String content;
    private int beforeHour;
    private int beforeMin;
    private int afterHour;
    private int afterMin;
    private long id;
    private int color;

    public LifeSchedularDataList() {
    }

    public LifeSchedularDataList(String title, String content, long id, int color,
                                 int beforeHour, int beforeMin, int afterHour, int afterMin) {
        this.title = title;
        this.content = content;
        this.id = id;
        this.color = color;
        this.beforeHour = beforeHour;
        this.beforeMin = beforeMin;
        this.afterHour = afterHour;
        this.afterMin = afterMin;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setAfterHour(int afterHour) {
        this.afterHour = afterHour;
    }

    public void setAfterMin(int afterMin) {
        this.afterMin = afterMin;
    }

    public void setBeforeHour(int beforeHour) {
        this.beforeHour = beforeHour;
    }

    public void setBeforeMin(int beforeMin) {
        this.beforeMin = beforeMin;
    }

    public int getAfterHour() {
        return afterHour;
    }

    public int getAfterMin() {
        return afterMin;
    }

    public int getBeforeHour() {
        return beforeHour;
    }

    public int getBeforeMin() {
        return beforeMin;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
