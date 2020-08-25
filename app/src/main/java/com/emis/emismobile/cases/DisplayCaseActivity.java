package com.emis.emismobile.cases;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.emis.emismobile.R;

public class DisplayCaseActivity extends AppCompatActivity {
    private String displayedCaseId;

    private TextView bodyTextView;
    private TextView dateTextView;
    private TextView titleTextView;
    private TextView priorityTextView;
    private ScrollView scrollView;

    private CasesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CasesViewModel.class);

        Intent intent = getIntent();
        displayedCaseId = intent.getStringExtra("case_id");

        setContentView(R.layout.activity_display_case);
        setupViewComponents();
        fetchSelectedCase();
    }

    private void setupViewComponents() {
        bodyTextView = this.findViewById(R.id.case_body);
        dateTextView = this.findViewById(R.id.case_date);
        titleTextView = this.findViewById(R.id.case_title);
        priorityTextView = this.findViewById(R.id.case_priority);
        scrollView = this.findViewById(R.id.case_scrollview);
    }

    private void fetchSelectedCase() {
        viewModel.getCaseById(displayedCaseId).observe(this, this::displaySelectedCase);
    }

    private void displaySelectedCase(Case c) {
        if (c != null && c.getBody() != null && !c.getBody().isEmpty()) {
            bodyTextView.setText(Html.fromHtml(c.getBody()));
            dateTextView.setText("Opened: "+c.getDate());
            titleTextView.setText(c.getTitle());
            priorityTextView.setText("Priority: "+c.getPriority());
            setTitle("Case");
        } else {
            titleTextView.setText("This case is empty.");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
