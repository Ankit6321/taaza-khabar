package com.example.taazakhabar.domain.useCases

import androidx.paging.PagingData
import com.example.taazakhabar.domain.model.News
import com.example.taazakhabar.domain.model.NewsCategory
import com.example.taazakhabar.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(category: NewsCategory): Flow<PagingData<News>> {
        return newsRepository.getNews(category.name.toLowerCase())
    }
}