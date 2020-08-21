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

import androidx.annotation.Nullable;
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
    private enum Position {ON, OFF}

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

    private VoteButtonsState buttonsState;

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
        buttonsState = new VoteButtonsState(voteType);
        setButtonColorsAndText();

        upvoteButton.setOnClickListener(v -> {
            buttonsState.pressUpvote();
            setButtonColorsAndText();
            voteService.upvote(displayedArticleId);
        });

        downvoteButton.setOnClickListener(v -> {
            buttonsState.pressDownvote();
            setButtonColorsAndText();
            voteService.downvote(displayedArticleId);
        });
    }

    private void setButtonColorsAndText() {
        setButtonText(upvoteButton, buttonsState.getButtonText(VoteType.UPVOTE));
        setButtonColor(upvoteButton, buttonsState.getButtonColor(VoteType.UPVOTE));
        setButtonText(downvoteButton, buttonsState.getButtonText(VoteType.DOWNVOTE));
        setButtonColor(downvoteButton, buttonsState.getButtonColor(VoteType.DOWNVOTE));
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

    private class VoteButtonsState {
        private final Map<VoteType, Map<Position, Integer>> buttonColor = initializeColorMap();
        private final Map<VoteType, Map<Position, String>> buttonTextMap = initializeTextMap();

        private Map<VoteType, Position> state;

        public VoteButtonsState(VoteType initialVote) {
            state = initializeState(initialVote);
        }

        private Map<VoteType, Position> initializeState(@Nullable VoteType voteType) {
            Map<VoteType, Position> initialState = new HashMap<>();
            initialState.put(VoteType.UPVOTE, Position.OFF);
            initialState.put(VoteType.DOWNVOTE, Position.OFF);

            if (voteType != null) {
                initialState.put(voteType, Position.ON);
            }

            return initialState;
        }

        private void pressUpvote() {
            if (Position.OFF.equals(state.get(VoteType.UPVOTE))) {
                state.put(VoteType.UPVOTE, Position.ON);
                if (Position.ON.equals(state.get(VoteType.DOWNVOTE))) {
                    state.put(VoteType.DOWNVOTE, Position.OFF);
                }
            } else {
                state.put(VoteType.UPVOTE, Position.OFF);
            }
        }

        private void pressDownvote() {
            if (Position.OFF.equals(state.get(VoteType.DOWNVOTE))) {
                state.put(VoteType.DOWNVOTE, Position.ON);
                if (Position.ON.equals(state.get(VoteType.UPVOTE))) {
                    state.put(VoteType.UPVOTE, Position.OFF);
                }
            } else {
                state.put(VoteType.DOWNVOTE, Position.OFF);
            }
        }

        private String getButtonText(VoteType buttonType) {
            Position currentPosition = state.get(buttonType);

            return buttonTextMap.get(buttonType).get(currentPosition);
        }

        private Integer getButtonColor(VoteType buttonType) {
            Position currentPosition = state.get(buttonType);

            return buttonColor.get(buttonType).get(currentPosition);
        }

        private Map<VoteType, Map<Position, Integer>> initializeColorMap() {
            int defaultColor = android.R.color.white;

            Map<Position, Integer> upvoteColor = new HashMap<Position, Integer>() {{
                put(Position.ON, R.color.upvoteGreen);
                put(Position.OFF, defaultColor);
            }};

            Map<Position, Integer> downvoteColor = new HashMap<Position, Integer>() {{
                put(Position.ON, R.color.downvoteRed);
                put(Position.OFF, defaultColor);
            }};

            return new HashMap<VoteType, Map<Position, Integer>>(){{
                put(VoteType.UPVOTE, upvoteColor);
                put(VoteType.DOWNVOTE, downvoteColor);
            }};
        }

        private Map<VoteType, Map<Position, String>> initializeTextMap() {
            Map<Position, String> upvoteText = new HashMap<Position, String>() {{
                put(Position.ON, getString(R.string.upvoteButtonTextPressed));
                put(Position.OFF, getString(R.string.upvoteButtonText));
            }};

            Map<Position, String> downvoteText = new HashMap<Position, String>() {{
                put(Position.ON, getString(R.string.downvoteButtonTextPressed));
                put(Position.OFF, getString(R.string.downvoteButtonText));
            }};

            return new HashMap<VoteType, Map<Position, String>>(){{
                put(VoteType.UPVOTE, upvoteText);
                put(VoteType.DOWNVOTE, downvoteText);
            }};
        }
    }
}
