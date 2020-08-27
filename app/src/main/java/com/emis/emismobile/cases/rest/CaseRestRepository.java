package com.emis.emismobile.cases.rest;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.emis.emismobile.EmisNowApiService;
import com.emis.emismobile.cases.Case;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CaseRestRepository {

    private final String API_BASE_URL = "http://162.62.53.126:4123";

    private static CaseRestRepository instance = null;
    private EmisNowApiService webService;

    private CaseRestRepository() {
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

    public static CaseRestRepository getInstance() {
        if (instance == null) {
            instance = new CaseRestRepository();
        }

        return instance;
    }

    public LiveData<Case> fetchCaseById(String id) {
        final MutableLiveData<Case> c = new MutableLiveData<>();

        webService.getCase(id).enqueue(new Callback<Case>() {
            @Override
            public void onResponse(@NonNull Call<Case> call,
                                   @NonNull Response<Case> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                c.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Case> call,
                                  @NonNull Throwable t) {
                Log.i("fetchCaseById", call.request().toString());
                Log.e("fetchCaseById", t.getMessage());
            }
        });

        return c;
    }

    public LiveData<List<Case>> fetchCases(String query, int limit, int start) {
        final MutableLiveData<List<Case>> cases = new MutableLiveData<>();

        webService.getCases(query, limit, start).enqueue(new Callback<List<Case>>() {
            @Override
            public void onResponse(@NonNull Call<List<Case>> call,
                                   @NonNull Response<List<Case>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                cases.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Case>> call,
                                  @NonNull Throwable t) {
                Log.i("fetchCases", call.request().toString());
                Log.e("fetchCases", t.getMessage());
            }
        });

        return cases;
    }

    public MutableLiveData<Boolean> createCase(Case newCase) {
        final MutableLiveData<Boolean> isSuccess = new MutableLiveData<>();

        webService.createCase(newCase).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    isSuccess.setValue(false);
                    return;
                }
                isSuccess.setValue(true);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                isSuccess.setValue(false);
                Log.i("createCase", call.request().toString());
                Log.e("createCase", t.getMessage());
            }
        });
        return isSuccess;
    }

}
