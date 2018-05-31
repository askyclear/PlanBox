package com.example.korea.planner.View.widget.firstwidget4x2;

/**
 * Created by korea on 2017-04-27.
 */

public class WidgetItem {
    private String content;
    private boolean checked = false;

    public WidgetItem(String content, boolean checked) {
        this.content = content;
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }
    public String getContent() {
        return content;
    }


}
