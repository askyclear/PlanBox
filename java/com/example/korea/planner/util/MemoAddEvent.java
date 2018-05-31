package com.example.korea.planner.util;

/**
 * Created by korea on 2017-04-26.
 */

public class MemoAddEvent {
    private String content;
    private String title;
    private int color;

    public MemoAddEvent(String title, String content, int color) {
        this.content = content;
        this.title = title;
        this.color = color;
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
}
