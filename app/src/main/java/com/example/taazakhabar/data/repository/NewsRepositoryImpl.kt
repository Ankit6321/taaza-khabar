package com.example.taazakhabar.data.repository

import com.example.taazakhabar.data.mapper.toDomain
import com.example.taazakhabar.data.model.remote.dtos.NewsResponse
import com.example.taazakhabar.data.remote.ApiService
import com.example.taazakhabar.domain.model.News
import com.example.taazakhabar.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : NewsRepository {
    override suspend fun getNews(category: String): Result<List<News>> {
        return try {
            val response: NewsResponse = apiService.getNews(category)
            Result.success(response.data.news_list.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}