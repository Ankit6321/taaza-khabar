package com.example.taazakhabar.domain.useCases

import com.example.taazakhabar.domain.model.Article
import com.example.taazakhabar.domain.repository.NewsRepository
import javax.inject.Inject

class ToggleSaveArticleUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(article: Article) {
        newsRepository.toggleSaveArticle(article)
    }
}
