package com.example.taazakhabar.data.mapper

import com.example.taazakhabar.data.model.remote.dtos.NewsDTO
import com.example.taazakhabar.domain.model.News

fun List<NewsDTO>.toDomain(): List<News> {
    return this.map {
        val newsObj = it.news_obj
        News(
            imageBaseUrl = newsObj.image_url,
            category = newsObj.category_names,
            title = newsObj.title,
            content = newsObj.content,
            author = newsObj.author_name
        )
    }
}