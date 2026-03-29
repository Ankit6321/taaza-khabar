package com.example.taazakhabar.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taazakhabar.data.local.entities.CachedScienceArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CachedScienceArticleDao {

    @Query("SELECT * FROM cached_science_articles_table")
    fun getCachedArticles(): Flow<List<CachedScienceArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCachedArticles(articles: List<CachedScienceArticleEntity>)

    @Query("DELETE FROM cached_science_articles_table")
    suspend fun deleteCachedArticles()
}