package com.example.taazakhabar.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taazakhabar.data.local.entities.SavedArticleEntity

@Dao
interface SavedArticleDao {

    @Query("SELECT * FROM saved_articles_table")
    fun getAllArticles(): PagingSource<Int, SavedArticleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllArticles(articles: List<SavedArticleEntity>)

    @Query("DELETE FROM saved_articles_table")
    suspend fun deleteAllArticles()
}