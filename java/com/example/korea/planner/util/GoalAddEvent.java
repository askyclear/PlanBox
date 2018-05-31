package com.example.korea.planner.util;

/**
 * Created by korea on 2017-04-26.
 */

public class GoalAddEvent {
    private String content;

    public GoalAddEvent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
