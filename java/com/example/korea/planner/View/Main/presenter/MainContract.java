package com.example.korea.planner.View.Main.presenter;

/**
 * Created by korea on 2017-03-14.
 */

public interface MainContract {
    interface View{
        //View method
        void init();
    }
    interface Presenter{
        //Presenter method
        void setView(View view);
    }
}
