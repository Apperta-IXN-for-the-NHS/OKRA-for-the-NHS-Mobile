package com.emis.emismobile;

import com.emis.emismobile.cases.Case;
import com.emis.emismobile.knowledge.Article;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EmisNowApiService {

    @GET("/articles")
    Call<List<Article>> getArticles(@Query("query") String query, @Query("limit") int limit, @Query("start") int start);

    @GET("/articles/{id}")
    Call<Article> getArticle(@Path("id") String id);

    @GET("/cases")
    Call<List<Case>> getCases(@Query("query") String query, @Query("limit") int limit, @Query("start") int start);

    @GET("/cases/{id}")
    Call<Case> getCase(@Path("id") String id);

    @POST("/newCase")
    Call<Case> newCase(@Body Case c);
}
