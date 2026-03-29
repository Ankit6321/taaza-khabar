package com.example.taazakhabar.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ArticleDto(
    @SerializedName("hash_id")
    val id: String,
    val source_url: String,
    val source_name: String,
    val image_url: String,
    val title: String,
    val content: String,
    @SerializedName("category_names")
    val categories: List<String>,
)