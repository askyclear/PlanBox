package com.example.korea.planner.data;

import io.realm.RealmObject;

/**
 * Created by korea on 2017-04-24.
 */

public class MemoData extends RealmObject {
    private long id;
    private int year;
    private int month;
    private int day;
    private String title;
    private String content;
    private int color;

    public MemoData() {

    }

    public MemoData(long id, int year, int month, int day, String title, String content, int color) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.title = title;
        this.content = content;
        this.color = color;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setColor(int color) {
        this.color = color;
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

    public int getColor() {
        return color;
    }

}
