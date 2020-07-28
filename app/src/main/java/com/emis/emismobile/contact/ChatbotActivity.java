package com.emis.emismobile.contact;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.emis.emismobile.R;

public class ChatbotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_chatbot);

        WebView myWebView = findViewById(R.id.chat_webview);
        myWebView.loadUrl("http://www.example.com");
    }
}
