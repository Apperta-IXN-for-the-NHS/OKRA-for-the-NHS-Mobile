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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProvider;

import com.emis.emismobile.R;
import com.emis.emismobile.knowledge.persistence.ArticleVoteLocalRepository.VoteType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayArticleActivity extends AppCompatActivity {
    private String displayedArticleId;

    private TextView bodyTextView;
    private TextView authorTextView;
    private TextView dateTextView;
    private TextView titleTextView;
    private LinearLayout buttonsLayout;
    private LinearLayout articleLayout;
    private ScrollView scrollView;
    private Button upvoteButton;
    private Button downvoteButton;

    private KnowledgeViewModel viewModel;

    private ArticleVoteService voteService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(KnowledgeViewModel.class);
        voteService = new ArticleVoteService(this);

        Intent intent = getIntent();
        displayedArticleId = intent.getStringExtra("article_id");

        setContentView(R.layout.activity_display_article);
        setupViewComponents();
        setupScroll();
        setupVoteButtons();

        fetchSelectedArticle();
    }

    private void setupViewComponents() {
        bodyTextView = this.findViewById(R.id.article_body);
        authorTextView = this.findViewById(R.id.article_author);
        dateTextView = this.findViewById(R.id.article_date);
        titleTextView = this.findViewById(R.id.article_title);
        upvoteButton = this.findViewById(R.id.upvote_button);
        downvoteButton = this.findViewById(R.id.downvote_button);
        buttonsLayout = this.findViewById(R.id.buttons_layout);
        scrollView = this.findViewById(R.id.article_scrollview);
        articleLayout = this.findViewById(R.id.article_linearlayout);
    }

    private void setupScroll() {
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            float y = 0;

            @Override
            public void onScrollChanged() {
                double diff = scrollView.getScrollY() - y;
                diff = Math.abs(diff);

                if (scrollView.getScrollY() > y) {
                    //scroll down
                    if (diff > 50) {
                        buttonsLayout.setVisibility(View.GONE);
                    }
                } else if (scrollView.getScrollY() < y) {
                    //scroll up
                    if (diff > 50) {
                        buttonsLayout.setVisibility(View.VISIBLE);
                    }
                }
                y = scrollView.getScrollY();
            }
        });
    }

    private void setupVoteButtons() {
        VoteType voteType = voteService.getVote(displayedArticleId);
        setButtonColorsAndText(voteType);

        upvoteButton.setOnClickListener(v -> {
            setButtonColorsAndText(VoteType.UPVOTE);
            voteService.upvote(displayedArticleId);
        });

        downvoteButton.setOnClickListener(v -> {
            setButtonColorsAndText(VoteType.DOWNVOTE);
            voteService.downvote(displayedArticleId);
        });
    }

    private void setButtonColorsAndText(VoteType voteType) {
        int defaultColor = android.R.color.white;

        if (VoteType.UPVOTE.equals(voteType)) {
            setButtonColor(upvoteButton, R.color.upvoteGreen);
            setButtonText(upvoteButton, "Liked");
            setButtonColor(downvoteButton, defaultColor);
            setButtonText(downvoteButton, "Dislike");
        } else if (VoteType.DOWNVOTE.equals(voteType)) {
            setButtonColor(downvoteButton, R.color.downvoteRed);
            setButtonText(downvoteButton, "Disliked");
            setButtonColor(upvoteButton, defaultColor);
            setButtonText(upvoteButton, "Like");
        }
    }

    private void setButtonColor(Button button, int color) {
        ViewCompat.setBackgroundTintList(button, ContextCompat.getColorStateList(this, color));
    }

    private void setButtonText(Button button, String text) {
        button.setText(text);
    }

    private void fetchSelectedArticle() {
        viewModel.getArticleById(displayedArticleId).observe(this, this::displaySelectedArticle);
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

    private void listRelatedArticles(Article article) {
        List<Article> relatedArticles = article.getRelated();
        if (!relatedArticles.isEmpty()) {
            TextView relatedLabel = new TextView(this.getBaseContext());
            relatedLabel.setText("Related Articles");
            relatedLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            relatedLabel.setTextColor(Color.parseColor("black"));
            articleLayout.addView(relatedLabel);
        }

        for (Article relatedArticle : relatedArticles) {
            Context context = this.getBaseContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View knowledgeView = inflater.inflate(R.layout.item_knowledge, articleLayout, false);
            knowledgeView.setClickable(true);
            knowledgeView.setTag(relatedArticle.getId());

            TextView articleTitleTV = knowledgeView.findViewById(R.id.article_title);
            articleTitleTV.setText(relatedArticle.getTitle());
            TextView articleDateTV = knowledgeView.findViewById(R.id.article_date);
            articleDateTV.setText(relatedArticle.getDate());

            knowledgeView.setOnClickListener(v -> openRelatedArticle(knowledgeView));

            articleLayout.addView(knowledgeView);
        }
    }

    private void openRelatedArticle(View knowledgeView) {
        Intent intent = new Intent(articleLayout.getContext(), DisplayArticleActivity.class);
        String articleId = knowledgeView.getTag().toString();
        System.out.println(articleId);
        intent.putExtra("article_id", articleId);
        articleLayout.getContext().startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private static class VoteButtonsState {
        private boolean[] pressed = new boolean[2];
        private String[][] buttonText = {
                {"Like", "Liked"},
                {"Dislike", "Disliked"}
        };

        VoteButtonsState(boolean like, boolean dislike) {
        }
    }
}
