package com.example.korea.planner.View.LifeSchedular;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.korea.planner.R;
import com.example.korea.planner.View.LifeSchedular.presenter.LifeSchdularContract;
import com.example.korea.planner.View.LifeSchedular.presenter.LifeSchedularPresenter;
import com.example.korea.planner.adapter.LifeTabPagerAdapter;
import com.example.korea.planner.data.LifeSchedularData;
import com.example.korea.planner.data.LifeSchedularDataList;
import com.example.korea.planner.util.BusProvider;
import com.example.korea.planner.util.ChangeChekedEvent;
import com.example.korea.planner.util.FabShowing;
import com.example.korea.planner.util.ItemClickEvent;
import com.github.clans.fab.FloatingActionButton;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by korea on 2017-03-15.
 *
 */

public class LifeSchedularActivity extends AppCompatActivity implements LifeSchdularContract.View, View.OnClickListener, TabLayout.OnTabSelectedListener {

    @BindView(R.id.toolbar_back)
    ImageButton toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_add)
    ImageButton toolbarAdd;
    @BindView(R.id.life_toolbar)
    Toolbar lifeToolbar;
    @BindView(R.id.life_pager)
    ViewPager lifePager;
    @BindView(R.id.life_tab)
    TabLayout lifeTab;
    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;
    private LifeSchdularContract.Presenter presenter;
    private LifeTabPagerAdapter pagerAdapter;
    private Realm realm;
    private RealmResults<LifeSchedularData> item;
    private List<LifeSchedularDataList> list;
    private String title, content;
    private long id = 0;
    private boolean isChangeCheked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifeschedular);
        BusProvider.getInstance().register(this);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            id = intent.getExtras().getLong("ItemPosition");
        }
        init();
    }

    @Override
    protected void onDestroy() {
        BusProvider.getInstance().unregister(this);
        realm.close();
        super.onDestroy();
    }

    @Override
    public void init() {
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        list = new ArrayList<>();
        title = "";
        content = "";
        if (id != 0) {
            item = realm.where(LifeSchedularData.class).equalTo("id", id).findAll();
            list.addAll(item.get(0).getList());
            title = item.get(0).getCategory_name();
            content = item.get(0).getCategory_content();
        }
        //탭 추가
        lifeTab.addTab(lifeTab.newTab().setText("기본"), 0);
        lifeTab.addTab(lifeTab.newTab().setText("목록"), 1);
        lifeTab.setTabGravity(TabLayout.GRAVITY_FILL);
        fabAdd.hide(true);
        //뷰페이저 어뎁터
        pagerAdapter = new LifeTabPagerAdapter(getSupportFragmentManager(), lifeTab.getTabCount(), id);

        lifePager.setAdapter(pagerAdapter);
        //뷰페이저 바뀜 리스너를 텝페이지 리스너로 함.
        lifePager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(lifeTab));

        lifeTab.addOnTabSelectedListener(this);
        //set presenter;
        presenter = new LifeSchedularPresenter();
        setSupportActionBar(lifeToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        presenter.setView(this, id);
    }

    @Override
    public void onBackPressed() {
        ChangeChekedEvent event = new ChangeChekedEvent(title, content, list);
        BusProvider.getInstance().post(event);
        isChangeCheked = event.isChangeCheked();

        if (isChangeCheked) {
            new MaterialDialog.Builder(this)
                    .content("변경사항이 있습니다. 저장하시겠습니까?")
                    .positiveText("저장")
                    .negativeText("아니오")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            presenter.addRealm();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish();
                        }
                    })
                    .build().show();
        } else {
            super.onBackPressed();
        }

    }

    @OnClick({R.id.toolbar_back, R.id.toolbar_add, R.id.fab_add})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_add:
                new MaterialDialog.Builder(this)
                        .title("저장")
                        .content("저장 하시겠습니까?")
                        .positiveText("저장")
                        .negativeText("취소")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                presenter.addRealm();
                            }
                        }).build().show();
                break;
            case R.id.toolbar_back:
                //변경사항 확인
                ChangeChekedEvent event = new ChangeChekedEvent(title, content, list);
                BusProvider.getInstance().post(event);
                isChangeCheked = event.isChangeCheked();

                if (isChangeCheked) {
                    new MaterialDialog.Builder(this)
                            .content("변경사항이 있습니다. 저장하시겠습니까?")
                            .positiveText("저장")
                            .negativeText("아니오")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    presenter.addRealm();
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    finish();
                                }
                            })
                            .build().show();
                } else {
                    finish();
                }
                break;
            case R.id.fab_add:
                Random mRand = new Random();
                presenter.matedrialDialog(mRand.nextInt(18));
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                fabAdd.hide(true);
                break;
            case 1:
                fabAdd.show(true);
                break;
            default:
                break;
        }
        lifePager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Subscribe
    public void FinishLoad(ItemClickEvent itemClickEvent) {
        //이 이벤트가 호출되었다는 것은 리스트에 수정 or 삭제가 있었다는 뜻이다.
        isChangeCheked = true;
        presenter.setItemClickEvent(itemClickEvent);
    }

    @Subscribe
    public void FinishLoad(FabShowing fabShowing) {
        if (fabShowing.getShowed() == 1) {
            fabAdd.show(true);
        } else {
            fabAdd.hide(true);
        }
    }
}
