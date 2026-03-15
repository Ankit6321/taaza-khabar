package com.example.taazakhabar.domain.useCases

import androidx.paging.PagingData
import com.example.taazakhabar.domain.model.News
import com.example.taazakhabar.domain.model.NewsTopics
import com.example.taazakhabar.domain.repository.NewsRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetTopicWiseNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(category: NewsTopics): Flow<PagingData<News>> {
        return newsRepository.getNews(category.name.toLowerCase(), trendingTopics = true)
    }
}