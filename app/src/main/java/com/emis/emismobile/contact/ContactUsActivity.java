package com.emis.emismobile.contact;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.emis.emismobile.R;

public class ContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
    }

    public void openChatbot(View view) {
        Intent intent = new Intent(this, ChatbotActivity.class);
        startActivity(intent);
    }

    public void callSupportNumber(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        String supportPhoneNumber = getString(R.string.supportContactNumber);
        intent.setData(Uri.parse(supportPhoneNumber));
        startActivity(intent);
    }
}
