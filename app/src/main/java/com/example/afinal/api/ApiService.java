package com.example.afinal.api;

import com.example.afinal.models.NewsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ApiService {
    String RAPID_API_KEY = "6d8c21976bmsh2cfefb64113b121p18701djsn3cfd719c0cf1";
    String RAPID_API_HOST = "sports-information.p.rapidapi.com";

    @Headers({
            "X-RapidAPI-Key: " + RAPID_API_KEY,
            "X-RapidAPI-Host: " + RAPID_API_HOST
    })
    @GET("soccer/news")
    Call<List<NewsModel>> getNewsFootball();

    @Headers({
            "X-RapidAPI-Key: " + RAPID_API_KEY,
            "X-RapidAPI-Host: " + RAPID_API_HOST
    })
    @GET("nba/news")
    Call<List<NewsModel>> getNewsNba();

    @Headers({
            "X-RapidAPI-Key: " + RAPID_API_KEY,
            "X-RapidAPI-Host: " + RAPID_API_HOST
    })
    @GET("nfl/news")
    Call<List<NewsModel>> getNewsBaseball();
}
