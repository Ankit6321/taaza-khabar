package com.example.taazakhabar.domain.model

data class Article(
    val id: String,
    val sourceUrl: String,
    val sourceName: String,
    val imageUrl: String,
    val title: String,
    val content: String,
    val createdAt: Long,
    val categories: List<String>,
)