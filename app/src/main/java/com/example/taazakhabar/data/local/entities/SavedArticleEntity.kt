package com.example.taazakhabar.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taazakhabar.data.local.fromStringList
import com.example.taazakhabar.data.remote.dto.NewsList

@Entity(tableName = "saved_articles_table")
data class SavedArticleEntity(
    @PrimaryKey(autoGenerate = false)
    override val id: String,
    override val sourceUrl: String,
    override val sourceName: String,
    override val imageUrl: String,
    override val title: String,
    override val content: String,
    override val categories: String,
): BaseArticleEntity