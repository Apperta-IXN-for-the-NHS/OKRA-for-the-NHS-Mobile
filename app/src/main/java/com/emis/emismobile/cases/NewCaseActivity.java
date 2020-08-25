package com.emis.emismobile.cases;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.emis.emismobile.R;
import com.google.android.material.textfield.TextInputLayout;

import static java.security.AccessController.getContext;

public class NewCaseActivity extends AppCompatActivity {

    private CasesViewModel viewModel;
    private Button openCaseButton;
    private TextInputLayout titleField;
    private TextInputLayout bodyField;
    private TextInputLayout priorityField;
    private TextView errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CasesViewModel.class);

        Intent intent = getIntent();
        setContentView(R.layout.activity_new_case);
        setTitle("Open a New Case");
        setUpPrioritiesMenu();

        titleField = this.findViewById(R.id.title_field);
        bodyField = this.findViewById(R.id.body_field);
        priorityField = this.findViewById(R.id.priorities_field);
        errorMessage = this.findViewById(R.id.error_message);

        openCaseButton = this.findViewById(R.id.open_case_button);
        openCaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCase();
            }
        });

    }

    public void setUpPrioritiesMenu(){
        String[] priorityOptions = new String[] {"Critical", "High", "Moderate", "Low"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, priorityOptions);
        AutoCompleteTextView dropdown = this.findViewById(R.id.priorities_dropdown);
        dropdown.setAdapter(adapter);
        dropdown.setText("Critical", false);
    }

    public void openCase(){
        String title = titleField.getEditText().getText().toString().trim();
        String body = bodyField.getEditText().getText().toString().trim();
        String priority = priorityField.getEditText().getText().toString().trim();

        if(formIsValid(title, body, priority)){
            System.out.println("valid");
        }else{
            System.out.println("invalid");
            errorMessage.setVisibility(View.VISIBLE);
        }
    }

    public boolean formIsValid(String title, String body, String priority){
        if(!title.isEmpty() && !title.equals(null) && !body.isEmpty() && !body.equals(null) && !priority.isEmpty() && !priority.equals(null)){
            return true;
        }
        return false;
    }
}
