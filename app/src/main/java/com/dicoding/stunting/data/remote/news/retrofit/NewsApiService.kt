package com.dicoding.stunting.data.remote.news.retrofit

import com.dicoding.stunting.data.remote.news.response.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    // https://newsapi.org/v2/everything?q=stunting&language=id&apiKey=[apiKey]
    @GET("everything")
    suspend fun getStuntingNews(
        @Query("q") q: String = "stunting",
        @Query("language") language: String = "id",
        @Query("apiKey") apiKey: String
    ): NewsResponse
}