package com.emis.emismobile.contact;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.emis.emismobile.LoadingDialog;
import com.emis.emismobile.LoadingWebClient;

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
            webview.loadUrl("https://emis-dummy-chatbot.herokuapp.com/");
        } catch (Exception ex) {
            return;
        }

        setContentView(webview);
    }
}