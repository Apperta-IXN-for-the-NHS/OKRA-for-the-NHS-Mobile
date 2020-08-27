package com.emis.emismobile.cases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class CasesFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Case> cases = new ArrayList<>();
    private CasesAdapter adapter;
    private TextInputLayout searchBar;
    private Button searchButton;
    private FloatingActionButton newCaseButton;
    private LinearLayout linearLayout;
    private LinearLayout searchLayout;
    private String query;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cases, container, false);
        recyclerView = root.findViewById(R.id.rvCases);
        searchBar = root.findViewById(R.id.search_bar);
        searchButton = root.findViewById(R.id.search_button);
        linearLayout = root.findViewById(R.id.linear_layout);
        searchLayout = root.findViewById(R.id.search_layout);
        newCaseButton = root.findViewById(R.id.new_case_button);

        newCaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitCaseForm();
            }
        });

        setUpRecyclerView();
        setUpDynamicFetchOnScroll();
        setUpSearchBar();
        query = "";

        fetchAndDisplayCases(10, 0);

        return root;
    }

    private void submitCaseForm(){
        Intent intent = new Intent(this.getContext(), NewCaseActivity.class);
        this.getContext().startActivity(intent);
    }

    private void setUpSearchBar() {
        searchButton.setOnClickListener(v -> {
            query = getSearchBarText();
            searchCases();
        });

        searchBar.getEditText().setOnKeyListener((v, keyCode, event) -> {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                query = getSearchBarText();
                searchCases();
            }
            return false;
        });

    }

    public void searchCases() {
        cases.clear();
        fetchAndDisplayCases(5, 0);
        //close keyboard
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(linearLayout.getWindowToken(), 0);
    }

    private String getSearchBarText() {
        return searchBar.getEditText().getText().toString();
    }

    private void setUpRecyclerView() {
        adapter = new CasesAdapter(cases);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpDynamicFetchOnScroll() {
        recyclerView.setOnTouchListener((v, event) -> {
            if (!recyclerView.canScrollVertically(1)) {
                loadMoreCases(5);
            }
            return false;
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                double diff = recyclerView.getScrollY() - dy;
                diff = Math.abs(diff);

                if (recyclerView.getScrollY() > dy) {
                    //scroll up
                    if (diff > 100) {
                        searchLayout.setVisibility(View.VISIBLE);
                    }
                } else if (recyclerView.getScrollY() < dy) {
                    //scroll down
                    if (diff > 100) {
                        searchLayout.setVisibility(View.GONE);
                    }
                }

            }
        });
    }

    private void fetchAndDisplayCases(int limit, int start) {
        CasesViewModel viewModel = new ViewModelProvider(this).get(CasesViewModel.class);
        viewModel.getCases(query, limit, start).observe(getViewLifecycleOwner(), this::updateCasesList);
    }

    private void updateCasesList(List<Case> newCases) {
        cases.addAll(newCases);
        adapter.notifyDataSetChanged();
    }

    private void loadMoreCases(int limit) {
        fetchAndDisplayCases(limit, cases.size());
    }
}