package com.example.korea.planner.util;

import com.example.korea.planner.data.LifeSchedularDataList;

import java.util.List;

/**
 * Created by korea on 2017-03-30.
 * event handler
 */

public class ChangeChekedEvent {
    private String title, content;
    private boolean isChangeCheked = false;
    private List<LifeSchedularDataList> list;

    public ChangeChekedEvent(String title, String content, List<LifeSchedularDataList> list) {
        this.title = title;
        this.content = content;
        this.list = list;
    }

    public boolean isChangeCheked() {
        return isChangeCheked;
    }

    public void setIsChangeCheked(boolean isChangeCheked) {
        this.isChangeCheked = isChangeCheked;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
