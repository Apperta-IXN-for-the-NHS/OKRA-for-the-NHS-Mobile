package com.emis.emismobile.cases;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.emis.emismobile.R;
import com.google.android.material.textfield.TextInputLayout;

public class NewCaseActivity extends AppCompatActivity {

    private CasesViewModel viewModel;
    private Button submitCaseButton;
    private TextInputLayout titleField;
    private TextInputLayout bodyField;
    private TextInputLayout priorityField;
    private AlertDialog.Builder emptyFieldsDialog;
    private AlertDialog.Builder createSuccessDialog;
    private AlertDialog.Builder createFailureDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CasesViewModel.class);

        Intent intent = getIntent();
        setContentView(R.layout.activity_new_case);
        setTitle("Submit a New Case");
        setUpPrioritiesMenu();

        titleField = this.findViewById(R.id.title_field);
        bodyField = this.findViewById(R.id.body_field);
        priorityField = this.findViewById(R.id.priorities_field);

        submitCaseButton = this.findViewById(R.id.submit_case_button);
        submitCaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitCase();
            }
        });

        buildEmptyFieldsDialog();
        buildCreateSuccessDialog();
        buildCreateFailureDialog();
    }

    public void setUpPrioritiesMenu() {
        String[] priorityOptions = new String[]{"Critical", "High", "Moderate", "Low"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, priorityOptions);
        AutoCompleteTextView dropdown = this.findViewById(R.id.priorities_dropdown);
        dropdown.setAdapter(adapter);
        dropdown.setText("Critical", false);
    }

    public void submitCase() {
        String title = titleField.getEditText().getText().toString().trim();
        String body = bodyField.getEditText().getText().toString().trim();
        String priority = priorityField.getEditText().getText().toString().trim();

        if (formIsValid(title, body, priority)) {
            int p = 0;
            switch (priority) {
                case "Critical":
                    p = 1;
                    break;
                case "High":
                    p = 2;
                    break;
                case "Moderate":
                    p = 3;
                    break;
                case "Low":
                    p = 4;
                    break;
            }
            createCase(title, body, p);

        } else {
            emptyFieldsDialog.show();
        }
    }

    public boolean formIsValid(String title, String body, String priority) {
        if (!title.isEmpty() && !title.equals(null) && !body.isEmpty() && !body.equals(null) && !priority.isEmpty() && !priority.equals(null)) {
            return true;
        }
        return false;
    }

    public void createCase(String title, String body, int priority) {
        Case newCase = new Case(title, priority, body);
        viewModel.createCase(newCase).observe(this, this::showDialog);
    }

    private void showDialog(Boolean isSuccess) {
        if (isSuccess) {
            createSuccessDialog.show();
        } else {
            createFailureDialog.show();
        }
    }

    public void buildEmptyFieldsDialog() {
        emptyFieldsDialog = new AlertDialog.Builder(this);
        emptyFieldsDialog.setTitle("Dialog");
        emptyFieldsDialog.setMessage("Please input all fields.");
        emptyFieldsDialog.setPositiveButton("OK", null);
    }

    public void buildCreateSuccessDialog() {
        createSuccessDialog = new AlertDialog.Builder(this);
        createSuccessDialog.setTitle("Dialog");
        createSuccessDialog.setMessage("Successfully submitted case.");
        createSuccessDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                onBackPressed();
            }
        });
    }

    public void buildCreateFailureDialog() {
        createFailureDialog = new AlertDialog.Builder(this);
        createFailureDialog.setTitle("Dialog");
        createFailureDialog.setMessage("Failed to submit case. Please check your internet connection and try again.");
        createFailureDialog.setPositiveButton("OK", null);
    }
}
