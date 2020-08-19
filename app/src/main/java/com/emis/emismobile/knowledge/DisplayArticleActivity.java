package com.emis.emismobile.knowledge;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
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
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class DisplayArticleActivity extends AppCompatActivity {
    private TextView bodyTextView;
    private TextView authorTextView;
    private TextView dateTextView;
    private TextView titleTextView;
    private LinearLayout buttonsLayout;
    private LinearLayout articleLayout;
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
        articleLayout = this.findViewById(R.id.article_linearlayout);

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
            listRelatedArticles(article);
        } else {
            bodyTextView.setText("This article is empty.");
        }
    }

    public void listRelatedArticles(Article article){
        List<Article> relatedArticles = article.getRelated();
        if(!relatedArticles.isEmpty()){
            TextView relatedLabel = new TextView(this.getBaseContext());
            relatedLabel.setText("Related Articles");
            relatedLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            relatedLabel.setTextColor(Color.parseColor("black"));
            articleLayout.addView(relatedLabel);
        }

        for (Article relatedArticle:relatedArticles) {
            Context context = this.getBaseContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View knowledgeView = inflater.inflate(R.layout.item_knowledge, articleLayout, false);
            knowledgeView.setClickable(true);
            knowledgeView.setTag(relatedArticle.getId());

            TextView articleTitleTV = knowledgeView.findViewById(R.id.article_title);
            articleTitleTV.setText(relatedArticle.getTitle());
            TextView articleDateTV = knowledgeView.findViewById(R.id.article_date);
            articleDateTV.setText(relatedArticle.getDate());

            knowledgeView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    openRelatedArticle(knowledgeView);
                }
            });

            articleLayout.addView(knowledgeView);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void openRelatedArticle(View knowledgeView){
        Intent intent = new Intent(articleLayout.getContext(), DisplayArticleActivity.class);
        String articleId = knowledgeView.getTag().toString();
        System.out.println(articleId);
        intent.putExtra("article_id", articleId);
        articleLayout.getContext().startActivity(intent);
    }

    private void setUpScroll() {
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            float y = 0;

            @Override
            public void onScrollChanged() {
                double diff = scrollView.getScrollY() - y;
                diff = Math.abs(diff);

                if (scrollView.getScrollY() > y) {
                    //scroll down
                    if(diff > 20){
                        buttonsLayout.setVisibility(View.GONE);
                    }
                } else if(scrollView.getScrollY() < y){
                    //scroll up
                    if(diff > 20){
                        buttonsLayout.setVisibility(View.VISIBLE);
                    }
                }
                y = scrollView.getScrollY();
            }
        });
    }
}
