package com.emis.emismobile;

import com.emis.emismobile.cases.Case;
import com.emis.emismobile.knowledge.Article;
import com.emis.emismobile.knowledge.Vote;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EmisNowApiService {

    @GET("/articles")
    Call<List<Article>> getArticles(@Query("query") String query, @Query("limit") int limit, @Query("start") int start);

    @GET("/articles/{id}")
    Call<Article> getArticle(@Path("id") String id);

    @Headers("Content-type: application/json")
    @POST("/articles/{id}/vote")
    Call<Void> postVote(@Path("id") String id, @Body Vote vote);

    @GET("/cases")
    Call<List<Case>> getCases(@Query("query") String query, @Query("limit") int limit, @Query("start") int start);

    @GET("/cases/{id}")
    Call<Case> getCase(@Path("id") String id);

    @POST("/cases")
    Call<Void> createCase(@Body Case c);
}
