package com.example.taazakhabar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taazakhabar.domain.model.Article
import com.example.taazakhabar.domain.useCases.IsArticleSavedUseCase
import com.example.taazakhabar.domain.useCases.ToggleSaveArticleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleDetailViewModel @Inject constructor(
    private val toggleSaveArticleUseCase: ToggleSaveArticleUseCase,
    private val isArticleSavedUseCase: IsArticleSavedUseCase
) : ViewModel() {

    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle = _selectedArticle.asStateFlow()

    val isSaved: StateFlow<Boolean> = _selectedArticle.flatMapLatest { article ->
        article?.let { isArticleSavedUseCase(it.id) } ?: flowOf(false)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun selectArticle(article: Article) {
        _selectedArticle.value = article
    }

    fun toggleSave() {
        viewModelScope.launch {
            _selectedArticle.value?.let {
                toggleSaveArticleUseCase(it)
            }
        }
    }
}
