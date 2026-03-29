package com.example.taazakhabar.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_science_articles_table")
data class CachedScienceArticleEntity(
    @PrimaryKey(autoGenerate = false)
    override val id: String,
    override val sourceUrl: String,
    override val sourceName: String,
    override val imageUrl: String,
    override val title: String,
    override val content: String,
    override val categories: String,
) : BaseArticleEntity