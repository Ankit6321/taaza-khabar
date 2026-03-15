package com.example.taazakhabar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taazakhabar.domain.model.News
import com.example.taazakhabar.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _state = MutableStateFlow<Result<List<News>>>(Result.success(emptyList()))
    val state = _state.asStateFlow()

    init {
        getNews()
    }

    private fun getNews() {
        viewModelScope.launch {
            _state.update{ newsRepository.getNews(category = "top_stories") }
        }
    }

}