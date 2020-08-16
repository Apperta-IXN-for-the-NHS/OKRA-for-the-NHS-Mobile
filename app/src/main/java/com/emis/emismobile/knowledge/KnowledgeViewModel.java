package com.emis.emismobile.knowledge;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class KnowledgeViewModel extends ViewModel {

    private final ArticleRepository articleRepository = ArticleRepository.getInstance();
    private LiveData<Article> selectedArticle;
    private LiveData<List<Article>> allArticles;

    public LiveData<Article> getArticleById(String articleId) {
        if (newArticleSelected(articleId)) {
            selectedArticle = articleRepository.fetchArticleById(articleId);
        }

        return selectedArticle;
    }

    private boolean newArticleSelected(String articleId) {
        return selectedArticle == null
                || selectedArticle.getValue() == null
                || !selectedArticle.getValue().getId().equals(articleId);
    }

    public LiveData<List<Article>> getArticles(int limit, int start) {
        if (allArticles == null) {
            allArticles = articleRepository.fetchArticles(limit, start);
        }

        return allArticles;
    }
}