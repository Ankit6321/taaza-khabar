package com.example.taazakhabar.domain.useCases

import com.example.taazakhabar.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsArticleSavedUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(id: String): Flow<Boolean> {
        return newsRepository.isArticleSaved(id)
    }
}
