package com.emis.emismobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.widget.ProgressBar;

public class LoadingDialog {
    private AlertDialog dialog;

    public void show(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.TransparentDialog);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));
        dialog = builder.create();
        dialog.show();
    }

    public void hide() {
        dialog.dismiss();
    }
}
