package com.example.taazakhabar.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.taazakhabar.domain.model.Article
import kotlinx.coroutines.flow.Flow

@Composable
fun NewsList(
    modifier: Modifier = Modifier,
    data: Flow<PagingData<Article>>,
    cachedData: Flow<List<Article>>,
    listItem: @Composable ((Article) -> Unit)
) {
    val pagingData = data.collectAsLazyPagingItems()
    val cachedData by cachedData.collectAsStateWithLifecycle(emptyList())

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(count = pagingData.itemCount) { index ->
                pagingData[index]?.let { news ->
                    listItem(news)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Handle loading state for appending items
            pagingData.loadState.apply {
                when {
                    append is LoadState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    append is LoadState.Error -> {
                        val error = append as LoadState.Error
                        item {
                            Text(
                                text = "Error: ${error.error.localizedMessage}",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // Handle initial refresh loading state
        pagingData.loadState.apply {
            when {
                refresh is LoadState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                refresh is LoadState.Error -> {
                    val error = refresh as LoadState.Error
                    Text(
                        text = "Error: ${error.error.localizedMessage}",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(cachedData, key = { it.id }) {article->
                            listItem(article)
                            Spacer(Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}