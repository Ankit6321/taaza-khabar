package com.example.taazakhabar.data.model.remote.dtos

data class NewsDTO(
    val bottom_banner_ids: List<String>,
    val fixed_rank: Boolean,
    val hash_id: String,
    val news_obj: NewsObj,
    val news_type: String,
    val no_index: Boolean,
    val publisher_interaction_meta: PublisherInteractionMetaX,
    val rank: Int,
    val read_override: Boolean,
    val source_url: String,
    val type: String,
    val version: Int
)