package org.apperta.okramobile.contact;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import org.apperta.okramobile.R;

import org.apperta.okramobile.LoadingDialog;
import org.apperta.okramobile.LoadingWebClient;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.show(this);

        WebView webview = new WebView(this);
        webview.setWebViewClient(new LoadingWebClient(loadingDialog));
        webview.getSettings().setJavaScriptEnabled(true);

        try {
            webview.loadUrl(getString(R.string.chat_url));
        } catch (Exception ex) {
            return;
        }

        setContentView(webview);
    }
}