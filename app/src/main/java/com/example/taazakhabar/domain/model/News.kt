package com.example.taazakhabar.domain.model

data class News (
    val imageBaseUrl: String,
    val category: List<String>,
    val title: String,
    val content: String,
    val author: String
)