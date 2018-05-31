package com.example.korea.planner.data;

import io.realm.RealmObject;

/**
 * Created by korea on 2017-04-24.
 */

public class AnniversaryData extends RealmObject {
    private long id;
    private int year;
    private int month;
    private int day;
    private String title;
    private String content;

    public AnniversaryData() {

    }

    public AnniversaryData(long id, int year, int month, int day, String title, String content) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.title = title;
        this.content = content;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getYear() {
        return year;
    }

    public long getId() {
        return id;
    }
}
