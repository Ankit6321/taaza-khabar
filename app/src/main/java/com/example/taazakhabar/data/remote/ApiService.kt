package com.example.taazakhabar.data.remote

import com.example.taazakhabar.data.remote.dto.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("news")
    suspend fun getNews(
        @Query("category") category: String = "top_stories",
        @Query("max_limit") maxLimit: Int = 10,
        @Query("include_card_data") includeCardData: Boolean = true,
        @Query("news_offset") newsOffset: String? = null
    ): NewsResponse

    @GET("search/trending_topics/{category}")
    suspend fun getTrendingTopics(
        @Path("category") category: String,
        @Query("page") page: Int = 10,
        @Query("type") type: String = "NEWS_CATEGORY"
    ): NewsResponse
}