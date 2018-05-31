package com.example.korea.planner.View.Info;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.korea.planner.R;
import com.example.korea.planner.util.RealmBackupRestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by korea on 2017-04-26.
 */

public class InfoActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.info_toolbar_back)
    ImageButton infoToolbarBack;
    @BindView(R.id.info_toolbar_title)
    TextView infoToolbarTitle;
    @BindView(R.id.info_toolbar)
    Toolbar infoToolbar;
    @BindView(R.id.info_version)
    TextView infoVersion;
    @BindView(R.id.info_creator)
    TextView infoCreator;
    @BindView(R.id.info_backup)
    Button infoBackup;
    @BindView(R.id.info_privacy)
    TextView infoPrivacy;
    @BindView(R.id.info_restore)
    Button infoRestore;
    @BindView(R.id.info_link_blog)
    TextView infoLinkBlog;
    RealmBackupRestore backup;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);
        init();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                break;
            case 2:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {

                }
                break;
            default:
                break;
        }
    }

    public void init() {
        setSupportActionBar(infoToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        try {
            PackageInfo i = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            infoVersion.setText("Ver " + i.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        infoCreator.setText("이메일 : askyclear@naver.com");

        infoPrivacy.setText("http://blog.naver.com/askyclear/221048046290");
        infoPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://blog.naver.com/askyclear/221048046290"));
                startActivity(intent);
            }
        });
        infoLinkBlog.setText("여기를 눌러주세요.");
        infoLinkBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://blog.naver.com/askyclear/221225506484"));
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.info_toolbar_back, R.id.info_backup, R.id.info_restore})
    @Override
    public void onClick(View v) {
        backup = new RealmBackupRestore(this);
        switch (v.getId()) {
            case R.id.info_toolbar_back:
                finish();
                break;
            case R.id.info_backup:
                backup.backup();
                break;
            case R.id.info_restore:
                backup.restore();
        }
    }

}
