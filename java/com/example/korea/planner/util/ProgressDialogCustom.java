package com.example.korea.planner.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * Created by korea on 2018-03-05.
 */

public class ProgressDialogCustom extends AsyncTask<Void, Void, Void> {
    private ProgressDialog asyncDialog;
    private String msg = null;


    public ProgressDialogCustom(Activity activity, String msg) {
        asyncDialog = new ProgressDialog(activity);
        this.msg = msg;
    }

    @Override
    protected void onPreExecute() {
        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        asyncDialog.setMessage(msg);

        asyncDialog.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (asyncDialog.isShowing()) {
            asyncDialog.dismiss();
        }

        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        int i = 0;
        while (!isCancelled()) {
            if (i > 100) {
                i = 0;
            }
            i += 1;
            asyncDialog.setProgress(i);
        }
        return null;
    }

    @Override
    protected void onCancelled() {
        if (asyncDialog.isShowing()) {
            asyncDialog.dismiss();
        }
        super.onCancelled();
    }
}
