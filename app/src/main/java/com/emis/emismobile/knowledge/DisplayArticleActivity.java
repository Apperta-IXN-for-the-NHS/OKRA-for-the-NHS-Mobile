package com.emis.emismobile.knowledge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProvider;

import com.emis.emismobile.R;
import com.emis.emismobile.knowledge.persistence.ArticleVoteLocalRepository;
import com.emis.emismobile.knowledge.persistence.ArticleVoteLocalRepository.VoteType;
import com.emis.emismobile.knowledge.web.rest.ArticleRestRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayArticleActivity extends AppCompatActivity {
    private enum ButtonToggle {ON, OFF}

    private String displayedArticleId;

    private TextView bodyTextView;
    private TextView authorTextView;
    private TextView dateTextView;
    private TextView titleTextView;
    private TextView viewsTextView;
    private TextView scoreTextView;
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

        Intent intent = getIntent();
        displayedArticleId = intent.getStringExtra("article_id");

        wireVoteService();
        setContentView(R.layout.activity_display_article);
        setupViewComponents();
        setupScroll();
        setupVoteButtons();

        fetchSelectedArticle();
    }

    private void wireVoteService() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),
                                                                   Context.MODE_PRIVATE);
        ArticleVoteLocalRepository voteRepository = new ArticleVoteLocalRepository(sharedPreferences);
        ArticleRestRepository restRepository = ArticleRestRepository.getInstance();
        voteService = new ArticleVoteService(voteRepository, restRepository);
    }

    private void setupViewComponents() {
        bodyTextView = this.findViewById(R.id.article_body);
        authorTextView = this.findViewById(R.id.article_author);
        dateTextView = this.findViewById(R.id.article_date);
        titleTextView = this.findViewById(R.id.article_title);
        viewsTextView = this.findViewById(R.id.article_views);
        scoreTextView = this.findViewById(R.id.article_score);
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
            viewsTextView.setText("Views: " + Integer.toString(article.getViews()));
            scoreTextView.setText("Net Score: " + Integer.toString(article.getScore()));
            setTitle("Article");
            listRelatedArticles(article);
        } else {
            titleTextView.setText("This article is empty.");
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

            ImageView viewsIcon = knowledgeView.findViewById(R.id.article_views_icon);
            viewsIcon.setImageResource(R.drawable.ic_visibility_black_24dp);
            ImageView scoreIcon = knowledgeView.findViewById(R.id.article_score_icon);
            scoreIcon.setImageResource(R.drawable.ic_plus_minus_variant);

            TextView articleViewsTV = knowledgeView.findViewById(R.id.article_views);
            articleViewsTV.setText(Integer.toString(relatedArticle.getViews()));
            TextView articleScoreTV = knowledgeView.findViewById(R.id.article_score);
            articleScoreTV.setText(Integer.toString(relatedArticle.getScore()));

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
        private final Map<VoteType, Map<ButtonToggle, Integer>> buttonColor = initializeColorMap();
        private final Map<VoteType, Map<ButtonToggle, String>> buttonTextMap = initializeTextMap();

        private Map<VoteType, ButtonToggle> state;

        public VoteButtonsState(VoteType initialVote) {
            state = initializeState(initialVote);
        }

        private Map<VoteType, ButtonToggle> initializeState(@Nullable VoteType voteType) {
            Map<VoteType, ButtonToggle> initialState = new HashMap<>();
            initialState.put(VoteType.UPVOTE, ButtonToggle.OFF);
            initialState.put(VoteType.DOWNVOTE, ButtonToggle.OFF);

            if (voteType != null) {
                initialState.put(voteType, ButtonToggle.ON);
            }

            return initialState;
        }

        private void pressUpvote() {
            if (ButtonToggle.OFF.equals(state.get(VoteType.UPVOTE))) {
                state.put(VoteType.UPVOTE, ButtonToggle.ON);
                if (ButtonToggle.ON.equals(state.get(VoteType.DOWNVOTE))) {
                    state.put(VoteType.DOWNVOTE, ButtonToggle.OFF);
                }
            } else {
                state.put(VoteType.UPVOTE, ButtonToggle.OFF);
            }
        }

        private void pressDownvote() {
            if (ButtonToggle.OFF.equals(state.get(VoteType.DOWNVOTE))) {
                state.put(VoteType.DOWNVOTE, ButtonToggle.ON);
                if (ButtonToggle.ON.equals(state.get(VoteType.UPVOTE))) {
                    state.put(VoteType.UPVOTE, ButtonToggle.OFF);
                }
            } else {
                state.put(VoteType.DOWNVOTE, ButtonToggle.OFF);
            }
        }

        private String getButtonText(VoteType buttonType) {
            ButtonToggle currentPosition = state.get(buttonType);

            return buttonTextMap.get(buttonType).get(currentPosition);
        }

        private Integer getButtonColor(VoteType buttonType) {
            ButtonToggle currentPosition = state.get(buttonType);

            return buttonColor.get(buttonType).get(currentPosition);
        }

        private Map<VoteType, Map<ButtonToggle, Integer>> initializeColorMap() {
            int defaultColor = R.color.white;

            Map<ButtonToggle, Integer> upvoteColor = new HashMap<ButtonToggle, Integer>() {{
                put(ButtonToggle.ON, R.color.upvoteGreen);
                put(ButtonToggle.OFF, defaultColor);
            }};

            Map<ButtonToggle, Integer> downvoteColor = new HashMap<ButtonToggle, Integer>() {{
                put(ButtonToggle.ON, R.color.downvoteRed);
                put(ButtonToggle.OFF, defaultColor);
            }};

            return new HashMap<VoteType, Map<ButtonToggle, Integer>>() {{
                put(VoteType.UPVOTE, upvoteColor);
                put(VoteType.DOWNVOTE, downvoteColor);
            }};
        }

        private Map<VoteType, Map<ButtonToggle, String>> initializeTextMap() {
            Map<ButtonToggle, String> upvoteText = new HashMap<ButtonToggle, String>() {{
                put(ButtonToggle.ON, getString(R.string.upvoteButtonTextPressed));
                put(ButtonToggle.OFF, getString(R.string.upvoteButtonText));
            }};

            Map<ButtonToggle, String> downvoteText = new HashMap<ButtonToggle, String>() {{
                put(ButtonToggle.ON, getString(R.string.downvoteButtonTextPressed));
                put(ButtonToggle.OFF, getString(R.string.downvoteButtonText));
            }};

            return new HashMap<VoteType, Map<ButtonToggle, String>>() {{
                put(VoteType.UPVOTE, upvoteText);
                put(VoteType.DOWNVOTE, downvoteText);
            }};
        }
    }
}
