package com.example.taazakhabar.data.local.entities

import com.example.taazakhabar.data.local.fromStringList
import com.example.taazakhabar.data.remote.dto.NewsList

interface BaseArticleEntity {
    val id: String
    val sourceUrl: String
    val sourceName: String
    val imageUrl: String
    val title: String
    val content: String
    val categories: String
}