package org.apperta.okramobile.knowledge;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import org.apperta.okramobile.knowledge.web.rest.ArticleRestRepository;

import java.util.List;

public class KnowledgeViewModel extends ViewModel {

    private final ArticleRestRepository articleRestRepository = ArticleRestRepository.getInstance();
    private LiveData<Article> selectedArticle;
    private LiveData<List<Article>> allArticles;

    public LiveData<Article> getArticleById(String articleId) {
        if (newArticleSelected(articleId)) {
            selectedArticle = articleRestRepository.fetchArticleById(articleId);
        }

        return selectedArticle;
    }

    private boolean newArticleSelected(String articleId) {
        return selectedArticle == null
                || selectedArticle.getValue() == null
                || !selectedArticle.getValue().getId().equals(articleId);
    }

    public LiveData<List<Article>> getArticles(String query, int limit, int start) {
        return articleRestRepository.fetchArticles(query, limit, start);
    }
}