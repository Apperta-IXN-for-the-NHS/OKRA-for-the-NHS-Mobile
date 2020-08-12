package com.emis.emismobile.knowledge;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.emis.emismobile.Article;
import com.emis.emismobile.EmisNowArticleServiceApi;
import com.emis.emismobile.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DisplayArticleActivity extends AppCompatActivity {
    private TextView bodyTextView;
    private TextView authorTextView;
    private TextView dateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_article);
        bodyTextView = this.findViewById(R.id.article_body);
        authorTextView = this.findViewById(R.id.article_author);
        dateTextView = this.findViewById(R.id.article_date);

        Intent intent = getIntent();
        String articleId = intent.getStringExtra("article_id");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EmisNowArticleServiceApi emisNowArticleServiceApi = retrofit.create(EmisNowArticleServiceApi.class);
        Call<Article> call = emisNowArticleServiceApi.getArticle(articleId);
        enqueueCall(call);
    }

    public void enqueueCall(Call<Article> call){
        call.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                if(!response.isSuccessful()){
                    bodyTextView.setText("Code: " + response.code());
                    return;
                }

                Article article = response.body();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    bodyTextView.setText(Html.fromHtml(article.getBody(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    bodyTextView.setText(Html.fromHtml(article.getBody()));
                }

                authorTextView.setText(article.getAuthor());
                dateTextView.setText(article.getDate());
                setTitle(article.getTitle());
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {
                bodyTextView.setText(t.getMessage());
            }
        });
    }
}
