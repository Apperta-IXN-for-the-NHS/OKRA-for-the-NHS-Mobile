package com.emis.emismobile.cases;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.emis.emismobile.R;

public class CasesFragment extends Fragment {

    private CasesViewModel casesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        casesViewModel =
                ViewModelProviders.of(this).get(CasesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cases, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        casesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}