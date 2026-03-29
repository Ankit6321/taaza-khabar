package com.example.taazakhabar.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taazakhabar.data.local.entities.CachedTrendingArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CachedTrendingArticleDao {
    @Query("SELECT * FROM cached_trending_articles_table")
    fun getCachedArticles(): Flow<List<CachedTrendingArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCachedArticles(articles: List<CachedTrendingArticleEntity>)

    @Query("DELETE FROM cached_trending_articles_table")
    suspend fun deleteCachedArticles()
}