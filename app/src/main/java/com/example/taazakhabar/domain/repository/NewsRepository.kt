package com.example.taazakhabar.domain.repository

import com.example.taazakhabar.domain.model.News

interface NewsRepository {

    suspend fun getNews(category: String): Result<List<News>>
}