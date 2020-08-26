package com.emis.emismobile.knowledge.web.rest;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.emis.emismobile.EmisNowApiService;
import com.emis.emismobile.knowledge.Article;
import com.emis.emismobile.knowledge.VoidCallback;
import com.emis.emismobile.knowledge.Vote;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleRestRepository {

    private final String API_BASE_URL = "http://162.62.53.126:4123";

    private static ArticleRestRepository instance = null;
    private EmisNowApiService webService;

    private ArticleRestRepository() {
        buildRetrofit();
    }

    private void buildRetrofit() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        webService = retrofit.create(EmisNowApiService.class);
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
                if (!response.isSuccessful()) {
                    return;
                }
                article.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Article> call,
                                  @NonNull Throwable t) {
                Log.i("fetchArticleById", call.request().toString());
                Log.e("fetchArticleById", t.getMessage());

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

                if (!response.isSuccessful()) {
                    return;
                }
                articles.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Article>> call,
                                  @NonNull Throwable t) {
                Log.i("fetchArticles", call.request().toString());
                Log.e("fetchArticles", t.getMessage());
            }
        });

        return articles;
    }

    public void postVoteWithCallback(Vote vote, VoidCallback voteCallback) {

        webService.postVote(vote.getArticleId(), vote).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,
                                   @NonNull Response<Void> response) {

                if (!response.isSuccessful()) {
                    voteCallback.onFailure();
                    return;
                }
                voteCallback.onSuccess();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call,
                                  @NonNull Throwable t) {
                Log.i("postVoteWithCallback", call.request().toString());
                Log.e("postVoteWithCallback", t.getMessage());
            }
        });
    }

}
