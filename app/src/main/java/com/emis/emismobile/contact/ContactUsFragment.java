package com.emis.emismobile.contact;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.emis.emismobile.R;

public class ContactUsFragment extends Fragment {

    private ContactUsViewModel contactUsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        contactUsViewModel = ViewModelProviders.of(this).get(ContactUsViewModel.class);
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);

        setupChatOnClickListener(view);
        setupPhoneOnClickListener(view);

        return view;
    }

    private void setupPhoneOnClickListener(View view) {
        Button phoneButton = view.findViewById(R.id.open_call_btn);

        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String supportPhoneNumber = getString(R.string.supportContactNumber);
                intent.setData(Uri.parse(supportPhoneNumber));
                startActivity(intent);
            }
        });
    }

    private void setupChatOnClickListener(View view) {
        Button chatButton = view.findViewById(R.id.open_chat_btn);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });
    }

}