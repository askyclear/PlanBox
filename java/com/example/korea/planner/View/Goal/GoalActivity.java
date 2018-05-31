package com.example.korea.planner.View.Goal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.korea.planner.R;
import com.example.korea.planner.View.Goal.Presenter.GoalContract;
import com.example.korea.planner.View.Goal.Presenter.GoalPresenter;
import com.example.korea.planner.adapter.GoalAdapter;
import com.example.korea.planner.data.GoalData;
import com.example.korea.planner.util.BusProvider;
import com.example.korea.planner.util.ProgressEvent;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by korea on 2017-04-26.
 */

public class GoalActivity extends AppCompatActivity implements GoalContract.View, View.OnClickListener {

    @BindView(R.id.goal_toolbar_back)
    ImageButton goalToolbarBack;
    @BindView(R.id.goal_toolbar_title)
    TextView goalToolbarTitle;
    @BindView(R.id.goal_toolbar)
    Toolbar goalToolbar;
    @BindView(R.id.goal_progress)
    ProgressBar goalProgress;
    @BindView(R.id.goal_recycler)
    RecyclerView goalRecycler;
    @BindView(R.id.goal_rule)
    TextView goalRule;
    @BindView(R.id.goal_percent)
    TextView goalPercent;
    private RealmResults<GoalData> items;
    private GoalContract.Presenter presenter;
    private GoalAdapter mAdapter;
    private Realm realm;
    private String goalRule_text = "목표달성률";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        ButterKnife.bind(this);
        init();
    }

    @Override
    public void init() {
        BusProvider.getInstance().register(this);
        realm = Realm.getDefaultInstance();
        presenter = new GoalPresenter();
        presenter.setView(this);
        items = presenter.getAddDb();
        //
        setSupportActionBar(goalToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        mAdapter = new GoalAdapter(items, this, 0);
        goalRecycler.setLayoutManager(layoutManager);
        goalRecycler.setAdapter(mAdapter);
        goalRule.setText(goalRule_text);
        goalPercent.setText(presenter.getGoalProgress() + "%");
    }

    @Subscribe
    public void ProgressLoad(ProgressEvent event) {
        goalPercent.setText(presenter.getGoalProgress() + "%");
        goalProgress.setProgress(presenter.getGoalProgress());
    }

    @Override
    protected void onDestroy() {
        BusProvider.getInstance().unregister(this);
        realm.close();
        super.onDestroy();
    }

    @OnClick({R.id.goal_toolbar_back})
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.goal_toolbar_back) {
            finish();
        }
    }
}
