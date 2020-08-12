package com.emis.emismobile;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface EmisNowArticleServiceApi {

    @GET("articles")
    Call<List<Article>> getArticles();

    @GET("articles/{id}")
    Call<Article> getArticle(@Path("id") String id);
}
