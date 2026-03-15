package com.example.taazakhabar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.taazakhabar.domain.model.NewsCategory
import com.example.taazakhabar.domain.model.NewsTopics
import com.example.taazakhabar.domain.useCases.GetNewsUseCase
import com.example.taazakhabar.domain.useCases.GetTopicWiseNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val getTopicWiseNewsUseCase: GetTopicWiseNewsUseCase,
) : ViewModel() {
    val pagingData = getTopicWiseNewsUseCase(NewsTopics.ENTERTAINMENT).cachedIn(viewModelScope)

    val trendingNews = getNewsUseCase(NewsCategory.TRENDING).cachedIn(viewModelScope)
}