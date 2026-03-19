package com.example.taazakhabar.data.remote

import com.example.taazakhabar.data.remote.dto.NewsList
import com.example.taazakhabar.domain.model.Article

fun List<NewsList>.toDomain(): List<Article> {
    return this.map {
        val articleDto = it.newsArticle
        Article(
            id = articleDto.id,
            sourceUrl = articleDto.source_url,
            sourceName = articleDto.source_name,
            imageUrl = articleDto.image_url,
            title = articleDto.title,
            content = articleDto.content,
            createdAt = articleDto.created_at,
            categories = articleDto.categories
        )
    }
}