package com.example.taazakhabar.domain.useCases

import com.example.taazakhabar.domain.model.Article
import com.example.taazakhabar.domain.model.NewsTopics
import com.example.taazakhabar.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCachedTopicNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(topic: NewsTopics): Flow<List<Article>> {
        return when (topic) {
            NewsTopics.SCIENCE -> newsRepository.getCachedScienceNews()
            NewsTopics.SPORTS -> newsRepository.getCachedSportsNews()
            NewsTopics.TECHNOLOGY -> newsRepository.getCachedTechnologyNews()
            NewsTopics.ENTERTAINMENT -> newsRepository.getCachedEntertainmentNews()
        }
    }
}
