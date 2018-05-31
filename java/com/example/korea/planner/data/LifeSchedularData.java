package com.example.korea.planner.data;


import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by korea on 2017-03-21.
 */

public class LifeSchedularData extends RealmObject {
    private long id;
    private String category_name;
    private String category_content;
    private RealmList<LifeSchedularDataList> list;

    public LifeSchedularData() {
    }

    public LifeSchedularData(long id, String category_name, String category_content, RealmList<LifeSchedularDataList> list) {
        this.id = id;
        this.category_name = category_name;
        this.category_content = category_content;
        this.list = list;
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setCategory_content(String category_content) {
        this.category_content = category_content;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public void setList(RealmList<LifeSchedularDataList> list) {
        this.list = list;
    }

    public String getCategory_name() {
        return this.category_name;
    }

    public String getCategory_content() {
        return this.category_content;
    }

    public long getId() {
        return this.id;
    }
    public RealmList getList() {
        return this.list;
    }
}
