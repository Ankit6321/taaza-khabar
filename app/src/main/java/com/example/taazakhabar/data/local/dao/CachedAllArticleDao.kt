package com.example.taazakhabar.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taazakhabar.data.local.entities.CachedAllArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CachedAllArticleDao {

    @Query("SELECT * FROM cached_all_articles_table")
    fun getCachedArticles(): Flow<List<CachedAllArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCachedArticles(articles: List<CachedAllArticleEntity>)

    @Query("DELETE FROM cached_all_articles_table")
    suspend fun deleteCachedArticles()
}