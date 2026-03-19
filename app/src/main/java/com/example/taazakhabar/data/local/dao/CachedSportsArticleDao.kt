package com.example.taazakhabar.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taazakhabar.data.local.entities.CachedSportsArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CachedSportsArticleDao {

    @Query("SELECT * FROM cached_sports_articles_table")
    fun getCachedArticles(): Flow<List<CachedSportsArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCachedArticles(articles: List<CachedSportsArticleEntity>)

    @Query("DELETE FROM cached_all_articles_table")
    suspend fun deleteCachedArticles()
}