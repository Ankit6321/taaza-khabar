package com.example.taazakhabar.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.taazakhabar.domain.model.NewsTopics
import com.example.taazakhabar.presentation.NewsViewModel
import com.example.taazakhabar.presentation.components.TaazaKhabarAppBar
import com.example.taazakhabar.presentation.components.TopicNewsItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicsScreen(
    viewModel: NewsViewModel
) {
    val topicWiseNews = viewModel.topicWiseNews.collectAsLazyPagingItems()
    val cachedTopicNews by viewModel.cachedTopicWiseNews.collectAsState()
    val selectedTopic by viewModel.selectedTopic.collectAsState()

    val isRefreshing = topicWiseNews.loadState.refresh is LoadState.Loading
    val refreshError = topicWiseNews.loadState.refresh as? LoadState.Error

    Column(modifier = Modifier.fillMaxSize()) {
        TaazaKhabarAppBar(
            error = refreshError?.error,
            isRefreshing = isRefreshing
        )

        ScrollableTabRow(
            selectedTabIndex = selectedTopic.ordinal,
            edgePadding = 16.dp,
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            divider = {},
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTopic.ordinal]),
                    color = androidx.compose.ui.graphics.Color.Red
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
                            color = if (selectedTopic == topic) androidx.compose.ui.graphics.Color.Red else androidx.compose.ui.graphics.Color.Gray
                        )
                    }
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                if (topicWiseNews.itemCount > 0) {
                    items(topicWiseNews.itemCount) { index ->
                        topicWiseNews[index]?.let { article ->
                            TopicNewsItem(article = article)
                        }
                    }
                } else if (cachedTopicNews.isNotEmpty()) {
                    items(cachedTopicNews) { article ->
                        TopicNewsItem(article = article)
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
}
