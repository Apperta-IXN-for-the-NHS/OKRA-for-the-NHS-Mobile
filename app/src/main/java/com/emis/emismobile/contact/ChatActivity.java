package com.emis.emismobile.contact;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.emis.emismobile.R;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_chat);

        WebView myWebView = findViewById(R.id.chat_web_view);
        myWebView.loadUrl("http://www.example.com");
    }
}