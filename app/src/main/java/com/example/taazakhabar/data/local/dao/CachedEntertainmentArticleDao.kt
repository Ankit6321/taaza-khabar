package com.example.taazakhabar.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taazakhabar.data.local.entities.CachedEntertainmentArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CachedEntertainmentArticleDao {

    @Query("SELECT * FROM cached_entertainment_articles_table")
    fun getCachedArticles(): Flow<List<CachedEntertainmentArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCachedArticles(articles: List<CachedEntertainmentArticleEntity>)

    @Query("DELETE FROM cached_all_articles_table")
    suspend fun deleteCachedArticles()
}