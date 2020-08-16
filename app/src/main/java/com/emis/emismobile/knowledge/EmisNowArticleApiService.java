package com.emis.emismobile.knowledge;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EmisNowArticleApiService {

    @GET("/articles")
    Call<List<Article>> getArticles(@Query("limit") int limit, @Query("start") int start);

    @GET("/articles/{id}")
    Call<Article> getArticle(@Path("id") String id);
}
