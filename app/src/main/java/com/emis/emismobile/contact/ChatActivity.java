package com.emis.emismobile.contact;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.emis.emismobile.R;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
//
//        setContentView(R.layout.activity_chat);
//
//        WebView myWebView = findViewById(R.id.chat_web_view);
//        myWebView.loadUrl("https://emis-dummy-chatbot.herokuapp.com/");
        WebView webview = new WebView(this);
        webview.getSettings().setJavaScriptEnabled(true);
        try {
            webview.loadUrl("https://emis-dummy-chatbot.herokuapp.com/");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        setContentView(webview);
    }
}