package com.example.taazakhabar.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taazakhabar.data.local.entities.SavedArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedArticleDao {

    @Query("SELECT * FROM saved_articles_table")
    fun getAllSavedArticles(): Flow<List<SavedArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: SavedArticleEntity)

    @Delete
    suspend fun deleteArticle(article: SavedArticleEntity)

    @Query("SELECT EXISTS(SELECT * FROM saved_articles_table WHERE id = :id)")
    fun isArticleSaved(id: String): Flow<Boolean>

    @Query("SELECT * FROM saved_articles_table WHERE id = :id")
    suspend fun getArticleById(id: String): SavedArticleEntity?

    @Query("DELETE FROM saved_articles_table")
    suspend fun deleteAllArticles()
}
