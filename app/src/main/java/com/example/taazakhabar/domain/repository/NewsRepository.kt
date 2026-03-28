package com.example.taazakhabar.domain.repository

import androidx.paging.PagingData
import com.example.taazakhabar.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getNews(category: String, trendingTopics: Boolean = false): Flow<PagingData<Article>>

    fun getCachedAllNews(): Flow<List<Article>>
    fun getCachedTrendingNews(): Flow<List<Article>>

    fun getCachedEntertainmentNews(): Flow<List<Article>>
    fun getCachedSportsNews(): Flow<List<Article>>
    fun getCachedScienceNews(): Flow<List<Article>>
    fun getCachedTechnologyNews(): Flow<List<Article>>

    suspend fun toggleSaveArticle(article: Article)
    fun isArticleSaved(id: String): Flow<Boolean>
    fun getSavedArticles(): Flow<List<Article>>
    suspend fun deleteAllSavedArticles()
}
