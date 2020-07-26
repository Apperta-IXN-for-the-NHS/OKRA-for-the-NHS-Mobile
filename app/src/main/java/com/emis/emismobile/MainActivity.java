package com.emis.emismobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.emis.emismobile.contact.ContactUsActivity;
import com.emis.emismobile.knowledge.KnowledgeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openKnowledge(View view) {
        Intent intent = new Intent(this, KnowledgeActivity.class);
        startActivity(intent);
    }

    public void openContactUs(View view) {
        Intent intent = new Intent(this, ContactUsActivity.class);
        startActivity(intent);
    }
}
