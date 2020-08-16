package com.emis.emismobile.knowledge;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EmisNowArticleApiService {

    @GET("/articles")
    Call<List<Article>> getArticles();

    @GET("/articles/{id}")
    Call<Article> getArticle(@Path("id") String id);
}
