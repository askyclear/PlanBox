package com.example.korea.planner.util;

/**
 * Created by korea on 2017-04-26.
 */

public class MemoUpdateEvent {
    private String content;
    private String title;
    private int color;
    private long id;

    public MemoUpdateEvent(String title, String content, int color, long id) {
        this.content = content;
        this.title = title;
        this.color = color;
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
