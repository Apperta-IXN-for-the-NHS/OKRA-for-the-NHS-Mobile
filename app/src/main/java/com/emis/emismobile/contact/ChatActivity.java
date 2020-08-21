package com.emis.emismobile.contact;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        WebView webview = new WebView(this);
        webview.getSettings().setJavaScriptEnabled(true);
        try {
            webview.loadUrl("https://emis-dummy-chatbot.herokuapp.com/");
        } catch (Exception ex) {
            return;
        }

        setContentView(webview);
    }
}