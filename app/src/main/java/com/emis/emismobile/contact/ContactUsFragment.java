package com.emis.emismobile.contact;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.emis.emismobile.R;
import com.google.android.material.card.MaterialCardView;

public class ContactUsFragment extends Fragment {

    private static final float BUTTON_ELEVATION = 12;
    private MaterialCardView chatButton, phoneButton, contactDetails;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);

        contactDetails = view.findViewById(R.id.contactDetailsCard);
        chatButton = view.findViewById(R.id.chatButton);
        phoneButton = view.findViewById(R.id.phoneButton);

        setupContactDetailsCard();
        setupChatButton();
        setupPhoneButton();

        return view;
    }

    private void setupContactDetailsCard() {
        contactDetails.setCardElevation(0);
    }

    private void setupPhoneButton() {
        phoneButton.setCardElevation(BUTTON_ELEVATION);

        phoneButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            String supportPhoneNumber = getString(R.string.supportContactNumber);
            intent.setData(Uri.parse(supportPhoneNumber));
            startActivity(intent);
        });
    }

    private void setupChatButton() {
        chatButton.setCardElevation(BUTTON_ELEVATION);

        chatButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            startActivity(intent);
        });
    }

}