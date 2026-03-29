package com.example.taazakhabar.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taazakhabar.data.local.entities.CachedTechnologyArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CachedTechnologyArticleDao {

    @Query("SELECT * FROM cached_technology_articles_table")
    fun getCachedArticles(): Flow<List<CachedTechnologyArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCachedArticles(articles: List<CachedTechnologyArticleEntity>)

    @Query("DELETE FROM cached_technology_articles_table")
    suspend fun deleteCachedArticles()
}