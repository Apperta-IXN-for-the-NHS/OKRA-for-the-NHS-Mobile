package com.emis.emismobile.knowledge;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emis.emismobile.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Article> articles = new ArrayList<>();
    private KnowledgeAdapter adapter;
    private TextInputLayout searchBar;
    private Button searchButton;
    private LinearLayout linearLayout;
    private LinearLayout searchLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_knowledge, container, false);
        recyclerView = root.findViewById(R.id.rvKnowledge);
        searchBar = root.findViewById(R.id.search_bar);
        searchButton = root.findViewById(R.id.search_button);
        linearLayout = root.findViewById(R.id.linear_layout);
        searchLayout = root.findViewById(R.id.search_layout);

        setUpRecyclerView();
        setUpDynamicFetchOnScroll();
        String query = getSearchBarText();
        fetchAndDisplayArticles(query, 10, 0);

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchArticles();
            }
        });

        searchBar.getEditText().setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    searchArticles();
                }
                return false;
            }
        });

        return root;
    }

    public void searchArticles(){
        articles.clear();
        fetchAndDisplayArticles(getSearchBarText(), 5,0);
        //close keyboard
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(linearLayout.getWindowToken(), 0);
    }

    private String getSearchBarText(){
        return searchBar.getEditText().getText().toString();
    }

    private void setUpRecyclerView() {
        adapter = new KnowledgeAdapter(articles);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    private void setUpDynamicFetchOnScroll() {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!recyclerView.canScrollVertically(1)) {
                    String query = getSearchBarText();
                    loadMoreArticles(query, 5);

                }
                return false;
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            float y = 0;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                double diff = recyclerView.getScrollY() - dy;
                diff = Math.abs(diff);
                System.out.println(diff);

                if (recyclerView.getScrollY() > dy) {
                    //scroll up
                    if(diff > 100){
                        searchLayout.setVisibility(View.VISIBLE);
                    }
                } else if(recyclerView.getScrollY() < dy){
                    //scroll down
                    if(diff > 100){
                        searchLayout.setVisibility(View.GONE);
                    }
                }

            }
        });
    }

    private void fetchAndDisplayArticles(String query, int limit, int start) {
        System.out.println("fet");
        KnowledgeViewModel viewModel = new ViewModelProvider(this).get(KnowledgeViewModel.class);
        viewModel.getArticles(query, limit, start).observe(getViewLifecycleOwner(), this::updateArticleList);
    }

    private void updateArticleList(List<Article> newArticles) {
        articles.addAll(newArticles);
        adapter.notifyDataSetChanged();
    }

    private void loadMoreArticles(String query, int limit) {
        fetchAndDisplayArticles(query, limit, articles.size());
    }
}