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

import java.util.ArrayList;
import java.util.List;

public class KnowledgeFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Article> articles = new ArrayList<>();
    private KnowledgeAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_knowledge, container, false);
        recyclerView = root.findViewById(R.id.rvKnowledge);

        setUpRecyclerView();
        setUpDynamicFetchOnScroll();

        fetchAndDisplayArticles(10, 0);

        return root;
    }

    private void setUpRecyclerView() {
        adapter = new KnowledgeAdapter(articles);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    private void setUpDynamicFetchOnScroll() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    loadMoreArticles(5);
                }
            }
        });
    }

    private void fetchAndDisplayArticles(int limit, int start) {
        KnowledgeViewModel viewModel = new ViewModelProvider(this).get(KnowledgeViewModel.class);
        viewModel.getArticles(limit, start).observe(getViewLifecycleOwner(), this::updateArticleList);
    }

    private void updateArticleList(List<Article> newArticles) {
        articles.addAll(newArticles);
        adapter.notifyDataSetChanged();
    }

    private void loadMoreArticles(int limit) {
        fetchAndDisplayArticles(limit, articles.size());
    }
}