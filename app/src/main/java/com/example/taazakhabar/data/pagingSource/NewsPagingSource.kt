package com.example.taazakhabar.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.taazakhabar.data.mapper.toDomain
import com.example.taazakhabar.data.model.remote.dtos.NewsResponse
import com.example.taazakhabar.data.remote.ApiService
import com.example.taazakhabar.domain.model.News
import javax.inject.Inject

class NewsPagingSource @Inject constructor(
    private val apiService: ApiService,
    private val category: String,
    private val trendingTopics: Boolean
) : PagingSource<Int, News>() {
    override fun getRefreshKey(state: PagingState<Int, News>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, News> {
        val currentLimit = params.key ?: 10
        return try {
            var response: NewsResponse
            if (!trendingTopics)
                response = apiService.getNews(category = category, maxLimit = currentLimit)
            else
                response = apiService.getTrendingTopics(category = category, page = currentLimit)

            LoadResult.Page(
                data = response.data.news_list.toDomain(),
                prevKey = if (currentLimit == 10) null else currentLimit - 10,
                nextKey = if (currentLimit >= 100) null else currentLimit + 10
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }
}