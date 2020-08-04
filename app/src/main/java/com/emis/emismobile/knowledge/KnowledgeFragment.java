package com.emis.emismobile.knowledge;

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

public class KnowledgeFragment extends Fragment {

    private KnowledgeViewModel knowledgeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        knowledgeViewModel = ViewModelProviders.of(this).get(KnowledgeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_knowledge, container, false);
        final TextView textView = root.findViewById(R.id.text_knowledge);
        knowledgeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}