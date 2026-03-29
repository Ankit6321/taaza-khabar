package com.example.taazakhabar.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.taazakhabar.data.local.ArticleDatabase
import com.example.taazakhabar.data.local.dao.CachedEntertainmentArticleDao
import com.example.taazakhabar.data.local.dao.CachedScienceArticleDao
import com.example.taazakhabar.data.local.dao.CachedSportsArticleDao
import com.example.taazakhabar.data.local.dao.CachedTechnologyArticleDao
import com.example.taazakhabar.data.local.dao.CachedTopArticleDao
import com.example.taazakhabar.data.local.dao.CachedTrendingArticleDao
import com.example.taazakhabar.data.local.dao.SavedArticleDao
import com.example.taazakhabar.data.local.entities.SavedArticleEntity
import com.example.taazakhabar.data.local.toDomain
import com.example.taazakhabar.data.paging.NewsPagingSource
import com.example.taazakhabar.data.remote.ApiService
import com.example.taazakhabar.domain.model.Article
import com.example.taazakhabar.domain.model.Constants
import com.example.taazakhabar.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val articleDatabase: ArticleDatabase
) : NewsRepository {
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
    private val savedArticleDao: SavedArticleDao = articleDatabase.savedArticleDao

    override fun getNews(category: String, trendingTopics: Boolean): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                initialLoadSize = Constants.PAGE_SIZE * 3,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NewsPagingSource(
                    apiService = apiService,
                    category = category,
                    trendingTopics = trendingTopics,
                    articleDatabase = articleDatabase
                )
            }
        ).flow
    }

    override fun getCachedAllNews(): Flow<List<Article>> {
        return cachedTopArticleDao.getCachedArticles().map { articles ->
            articles.map { article -> article.toDomain() }
        }
    }

    override fun getCachedTrendingNews(): Flow<List<Article>> {
        return cachedTrendingArticleDao.getCachedArticles().map { articles ->
            articles.map { article -> article.toDomain() }
        }
    }

    override fun getCachedEntertainmentNews(): Flow<List<Article>> {
        return cachedEntertainmentArticleDao.getCachedArticles().map { articles ->
            articles.map { article -> article.toDomain() }
        }
    }

    override fun getCachedSportsNews(): Flow<List<Article>> {
        return cachedSportsArticleDao.getCachedArticles().map { articles ->
            articles.map { article -> article.toDomain() }
        }
    }

    override fun getCachedScienceNews(): Flow<List<Article>> {
        return cachedScienceArticleDao.getCachedArticles().map { articles ->
            articles.map { article -> article.toDomain() }
        }
    }

    override fun getCachedTechnologyNews(): Flow<List<Article>> {
        return cachedTechnologyArticleDao.getCachedArticles().map { articles ->
            articles.map { article -> article.toDomain() }
        }
    }

    override suspend fun toggleSaveArticle(article: Article) {
        val existing = savedArticleDao.getArticleById(article.id)
        if (existing != null) {
            savedArticleDao.deleteArticle(existing)
        } else {
            savedArticleDao.insertArticle(
                SavedArticleEntity(
                    id = article.id,
                    sourceUrl = article.sourceUrl,
                    sourceName = article.sourceName,
                    imageUrl = article.imageUrl,
                    title = article.title,
                    content = article.content,
                    categories = article.categories.joinToString(",")
                )
            )
        }
    }

    override fun isArticleSaved(id: String): Flow<Boolean> {
        return savedArticleDao.isArticleSaved(id)
    }

    override fun getSavedArticles(): Flow<List<Article>> {
        return savedArticleDao.getAllSavedArticles().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun deleteAllSavedArticles() {
        savedArticleDao.deleteAllArticles()
    }
}
