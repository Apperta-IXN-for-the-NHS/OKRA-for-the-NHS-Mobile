package com.emis.emismobile.knowledge;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleRepository {

    private final String API_BASE_URL = "http://162.62.53.126:4123";

    private static ArticleRepository instance = null;
    private EmisNowArticleApiService webService;

    private ArticleRepository() {
        buildRetrofit();
    }

    private void buildRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        webService = retrofit.create(EmisNowArticleApiService.class);
    }

    public static ArticleRepository getInstance() {
        if (instance == null) {
            instance = new ArticleRepository();
        }

        return instance;
    }

    public LiveData<Article> getArticleById(String id) {
        final MutableLiveData<Article> article = new MutableLiveData<>();

        webService.getArticle(id).enqueue(new Callback<Article>() {
            @Override
            public void onResponse(@NonNull Call<Article> call,
                                   @NonNull Response<Article> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                article.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Article> call,
                                  @NonNull Throwable t) {
                // todo
                System.err.println(t.getMessage());
            }
        });

        return article;
    }

    public LiveData<List<Article>> getArticles() {
        final MutableLiveData<List<Article>> articles = new MutableLiveData<>();

        webService.getArticles().enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(@NonNull Call<List<Article>> call,
                                   @NonNull Response<List<Article>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                articles.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Article>> call,
                                  @NonNull Throwable t) {
                // todo
                System.err.println(t.getMessage());
            }
        });

        return articles;
    }
}
