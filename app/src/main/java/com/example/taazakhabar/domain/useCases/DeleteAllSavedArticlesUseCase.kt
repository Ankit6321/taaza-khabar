package com.example.taazakhabar.domain.useCases

import com.example.taazakhabar.domain.repository.NewsRepository
import javax.inject.Inject

class DeleteAllSavedArticlesUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke() {
        newsRepository.deleteAllSavedArticles()
    }
}
