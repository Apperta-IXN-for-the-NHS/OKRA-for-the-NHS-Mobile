package com.emis.emismobile.cases.rest;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.emis.emismobile.EmisNowApiService;
import com.emis.emismobile.cases.Case;

import java.util.List;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CaseRestRepository {

    private final String API_BASE_URL = "http://162.62.53.126:4123";
    private static final String TAG = "CaseRestRepository";

    private static CaseRestRepository instance = null;
    private EmisNowApiService webService;

    private CaseRestRepository() {
        buildRetrofit();
    }

    private void buildRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
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
                logRequest(call.request());

                if (!response.isSuccessful()) {
                    logError(response);

                    return;
                }
                logResponse(response);

                c.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Case> call,
                                  @NonNull Throwable t) {
                // todo
                System.err.println(t.getMessage());
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
                logRequest(call.request());

                if (!response.isSuccessful()) {
                    logError(response);

                    return;
                }
                logResponse(response);

                cases.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Case>> call,
                                  @NonNull Throwable t) {
                // todo
                System.err.println(t.getMessage());
            }
        });

        return cases;
    }

    public void newCase(Case c){
        Call<Case> call = webService.newCase(c);
        call.enqueue(new Callback<Case>(){

            @Override
            public void onResponse(Call<Case> call, Response<Case> response) {
                if(!response.isSuccessful()){
                    System.out.println("code: " + response.code());
                    return;
                }
            }

            @Override
            public void onFailure(Call<Case> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
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
