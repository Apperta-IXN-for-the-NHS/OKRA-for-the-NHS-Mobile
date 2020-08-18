package com.emis.emismobile.knowledge;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.emis.emismobile.R;

public class DisplayArticleActivity extends AppCompatActivity {
    private TextView bodyTextView;
    private TextView authorTextView;
    private TextView dateTextView;
    private TextView titleTextView;
    private LinearLayout buttonsLayout;
    private ScrollView scrollView;
    private Button likeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_article);
        bodyTextView = this.findViewById(R.id.article_body);
        authorTextView = this.findViewById(R.id.article_author);
        dateTextView = this.findViewById(R.id.article_date);
        titleTextView = this.findViewById(R.id.article_title);
        likeButton = this.findViewById(R.id.likeButton);
        buttonsLayout = this.findViewById(R.id.buttons_layout);
        scrollView = this.findViewById(R.id.article_scrollview);

        KnowledgeViewModel viewModel = new ViewModelProvider(this).get(KnowledgeViewModel.class);
        setUpScroll();
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
            titleTextView.setText(article.getTitle());
            setTitle("Article");
        } else {
            bodyTextView.setText("This article is empty.");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setUpScroll() {
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            float y = 0;

            @Override
            public void onScrollChanged() {
                if (scrollView.getScrollY() > y) {
                    //scroll down
                    buttonsLayout.setVisibility(View.GONE);
                    System.out.println("down");
                } else {
                    //scroll up
                    buttonsLayout.setVisibility(View.VISIBLE);
                    System.out.println("up");
                }
                y = scrollView.getScrollY();
            }
        });
    }
}
