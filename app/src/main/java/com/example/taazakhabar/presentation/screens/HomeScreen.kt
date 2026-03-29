package com.example.taazakhabar.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.taazakhabar.domain.model.Article
import com.example.taazakhabar.presentation.NewsViewModel
import com.example.taazakhabar.presentation.components.NewsItem
import com.example.taazakhabar.presentation.components.NewsItemSmall
import com.example.taazakhabar.presentation.components.AppBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: NewsViewModel,
    onArticleClick: (Article) -> Unit
) {
    val trendingNews = viewModel.trendingNews.collectAsLazyPagingItems()
    val allNews = viewModel.topNews.collectAsLazyPagingItems()
    
    val cachedTrending by viewModel.cachedTrendingNews.collectAsStateWithLifecycle()
    val cachedAll by viewModel.cachedTopNews.collectAsStateWithLifecycle()
    val savedArticleIds by viewModel.savedArticleIds.collectAsStateWithLifecycle()

    val isRefreshing = allNews.loadState.refresh is LoadState.Loading || trendingNews.loadState.refresh is LoadState.Loading
    val refreshError = (allNews.loadState.refresh as? LoadState.Error) 
        ?: (trendingNews.loadState.refresh as? LoadState.Error)

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    
    val showFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 2
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppBar(
                error = refreshError?.error,
                isRefreshing = isRefreshing
            )

            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = {
                    trendingNews.refresh()
                    allNews.refresh()
                },
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    item {
                        Column(modifier = Modifier.padding(top = 8.dp)) {
                            Text(
                                text = "Trending News",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            
                            if (trendingNews.itemCount > 0) {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
                                ) {
                                    items(count = trendingNews.itemCount) { index ->
                                        trendingNews[index]?.let { article ->
                                            NewsItem(
                                                article = article.copy(isSaved = savedArticleIds.contains(article.id)),
                                                onClick = { onArticleClick(article) },
                                                onToggleSave = { viewModel.toggleSaveArticle(article) },
                                                modifier = Modifier.width(300.dp)
                                            )
                                        }
                                    }
                                }
                            } else if (cachedTrending.isNotEmpty()) {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
                                ) {
                                    items(cachedTrending) { article ->
                                        NewsItem(
                                            article = article,
                                            onClick = { onArticleClick(article) },
                                            onToggleSave = { viewModel.toggleSaveArticle(article) },
                                            modifier = Modifier.width(300.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Latest News",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    if (allNews.itemCount > 0) {
                        items(count = allNews.itemCount) { index ->
                            allNews[index]?.let { article ->
                                NewsItemSmall(
                                    article = article.copy(isSaved = savedArticleIds.contains(article.id)),
                                    onClick = { onArticleClick(article) },
                                    onToggleSave = { viewModel.toggleSaveArticle(article) },
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
                    } else if (cachedAll.isNotEmpty()) {
                        items(cachedAll) { article ->
                            NewsItemSmall(
                                article = article,
                                onClick = { onArticleClick(article) },
                                onToggleSave = { viewModel.toggleSaveArticle(article) },
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                    
                    if (isRefreshing && allNews.itemCount == 0 && cachedAll.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillParentMaxSize()
                                    .padding(bottom = 100.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = showFab,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Scroll to top"
                )
            }
        }
    }
}
