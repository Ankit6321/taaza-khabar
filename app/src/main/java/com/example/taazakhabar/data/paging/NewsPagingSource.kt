package com.example.taazakhabar.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.taazakhabar.data.local.ArticleDatabase
import com.example.taazakhabar.data.local.dao.CachedTopArticleDao
import com.example.taazakhabar.data.local.dao.CachedEntertainmentArticleDao
import com.example.taazakhabar.data.local.dao.CachedScienceArticleDao
import com.example.taazakhabar.data.local.dao.CachedSportsArticleDao
import com.example.taazakhabar.data.local.dao.CachedTechnologyArticleDao
import com.example.taazakhabar.data.local.dao.CachedTrendingArticleDao
import com.example.taazakhabar.data.local.entities.CachedTopArticleEntity
import com.example.taazakhabar.data.local.entities.CachedEntertainmentArticleEntity
import com.example.taazakhabar.data.local.entities.CachedScienceArticleEntity
import com.example.taazakhabar.data.local.entities.CachedSportsArticleEntity
import com.example.taazakhabar.data.local.entities.CachedTechnologyArticleEntity
import com.example.taazakhabar.data.local.entities.CachedTrendingArticleEntity
import com.example.taazakhabar.data.local.toEntity
import com.example.taazakhabar.data.remote.ApiService
import com.example.taazakhabar.data.remote.dto.NewsResponse
import com.example.taazakhabar.data.remote.toDomain
import com.example.taazakhabar.domain.model.Article
import com.example.taazakhabar.domain.model.Constants

class NewsPagingSource(
    private val apiService: ApiService,
    private val articleDatabase: ArticleDatabase,
    private val category: String,
    private val trendingTopics: Boolean
) : PagingSource<Int, Article>() {
    private val cachedTopArticleDao: CachedTopArticleDao = articleDatabase.cachedTopArticleDao
    private val cachedSportsArticleDao: CachedSportsArticleDao =
        articleDatabase.cachedSportsArticleDao
    private val cachedScienceArticleDao: CachedScienceArticleDao =
        articleDatabase.cachedScienceArticleDao
    private val cachedTechnologyArticleDao: CachedTechnologyArticleDao =
        articleDatabase.cachedTechnologyArticleDao
    private val cachedEntertainmentArticleDao: CachedEntertainmentArticleDao =
        articleDatabase.cachedEntertainmentArticleDao
    private val cachedTrendingArticleDao: CachedTrendingArticleDao =
        articleDatabase.cachedTrendingArticleDao


    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val currentLimit = params.key ?: Constants.PAGE_SIZE
        val normalizedCategory = category.lowercase().trim()
        return try {
            var response: NewsResponse
            if (!trendingTopics)
                response = apiService.getNews(category = category, maxLimit = currentLimit)
            else
                response = apiService.getTrendingTopics(category = category, page = currentLimit)

            if (params.key == null && response.data.news_list.isNotEmpty()) {
                if (!trendingTopics)
                    when (category) {
                        "top_stories" -> {
                            cachedTopArticleDao.deleteCachedArticles()

                            val cacheList =
                                response.data.news_list.take(Constants.CACHE_SIZE).toEntity(
                                    constructor = ::CachedTopArticleEntity
                                )
                            cachedTopArticleDao.addCachedArticles(cacheList)
                        }

                        "trending" -> {
                            cachedTrendingArticleDao.deleteCachedArticles()

                            val cacheList =
                                response.data.news_list.take(Constants.CACHE_SIZE).toEntity(
                                    constructor = ::CachedTrendingArticleEntity
                                )
                            cachedTrendingArticleDao.addCachedArticles(cacheList)
                        }
                    }
                else
                    when (category) {
                        "science" -> {
                            cachedScienceArticleDao.deleteCachedArticles()

                            val cacheList =
                                response.data.news_list.take(Constants.CACHE_SIZE).toEntity(
                                    constructor = ::CachedScienceArticleEntity
                                )
                            cachedScienceArticleDao.addCachedArticles(cacheList)
                        }

                        "sports" -> {
                            cachedSportsArticleDao.deleteCachedArticles()

                            val cacheList =
                                response.data.news_list.take(Constants.CACHE_SIZE).toEntity(
                                    constructor = ::CachedSportsArticleEntity
                                )
                            cachedSportsArticleDao.addCachedArticles(cacheList)
                        }

                        "technology" -> {
                            cachedTechnologyArticleDao.deleteCachedArticles()

                            val cacheList =
                                response.data.news_list.take(Constants.CACHE_SIZE).toEntity(
                                    constructor = ::CachedTechnologyArticleEntity
                                )
                            cachedTechnologyArticleDao.addCachedArticles(cacheList)
                        }

                        "entertainment" -> {
                            cachedEntertainmentArticleDao.deleteCachedArticles()

                            val cacheList =
                                response.data.news_list.take(Constants.CACHE_SIZE).toEntity(
                                    constructor = ::CachedEntertainmentArticleEntity
                                )
                            cachedEntertainmentArticleDao.addCachedArticles(cacheList)
                        }
                    }
            }

            LoadResult.Page(
                data = response.data.news_list.toDomain(),
                prevKey = if (currentLimit == Constants.PAGE_SIZE) null else currentLimit - Constants.PAGE_SIZE,
                nextKey = if (currentLimit >= Constants.MAX_ARTICLES) null else currentLimit + Constants.PAGE_SIZE
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }
}