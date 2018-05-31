package com.example.korea.planner.util;

/**
 * Created by korea on 2017-04-25.
 */

public class CalendarDeleteEvent {
    private long id;

    public CalendarDeleteEvent(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
