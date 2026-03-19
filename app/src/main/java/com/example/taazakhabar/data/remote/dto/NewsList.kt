package com.example.taazakhabar.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NewsList(
    @SerializedName("news_obj")
    val newsArticle: ArticleDto
)