package com.example.taazakhabar.data.remote

import com.example.taazakhabar.data.model.remote.dtos.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // https://inshorts.com/api/en/news?category=top_stories&max_limit=10&include_card_data=true
    // https://inshorts.com/api/en/search/trending_topics/{category}?page=1&type=NEWS_CATEGORY
    // https://inshorts.com/api/en/search/trending_topics/sports?page=1&type=NEWS_CATEGORY
    @GET("news")
    suspend fun getNews(
        @Query("category") category: String = "top_stories",
        @Query("max_limit") maxLimit: Int = 10,
        @Query("include_card_data") includeCardData: Boolean = true
    ): NewsResponse

    @GET("search/trending_topics/{category}")
    suspend fun getTrendingTopics(
        @Path("category") category: String,
        @Query("page") page: Int = 10,
        @Query("type") type: String = "NEWS_CATEGORY"
    ): NewsResponse
}