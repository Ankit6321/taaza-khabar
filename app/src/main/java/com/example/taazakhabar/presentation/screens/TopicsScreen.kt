package com.example.taazakhabar.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.taazakhabar.domain.model.Article
import com.example.taazakhabar.domain.model.NewsTopics
import com.example.taazakhabar.presentation.NewsViewModel
import com.example.taazakhabar.presentation.components.TaazaKhabarAppBar
import com.example.taazakhabar.presentation.components.TopicNewsItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicsScreen(
    viewModel: NewsViewModel,
    onArticleClick: (Article) -> Unit
) {
    val topicWiseNews = viewModel.topicWiseNews.collectAsLazyPagingItems()
    val cachedTopicNews by viewModel.cachedTopicWiseNews.collectAsState()
    val selectedTopic by viewModel.selectedTopic.collectAsState()

    val isRefreshing = topicWiseNews.loadState.refresh is LoadState.Loading
    val refreshError = topicWiseNews.loadState.refresh as? LoadState.Error

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    
    val showFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 2
        }
    }

    // Smooth scroll to top when topic changes
    LaunchedEffect(selectedTopic) {
        listState.animateScrollToItem(0)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TaazaKhabarAppBar(
                error = refreshError?.error,
                isRefreshing = isRefreshing
            )
            
            ScrollableTabRow(
                selectedTabIndex = selectedTopic.ordinal,
                edgePadding = 16.dp,
                containerColor = Color.Transparent,
                divider = {},
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTopic.ordinal]),
                        color = Color.Red
                    )
                }
            ) {
                NewsTopics.entries.forEach { topic ->
                    Tab(
                        selected = selectedTopic == topic,
                        onClick = { viewModel.onTopicSelected(topic) },
                        text = {
                            Text(
                                text = topic.name,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedTopic == topic) Color.Red else Color.Gray
                            )
                        }
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    if (topicWiseNews.itemCount > 0) {
                        items(topicWiseNews.itemCount) { index ->
                            topicWiseNews[index]?.let { article ->
                                TopicNewsItem(
                                    article = article,
                                    onClick = { onArticleClick(article) }
                                )
                            }
                        }
                    } else if (cachedTopicNews.isNotEmpty()) {
                        items(cachedTopicNews) { article ->
                            TopicNewsItem(
                                article = article,
                                onClick = { onArticleClick(article) }
                            )
                        }
                    }

                    if (isRefreshing && topicWiseNews.itemCount == 0 && cachedTopicNews.isEmpty()) {
                        item {
                            Box(Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }

        // Floating Action Button to scroll to top
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
