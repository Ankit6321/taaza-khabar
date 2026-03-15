package com.example.taazakhabar.domain.repository

import androidx.paging.PagingData
import com.example.taazakhabar.domain.model.News
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getNews(category: String, trendingTopics: Boolean = false): Flow<PagingData<News>>
}