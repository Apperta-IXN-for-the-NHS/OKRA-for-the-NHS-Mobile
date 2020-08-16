package com.emis.emismobile.knowledge;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.emis.emismobile.R;

public class DisplayArticleActivity extends AppCompatActivity {
    private TextView bodyTextView;
    private TextView authorTextView;
    private TextView dateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_article);
        bodyTextView = this.findViewById(R.id.article_body);
        authorTextView = this.findViewById(R.id.article_author);
        dateTextView = this.findViewById(R.id.article_date);

        KnowledgeViewModel viewModel = new ViewModelProvider(this).get(KnowledgeViewModel.class);

        Intent intent = getIntent();
        String articleId = intent.getStringExtra("article_id");

        viewModel.getArticleById(articleId).observe(this, this::displaySelectedArticle);
    }

    private void displaySelectedArticle(Article article) {
        if (article != null && article.getBody() != null && !article.getBody().isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bodyTextView.setText(Html.fromHtml(article.getBody(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                bodyTextView.setText(Html.fromHtml(article.getBody()));
            }

            authorTextView.setText(article.getAuthor());
            dateTextView.setText(article.getDate());
            setTitle(article.getTitle());
        } else {
            bodyTextView.setText("This article is empty.");
        }
    }
}
