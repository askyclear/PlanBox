package com.example.korea.planner.data;

import io.realm.RealmObject;

/**
 * Created by korea on 2017-04-24.
 */

public class GoalData extends RealmObject {
    private long id;
    private int year;
    private int month;
    private int day;
    private String goalcontent;
    private boolean succesed;

    public GoalData() {

    }

    public GoalData(long id, int year, int month, int day, String goalcontent) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.goalcontent = goalcontent;
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

    public void setGoal(String goalcontent) {
        this.goalcontent = goalcontent;
    }

    public long getId() {
        return id;
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

    public String getGoal() {
        return goalcontent;
    }

    public boolean isSuccesed() {
        return succesed;
    }

    public void setSuccesed(boolean succesed) {
        this.succesed = succesed;
    }
}
