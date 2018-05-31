package com.example.korea.planner.View.Memo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.korea.planner.R;
import com.example.korea.planner.View.Memo.Presenter.MemoContract;
import com.example.korea.planner.View.Memo.Presenter.MemoPresenter;
import com.example.korea.planner.View.MemoAdd.MemoAddActivity;
import com.example.korea.planner.adapter.MemoAdapter;
import com.example.korea.planner.data.MemoData;
import com.example.korea.planner.util.BusProvider;
import com.example.korea.planner.util.MemoAddEvent;
import com.example.korea.planner.util.MemoUpdateEvent;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by korea on 2017-04-26.
 * 전체 Memo 관리
 */

public class MemoActivity extends AppCompatActivity implements MemoContract.View, View.OnClickListener {
    ImageButton memoToolbarBack;
    @BindView(R.id.memo_toolbar_title)
    TextView memoToolbarTitle;
    @BindView(R.id.memo_toolbar)
    Toolbar memoToolbar;
    @BindView(R.id.memo_recycler)
    RecyclerView memoRecycler;
    private RealmResults<MemoData> items;
    private MemoContract.Presenter presenter;
    private LinearLayoutManager layoutManager;
    private MemoAdapter memoAdapter;
    private Realm realm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        ButterKnife.bind(this);
        init();
    }

    @Override
    public void init() {
        realm = Realm.getDefaultInstance();
        BusProvider.getInstance().register(this);
        presenter = new MemoPresenter();
        presenter.setView(this);
        //
        setSupportActionBar(memoToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //
        items = presenter.getAllDb();
        //
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        memoAdapter = new MemoAdapter(items, this);

        memoRecycler.setLayoutManager(layoutManager);
        memoRecycler.setAdapter(memoAdapter);

    }

    @OnClick({R.id.memo_toolbar_back, R.id.memo_fab_add})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.memo_toolbar_back:
                finish();
                break;
            case R.id.memo_fab_add:
                Intent intent = new Intent(this, MemoAddActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                break;
        }
    }

    @Subscribe
    public void AddLoad(MemoAddEvent event) {
        presenter.saveDb(event.getTitle(), event.getContent(), event.getColor());
        items = presenter.getAllDb();
        memoAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void UpdateLoad(MemoUpdateEvent event) {
        presenter.updateDb(event.getId(), event.getTitle(), event.getContent(), event.getColor());
        items = presenter.getAllDb();
        memoAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        BusProvider.getInstance().unregister(this);
        realm.close();
        super.onDestroy();
    }
}
