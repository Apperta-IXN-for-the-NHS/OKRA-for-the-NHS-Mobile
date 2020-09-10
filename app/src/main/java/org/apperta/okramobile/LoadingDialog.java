package org.apperta.okramobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

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
