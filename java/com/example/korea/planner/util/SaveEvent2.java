package com.example.korea.planner.util;

/**
 * Created by korea on 2017-03-23.
 */

public class SaveEvent2 {
    private String category_name, category_content;

    public SaveEvent2(String category_name, String category_content) {
        this.category_content = category_content;
        this.category_name = category_name;
    }

    public SaveEvent2() {
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public void setCategory_content(String category_content) {
        this.category_content = category_content;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getCategory_content() {
        return category_content;
    }
}
