package com.example.korea.planner.View.MemoAdd;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.korea.planner.R;
import com.example.korea.planner.data.MemoData;
import com.example.korea.planner.util.BusProvider;
import com.example.korea.planner.util.MemoAddEvent;
import com.example.korea.planner.util.MemoUpdateEvent;
import com.jrummyapps.android.colorpicker.ColorPickerDialog;
import com.jrummyapps.android.colorpicker.ColorPickerDialogListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by korea on 2017-04-26.
 */

public class MemoAddActivity extends AppCompatActivity implements View.OnClickListener, ColorPickerDialogListener {
    @BindView(R.id.memo_add_toolbar_back)
    ImageButton memoAddToolbarBack;
    @BindView(R.id.memo_add_save)
    ImageButton memoAddSave;
    @BindView(R.id.memo_add_pallete)
    ImageButton memoAddPallete;
    @BindView(R.id.memo_add_toolbar)
    Toolbar memoAddToolbar;
    @BindView(R.id.memo_add_title)
    EditText memoAddTitle;
    @BindView(R.id.memo_add_content)
    EditText memoAddContent;
    @BindView(R.id.memo_add_layout)
    LinearLayout memoAddLayout;
    private int color;
    private String title, content;
    private long id;
    private Realm realm;
    private RealmResults<MemoData> items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_add_fragment);
        ButterKnife.bind(this);
        init();
    }

    public void init() {
        setSupportActionBar(memoAddToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Intent intent = getIntent();
        id = intent.getLongExtra("id", 0);
        realm = Realm.getDefaultInstance();
        items = realm.where(MemoData.class).equalTo("id", id).findAll();
        if (items.size() != 0) {
            color = items.get(0).getColor();
            title = items.get(0).getTitle();
            content = items.get(0).getContent();

        } else {
            color = Color.parseColor("#ffffff");
            title = "";
            content = "";
        }
        memoAddLayout.setBackgroundColor(color);
        memoAddTitle.setText(title);
        memoAddContent.setText(content);

    }

    @OnClick({R.id.memo_add_toolbar_back, R.id.memo_add_save, R.id.memo_add_pallete})
    @Override
    public void onClick(View v) {
        switch ((v.getId())) {
            case R.id.memo_add_toolbar_back:
                if (memoAddTitle.getText().toString().equals(title) && memoAddContent.getText().toString().equals(content)) {
                    finish();
                } else {
                    new MaterialDialog.Builder(this)
                            .title("변경내용")
                            .content("변경내용이 있습니다. 저장하시겠습니까?")
                            .positiveText("저장")
                            .negativeText("취소")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    if (id == 0) {
                                        BusProvider.getInstance().post(new MemoAddEvent(memoAddTitle.getText().toString(),
                                                memoAddContent.getText().toString(), color));
                                    } else {
                                        BusProvider.getInstance().post(new MemoUpdateEvent(memoAddTitle.getText().toString(),
                                                memoAddContent.getText().toString(), color, id));
                                    }
                                    finish();
                                }
                            }).onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish();
                        }
                    }).build().show();
                }
                break;
            case R.id.memo_add_save:
                new MaterialDialog.Builder(this)
                        .title("메모 저장")
                        .content("저장 하시겠습니까?")
                        .positiveText("저장")
                        .negativeText("취소")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if (id == 0) {
                                    BusProvider.getInstance().post(new MemoAddEvent(memoAddTitle.getText().toString(),
                                            memoAddContent.getText().toString(), color));
                                } else {
                                    BusProvider.getInstance().post(new MemoUpdateEvent(memoAddTitle.getText().toString(),
                                            memoAddContent.getText().toString(), color, id));
                                }
                                finish();
                            }
                        }).build().show();
                break;
            case R.id.memo_add_pallete:
                ColorPickerDialog.newBuilder().setDialogTitle(R.string.color_picker).setShowAlphaSlider(true).show(this);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (memoAddTitle.getText().toString().equals(title) && memoAddContent.getText().toString().equals(content)) {
            finish();
        } else {
            new MaterialDialog.Builder(this)
                    .title("변경내용")
                    .content("변경내용이 있습니다. 저장하시겠습니까?")
                    .positiveText("저장")
                    .negativeText("취소")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (id == 0) {
                                BusProvider.getInstance().post(new MemoAddEvent(memoAddTitle.getText().toString(),
                                        memoAddContent.getText().toString(), color));
                            } else {
                                BusProvider.getInstance().post(new MemoUpdateEvent(memoAddTitle.getText().toString(),
                                        memoAddContent.getText().toString(), color, id));
                            }
                            finish();
                        }
                    }).onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    finish();
                }
            }).build().show();
        }
    }

    @Override
    public void onColorSelected(int i, @ColorInt int i1) {
        color = i1;
        memoAddLayout.setBackgroundColor(color);
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @Override
    public void onDialogDismissed(int i) {

    }
}
