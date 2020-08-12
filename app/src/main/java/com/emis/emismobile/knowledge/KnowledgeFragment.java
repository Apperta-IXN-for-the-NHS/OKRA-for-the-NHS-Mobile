package com.emis.emismobile.knowledge;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emis.emismobile.Article;
import com.emis.emismobile.EmisNowArticleServiceApi;
import com.emis.emismobile.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KnowledgeFragment extends Fragment {

    private KnowledgeViewModel knowledgeViewModel;
    private RecyclerView rvKnowledge;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        knowledgeViewModel = ViewModelProviders.of(this).get(KnowledgeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_knowledge, container, false);
        rvKnowledge = root.findViewById(R.id.rvKnowledge);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EmisNowArticleServiceApi emisNowArticleServiceApi = retrofit.create(EmisNowArticleServiceApi.class);
        Call<List<Article>> call = emisNowArticleServiceApi.getArticles();
        enqueueCall(call);
        return root;
    }

    public void enqueueCall(Call<List<Article>> call){
        call.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                if(!response.isSuccessful()){
                    System.out.println("Code: " + response.code());
                    return;
                }

                List<Article> articles = response.body();
                KnowledgeAdapter adapter = new KnowledgeAdapter(articles);
                rvKnowledge.setAdapter(adapter);
                rvKnowledge.setLayoutManager(new LinearLayoutManager(rvKnowledge.getContext()));
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}