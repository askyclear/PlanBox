package com.example.korea.planner.View.LifeSchedular.presenter;

import android.support.v7.app.AppCompatActivity;

import com.example.korea.planner.View.LifeSchedular.LifeSchedularActivity;
import com.example.korea.planner.data.LifeSchedularDataList;
import com.example.korea.planner.util.ItemClickEvent;

import java.util.List;

/**
 * Created by korea on 2017-03-15.
 */

public interface LifeSchdularContract {
    interface View{
        //set View
        void init();
    }
    interface Presenter{
        //set Presenter
        void setView(AppCompatActivity view, long id);

        void addRealm();

        void matedrialDialog(int n);

        void setItemClickEvent(ItemClickEvent itemClickEvent);
    }
}
