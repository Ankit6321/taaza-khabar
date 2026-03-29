package com.example.taazakhabar.data.local

import com.example.taazakhabar.data.local.entities.BaseArticleEntity
import com.example.taazakhabar.data.remote.dto.NewsList
import com.example.taazakhabar.domain.model.Article

fun <T : BaseArticleEntity> List<NewsList>.toEntity(
    constructor: (String, String, String, String, String, String, String) -> T
): List<T> {
    return this.map { item ->
        val article = item.newsArticle
        constructor(
            article.id,
            article.source_url,
            article.source_name,
            article.image_url,
            article.title,
            article.content,
            article.categories.fromStringList()
        )
    }
}

fun BaseArticleEntity.toDomain(): Article {
    return Article(
        id = this.id,
        sourceUrl = this.sourceUrl,
        sourceName = this.sourceName,
        imageUrl = this.imageUrl,
        title = this.title,
        content = this.content,
        categories = this.categories.toStringList()
    )
}

