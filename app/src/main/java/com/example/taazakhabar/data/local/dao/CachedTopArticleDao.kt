package com.example.taazakhabar.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taazakhabar.data.local.entities.CachedTopArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CachedTopArticleDao {

    @Query("SELECT * FROM cached_top_articles_table")
    fun getCachedArticles(): Flow<List<CachedTopArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCachedArticles(articles: List<CachedTopArticleEntity>)

    @Query("DELETE FROM cached_top_articles_table")
    suspend fun deleteCachedArticles()
}