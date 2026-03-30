package com.example.taazakhabar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.taazakhabar.domain.model.Article
import com.example.taazakhabar.domain.model.NewsCategory
import com.example.taazakhabar.domain.model.NewsTopics
import com.example.taazakhabar.domain.useCases.DeleteAllSavedArticlesUseCase
import com.example.taazakhabar.domain.useCases.GetCachedTopNewsUseCase
import com.example.taazakhabar.domain.useCases.GetCachedTopicNewsUseCase
import com.example.taazakhabar.domain.useCases.GetCachedTrendingNewsUseCase
import com.example.taazakhabar.domain.useCases.GetNewsUseCase
import com.example.taazakhabar.domain.useCases.GetSavedArticlesUseCase
import com.example.taazakhabar.domain.useCases.GetTopicWiseNewsUseCase
import com.example.taazakhabar.domain.useCases.ToggleSaveArticleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val getCachedTrendingNewsUseCase: GetCachedTrendingNewsUseCase,
    private val getCachedTopNewsUseCase: GetCachedTopNewsUseCase,
    private val getTopicWiseNewsUseCase: GetTopicWiseNewsUseCase,
    private val getCachedTopicNewsUseCase: GetCachedTopicNewsUseCase,
    private val getSavedArticlesUseCase: GetSavedArticlesUseCase,
    private val deleteAllSavedArticlesUseCase: DeleteAllSavedArticlesUseCase,
    private val toggleSaveArticleUseCase: ToggleSaveArticleUseCase
) : ViewModel() {

    val savedArticles = getSavedArticlesUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(3000),
        emptyList()
    )

    val savedArticleIds = savedArticles.map { articles ->
        articles.map { it.id }.toSet()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3000), emptySet())

    val trendingNews: Flow<PagingData<Article>> = getNewsUseCase(NewsCategory.TRENDING)
        .cachedIn(viewModelScope)

    val topNews: Flow<PagingData<Article>> = getNewsUseCase(NewsCategory.TOP_STORIES)
        .cachedIn(viewModelScope)

    val cachedTrendingNews: StateFlow<List<Article>> = combine(
        getCachedTrendingNewsUseCase(),
        savedArticleIds
    ) { articles, savedIds ->
        articles.map { article ->
            article.copy(isSaved = savedIds.contains(article.id))
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(3000),
        emptyList()
    )

    val cachedTopNews: StateFlow<List<Article>> = combine(
        getCachedTopNewsUseCase(),
        savedArticleIds
    ) { articles, savedIds ->
        articles.map { article ->
            article.copy(isSaved = savedIds.contains(article.id))
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(3000),
        emptyList()
    )

    private val _selectedTopic = MutableStateFlow(NewsTopics.SCIENCE)
    val selectedTopic: StateFlow<NewsTopics> = _selectedTopic.asStateFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle: StateFlow<Article?> = _selectedArticle.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val topicWiseNews: Flow<PagingData<Article>> = _selectedTopic.flatMapLatest { topic ->
        getTopicWiseNewsUseCase(topic)
    }.cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val cachedTopicWiseNews: StateFlow<List<Article>> = _selectedTopic.flatMapLatest { topic ->
        combine(
            getCachedTopicNewsUseCase(topic),
            savedArticleIds
        ) { articles, savedIds ->
            articles.map { article ->
                article.copy(isSaved = savedIds.contains(article.id))
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(3000),
        emptyList()
    )

    fun onTopicSelected(topic: NewsTopics) {
        _selectedTopic.value = topic
    }

    fun onTabSelected(tabIndex: Int) {
        _selectedTab.value = tabIndex
    }

    fun onArticleClicked(article: Article?) {
        _selectedArticle.value = article
    }

    fun toggleSaveArticle(article: Article) {
        viewModelScope.launch {
            toggleSaveArticleUseCase(article)
        }
    }

    fun clearAllSavedArticles() {
        viewModelScope.launch {
            deleteAllSavedArticlesUseCase()
        }
    }
}
