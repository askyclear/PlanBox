package com.example.korea.planner.View.widget.firstwidget4x2;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.example.korea.planner.R;

/**
 * Created by korea on 2017-05-01.
 */

public class WidgetConfigActivity extends Activity {
    private int mAppwidgetId;
    private AppWidgetManager appWidgetManager;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_config);
        linearLayout = (LinearLayout) findViewById(R.id.widget_config_layout);
        linearLayout.setAlpha(0.0f);


    }
}
