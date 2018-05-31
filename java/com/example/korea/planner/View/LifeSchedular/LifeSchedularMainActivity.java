package com.example.korea.planner.View.LifeSchedular;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.korea.planner.R;
import com.example.korea.planner.View.LifeSchedular.presenter.LifeSchdularContract;
import com.example.korea.planner.View.LifeSchedular.setting.LifeSettingActivity;
import com.example.korea.planner.adapter.LifeSchedularMainAdapter;
import com.example.korea.planner.data.LifeSchedularData;
import com.github.clans.fab.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by korea on 2017-03-24.
 *
 */

public class LifeSchedularMainActivity extends AppCompatActivity implements View.OnClickListener, LifeSchdularContract.View {
    @BindView(R.id.life_main_toolbar)
    Toolbar lifeMainToolbar;
    @BindView(R.id.life_main_recycler)
    RecyclerView lifeMainRecycler;
    @BindView(R.id.life_main_fab)
    FloatingActionButton lifeMainFab;
    Realm realm;
    RealmResults<LifeSchedularData> data;
    private GridLayoutManager manager;
    private LifeSchedularMainAdapter mAdapter;


    @Override
    protected void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
        setContentView(R.layout.activity_lifeschedularmain);
        init();
    }

    public void init() {
        ButterKnife.bind(this);
        setSupportActionBar(lifeMainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Realm.init(this.getApplicationContext());
        realm = Realm.getDefaultInstance();
        //realm.beginTransaction();
        data = realm.where(LifeSchedularData.class).findAll();
        manager = new GridLayoutManager(this, 4);
        lifeMainRecycler.setLayoutManager(manager);

        mAdapter = new LifeSchedularMainAdapter(data, this);
        lifeMainRecycler.setAdapter(mAdapter);

        //realm.commitTransaction();

    }

    @OnClick({R.id.life_main_fab, R.id.life_main_back, R.id.life_main_setting})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.life_main_fab:
                Intent intent = new Intent(v.getContext(), LifeSchedularActivity.class);
                startActivity(intent);
                break;
            case R.id.life_main_back:
                finish();
                break;
            case R.id.life_main_setting:
                Intent intent1 = new Intent(v.getContext(), LifeSettingActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

}
