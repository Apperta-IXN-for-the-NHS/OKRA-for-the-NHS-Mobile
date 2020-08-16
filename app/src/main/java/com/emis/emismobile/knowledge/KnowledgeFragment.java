package com.emis.emismobile.knowledge;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emis.emismobile.R;

import java.util.List;

public class KnowledgeFragment extends Fragment {

    private RecyclerView rvKnowledge;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_knowledge, container, false);
        rvKnowledge = root.findViewById(R.id.rvKnowledge);

        fetchAndDisplayArticles();

        return root;
    }

    private void fetchAndDisplayArticles() {
        KnowledgeViewModel viewModel = new ViewModelProvider(this).get(KnowledgeViewModel.class);
        viewModel.getAllArticles().observe(getViewLifecycleOwner(), this::displayArticleList);
    }

    private void displayArticleList(List<Article> articles) {
        KnowledgeAdapter adapter = new KnowledgeAdapter(articles);
        rvKnowledge.setLayoutManager(new LinearLayoutManager(rvKnowledge.getContext()));
        rvKnowledge.setAdapter(adapter);
    }
}