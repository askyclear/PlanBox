package com.example.korea.planner.util;

/**
 * Created by korea on 2017-04-02.
 * and
 */

public class SettingChange {
    private int position, select_item_position;

    public SettingChange(int position, int select) {
        this.position = position;
        this.select_item_position = select;
    }

    public int getPosition() {
        return position;
    }

    public int getSelect_item_position() {
        return select_item_position;
    }
}
