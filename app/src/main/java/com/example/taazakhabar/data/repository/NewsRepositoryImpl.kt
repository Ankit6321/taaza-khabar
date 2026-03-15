package com.example.taazakhabar.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.taazakhabar.data.pagingSource.NewsPagingSource
import com.example.taazakhabar.data.remote.ApiService
import com.example.taazakhabar.domain.model.News
import com.example.taazakhabar.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : NewsRepository {
    override fun getNews(category: String, trendingTopics: Boolean): Flow<PagingData<News>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NewsPagingSource(
                    apiService = apiService,
                    category = category,
                    trendingTopics = trendingTopics
                )
            }
        ).flow
    }
}