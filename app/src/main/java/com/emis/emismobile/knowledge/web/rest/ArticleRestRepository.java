package com.emis.emismobile.knowledge.web.rest;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.emis.emismobile.knowledge.Article;

import java.util.List;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleRestRepository {

    private final String API_BASE_URL = "http://162.62.53.126:4123";
    private static final String TAG = "ArticleRestRepository";

    private static ArticleRestRepository instance = null;
    private EmisNowArticleApiService webService;

    private ArticleRestRepository() {
        buildRetrofit();
    }

    private void buildRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        webService = retrofit.create(EmisNowArticleApiService.class);
    }

    public static ArticleRestRepository getInstance() {
        if (instance == null) {
            instance = new ArticleRestRepository();
        }

        return instance;
    }

    public LiveData<Article> fetchArticleById(String id) {
        final MutableLiveData<Article> article = new MutableLiveData<>();

        webService.getArticle(id).enqueue(new Callback<Article>() {
            @Override
            public void onResponse(@NonNull Call<Article> call,
                                   @NonNull Response<Article> response) {
                logRequest(call.request());

                if (!response.isSuccessful()) {
                    logError(response);

                    return;
                }
                logResponse(response);

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

    public LiveData<List<Article>> fetchArticles(String query, int limit, int start) {
        final MutableLiveData<List<Article>> articles = new MutableLiveData<>();

        webService.getArticles(query, limit, start).enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(@NonNull Call<List<Article>> call,
                                   @NonNull Response<List<Article>> response) {
                logRequest(call.request());

                if (!response.isSuccessful()) {
                    logError(response);

                    return;
                }
                logResponse(response);

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

    private static void logRequest(Request request) {
        Log.i(TAG, String.format("Making a request: %s", request.toString()));
    }

    private static void logResponse(Response response) {
        Log.i(TAG, String.format("Response: code %d: %s", response.code(), response.message()));
    }

    private static void logError(Response response) {
        Log.e(TAG, String.format("Error: code %d: %s", response.code(), response.message()));
    }
}
