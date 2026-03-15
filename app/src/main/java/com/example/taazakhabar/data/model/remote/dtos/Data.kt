package com.example.taazakhabar.data.model.remote.dtos

data class Data(
    val feed_type: String,
    val min_news_id: String,
    val news_list: List<NewsDTO>,
    val reload_required: Boolean
)