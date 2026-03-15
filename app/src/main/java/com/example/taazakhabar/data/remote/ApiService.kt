package com.example.taazakhabar.data.remote

import com.example.taazakhabar.data.model.remote.dtos.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // https://inshorts.com/api/en/news?category=top_stories&max_limit=10&include_card_data=true

    @GET("news")
    suspend fun getNews(
        @Query("category") category: String = "top_stories",
        @Query("max_limit") maxLimit: Int = 10,
        @Query("include_card_data") includeCardData: Boolean = true
    ): NewsResponse
}