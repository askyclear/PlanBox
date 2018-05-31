package com.example.korea.planner.util;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.korea.planner.View.widget.firstwidget4x2.PlanObjectWidget;
import com.example.korea.planner.View.widget.secondwidget4x4.SecondWidget4x4;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import io.realm.Realm;
import io.realm.internal.RealmProxyMediator;

import static android.content.ContentValues.TAG;

/**
 * Created by korea on 2017-08-22.
 */

public class RealmBackupRestore {
    private static File EXPORT_REALM_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    private static String EXPORT_REALM_FILE_NAME = "planbox.realm";
    private static String IMPORT_REALM_FILE_NAME = "default.realm";
    private Activity activity;
    private Realm realm;
    private File exportRealmFile;
    static final String ACTION_UPDATE_CLICK = "UPDATE_CLICK";
    static final String APP_WIDGET_ID = "WIDGET_ID";

    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static String restoreFilePath = EXPORT_REALM_PATH + "/" + EXPORT_REALM_FILE_NAME;

    public RealmBackupRestore(Activity activity) {
        this.activity = activity;
    }

    public void backup() {
        realm = Realm.getDefaultInstance();
        // First check if we have storage permissions
        if (checkStoragePermissions(activity)) {

            Log.d(TAG, "Realm DB Path = " + realm.getPath());


            if (!EXPORT_REALM_PATH.exists()) {
                EXPORT_REALM_PATH.mkdirs();
            }
            // create a backup file
            exportRealmFile = new File(EXPORT_REALM_PATH, EXPORT_REALM_FILE_NAME);

            // if backup file already exists, delete it
            if (exportRealmFile.exists())
                exportRealmFile.delete();

            // copy current realm to backup file
            ProgressDialogCustom task = new ProgressDialogCustom(activity, "백업 중입니다.");
            task.execute();
            realm.writeCopyTo(exportRealmFile);
            task.cancel(true);

            String msg = "File exported to Path: " + EXPORT_REALM_PATH + "/" + EXPORT_REALM_FILE_NAME;
            Toast.makeText(activity.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            Log.d(TAG, msg);
        }
        realm.close();
    }

    public void restore() {

        if (checkStoragePermissions(activity)) {
            //Restore


            Log.d(TAG, "oldFilePath = " + restoreFilePath);

            copyBundledRealmFile();

            Log.d(TAG, "Data restore is done");
        }
        //Realm.init(activity);
        //app widget에게 알리기
        AppWidgetManager appWidgetManger = AppWidgetManager.getInstance(activity);
        int[] appwidgetIds = appWidgetManger.getAppWidgetIds(new ComponentName(activity, PlanObjectWidget.class));
        for (int i = 0; i < appwidgetIds.length; i++) {
            Intent intent2 = new Intent(activity, PlanObjectWidget.class);
            intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appwidgetIds[i]);
            intent2.setAction(ACTION_UPDATE_CLICK);
            activity.sendBroadcast(intent2);
        }
        int[] appwidgetIds2 = appWidgetManger.getAppWidgetIds(new ComponentName(activity, SecondWidget4x4.class));
        for (int i = 0; i < appwidgetIds2.length; i++) {
            Intent intent2 = new Intent(activity, SecondWidget4x4.class);
            intent2.putExtra(APP_WIDGET_ID, appwidgetIds2[i]);
            intent2.setAction(ACTION_UPDATE_CLICK);
            activity.sendBroadcast(intent2);
        }
        Realm.init(activity.getApplicationContext());

    }

    private String copyBundledRealmFile() {
        ProgressDialogCustom progressDialogCustom = new ProgressDialogCustom(activity, "복원중입니다.");
        progressDialogCustom.execute();
        realm = Realm.getDefaultInstance();
        File file = null;
        try {
            file = new File(activity.getApplicationContext().getFilesDir(), IMPORT_REALM_FILE_NAME);
            File restoreFile = new File(restoreFilePath);
            if (restoreFile.exists()) {
                if (!realm.isClosed()) {
                    realm.close();
                }
                FileOutputStream outputStream = new FileOutputStream(file);
                FileInputStream inputStream = new FileInputStream(restoreFile);

                byte[] buf = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, bytesRead);
                }
                outputStream.flush();
                outputStream.close();
                Toast.makeText(activity.getApplicationContext(), "복구가 완료되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity.getApplicationContext(), "백업 파일이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(activity.getApplicationContext(), "복구에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            if (!realm.isClosed()) {
                realm.close();
            }
            progressDialogCustom.cancel(true);
            return file.getAbsolutePath();
        }


    }

    private boolean checkStoragePermissions(Activity activity) {
        // Check if we have write permission
        if (Build.VERSION.SDK_INT >= 23) {
            int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int permission2 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, 1);
            }
            if (permission2 == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, 2);
            }
            if (permission == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else
            return true;
    }

    private String dbPath() {
        return realm.getPath();
    }
}
