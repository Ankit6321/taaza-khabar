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
import com.example.taazakhabar.presentation.components.AppBar
import com.example.taazakhabar.presentation.components.TopNewsItem
import com.example.taazakhabar.presentation.components.TrendingNewsItem
import kotlinx.coroutines.delay
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

    val isRefreshing =
        allNews.loadState.refresh is LoadState.Loading || trendingNews.loadState.refresh is LoadState.Loading
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
                    coroutineScope.launch {
                        trendingNews.refresh()
                        allNews.refresh()

                        while (trendingNews.loadState.refresh is LoadState.Loading ||
                            allNews.loadState.refresh is LoadState.Loading
                        ) {
                            delay(100)
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Column {
                            Text(
                                text = "Trending News",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 22.sp
                                ),
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = 16.dp,
                                    bottom = 12.dp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            if (trendingNews.itemCount > 0) {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    contentPadding = PaddingValues(horizontal = 16.dp)
                                ) {
                                    items(count = trendingNews.itemCount) { index ->
                                        trendingNews[index]?.let { article ->
                                            TrendingNewsItem(
                                                article = article.copy(
                                                    isSaved = savedArticleIds.contains(article.id)
                                                ),
                                                onClick = { onArticleClick(article) },
                                                onToggleSave = { viewModel.toggleSaveArticle(article) },
                                                modifier = Modifier.width(310.dp)
                                            )
                                        }
                                    }
                                }
                            } else if (cachedTrending.isNotEmpty()) {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    contentPadding = PaddingValues(horizontal = 16.dp)
                                ) {
                                    items(cachedTrending) { article ->
                                        TrendingNewsItem(
                                            article = article,
                                            onClick = { onArticleClick(article) },
                                            onToggleSave = { viewModel.toggleSaveArticle(article) },
                                            modifier = Modifier.width(310.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Latest News",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 22.sp
                            ),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    if (allNews.itemCount > 0) {
                        items(count = allNews.itemCount) { index ->
                            allNews[index]?.let { article ->
                                TopNewsItem(
                                    article = article.copy(
                                        isSaved = savedArticleIds.contains(article.id)
                                    ),
                                    onClick = { onArticleClick(article) },
                                    onToggleSave = { viewModel.toggleSaveArticle(article) },
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
                    } else if (cachedAll.isNotEmpty()) {
                        items(cachedAll) { article ->
                            TopNewsItem(
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
                .padding(24.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                shape = MaterialTheme.shapes.large
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Scroll to top"
                )
            }
        }
    }
}
