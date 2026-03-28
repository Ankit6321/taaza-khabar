package com.example.taazakhabar.domain.useCases

import com.example.taazakhabar.domain.model.Article
import com.example.taazakhabar.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedArticlesUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(): Flow<List<Article>> {
        return newsRepository.getSavedArticles()
    }
}
